package mlos.sgl.demo.hoeyshamos;

import static mlos.sgl.core.Geometry.properIntersect;

import java.util.Collection;

import com.google.common.collect.Lists;

import mlos.sgl.core.Geometry;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;

public class HoeyShamos {
    
    public interface EventListener {
        
        void intersection(Vec2d point, Segment a, Segment b);
        
        void finished();
    }

    private EventListener listener;
    
    
    private final Collection<Segment> segments;
    
    public HoeyShamos(Iterable<Segment> segments) {
        this.segments = Lists.newArrayList(segments);
    }
    
    public void setListener(EventListener listener) {
        this.listener = listener;
    }
    
    
    public void run() {
    
        for (Segment s : segments) {
            for (Segment t : segments) {
                if (properIntersect(s, t)) {
                    Vec2d p = Geometry.intersectionPoint(s, t);
                    listener.intersection(p, s, t);
                }
            }
        }
        
        listener.finished();
    }

}
