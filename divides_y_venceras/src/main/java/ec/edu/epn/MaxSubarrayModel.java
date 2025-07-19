package ec.edu.epn;
import java.util.ArrayList;
import java.util.List;

public class MaxSubarrayModel {
    public static class Step {
        public final int left, right, mid, leftSum, rightSum, crossSum, maxSum;
        public Step(int left, int right, int mid, int leftSum, int rightSum, int crossSum, int maxSum) {
            this.left = left;
            this.right = right;
            this.mid = mid;
            this.leftSum = leftSum;
            this.rightSum = rightSum;
            this.crossSum = crossSum;
            this.maxSum = maxSum;
        }
    }

    private List<Step> steps;

    public List<Step> getSteps() {
        return steps;
    }

    public int maxSubArray(int[] arr) {
        steps = new ArrayList<>();
        if (arr == null || arr.length == 0) return 0;
        return maxSubArrayDC(arr, 0, arr.length - 1);
    }

    private int maxSubArrayDC(int[] arr, int left, int right) {
        if (left == right) {
            steps.add(new Step(left, right, left, arr[left], arr[left], arr[left], arr[left]));
            return arr[left];
        }
        int mid = (left + right) / 2;
        int leftSum = maxSubArrayDC(arr, left, mid);
        int rightSum = maxSubArrayDC(arr, mid + 1, right);
        int crossSum = maxCrossingSum(arr, left, mid, right);
        int maxSum = Math.max(Math.max(leftSum, rightSum), crossSum);
        steps.add(new Step(left, right, mid, leftSum, rightSum, crossSum, maxSum));
        return maxSum;
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