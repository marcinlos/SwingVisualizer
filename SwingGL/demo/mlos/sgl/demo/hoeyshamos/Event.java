package mlos.sgl.demo.hoeyshamos;

import static com.google.common.base.Preconditions.checkNotNull;
import static mlos.sgl.core.Geometry.intersectionPoint;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;

public class Event {
    
    public final EventType type;
    
    public final Vec2d point;
    public final Segment p;
    public final Segment q;

    public Event(EventType type, Vec2d point, Segment p, Segment q) {
        if (type == EventType.INTERSECTION) {
            checkNotNull(q);
        }
        
        this.type = checkNotNull(type);
        this.point = checkNotNull(point);
        this.p = checkNotNull(p);
        this.q = q;
    }        
    
    public static Event begin(Segment s) {
        return new Event(EventType.BEGIN, s.a, s, null);
    }
    
    public static Event end(Segment s) {
        return new Event(EventType.END, s.b, s, null);
    }
    
    public static Event intersection(Segment p, Segment q) {
        Vec2d v = intersectionPoint(p, q);
        return new Event(EventType.INTERSECTION, v, p, q);
    }
    
    public boolean isBegin() {
        return type == EventType.BEGIN;
    }
    
    public boolean isEnd() {
        return type == EventType.END;
    }

    public boolean isIntersection() {
        return type == EventType.INTERSECTION;
    }
    
}
