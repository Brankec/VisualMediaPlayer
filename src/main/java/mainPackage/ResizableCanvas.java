package mainPackage;

import javafx.scene.canvas.Canvas;

public class ResizableCanvas extends Canvas {

    public ResizableCanvas() {
        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    @Override
    public void resize(double width, double height)
    {
        super.setWidth(width);
        super.setHeight(height);
    }

    private void draw() {
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}