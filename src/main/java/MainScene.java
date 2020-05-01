import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MainScene extends Scene {

    public MainSceneController controller;
    public Player player;

    public TableView<Track> trackTable;
    public Button prevButton;
    public Button pauseButton;
    public Button nextButton;
    public Label timeLabel;
    public Label volumeLabel;
    public Slider trackSlider;
    public Slider volumeSlider;
    public InvalidationListener trackSliderChangeListener;

    public MainScene(Group root, List<File> files) {
        super(root, MusicPlayerMain.WINDOW_WIDTH, MusicPlayerMain.WINDOW_HEIGHT);

        controller = new MainSceneController(this);

        trackSliderChangeListener = obs -> player.seek((long) (player.getTotalDuration().toMillis() * trackSlider.getValue() / 100.0));

        ObservableList<Track> tracks = files.stream().map(Track::new)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        player = new Player(this, tracks);

        trackTable = setupTable(tracks);

        prevButton = new Button("prev");
        pauseButton = new Button("pause");
        nextButton = new Button("next");

        prevButton.setOnAction(controller::selectPrev);
        pauseButton.setOnAction(controller::onPause);
        nextButton.setOnAction(controller::selectNext);

        volumeSlider = setupVolumeSlider();
        volumeLabel = new Label("50%");

        HBox buttonsBar = new HBox(prevButton, pauseButton, nextButton, volumeSlider, volumeLabel);
        buttonsBar.setAlignment(Pos.CENTER);
        buttonsBar.setSpacing(10);

        trackSlider = setupTrackSlider();

        timeLabel = new Label("00.00");

        HBox hBox = new HBox(trackSlider, timeLabel);
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        hBox.setPrefWidth(getWidth());

        VBox bottomBox = new VBox(hBox, buttonsBar);
        bottomBox.setPrefHeight(100);
        bottomBox.setPrefWidth(getWidth());
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setSpacing(15);

        VBox mainContainer = new VBox(trackTable, bottomBox);
        mainContainer.setAlignment(Pos.CENTER);

        root.getChildren().addAll(mainContainer);
        trackTable.getSelectionModel().select(0);
    }

    private Slider setupTrackSlider() {
        Slider trackSlider = new Slider();
        trackSlider.setPrefWidth(400);
        trackSlider.setMaxWidth(400);
        trackSlider.setMinWidth(400);
        trackSlider.setMin(0.0);
        trackSlider.setMax(100.0);
        return trackSlider;
    }

    private Slider setupVolumeSlider() {
        Slider volumeSlider = new Slider();
        volumeSlider.setPrefWidth(100);
        volumeSlider.setMaxWidth(100);
        volumeSlider.setMinWidth(100);
        volumeSlider.setMin(0.0);
        volumeSlider.setMax(1.0);
        volumeSlider.setValue(0.5);
        volumeSlider.valueProperty().addListener(obs -> {
            player.setVolume(volumeSlider.getValue());
            volumeLabel.setText(String.format("%d%%", (int) (100 * volumeSlider.getValue())));
        });
        return volumeSlider;
    }

    private TableView<Track> setupTable(ObservableList<Track> tracks) {
        TableView<Track> table = new TableView<>(tracks);
        table.setPrefHeight(getHeight() - 100);
        table.setPrefWidth(getWidth());
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Track, String> numberColumn = new TableColumn<>("");
        numberColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(Integer.toString(table.getItems().indexOf(cellData.getValue()) + 1)));
        numberColumn.setMinWidth(25);
        numberColumn.setPrefWidth(25);
        numberColumn.setMaxWidth(200);
        numberColumn.setStyle("-fx-alignment: center;");

        TableColumn<Track, String> nameColumn = new TableColumn<>("Track");
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyStringWrapper(cellData.getValue().source.getName()));

        TableColumn<Track, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(cellData -> {
            int trackDuration = (int) cellData.getValue().media.getDuration().toSeconds();
            return new ReadOnlyStringWrapper(String.format("%d.%02d",trackDuration / 60, trackDuration % 60));
        });
        durationColumn.setMinWidth(75);
        durationColumn.setMaxWidth(100);
        durationColumn.setStyle("-fx-alignment: center;");

        table.getColumns().addAll(numberColumn, nameColumn, durationColumn);
        table.getSelectionModel().selectedItemProperty().addListener(controller::onTrackSelect);
        return table;
    }

    static void show(List<File> files) {
        Group root = new Group();
        MusicPlayerMain.getStage().setScene(new MainScene(root, files));
        MusicPlayerMain.getStage().show();
    }

}
