package ec.edu.epn;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MaxSubarrayView {
    private TextField inputField;
    private Label resultLabel;
    private Label stepLabel;
    private Button calcButton, nextButton, prevButton;
    private ProgressBar progressBar;

    public void start(Stage primaryStage, MaxSubarrayController controller) {
        primaryStage.setTitle(" Divide y Conquista: Máxima suma de subarreglo ");

        inputField = new TextField();
        inputField.setPromptText("Ejemplo: 1 -3 2 1 -1 3 -2 3");
        inputField.setFont(Font.font("Segoe UI", 16));
        inputField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-background-color: #f5faff;");

        calcButton = new Button("✨ Calcular y mostrar pasos ✨");
        calcButton.setFont(Font.font("Segoe UI Semibold", 15));
        calcButton.setStyle("-fx-background-color: #00b8ff; -fx-text-fill: white; -fx-background-radius: 10;");

        resultLabel = new Label();
        resultLabel.setFont(Font.font("Segoe UI Semibold", 18));
        resultLabel.setTextFill(Color.web("#00b8ff"));

        stepLabel = new Label();
        stepLabel.setFont(Font.font("Consolas", 15));
        stepLabel.setStyle("-fx-background-color: #fffbe7; -fx-padding: 12px; -fx-background-radius: 10; -fx-border-radius: 10;");

        nextButton = new Button("▶ Siguiente");
        nextButton.setFont(Font.font("Segoe UI", 15));
        nextButton.setStyle("-fx-background-color: #00e676; -fx-text-fill: white; -fx-background-radius: 10;");
        nextButton.setDisable(true);

        prevButton = new Button("◀ Anterior");
        prevButton.setFont(Font.font("Segoe UI", 15));
        prevButton.setStyle("-fx-background-color: #ff3d00; -fx-text-fill: white; -fx-background-radius: 10;");
        prevButton.setDisable(true);

        progressBar = new ProgressBar(0);
        progressBar.setPrefWidth(250);
        progressBar.setStyle("-fx-accent: #00b8ff;");

        calcButton.setOnAction(e -> controller.handleCalculate());
        nextButton.setOnAction(e -> controller.handleNextStep());
        prevButton.setOnAction(e -> controller.handlePrevStep());

        HBox navBox = new HBox(10, prevButton, nextButton, progressBar);
        navBox.setPadding(new Insets(10));
        VBox layout = new VBox(16,
                new Label(" Introduce números enteros separados por espacio:"),
                inputField,
                calcButton,
                resultLabel,
                stepLabel,
                navBox
        );
        layout.setPadding(new Insets(26));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom, #e0f7fa, #fffde7);");

        Scene scene = new Scene(layout, 630, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public String getInputArrayString() {
        return inputField.getText();
    }

    public void showResult(int result) {
        resultLabel.setText(" Suma máxima de subarreglo contiguo: " + result );
    }

    public void showStep(MaxSubarrayModel.Step step, int[] arr, int stepIdx, int totalSteps) {
        StringBuilder sb = new StringBuilder();
        sb.append("Paso ").append(stepIdx + 1).append(" de ").append(totalSteps).append(":\n");
        sb.append("Rango analizado: [").append(step.left).append(", ").append(step.right).append("]\n");
        sb.append("Valores: ");
        for (int i = step.left; i <= step.right; i++) {
            if (i == step.mid) sb.append("[");
            sb.append(arr[i]);
            if (i == step.mid) sb.append("]");
            if (i < step.right) sb.append(", ");
        }
        sb.append("\nDivisión en mid = ").append(step.mid).append("\n");
        sb.append("Suma izquierda: ").append(step.leftSum).append("\n");
        sb.append("Suma derecha: ").append(step.rightSum).append("\n");
        sb.append("Suma cruzada: ").append(step.crossSum).append("\n");
        sb.append("Máximo en este paso: ").append(step.maxSum);
        stepLabel.setText(sb.toString());
    }

    public void showError(String message) {
        resultLabel.setText("⚠ " + message);
        resultLabel.setTextFill(Color.web("#ff3d00"));
    }

    public void enableStepButtons(boolean prev, boolean next) {
        prevButton.setDisable(!prev);
        nextButton.setDisable(!next);
    }

    public void updateProgressBar(double progress) {
        progressBar.setProgress(progress);
    }
}