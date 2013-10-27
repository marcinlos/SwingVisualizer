package mlos.sgl.demo.monotonic;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import mlos.sgl.App;
import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPolygon;
import mlos.sgl.core.Polygon;
import mlos.sgl.ui.InputAdapter;

public class MonotonicPolygons extends Scene {

    private final Executor exec = Executors.newSingleThreadExecutor();

    private final class PolyProcessor implements Runnable {

        private final Polygon poly;

        public PolyProcessor(Polygon poly) {
            this.poly = poly;
        }

        @Override
        public void run() {
            ClassifyVertices classifier = new ClassifyVertices(poly.vs);
            ClassificationVisualizer visualizer = new ClassificationVisualizer(
                    MonotonicPolygons.this);
            classifier.setListener(visualizer);
            
            classifier.classify();
        }
    }

    public MonotonicPolygons() {
        super("triangulation");

        handlerStack.pushBack(new InputAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int c = e.getKeyCode();
                if (c == KeyEvent.VK_F5) {
                    for (Polygon poly : extractPolys()) {
                        exec.execute(new PolyProcessor(poly));
                    }
                }
            }
        });
    }

    private List<Polygon> extractPolys() {
        List<Polygon> polys = new ArrayList<>();

        for (CanvasObject object : canvas.getObjects()) {
            if (object instanceof CanvasPolygon) {
                CanvasPolygon poly = (CanvasPolygon) object;
                polys.add(new Polygon(poly.getPoints()));
            }
        }
        return polys;
    }

    public static void main(String[] args) {
        App.create(new MonotonicPolygons());
    }

}
