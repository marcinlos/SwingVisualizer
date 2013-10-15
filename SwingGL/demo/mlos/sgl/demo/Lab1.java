package mlos.sgl.demo;

import java.awt.Color;

import mlos.sgl.App;
import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.Rect;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;
import mlos.sgl.util.Randomizer;

public class Lab1 extends Scene {
    
    private static double orient2d(Vec2d a, Vec2d b, Vec2d c) {
        return a.x * b.y - a.y * b.x 
                - b.x * c.y + b.y * c.x 
                + a.x * c.y - a.y * c.x;
    }

    public Lab1(String name, Iterable<Vec2d> points, Segment seg, double rx, double ry) {
        super(name);

        view().setViewport(Rect.aroundOrigin(rx, ry));
//        view().append(Transforms.r(0.5));
        
        for (Vec2d v : points) {
            CanvasPoint p = new CanvasPoint(v);
            double d = orient2d(seg.a, seg.b, v);
            Color c = d > 0 ? Color.blue: d < 0 ? Color.green: Color.red;
            p.setSize(d == 0 ? 15 : 7);
            p.setBorderSize(0);
            p.setColor(c);
            addObject(p);
        }
    }
    
    public static void main(String[] args) {
        Segment s = new Segment(new Vec2d(-1, 0), new Vec2d(1, 0.1));
        
        double r = 1000;
        Vec2d a = new Vec2d(r * -1, r * -0.05 + 0.05);
        Vec2d b = new Vec2d(r * 1, r * 0.05 + 0.05);
        
        Segment line = new Segment(a, b);
        
        Scene[] scenes = {
            new Lab1("1e2", Randomizer.inSquare(100).list(1000), s, 120, 120),
            new Lab1("1e14", Randomizer.inSquare(1e14).list(1000), s, 1.2e14, 1.2e14),
            new Lab1("circle", Randomizer.onCircle(100).list(1000), s, 120, 120),
            new Lab1("line", Randomizer.onSegment(line).list(1000), s, 1000, 70)
        };
        App.create(scenes);
    }

}
