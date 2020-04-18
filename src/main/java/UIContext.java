import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;

public class UIContext {

    private Map<String, Node> components = new HashMap<>();

    public UIContext(Node ...components) {
        for (Node component : components) {
            this.components.put(component.getId(), component);
        }
    }

    public <Component extends Node> Component get(String id) {
        return (Component) components.get(id);
    }

    public void add(String id, Node component) {
        components.put(id, component);
    }

}
