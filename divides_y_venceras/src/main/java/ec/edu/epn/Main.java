package ec.edu.epn;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage) {
        MaxSubarrayModel model = new MaxSubarrayModel();
        MaxSubarrayView view = new MaxSubarrayView();
        MaxSubarrayController controller = new MaxSubarrayController(model, view);
        view.start(primaryStage, controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}