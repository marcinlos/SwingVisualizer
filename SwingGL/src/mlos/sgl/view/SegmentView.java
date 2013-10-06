package mlos.sgl.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Point;
import mlos.sgl.core.Segment;

public class SegmentView implements ObjectView {

    private final CanvasSegment segment;
    
    public SegmentView(CanvasSegment segment) {
        this.segment = segment;
    }

    @Override
    public void paint(CanvasPanel panel, Graphics2D ctx) {
        Segment seg = segment.getSegment();
        Point a = panel.toScreen(seg.a);
        Point b = panel.toScreen(seg.b);
        Color color = getColor();
        int thickness = segment.getThickness();
        
        ctx.setColor(color);
        Stroke stroke = new BasicStroke(thickness);
        ctx.setStroke(stroke);
        ctx.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);
    }
    
    private Color getColor() {
        if (segment.isHover()) {
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
