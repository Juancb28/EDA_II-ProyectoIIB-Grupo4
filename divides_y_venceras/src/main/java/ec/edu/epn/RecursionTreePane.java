package ec.edu.epn;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class RecursionTreePane extends Pane {
    private static final double NODE_WIDTH = 120;
    private static final double NODE_HEIGHT = 60;
    @SuppressWarnings("unused")
    private static final double H_GAP = 30;
    private static final double V_GAP = 60;

    public void drawTree(int[] arr, java.util.List<MaxSubarrayModel.Step> steps, int maxStepIdx) {
        this.getChildren().clear();
        if (arr == null || arr.length == 0 || steps == null || steps.isEmpty() || maxStepIdx < 0) return;
        int rootIdx = Math.min(maxStepIdx, steps.size() - 1);
        double paneWidth = Math.max(1200, (arr.length * 220));
        double paneHeight = Math.max(600, (maxStepIdx + 2) * 120);
        this.setPrefWidth(paneWidth);
        this.setPrefHeight(paneHeight);
        drawNode(steps, arr, rootIdx, 0, paneWidth, V_GAP, null, maxStepIdx);
    }

    private void drawNode(java.util.List<MaxSubarrayModel.Step> steps, int[] arr, int stepIdx, double xMin, double xMax, double y, double[] parentCenter, int maxStepIdx) {
        if (stepIdx < 0 || stepIdx > maxStepIdx || stepIdx >= steps.size()) return;
        MaxSubarrayModel.Step step = steps.get(stepIdx);
        double x = (xMin + xMax) / 2;
        // Dibuja línea al padre
        if (parentCenter != null) {
            Line line = new Line(parentCenter[0], parentCenter[1], x, y);
            line.setStroke(Color.GRAY);
            this.getChildren().add(line);
        }
        // Texto informativo (una línea por cada dato)
        String[] lines = {
            "[" + java.util.stream.IntStream.rangeClosed(step.left, step.right).mapToObj(i -> String.valueOf(arr[i])).reduce((a,b)->a+", "+b).orElse("") + "]",
            "Suma: " + step.maxSum,
            "División en mid: " + step.mid,
            "Izq: " + step.leftSum + " Der: " + step.rightSum + " Cruza: " + step.crossSum
        };
        // Calcular ancho y alto del nodo según el texto
        double maxTextWidth = 0;
        double lineHeight = 16;
        for (String line : lines) {
            Text t = new Text(line);
            t.setStyle("-fx-font-size: 12px;");
            maxTextWidth = Math.max(maxTextWidth, t.getLayoutBounds().getWidth());
        }
        double nodeWidth = Math.max(NODE_WIDTH, maxTextWidth + 16);
        double nodeHeight = Math.max(NODE_HEIGHT, lines.length * lineHeight + 16);
        // Dibuja el nodo (rectángulo)
        Rectangle rect = new Rectangle(x - nodeWidth/2, y, nodeWidth, nodeHeight);
        rect.setArcWidth(16);
        rect.setArcHeight(16);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.DARKBLUE);
        rect.setStrokeWidth(2);
        this.getChildren().add(rect);
        // Dibuja el texto línea por línea
        double textY = y + 20;
        for (String line : lines) {
            Text t = new Text(x - nodeWidth/2 + 8, textY, line);
            t.setStyle("-fx-font-size: 12px;");
            this.getChildren().add(t);
            textY += lineHeight;
        }
        // Si es hoja, muestra retornar a la izquierda, centrado verticalmente
        if (step.left == step.right) {
            double retY = y + nodeHeight/2 + 6;
            Text ret = new Text(x - nodeWidth/2 - 100, retY, "retornar " + arr[step.left]);
            ret.setFill(Color.DARKGREEN);
            ret.setStyle("-fx-font-size: 12px;");
            this.getChildren().add(ret);
            return;
        }
        // Busca los hijos (pasos que cubren los rangos izquierdo y derecho)
        int leftChild = -1, rightChild = -1;
        for (int i = stepIdx - 1; i >= 0; i--) {
            MaxSubarrayModel.Step s = steps.get(i);
            if (s.left == step.left && s.right == step.mid) leftChild = i;
            if (s.left == step.mid + 1 && s.right == step.right) rightChild = i;
            if (leftChild != -1 && rightChild != -1) break;
        }
        double childY = y + nodeHeight + V_GAP;
        double midX = (xMin + xMax) / 2;
        // Conexión a hijos: del borde inferior del padre al borde superior del hijo
        if (leftChild != -1) {
            @SuppressWarnings("unused")
            double childX = (xMin + midX) / 2;
            @SuppressWarnings("unused")
            double childNodeWidth = nodeWidth; // Aproximación: mismo ancho
            drawNode(steps, arr, leftChild, xMin, midX, childY, new double[]{x, y + nodeHeight}, maxStepIdx);
        }
        if (rightChild != -1) {
            @SuppressWarnings("unused")
            double childX = (midX + xMax) / 2;
            @SuppressWarnings("unused")
            double childNodeWidth = nodeWidth; // Aproximación: mismo ancho
            drawNode(steps, arr, rightChild, midX, xMax, childY, new double[]{x, y + nodeHeight}, maxStepIdx);
        }
    }
} 