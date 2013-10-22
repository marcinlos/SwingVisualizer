package mlos.sgl.demo.hoeyshamos;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import mlos.sgl.App;
import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Rect;
import mlos.sgl.core.Segment;
import mlos.sgl.ui.InputAdapter;
import mlos.sgl.util.Randomizer;

import com.google.common.collect.ImmutableList;

public class SegmentIntersection extends Scene {
    
    private final Executor exec = Executors.newSingleThreadExecutor();

    public SegmentIntersection(String name, Iterable<Segment> segments, 
            double rx, double ry) {
        super(name);
        view.setViewport(rx, ry);
        
        for (Segment s : segments) {
            CanvasSegment canvasSegment = new CanvasSegment(s);
            addObject(canvasSegment);
        }
        
        handlerStack.pushBack(new InputAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    run(findSegments());
                }
            }
        });
    }
    
    public Collection<Segment> findSegments() {
        List<Segment> segments = new ArrayList<>();
        for (CanvasObject object : canvas.getObjects()) {
            if (object instanceof CanvasSegment) {
                CanvasSegment cs = (CanvasSegment) object;
                segments.add(cs.getSegment());
            }
        }
        return segments;
    }
    
    private void run(final Iterable<Segment> segments) {
        exec.execute(new Runnable() {
            
            @Override
            public void run() {
                HoeyShamos alg = new HoeyShamos(segments);
                alg.setListener(new HoeyShamosVisualizer(SegmentIntersection.this));
                alg.run();
            }
        });
    }

    public static void main(String[] args) {
        ImmutableList<Segment> emptyList = ImmutableList.of();
        SegmentIntersection empty = new SegmentIntersection("Empty", emptyList, 1, 1);
        
        Iterable<Segment> randList = Randomizer
                .inRect(Rect.aroundOrigin(10))
//                .onCircle(10)
                .segments()
                .list(20);
        
        SegmentIntersection rand = new SegmentIntersection("In rect", randList, 12, 12);
        rand.getView().setViewport(Rect.aroundOrigin(12));
        
        App.create(empty, rand);
    }
    
}
