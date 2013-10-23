package mlos.sgl.demo.hoeyshamos;

import static com.google.common.base.Preconditions.checkNotNull;
import static mlos.sgl.core.Geometry.intersectionPoint;

import java.util.Objects;

import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;

public class Event implements Comparable<Event> {
    
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
        boolean ordered = p.a.x < q.a.x;
        Segment pp = ordered ? p : q;
        Segment qq = ordered ? q : p;
        Vec2d v = intersectionPoint(pp, qq);
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
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Event) {
            Event other = (Event) o;
            return type.equals(other.type) 
                    && point.equals(other.point)
                    && p.equals(other.p) 
                    && q.equals(other.q);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return String.format("Event[%s]: at %s (segments: %s, %s)", type, point, p, q);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, point, p, q);
    }

    @Override
    public int compareTo(Event o) {
        return Double.compare(point.x, o.point.x);
    }
    
}
