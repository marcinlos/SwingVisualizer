package mlos.sgl.demo;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import mlos.sgl.App;
import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Rect;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;
import mlos.sgl.io.Formats;
import mlos.sgl.io.Printer;
import mlos.sgl.ui.InputAdapter;
import mlos.sgl.util.Randomizer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class ConvexHull extends Scene {
    
    private Iterable<Vec2d> hull;
    
    private final Executor exec = Executors.newSingleThreadExecutor();

    public ConvexHull(String name, Iterable<Vec2d> points, double rx, double ry) {
        super(name);
        view.setViewport(Rect.aroundOrigin(rx, ry));
        
        for (Vec2d v : points) {
            CanvasPoint p = new CanvasPoint(v);
            p.setSize(6);
            p.setBorderSize(0);
            addObject(p);
        }
        
        handlerStack.pushBack(new InputAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_F5) {
                    run(graham);
                } else if (c == KeyEvent.VK_F6) {
                    run(jarvis);
                } else if (c == KeyEvent.VK_S) {
                    if (e.isControlDown()) {
                        saveHullToFile();
                    }
                }
            }
        });
    }
    
    private void saveHullToFile() {
        JFileChooser fileChooser = new JFileChooser();
        int val = fileChooser.showSaveDialog(canvasPanel);
        if (val == JFileChooser.APPROVE_OPTION) {
            try {
                saveHull(fileChooser.getSelectedFile());
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null, 
                        "Cannot write to file", "Error", 
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void saveHull(File selectedFile) throws IOException {
        Printer printer = new Printer(new FileOutputStream(selectedFile));
        printer.write(Formats.COORDS_SPACE_SEPARATED, hull);
    }
    
    private void printPolygon(Iterable<Vec2d> points) {
        Iterator<Vec2d> it = points.iterator();
        Vec2d p = it.next();
        while (it.hasNext()) {
            Vec2d q = it.next();
            CanvasSegment s = new CanvasSegment(new Segment(p, q));
            s.setZ(0.9);
            addObject(s);
            p = q;
        }
        Vec2d start = points.iterator().next();
        addSegment(p, start);
    }

    private interface HullAlgorithm {
        Collection<Vec2d> compute(Collection<Vec2d> points);
    }
    
    private final HullAlgorithm graham = new HullAlgorithm() {
        @Override
        public Collection<Vec2d> compute(Collection<Vec2d> points) {
            return new Graham(ConvexHull.this, points).compute();
        }
    };
    
    private final HullAlgorithm jarvis = new HullAlgorithm() {
        @Override
        public Collection<Vec2d> compute(Collection<Vec2d> points) {
            return new Jarvis(points).compute();
        }
    };
    
    private Map<Vec2d, CanvasPoint> extractPoints(Set<CanvasObject> objects) {
        Map<Vec2d, CanvasPoint> points = new HashMap<>();
        for (CanvasObject object : objects) {
            if (object instanceof CanvasPoint) {
                CanvasPoint p = (CanvasPoint) object;
                points.put(p.getPoint(), p);
            }
        }
        return points;
    }
    
    private void run(final HullAlgorithm algorithm) {
        Set<CanvasObject> objects = canvas.getObjects();
        final Map<Vec2d, CanvasPoint> points = extractPoints(objects);

        exec.execute(new Runnable() {
            @Override
            public void run() {
                Iterable<Vec2d> hull = algorithm.compute(points.keySet());
                displayHull(points, hull);
                
            }
        });
    }
    
    private void displayHull(Map<Vec2d, CanvasPoint> points, Iterable<Vec2d> hull) {
        for (Vec2d p : hull) {
            CanvasPoint point = points.get(p);
            point.setColor(Color.blue);
        }
        printPolygon(hull);
        refresh();
    }
    
    public static void main(String[] args) {
        Scene s1 = new ConvexHull("1e2", Randomizer.inSquare(100).list(100), 120, 120);
        Scene s2 = new ConvexHull("circle", Randomizer.onCircle(10).list(100), 12, 12);
        
        Scene s3 = createScene3();
        Scene s4 = createScene4();
        
        Scene[] scenes = { s1, s2, s3, s4 };
        App.create(scenes);
    }

    private static Scene createScene4() {
        Rect r = Rect.bounds(0, 0, 10, 10);
        
        Vec2d lb = r.leftBottom();
        Vec2d rb = r.rightBottom();
        Vec2d rt = r.rightTop();
        Vec2d lt = r.leftTop();
        
        Iterable<Vec2d> points4 = Iterables.concat(
            Randomizer.onSegment(lb, lt).list(25),
            Randomizer.onSegment(lb, rb).list(25),
            Randomizer.onSegment(lb, rt).list(20),
            Randomizer.onSegment(lt, rb).list(20),
            ImmutableList.of(lb, lt, rb, rt)
        );
        Scene s4 = new ConvexHull("square+diag", points4, 12, 12);
        s4.getView().setViewport(Rect.bounds(-2, -2, 12, 12));
        return s4;
    }

    private static Scene createScene3() {
        Vec2d[] points = {
            new Vec2d(-10, 10),
            new Vec2d(-10, -10),
            new Vec2d(10, -10),
            new Vec2d(10, 10),
            
            new Vec2d(-10, 10),
        };
        List<Vec2d> points3 = Randomizer.onPoly(points).list(100);
        Scene s3 = new ConvexHull("square", points3, 12, 12);
        return s3;
    }

}
