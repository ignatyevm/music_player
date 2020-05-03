import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MainScene extends Scene {

    public MainSceneController controller;
    public Player player;

    public TableView<Track> trackTable;
    public JFXButton prevButton;
    public JFXButton pauseButton;
    public JFXButton nextButton;
    public Label timeLabel;
    public Label volumeLabel;
    public JFXSlider trackSlider;
    public JFXSlider volumeSlider;

    public ImageView pauseButtonGraphic;
    public ImageView playButtonGraphic;
    public ImageView prevButtonGraphic;
    public ImageView nextButtonGraphic;

    public InvalidationListener trackSliderChangeListener;
    public boolean trackSliderDragged = false;

    public MainScene(Group root, List<File> files) {
        super(root, MusicPlayerMain.WINDOW_WIDTH, MusicPlayerMain.WINDOW_HEIGHT);

        getStylesheets().add("main.css");

        controller = new MainSceneController(this);

        trackSliderChangeListener = obs -> {
            if (!trackSliderDragged) {
                player.seek((long) (player.getTotalDuration().toMillis() * trackSlider.getValue() / 100.0));
            }
        };

        ObservableList<Track> tracks = files.stream().map(Track::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        player = new Player(this, tracks);

        setupTable(tracks);

        setupControlButtons();
        setupTrackSlider();
        setupVolumeSlider();

        volumeLabel = new Label("50%");
        timeLabel = new Label("00.00");

        HBox buttonsBar = new HBox(prevButton, pauseButton, nextButton, volumeSlider, volumeLabel);
        buttonsBar.setSpacing(10);
        buttonsBar.setAlignment(Pos.CENTER);
        buttonsBar.getStyleClass().add("transparent");

        HBox hBox = new HBox(trackSlider, timeLabel);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(15);
        hBox.setPrefWidth(getWidth());
        hBox.getStyleClass().add("transparent");

        VBox bottomBox = new VBox(hBox, buttonsBar);
        bottomBox.setPrefHeight(100);
        bottomBox.setPrefWidth(getWidth());
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(15);
        bottomBox.getStyleClass().add("transparent");

        VBox mainContainer = new VBox(trackTable, bottomBox);
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.getStyleClass().add("transparent");

        root.getChildren().addAll(mainContainer);
        trackTable.getSelectionModel().select(0);
        player.setVolume(0.5);
    }

    private void setupControlButtons() {

        pauseButtonGraphic = new ImageView(getClass().getClassLoader().getResource("pause.png").toString());
        prevButtonGraphic = new ImageView(getClass().getClassLoader().getResource("prev.png").toString());
        nextButtonGraphic = new ImageView(getClass().getClassLoader().getResource("next.png").toString());
        playButtonGraphic = new ImageView(getClass().getClassLoader().getResource("play.png").toString());
        playButtonGraphic.setFitWidth(26);
        playButtonGraphic.setFitHeight(26);

        prevButton = new JFXButton("", prevButtonGraphic);
        pauseButton = new JFXButton("", pauseButtonGraphic);
        nextButton = new JFXButton("", nextButtonGraphic);

        JFXButton[] buttons = new JFXButton[]{ prevButton, pauseButton, nextButton };

        for (JFXButton button : buttons) {
            button.setButtonType(JFXButton.ButtonType.RAISED);
            button.getStyleClass().add("rounded-button");
            ((ImageView) button.getGraphic()).setFitWidth(26);
            ((ImageView) button.getGraphic()).setFitHeight(26);
            button.setMaxSize(36, 36);
            button.setMinSize(36, 36);
            button.setPrefSize(36, 36);
            button.setRipplerFill(Color.BLACK);
        }

        prevButton.setOnAction(controller::selectPrev);
        pauseButton.setOnAction(controller::onPause);
        nextButton.setOnAction(controller::selectNext);
    }

    private void setupTrackSlider() {
        trackSlider = new JFXSlider();
        trackSlider.setPrefWidth(400);
        trackSlider.setMaxWidth(400);
        trackSlider.setMinWidth(400);
        trackSlider.setMin(0.0);
        trackSlider.setMax(100.0);
        trackSlider.getStyleClass().add("transparent");

        trackSlider.labelFormatterProperty().setValue(new StringConverter<Double>() {
            @Override
            public String toString(Double aDouble) {
                int totalTime = (int) player.getTotalDuration().toSeconds();
                int nextTime = (int) (totalTime * trackSlider.getValue() / 100.0);
                return String.format("%d:%02d", nextTime / 60, nextTime % 60);
            }

            @Override
            public Double fromString(String s) {
                return 1.0;
            }
        });

        trackSlider.setOnMousePressed(event -> {
            trackSlider.valueProperty().removeListener(trackSliderChangeListener);
            trackSliderDragged = true;
        });

        trackSlider.setOnMouseReleased(event -> {
            int totalTime = (int) player.getTotalDuration().toMillis();
            player.seek((long) (totalTime * trackSlider.getValue() / 100.0));
            trackSlider.valueProperty().addListener(trackSliderChangeListener);
            trackSliderDragged = false;
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void setupVolumeSlider() {
        volumeSlider = new JFXSlider();
        volumeSlider.setPrefWidth(100);
        volumeSlider.setMaxWidth(100);
        volumeSlider.setMinWidth(100);
        volumeSlider.setMin(0.0);
        volumeSlider.setMax(1.0);
        volumeSlider.setValue(0.5);
        volumeSlider.getStyleClass().add("transparent");
        volumeSlider.getStyleClass().add("volume-slider");
        volumeSlider.valueProperty().addListener(obs -> {
            player.setVolume(volumeSlider.getValue());
            volumeLabel.setText(String.format("%d%%", (int) (100 * volumeSlider.getValue())));
        });
    }

    private void setupTable(ObservableList<Track> tracks) {
        trackTable = new TableView<>(tracks);
        trackTable.setPrefHeight(getHeight() - 100);
        trackTable.setPrefWidth(getWidth());
        trackTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Track, String> numberColumn = new TableColumn<>("");
        numberColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(Integer.toString(trackTable.getItems().indexOf(cellData.getValue()) + 1)));
        numberColumn.setMinWidth(25);
        numberColumn.setPrefWidth(25);
        numberColumn.setMaxWidth(200);
        numberColumn.setStyle("-fx-alignment: center;");

        TableColumn<Track, String> nameColumn = new TableColumn<>("Track");
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().source.getName()));

        TableColumn<Track, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(cellData -> {
            int trackDuration = (int) cellData.getValue().duration.toSeconds();
            return new ReadOnlyStringWrapper(String.format("%d:%02d",trackDuration / 60, trackDuration % 60));
        });
        durationColumn.setMinWidth(75);
        durationColumn.setMaxWidth(100);
        durationColumn.setStyle("-fx-alignment: center;");

        trackTable.getColumns().addAll(numberColumn, nameColumn, durationColumn);
        trackTable.getSelectionModel().selectedItemProperty().addListener(controller::onTrackSelect);
    }

    static void show(List<File> files) {
        Group root = new Group();
        MusicPlayerMain.getStage().setScene(new MainScene(root, files));
        MusicPlayerMain.getStage().show();
    }

}
