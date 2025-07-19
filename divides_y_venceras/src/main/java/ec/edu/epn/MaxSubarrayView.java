package ec.edu.epn;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class MaxSubarrayView {
    private TextField inputField;
    private Label resultLabel;
    private Label stepLabel;
    private Button calcButton, nextButton, prevButton;

    public void start(@SuppressWarnings("exports") Stage primaryStage, MaxSubarrayController controller) {
        primaryStage.setTitle("Máxima Suma de Subarreglo (Divide y Conquista) - Paso a Paso");

        inputField = new TextField();
        inputField.setPromptText("Ejemplo: 1 -3 2 1 -1 3 -2 3");

        calcButton = new Button("Calcular todo y ver pasos");
        resultLabel = new Label();
        stepLabel = new Label();

        nextButton = new Button("Siguiente paso");
        prevButton = new Button("Paso anterior");
        nextButton.setDisable(true);
        prevButton.setDisable(true);

        calcButton.setOnAction(e -> controller.handleCalculate());
        nextButton.setOnAction(e -> controller.handleNextStep());
        prevButton.setOnAction(e -> controller.handlePrevStep());

        HBox navBox = new HBox(10, prevButton, nextButton);
        VBox layout = new VBox(10,
                new Label("Introduce números enteros separados por espacio:"),
                inputField,
                calcButton,
                resultLabel,
                stepLabel,
                navBox
        );
        layout.setPadding(new Insets(18));
        primaryStage.setScene(new Scene(layout, 500, 250));
        primaryStage.show();
    }

    public String getInputArrayString() {
        return inputField.getText();
    }

    public void showResult(int result) {
        resultLabel.setText("La suma máxima de un subarreglo contiguo es: " + result);
    }

    public void showStep(MaxSubarrayModel.Step step, int[] arr, int stepIdx, int totalSteps) {
        StringBuilder sb = new StringBuilder();
        sb.append("Paso ").append(stepIdx + 1).append(" de ").append(totalSteps).append(":\n");
        sb.append("Rango analizado: [").append(step.left).append(", ").append(step.right).append("] ");
        sb.append("(");
        for (int i = step.left; i <= step.right; i++) {
            sb.append(arr[i]);
            if (i < step.right) sb.append(", ");
        }
        sb.append(")\n");
        sb.append("División en mid = ").append(step.mid).append("\n");
        sb.append("Suma izquierda: ").append(step.leftSum).append("\n");
        sb.append("Suma derecha: ").append(step.rightSum).append("\n");
        sb.append("Suma cruzada: ").append(step.crossSum).append("\n");
        sb.append("Máximo de este paso: ").append(step.maxSum);
        stepLabel.setText(sb.toString());
    }

    public void showError(String message) {
        resultLabel.setText("Error: " + message);
    }

    public void enableStepButtons(boolean prev, boolean next) {
        prevButton.setDisable(!prev);
        nextButton.setDisable(!next);
    }
}