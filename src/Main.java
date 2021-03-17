import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private CanvasManager canvasManager = new CanvasManager("Space Invaders", 1024, 768);

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(canvasManager.createElements()));
        stage.resizableProperty().setValue(Boolean.FALSE);
        stage.setTitle(canvasManager.title);
        stage.show();

        Node root = stage.getScene().getRoot();
        root.setFocusTraversable(true);
        root.requestFocus();
        root.setOnKeyPressed(e -> canvasManager.keyPress(e));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
