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

import mlos.sgl.core.ScreenTransform;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public class CanvasPanel implements ScreenTransform {

    /** Virtual width of the canvas */
    private double width;

    /** Virtual height of the canvas */
    private double height;
    
    private Transform toScreen;
    
    private Transform toVirtual;
    
    /** Painter drawing content */
    private Painter painter;

    private final class SwingPanelAdapter extends JPanel {
        @Override
        protected final void paintComponent(Graphics g) {
            super.paintComponent(g);
            recomputeTransform();
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
    
    private void recomputeTransform() {
        int sw = getScreenWidth();
        int sh = getScreenHeight();
        Transform.Builder builder = new Transform.Builder()
            .flipY()
            .tY(height)
            .s(sw / width, sh / height);
        
        toScreen = builder.create();
        toVirtual = builder.invert().create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec2d toScreen(Vec2d p) {
        return toScreen.apply(p);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec2d toScreen(double x, double y) {
        return toScreen(new Vec2d(x, y));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec2d toVirtual(double x, double y) {
        return toVirtual(new Vec2d(x, y));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Vec2d toVirtual(Vec2d p) {
        return toVirtual.apply(p);
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
