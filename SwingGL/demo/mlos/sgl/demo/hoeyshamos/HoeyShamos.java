package mlos.sgl.demo.hoeyshamos;

import static mlos.sgl.core.Geometry.properIntersect;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.NavigableSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

public class HoeyShamos {
    
    public interface EventListener {
        
        void beginEvent(Event e);
        
        void newEvent(Event e);
        
        void lineMoved(double x);
        
        void checking(Segment p, Segment q);
        
        void finished();
    }
    
    private EventListener listener;
    
    // Evil, stateful comparator - use with extreme caution!
    private class YComparator implements Comparator<Segment> {
        
        private double x;
        
        public void setPos(double x) {
            this.x = x;
        }
        
        public double getPos() {
            return x;
        }
        
        private double calcY(Segment s) {
            double t = (x - s.a.x) / (s.b.x - s.a.x);
            return (1 - t) * s.a.y + t * s.b.y;
        }

        @Override
        public int compare(Segment o1, Segment o2) {
            return Double.compare(calcY(o1), calcY(o2));
        }
        
    }
    
    private final YComparator comparator = new YComparator();
    
    private final Collection<Segment> segments;
    
    private final Queue<Event> Q = new PriorityQueue<>();
    private final NavigableSet<Segment> T = new TreeSet<>(comparator);
    
    private final Set<Vec2d> intersections = new HashSet<>();
    
    public HoeyShamos(Iterable<Segment> segments) {
        this.segments = Lists.newArrayList(segments);
    }
    
    public void setListener(EventListener listener) {
        this.listener = listener;
    }
    
    private void addEvent(Event e) {
        boolean shouldAdd = true;
        if (e.isIntersection()) {
            if (e.point.x < comparator.getPos()) {
                shouldAdd = false;
            }
        }
        if (shouldAdd) {
            Q.add(e);
            System.out.println("Added " + e);
            listener.newEvent(e);
        }
    }


    private Event nextEvent() {
        Event e = Q.poll();
        System.out.println("Removing " + e);
        return e;
    }

    
    public void run() {
        
        for (Segment s : segments) {
            addEvent(Event.begin(s));
            addEvent(Event.end(s));
        }
        
        while (! Q.isEmpty()) {
            Event e = nextEvent();
            listener.beginEvent(e);
             
            if (e.isBegin()) {
                moveLine(e.point.x);
                Segment s = e.p;
                T.add(e.p);
                
                chekForIntersection(s, T.lower(s));
                chekForIntersection(s, T.higher(s));
                
            } else if (e.isEnd()) {
                Segment s = e.p;
                chekForIntersection(T.lower(s), T.higher(s));
                T.remove(s);
                moveLine(e.point.x);
            } else if (e.isIntersection()) {
                Segment s1 = T.lower(e.p);
                Segment s2 = T.higher(e.q);
                chekForIntersection(e.p, s2);
                chekForIntersection(e.q, s1);
                
                T.remove(e.p);
                T.remove(e.q);
                moveLine(e.point.x);
                halfStep();
                T.add(e.p);
                T.add(e.q);
            }
        }
        System.out.println(intersections);
        listener.finished();
    }

    private void chekForIntersection(Segment p, Segment q) {
        if (p != null && q != null) {
            listener.checking(p, q);
            System.out.println("Checking " + p + " vs " + q);
        }
        if (p != null && q != null && properIntersect(p, q)) {
            Ordering<Segment> order = Ordering.from(comparator);
            Segment first = order.min(p, q);
            Segment second = order.max(p, q);
            Event e = Event.intersection(first, second);
            if (intersections.add(e.point)) {
                addEvent(e);
            }
        }
    }
    
    private void moveLine(double x) {
        comparator.setPos(x);
        listener.lineMoved(x);
    }
    
    private void halfStep() {
        double x = comparator.getPos();
        double next = Q.isEmpty() ? x + 1 : Q.peek().point.x;
        double t = 1.0 / 1024;
        moveLine((1 - t) * x + t * next);
    }

}
