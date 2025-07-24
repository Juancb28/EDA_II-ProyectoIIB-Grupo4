package ec.edu.epn;
import java.util.List;
import javafx.scene.control.TreeItem;
import java.util.ArrayList;

public class MaxSubarrayController {
    private MaxSubarrayModel model;
    private MaxSubarrayView view;

    private int[] arr;
    private List<MaxSubarrayModel.Step> steps;
    private int stepIdx = 0;

    public MaxSubarrayController(MaxSubarrayModel model, MaxSubarrayView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * Procesa la entrada del usuario, calcula la suma máxima y muestra los pasos y el árbol.
     */
    public void handleCalculate() {
        try {
            String input = view.getInputArrayString();
            String[] tokens = input.trim().split("\\s+");
            java.util.List<Integer> validNumbers = new ArrayList<>();
            for (String token : tokens) {
                String t = token.trim();
                if (t.isEmpty()) continue;
                try {
                    validNumbers.add(Integer.parseInt(t));
                } catch (NumberFormatException ex) {
                    view.showError("Error: El valor '" + t + "' no es un número entero válido. Usa espacios para separar los números.");
                    view.showRecursionTree(null);
                    view.clearStep();
                    return;
                }
            }
            if (validNumbers.isEmpty()) {
                view.showError("Error: Ingresa al menos un número entero separado por espacios.");
                view.showRecursionTree(null);
                view.clearStep();
                return;
            }
            arr = new int[validNumbers.size()];
            for (int i = 0; i < validNumbers.size(); i++) arr[i] = validNumbers.get(i);

            long start = System.nanoTime();

            int result = model.maxSubArray(arr);
            long end = System.nanoTime();
            view.showCost(end - start);
            view.showResult(result);
            // Mostrar pasos y árbol como antes
            steps = model.getSteps();
            stepIdx = 0;
            if (!steps.isEmpty()) {
                view.showStep(steps.get(stepIdx), arr, stepIdx, steps.size());
                updateStepButtons();
            }
            // Construir y mostrar el árbol de recursión
            view.showRecursionTree(arr, steps, stepIdx);
        } catch (Exception e) {
            e.printStackTrace(); // Para depuración
            view.showError("Entrada inválida. Por favor, usa números enteros separados por espacios.");
            view.showRecursionTree(null);
            view.clearStep();
        }
    }

    /**
     * Avanza al siguiente paso del algoritmo y actualiza la vista y el árbol.
     */
    public void handleNextStep() {
        if (steps == null || stepIdx >= steps.size() - 1) return;
        stepIdx++;
        view.showStep(steps.get(stepIdx), arr, stepIdx, steps.size());
        updateStepButtons();
        view.showRecursionTree(arr, steps, stepIdx);
    }

    /**
     * Retrocede al paso anterior del algoritmo y actualiza la vista y el árbol.
     */
    public void handlePrevStep() {
        if (steps == null || stepIdx <= 0) return;
        stepIdx--;
        view.showStep(steps.get(stepIdx), arr, stepIdx, steps.size());
        updateStepButtons();
        view.showRecursionTree(arr, steps, stepIdx);
    }

    /**
     * Habilita o deshabilita los botones de navegación según el paso actual.
     */
    private void updateStepButtons() {
        view.enableStepButtons(stepIdx > 0, stepIdx < steps.size() - 1);
    }

    // Construye el árbol de recursión directamente, sin usar la lista de pasos
    @SuppressWarnings("unused")
    private TreeItem<String> buildRecursionTree(int[] arr, List<MaxSubarrayModel.Step> steps) {
        if (arr == null || arr.length == 0) return null;
        return buildNode(arr, 0, arr.length - 1);
    }

    @SuppressWarnings("unchecked")
    private TreeItem<String> buildNode(int[] arr, int left, int right) {
        if (left == right) {
            String label = arr[left] + "\nretornar " + arr[left];
            return new TreeItem<>(label);
        }
        int mid = (left + right) / 2;
        TreeItem<String> leftNode = buildNode(arr, left, mid);
        TreeItem<String> rightNode = buildNode(arr, mid + 1, right);
        int leftSum = maxSubArray(arr, left, mid);
        int rightSum = maxSubArray(arr, mid + 1, right);
        int crossSum = maxCrossingSum(arr, left, mid, right);
        int maxSum = Math.max(Math.max(leftSum, rightSum), crossSum);
        StringBuilder label = new StringBuilder();
        label.append("[");
        for (int i = left; i <= right; i++) {
            label.append(arr[i]);
            if (i < right) label.append(", ");
        }
        label.append("] = ").append(maxSum);
        TreeItem<String> node = new TreeItem<>(label.toString());
        node.getChildren().addAll(leftNode, rightNode);
        return node;
    }

    // Métodos auxiliares para calcular sumas (idénticos a los del modelo)
    private int maxSubArray(int[] arr, int left, int right) {
        if (left == right) return arr[left];
        int mid = (left + right) / 2;
        int leftSum = maxSubArray(arr, left, mid);
        int rightSum = maxSubArray(arr, mid + 1, right);
        int crossSum = maxCrossingSum(arr, left, mid, right);
        return Math.max(Math.max(leftSum, rightSum), crossSum);
    }
    private int maxCrossingSum(int[] arr, int left, int mid, int right) {
        int sum = 0;
        int leftSum = Integer.MIN_VALUE;
        for (int i = mid; i >= left; i--) {
            sum += arr[i];
            if (sum > leftSum) leftSum = sum;
        }
        sum = 0;
        int rightSum = Integer.MIN_VALUE;
        for (int i = mid + 1; i <= right; i++) {
            sum += arr[i];
            if (sum > rightSum) rightSum = sum;
        }
        return leftSum + rightSum;
    }
}