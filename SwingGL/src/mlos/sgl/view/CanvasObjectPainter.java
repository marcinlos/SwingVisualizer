package mlos.sgl.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.canvas.CanvasVisitor;
import mlos.sgl.core.Point;
import mlos.sgl.core.Segment;

public class CanvasObjectPainter implements CanvasVisitor {

    private final CanvasPanel panel;
    
    private final Graphics2D ctx;
    
    public CanvasObjectPainter(CanvasPanel panel, Graphics2D ctx) {
        this.panel = panel;
        this.ctx = ctx;
    }
    
    @Override
    public void visit(CanvasPoint point) {
        Point p = point.getPoint();
        int size = point.getSize();
        Color color = point.getColor();

        ScreenPoint s = panel.toScreen(p);
        int hsize = size / 2;
        ctx.setColor(color);
        ctx.fillOval(s.x - hsize, s.y - hsize, size, size);

        ctx.setColor(point.getBorderColor());
        Stroke stroke = new BasicStroke(point.getBorderSize());
        ctx.setStroke(stroke);
        ctx.drawOval(s.x - hsize, s.y - hsize, size, size);
    }
    
    @Override
    public void visit(CanvasSegment segment) {
        Segment seg = segment.getSegment();
        ScreenPoint a = panel.toScreen(seg.a);
        ScreenPoint b = panel.toScreen(seg.b);
        Color color = segment.getColor();
        int thickness = segment.getThickness();
        
        ctx.setColor(color);
        Stroke stroke = new BasicStroke(thickness);
        ctx.setStroke(stroke);
        ctx.drawLine(a.x, a.y, b.x, b.y);
    }


}
