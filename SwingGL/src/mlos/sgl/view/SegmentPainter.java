package mlos.sgl.view;

import java.awt.Color;
import java.awt.Graphics2D;

import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Transform;

public class SegmentPainter implements ObjectPainter {

    private final CanvasSegment segment;
    
    public SegmentPainter(CanvasSegment segment) {
        this.segment = segment;
    }

    @Override
    public void paint(Transform toScreen, Graphics2D ctx) {
        Drawer d = new Drawer(ctx, toScreen);
        d.color(getColor());
        
        int width = segment.getThickness();
        if (segment.isDashed()) {
            d.dashed(width, 3, 4);
        } else {
            d.solid(width);
        }
        d.line(segment.getSegment());
        d.restore();
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
