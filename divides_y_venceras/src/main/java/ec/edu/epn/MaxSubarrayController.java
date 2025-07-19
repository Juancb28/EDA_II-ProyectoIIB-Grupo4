package ec.edu.epn;
import java.util.List;

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

    public void handleCalculate() {
        try {
            String input = view.getInputArrayString();
            String[] tokens = input.trim().split("\\s+");
            arr = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                arr[i] = Integer.parseInt(tokens[i]);
            }
            int result = model.maxSubArray(arr);
            view.showResult(result);

            steps = model.getSteps();
            stepIdx = 0;
            if (!steps.isEmpty()) {
                view.showStep(steps.get(stepIdx), arr, stepIdx, steps.size());
                updateStepButtons();
                view.updateProgressBar((double)(stepIdx + 1) / steps.size());
            }
        } catch (Exception e) {
            view.showError("Entrada inválida. Usa números enteros separados por espacios.");
        }
    }

    public void handleNextStep() {
        if (steps == null || stepIdx >= steps.size() - 1) return;
        stepIdx++;
        view.showStep(steps.get(stepIdx), arr, stepIdx, steps.size());
        updateStepButtons();
        view.updateProgressBar((double)(stepIdx + 1) / steps.size());
    }

    public void handlePrevStep() {
        if (steps == null || stepIdx <= 0) return;
        stepIdx--;
        view.showStep(steps.get(stepIdx), arr, stepIdx, steps.size());
        updateStepButtons();
        view.updateProgressBar((double)(stepIdx + 1) / steps.size());
    }

    private void updateStepButtons() {
        view.enableStepButtons(stepIdx > 0, stepIdx < steps.size() - 1);
    }
}