package ec.edu.epn;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.layout.Priority;
import javafx.scene.Group;


public class MaxSubarrayView {
    private TextField inputField;
    private Label resultLabel;
    private TextArea stepArea;
    private Button calcButton, nextButton, prevButton;
    private RecursionTreePane recursionTreePane;
    private Label costLabel;


    /**
     * Inicializa y muestra la interfaz principal de la aplicación.
     * Configura los estilos, eventos y disposición de los controles.
     * @param primaryStage Ventana principal de JavaFX
     * @param controller Controlador de la lógica de la aplicación
     */
    public void start(@SuppressWarnings("exports") Stage primaryStage, MaxSubarrayController controller) {
        primaryStage.setTitle("Máxima Suma de Subarreglo (Divide y Conquista) - Paso a Paso");
        primaryStage.setResizable(false);
        // Establecer ícono
        try {
            Image icon = new Image(getClass().getResourceAsStream("/ec/edu/epn/img/image.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el ícono: " + e.getMessage());
        }

        Label titleLabel = new Label("Máxima Suma de Subarreglo");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #5F7470; -fx-padding: 0 0 10 0;");

        inputField = new TextField();
        inputField.setPromptText("Ejemplo: 1 -3 2 1 -1 3 -2 3");
        inputField.setStyle("-fx-background-radius: 8; -fx-padding: 8; -fx-font-size: 14px; -fx-background-color: #D2D4C8; -fx-border-color: #889696; -fx-border-radius: 8;");

        calcButton = new Button("Calcular todo y ver pasos");
        calcButton.setStyle("-fx-background-color: #5F7470; -fx-text-fill: #fff; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16; -fx-border-color: #889696; -fx-border-width: 2;");
        calcButton.setOnMouseEntered(e -> calcButton.setStyle("-fx-background-color: #889696; -fx-text-fill: #fff; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16; -fx-border-color: #5F7470; -fx-border-width: 2;"));
        calcButton.setOnMouseExited(e -> calcButton.setStyle("-fx-background-color: #5F7470; -fx-text-fill: #fff; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16; -fx-border-color: #889696; -fx-border-width: 2;"));
        // Elimino resultBox y su uso en el layout
        resultLabel = new Label();
        resultLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #5F7470; -fx-background-color: #D2D4C8; -fx-padding: 10; -fx-background-radius: 8; -fx-alignment: center; -fx-border-color: #889696; -fx-border-width: 2;");
        resultLabel.setMaxWidth(Double.MAX_VALUE);
        resultLabel.setMinHeight(32);
        resultLabel.setWrapText(true);
        resultLabel.setVisible(false);
        stepArea = new TextArea();
        stepArea.setEditable(false);
        stepArea.setWrapText(true);
        stepArea.setFocusTraversable(false);
        stepArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-font-size: 16px; -fx-background-color: #B8BDB5; -fx-padding: 16; -fx-background-radius: 8; -fx-border-width: 0; -fx-border-color: transparent; -fx-text-fill: #5F7470;");
        stepArea.setMinHeight(0);
        stepArea.setPrefHeight(0);
        stepArea.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(stepArea, Priority.ALWAYS);
        VBox.setVgrow(resultLabel, Priority.NEVER);
        VBox stepBox = new VBox(resultLabel, stepArea);
        stepBox.setSpacing(16);
        stepBox.setPadding(new Insets(0, 0, 8, 0));
        stepBox.setMaxWidth(400);
        stepBox.setMinWidth(320);
        stepBox.setPrefWidth(400);
        stepBox.setStyle("-fx-background-color: #D2D4C8; -fx-border-radius: 12; -fx-background-radius: 12; -fx-border-color: #889696; -fx-border-width: 2;");
        VBox.setVgrow(stepBox, Priority.ALWAYS);

        // Botones de navegación y calcular juntos
        nextButton = new Button("Siguiente paso");
        nextButton.setStyle("-fx-background-color: #889696; -fx-text-fill: #fff; -fx-background-radius: 8; -fx-padding: 6 14; -fx-border-color: #5F7470; -fx-border-width: 2;");
        nextButton.setOnMouseEntered(e -> nextButton.setStyle("-fx-background-color: #5F7470; -fx-text-fill: #fff; -fx-background-radius: 8; -fx-padding: 6 14; -fx-border-color: #889696; -fx-border-width: 2;"));
        nextButton.setOnMouseExited(e -> nextButton.setStyle("-fx-background-color: #889696; -fx-text-fill: #fff; -fx-background-radius: 8; -fx-padding: 6 14; -fx-border-color: #5F7470; -fx-border-width: 2;"));
        prevButton = new Button("Paso anterior");
        prevButton.setStyle("-fx-background-color: #B8BDB5; -fx-text-fill: #5F7470; -fx-background-radius: 8; -fx-padding: 6 14; -fx-border-color: #889696; -fx-border-width: 2;");
        prevButton.setOnMouseEntered(e -> prevButton.setStyle("-fx-background-color: #889696; -fx-text-fill: #fff; -fx-background-radius: 8; -fx-padding: 6 14; -fx-border-color: #5F7470; -fx-border-width: 2;"));
        prevButton.setOnMouseExited(e -> prevButton.setStyle("-fx-background-color: #B8BDB5; -fx-text-fill: #5F7470; -fx-background-radius: 8; -fx-padding: 6 14; -fx-border-color: #889696; -fx-border-width: 2;"));
        nextButton.setDisable(true);
        prevButton.setDisable(true);
        HBox buttonBox = new HBox(12, calcButton, prevButton, nextButton);
        buttonBox.setStyle("-fx-alignment: center-left;");
        buttonBox.setPadding(new Insets(0, 0, 8, 0));

        // Conectar eventos de los botones
        calcButton.setOnAction(e -> controller.handleCalculate());
        nextButton.setOnAction(e -> controller.handleNextStep());
        prevButton.setOnAction(e -> controller.handlePrevStep());

        // Área de pasos ocupa todo el espacio disponible
        stepArea.setMinHeight(0);
        stepArea.setPrefHeight(0);
        stepArea.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(stepArea, Priority.ALWAYS);
        VBox.setVgrow(resultLabel, Priority.NEVER);
         stepBox = new VBox(resultLabel, stepArea);
        stepBox.setSpacing(16);
        stepBox.setPadding(new Insets(0, 0, 8, 0));
        stepBox.setMaxWidth(400);
        stepBox.setMinWidth(320);
        stepBox.setPrefWidth(400);
        VBox.setVgrow(stepBox, Priority.ALWAYS);

        // Gráfico con scroll
        recursionTreePane = new RecursionTreePane();
        recursionTreePane.setPrefHeight(400);
        recursionTreePane.setMinHeight(300);
        recursionTreePane.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(recursionTreePane, Priority.ALWAYS);
        Group treeGroup = new Group(recursionTreePane);
        // Zoom con Ctrl + scroll
        treeGroup.setOnScroll(event -> {
            if (event.isControlDown()) {
                double scale = recursionTreePane.getScaleX();
                double delta = event.getDeltaY() > 0 ? 1.1 : 0.9;
                double newScale = Math.max(0.2, Math.min(3.0, scale * delta));
                recursionTreePane.setScaleX(newScale);
                recursionTreePane.setScaleY(newScale);
                event.consume();
            }
        });
        ScrollPane scrollPane = new ScrollPane(treeGroup);
        scrollPane.setFitToWidth(false);
        scrollPane.setPrefHeight(500);
        scrollPane.setPrefWidth(600);
        scrollPane.setStyle("-fx-background: #f7fbff; -fx-border-radius: 12; -fx-background-radius: 12;");
        VBox treeBox = new VBox(scrollPane);
        treeBox.setPadding(new Insets(0, 0, 0, 0));
        treeBox.setMaxWidth(600);
        treeBox.setPrefWidth(600);
        VBox.setVgrow(treeBox, Priority.ALWAYS);
        // Centrar el scroll horizontalmente tras cada actualización del árbol
        recursionTreePane.widthProperty().addListener((obs, oldVal, newVal) -> {
            scrollPane.setHvalue(0.5);
        });

        // Layout principal horizontal
        HBox mainBox = new HBox(24, stepBox, treeBox);
        mainBox.setPadding(new Insets(0, 0, 0, 0));
        mainBox.setFillHeight(true);
        mainBox.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(stepBox, Priority.ALWAYS);
        HBox.setHgrow(treeBox, Priority.ALWAYS);


        costLabel = new Label();
        costLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #5F7470; -fx-background-color: #E0E2DB; -fx-padding: 8 16; -fx-background-radius: 8; -fx-alignment: center-right;");
        costLabel.setVisible(false);
        HBox topBar = new HBox();
        topBar.setSpacing(12);
        topBar.setPadding(new Insets(16, 24, 0, 24));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(titleLabel, spacer, costLabel);

        VBox layout = new VBox(10,
                topBar,

        VBox layout = new VBox(20,
                titleLabel,

                new Label("Introduce números enteros separados por espacios:") {{ setStyle("-fx-text-fill: #5F7470; -fx-font-size: 15px;"); }},
                inputField,
                buttonBox,
                mainBox
        );
        layout.setPadding(new Insets(24));
        layout.setStyle("-fx-background-color: #E0E2DB; -fx-border-radius: 12; -fx-background-radius: 12;");
        layout.setFillWidth(true);
        primaryStage.setScene(new Scene(layout, 1100, 800));
        primaryStage.show();
    }

    /**
     * Obtiene el texto ingresado por el usuario en el campo de entrada.
     * @return Cadena con los números ingresados
     */
    public String getInputArrayString() {
        return inputField.getText();
    }

    /**
     * Muestra el resultado de la suma máxima en el label destacado.
     * @param result Valor de la suma máxima
     */
    public void showResult(int result) {
        resultLabel.setText("La suma máxima de un subarreglo contiguo es: " + result);
        resultLabel.setVisible(true);
    }

    /**
     * Muestra la información detallada de un paso del algoritmo en el área de texto.
     * @param step Objeto Step con los datos del paso
     * @param arr Arreglo de entrada
     * @param stepIdx Índice del paso actual
     * @param totalSteps Total de pasos
     */
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
        stepArea.setText(sb.toString());
    }

    /**
     * Muestra un popup de error con el mensaje especificado.
     * @param message Mensaje de error a mostrar
     */
    public void showError(String message) {
        // Popup de error
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de entrada");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Habilita o deshabilita los botones de navegación de pasos.
     * @param prev true para habilitar "Paso anterior"
     * @param next true para habilitar "Siguiente paso"
     */
    public void enableStepButtons(boolean prev, boolean next) {
        prevButton.setDisable(!prev);
        nextButton.setDisable(!next);
    }

    /**
     * (Obsoleto) Método para mostrar el árbol de recursión textual (no usado).
     * @param root Raíz del árbol textual
     */
    public void showRecursionTree(javafx.scene.control.TreeItem<String> root) {
        // Ya no se usa el TreeView textual
    }

    /**
     * Muestra el árbol de recursión gráfico hasta el paso indicado.
     * @param arr Arreglo de entrada
     * @param steps Lista de pasos del algoritmo
     * @param stepIdx Índice del paso actual (nivel máximo a mostrar)
     */
    public void showRecursionTree(int[] arr, java.util.List<MaxSubarrayModel.Step> steps, int stepIdx) {
        recursionTreePane.drawTree(arr, steps, stepIdx);
    }

    /**
     * Limpia el área de pasos.
     */
    public void clearStep() {
        stepArea.setText("");
    }


    /**
     * Muestra el coste computacional en milisegundos (con 3 decimales) en la esquina superior derecha.
     * @param nanos Tiempo en nanosegundos
     */
    public void showCost(long nanos) {
        double ms = nanos / 1_000_000.0;
        costLabel.setText(String.format("Coste: %.3f ms", ms));
        if (!costLabel.isVisible()) costLabel.setVisible(true);
        costLabel.applyCss();
        costLabel.layout();
    }

}