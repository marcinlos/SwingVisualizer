package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;

import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public class CanvasPanel extends JPanel {

    private Transform normToScreen;

    private Transform screenToNorm;

    /** Painter drawing content */
    private Painter painter;

    /**
     * Creates new canvas panel.
     */
    public CanvasPanel() {
        setDoubleBuffered(true);
        setBackground(Color.white);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        
        addComponentListener(new ComponentAdapter() {
            
            @Override
            public void componentShown(ComponentEvent e) {
                requestFocusInWindow();
                recomputeTransform();
            }
            
            @Override
            public void componentResized(ComponentEvent e) {
                recomputeTransform();
            }

        });
    }
    
    /**
     * Specifies the painter used to draw panel content.
     * 
     * @param painter
     *            Painter used to draw canvas content
     */
    public void setPainter(Painter painter) {
        this.painter = checkNotNull(painter);
    }

    /**
     * Causes canvas repaint.
     */
    public void refresh() {
        repaint();
    }

    private void recomputeTransform() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        
        Transform.Builder builder = new Transform.Builder()
                .flipY()
                .t(1, 1)
                .s(panelWidth / 2.0, panelHeight / 2.0);

        normToScreen = builder.create();
        screenToNorm = builder.invert().create();
    }

    public Transform normToScreen() {
        return normToScreen;
    }

    public Transform screenToNorm() {
        return screenToNorm;
    }

    /**
     * Transforms the coordinates in the normalized space to the screen
     * coordinates.
     * 
     * @param p
     *            ScreenPoint in virtual coordinates
     * @return Vec2d in screen coordinates
     */
    public Vec2d toScreen(Vec2d p) {
        return normToScreen.apply(p);
    }

    /**
     * Transforms screen coordinates to the coordinates in the normalized space.
     * 
     * @param p
     *            ScreenPoint in screen coordinates
     * @return ScreenPoint in virtual coordinates
     */
    public Vec2d toNorm(Vec2d p) {
        return screenToNorm.apply(p);
    }

    @Override
    protected final void paintComponent(Graphics g) {
        super.paintComponent(g);
        recomputeTransform();
        if (painter != null) {
            Graphics2D graphics = (Graphics2D) g;
            painter.paint(normToScreen, graphics);
        }
    }

}
