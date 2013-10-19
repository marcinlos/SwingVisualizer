package mlos.sgl.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public class SegmentPainter implements ObjectPainter {

    private final CanvasSegment segment;
    
    private static final float[] DASH = { 3.0f, 3.0f };

    public SegmentPainter(CanvasSegment segment) {
        this.segment = segment;
    }

    @Override
    public void paint(Transform toScreen, Graphics2D ctx) {
        Segment seg = segment.getSegment();
        Vec2d a = toScreen.apply(seg.a);
        Vec2d b = toScreen.apply(seg.b);
        Color color = getColor();
        

        ctx.setColor(color);
        Stroke stroke = createStroke();
        ctx.setStroke(stroke);
        ctx.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
    }

    private BasicStroke createStroke() {
        int thickness = segment.getThickness();
        
        if (segment.isDashed()) {
            return createDashed(thickness);
        }
        return new BasicStroke(thickness);
    }

    private BasicStroke createDashed(int thickness) {
        return new BasicStroke(thickness, BasicStroke.CAP_ROUND, 
                BasicStroke.JOIN_BEVEL, 10, DASH, 0);
    }

    private Color getColor() {
        if (segment.isSelected()) {
            return segment.getSelectedColor();
        } else if (segment.isHover()) {
            return segment.getHoverColor();
        } else {
            return segment.getColor();
        }
    }

    @Override
    public CanvasSegment getObject() {
        return segment;
    }

}
