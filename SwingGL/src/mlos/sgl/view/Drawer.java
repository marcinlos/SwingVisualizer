package mlos.sgl.view;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Arrays;
import java.util.Collection;

import mlos.sgl.core.Segment;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Transforms;
import mlos.sgl.core.Vec2d;

public class Drawer {
    
    private final Graphics2D gfx;
    private final Transform toScreen;
    
    private Color oldColor;
    private Stroke oldStroke;
    
    private class PointList {
        public final int n;
        public final int[] xs;
        public final int[] ys;
        
        public PointList(int n, int[] xs, int[] ys) {
            this.n = n;
            this.xs = xs;
            this.ys = ys;
        }
    }
    
    public Drawer(Graphics2D gfx) {
        this(gfx, Transforms.ID);
    }

    public Drawer(Graphics2D gfx, Transform toScreen) {
        this.gfx = gfx;
        this.toScreen = toScreen;
    }
    
    public Drawer color(float r, float g, float b) {
        return color(r, g, b, 1.0f);
    }
    
    public Drawer color(float r, float g, float b, float a) {
        return color(new Color(r, g, b, a));
    }
    
    public Drawer color(Color color) {
        oldColor = gfx.getColor();
        gfx.setColor(color);
        return this;
    }
    
    public Drawer stroke(Stroke stroke) {
        oldStroke = gfx.getStroke();
        gfx.setStroke(stroke);
        return this;
    }
    
    public Drawer width(float width) {
        return solid(width);
    }
    
    public Drawer solid(float width) {
        return stroke(new BasicStroke(width, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_BEVEL, 1));
    }
    
    public Drawer dashed(float width, float... dash) {
        Stroke s = new BasicStroke(width, BasicStroke.CAP_BUTT, 
                BasicStroke.JOIN_BEVEL, 1, dash, 0);
        return stroke(s);
    }
    
    public Drawer line(float x1, float y1, float x2, float y2) {
        return line(new Vec2d(x1, y1), new Vec2d(x2, y2));
    }
    
    public Drawer line(Vec2d a, Vec2d b) {
        return line(new Segment(a, b));
    }
    
    public Drawer line(Segment s) {
        Vec2d a = toScreen.apply(s.a);
        Vec2d b = toScreen.apply(s.b);

        int x1 = (int) a.x;
        int y1 = (int) a.y;
        int x2 = (int) b.x;
        int y2 = (int) b.y;
        
        gfx.drawLine(x1, y1, x2, y2);
        return this;
    }
    
    private PointList toPointList(Collection<Vec2d> points) {
        int n = points.size();
        
        int[] xs = new int[n];
        int[] ys = new int[n];

        int i = 0;
        for (Vec2d point : points) {
            Vec2d p = toScreen.apply(point);
            xs[i] = (int) p.x;
            ys[i] = (int) p.y;
            ++ i;
        }
        return new PointList(n, xs, ys);
    }
    
    public Drawer polyLine(Vec2d... points) {
        return polyLine(Arrays.asList(points));
    }
    
    public Drawer polyLine(Collection<Vec2d> points) {
        int n = points.size();
        checkArgument(n > 1, "polyLine needs at least 2 points, got %d", n);
        
        PointList p = toPointList(points);
        gfx.drawPolyline(p.xs, p.ys, p.n);
        return this;
    }
    
    public Drawer polygon(Vec2d... points) {
        return polygon(Arrays.asList(points));
    }
    
    public Drawer polygon(Collection<Vec2d> points) {
        int n = points.size();
        checkArgument(n > 2, "polygon needs at least 3 points, got %d", n);
        
        PointList p = toPointList(points);
        gfx.drawPolygon(p.xs, p.ys, p.n);
        return this;
    }
    
    public Drawer fillPolygon(Collection<Vec2d> points) {
        int n = points.size();
        checkArgument(n > 2, "polygon needs at least 3 points, got %d", n);
        
        PointList p = toPointList(points);
        gfx.fillPolygon(p.xs, p.ys, p.n);
        return this;
    }
    
    public Drawer oval(Vec2d v, double rx, double ry) {
        Vec2d rdir = toScreen.applyToDir(new Vec2d(rx, ry));
        Vec2d center = toScreen.apply(v);
        int w = (int) Math.abs(rdir.x);
        int h = (int) Math.abs(rdir.y);
        int x = (int) center.x - w;
        int y = (int) center.y - h;
        
        gfx.drawOval(x, y, 2 * w, 2 * h);
        return this;
    }

    public Drawer circle(Vec2d v, double r) {
        return oval(v, r, r);
    }
    
    public Drawer fillOval(Vec2d v, double rx, double ry) {
        Vec2d rdir = toScreen.applyToDir(new Vec2d(rx, ry));
        Vec2d center = toScreen.apply(v);
        int w = (int) Math.abs(rdir.x);
        int h = (int) Math.abs(rdir.y);
        int x = (int) center.x - w;
        int y = (int) center.y - h;
        gfx.fillOval(x, y, 2 * w, 2 * h);
        return this;
    }
    
    
    public Drawer fillCircle(Vec2d v, double r) {
        return fillOval(v, r, r);
    }
    
    public void restore() {
        if (oldColor != null) {
            gfx.setColor(oldColor);
            oldColor = null;
        }
        if (oldStroke != null) {
            gfx.setStroke(oldStroke);
            oldStroke = null;
        }
    }

}
