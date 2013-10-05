package mlos.sgl.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import mlos.sgl.core.Point;
import mlos.sgl.model.CanvasPoint;

public class PointView implements View {
    
    private CanvasPoint point;

    public PointView(CanvasPoint point) {
        this.point = point;
    }

    @Override
    public void paint(CanvasPanel canvas, Graphics2D ctx) {
        Point p = point.getPoint();
        int size = point.getSize();
        Color color = point.getColor();

        ScreenPoint s = canvas.toScreen(p);
        int hsize = size / 2;
        ctx.setColor(color);
        ctx.fillOval(s.x - hsize, s.y - hsize, size, size);

        ctx.setColor(point.getBorderColor());
        Stroke stroke = new BasicStroke(point.getBorderSize());
        ctx.setStroke(stroke);
        ctx.drawOval(s.x - hsize, s.y - hsize, size, size);
    }

    @Override
    public CanvasPoint getObject() {
        return point;
    }

}
