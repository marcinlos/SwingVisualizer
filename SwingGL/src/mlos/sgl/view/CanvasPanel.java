package mlos.sgl.view;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import mlos.sgl.core.Vec2;

public class CanvasPanel {

    /** Virtual width of the canvas */
    private double width;

    /** Virtual height of the canvas */
    private double height;

    /** Painter drawing content */
    private Painter painter;

    private final class SwingPanelAdapter extends JPanel {
        @Override
        protected final void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics = (Graphics2D) g;
            painter.paint(CanvasPanel.this, graphics);
        }
    }

    private final JPanel swingPanel = new SwingPanelAdapter();

    /**
     * Creates new {@code 1 x 1} canvas.
     * 
     * @param painter
     *            Painter used to draw canvas content
     */
    public CanvasPanel(Painter painter) {
        this(painter, 1, 1);
    }

    /**
     * Creates new {@code width x height} canvas.
     * 
     * @param painter
     *            Painter used to draw canvas content
     * @param width
     *            Width of the canvas
     * @param height
     *            Height of the canvas
     */
    public CanvasPanel(Painter painter, double width, double height) {
        this.painter = painter;
        this.width = width;
        this.height = height;
    }

    public final JPanel swingPanel() {
        return swingPanel;
    }

    /**
     * Causes canvas repaint.
     */
    public void refresh() {
        swingPanel.repaint();
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        doSetWidth(width);
        refresh();
    }

    private void doSetWidth(double width) {
        // checkArgument(width > 0, "Width %s must be positive", width);
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        doSetHeight(height);
        refresh();
    }

    private void doSetHeight(double height) {
        // checkArgument(height > 0, "Height %s must be positive", height);
        this.height = height;
    }

    public void setSize(double width, double height) {
        doSetWidth(width);
        doSetHeight(height);
        refresh();
    }

    protected int getScreenWidth() {
        return swingPanel.getWidth();
    }

    protected int getScreenHeight() {
        return swingPanel.getHeight();
    }

    /**
     * Transforms the virtual coordinates to the screen coordinates.
     * 
     * @param p
     *            Point in virtual coordinates
     * @return Point in screen coordinates
     */
    public Point toScreen(Vec2 p) {
        return toScreen(p.x, p.y);
    }

    /**
     * Transforms the virtual coordinates to screen coordinates.
     * 
     * @param x
     *            Abcissa of the point
     * @param y
     *            Ordinate of the point
     * @return Point in screen coordinates
     */
    public Point toScreen(double x, double y) {
        int sw = getScreenWidth();
        int sh = getScreenHeight();
        double invy = height - y;
        int screenx = (int) (x * sw / width);
        int screeny = (int) (invy * sh / height);
        return new Point(screenx, screeny);
    }

    /**
     * Transforms screen coordinates to the virtual coordinates.
     * 
     * @param x
     *            Abcissa of the point
     * @param y
     *            Ordinate of the point
     * @return Point in virtual coordinates
     */
    public Vec2 toVirtual(int x, int y) {
        int sw = getScreenWidth();
        int sh = getScreenHeight();
        int invy = sh - y;
        double vx = width * x / sw;
        double vy = height * invy / sh;
        return new Vec2(vx, vy);
    }

    /**
     * Transforms screen coordinates to the virtual coordinates.
     * 
     * @param p
     *            Point in screen coordinates
     * @return Point in virtual coordinates
     */
    public Vec2 toVirtual(Point p) {
        return toVirtual(p.x, p.y);
    }

}
