package mlos.sgl.demo;

import java.awt.Color;
import java.util.List;

import javax.swing.SwingUtilities;

import mlos.sgl.MainWindow;
import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.Rect;
import mlos.sgl.core.Vec2d;
import mlos.sgl.util.Randomizer;

public class Main {
    
    public static void setup() {
        MainWindow window = new MainWindow(1, 1);
        window.addScene(new Scene("Test") {
        
            private Vec2d last;
            
            private void point(double x, double y) {
                Vec2d n = new Vec2d(x, y);
                if (last != null) {
                    addSegment(last, n);
                }
                last = n;
            }
            
            {
           // addObject(new CanvasPoint(new Vec2d(0.5, 0.5)));
           // addObject(new CanvasSegment(new Segment(new Vec2d(-0.1, 0.1), new Vec2d(0.7, 0.4))));
            
//            point(-0.1,0);
//            point(-0.1,-0.1);
//            point(-0.2,-0.1);
//            point(-0.2,0);
//            point(-0.1,0);
//            
//            point(-0.1,0.6);
//            point(0.1,0.6);
//            point(0.1,0);
//            
//            point(0.1,-0.1);
//            point(0.2,-0.1);
//            point(0.2,0);
//            point(0.1,0);
//            
//            addObject(new CanvasPoint(new Vec2d(0, 0.7)));
//            addObject(new CanvasPoint(new Vec2d(0.1, 0.8)));
            
                List<Vec2d> vs = Randomizer.inRect(Rect.aroundOrigin(0.5, 0.5)).list(800);
                for (Vec2d v : vs) {
                    CanvasPoint p = new CanvasPoint(v);
                    p.setColor(Color.cyan);
                    addObject(p);
                }
                
                List<Vec2d> vss = Randomizer.onCircle(0.5).list(140);
                for (Vec2d v : vss) {
                    CanvasPoint p = new CanvasPoint(v);
                    addObject(p);
                }
                
            }
        });
        
        window.addScene(new Scene("Demo") {
            
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setup();
            }
        });
    }

}
