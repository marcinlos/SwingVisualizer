package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import mlos.sgl.core.ScreenTransform;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public class CanvasPanel extends JPanel implements ScreenTransform {

    private Transform toScreen;
    
    private Transform toVirtual;
    
    /** Painter drawing content */
    private final Painter painter;


    /**
     * Creates new {@code width x height} canvas.
     * 
     * @param painter
     *            Painter used to draw canvas content
     */
    public CanvasPanel(Painter painter) {
        this.painter = checkNotNull(painter);
        setBackground(Color.white);
    }

    /**
     * Causes canvas repaint.
     */
    public void refresh() {
        repaint();
    }
    
    private void recomputeTransform() {
        int sw = getWidth();
        int sh = getHeight();
        Transform.Builder builder = new Transform.Builder()
            .flipY()
            .t(1, 1)
            .s(sw / 2, sh / 2);
        
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
    public Vec2d toVirtual(Vec2d p) {
        return toVirtual.apply(p);
    }
    
    @Override
    protected final void paintComponent(Graphics g) {
        super.paintComponent(g);
        recomputeTransform();
        Graphics2D graphics = (Graphics2D) g;
        painter.paint(toScreen, graphics);
    }

}
