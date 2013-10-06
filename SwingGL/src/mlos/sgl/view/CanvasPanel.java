package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import mlos.sgl.core.Vec2d;
import mlos.sgl.core.ScreenTransform;

public class CanvasPanel implements ScreenTransform {

    /** Virtual width of the canvas */
    private double width;

    /** Virtual height of the canvas */
    private double height;
    
//    /** Virtual coordinate system of the canvas */
//    private Rect viewport;

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
        this.painter = checkNotNull(painter);
        doSetWidth(width);
        doSetHeight(height);

        swingPanel.setBackground(Color.white);
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
        checkArgument(width > 0, "Width %s must be positive", width);
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
        checkArgument(height > 0, "Height %s must be positive", height);
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
     * {@inheritDoc}
     */
    @Override
    public Vec2d toScreen(Vec2d p) {
        return toScreen(p.x, p.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec2d toScreen(double x, double y) {
        int sw = getScreenWidth();
        int sh = getScreenHeight();
        double invy = height - y;
        int screenx = (int) (x * sw / width);
        int screeny = (int) (invy * sh / height);
        return new Vec2d(screenx, screeny);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec2d toVirtual(double x, double y) {
        int sw = getScreenWidth();
        int sh = getScreenHeight();
        double invy = sh - y;
        double vx = width * x / sw;
        double vy = height * invy / sh;
        return new Vec2d(vx, vy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec2d toVirtual(Vec2d p) {
        return toVirtual(p.x, p.y);
    }
    
    public Painter getPainter() {
        return painter;
    }

    /**
     * Sets the current painter used to draw panel content.
     * 
     * @param painter
     *            Painter to paint panel with
     */
    public void setPainter(Painter painter) {
        this.painter = checkNotNull(painter);
    }

    public void addMouseListener(MouseListener listener) {
        swingPanel.addMouseListener(checkNotNull(listener));
    }

    public void addMouseMotionListener(MouseMotionListener listener) {
        swingPanel.addMouseMotionListener(checkNotNull(listener));
    }

    public void addMouseWheelListener(MouseWheelListener listener) {
        swingPanel.addMouseWheelListener(checkNotNull(listener));
    }

    public void addKeyListener(KeyListener listener) {
        swingPanel.addKeyListener(checkNotNull(listener));
    }

}
