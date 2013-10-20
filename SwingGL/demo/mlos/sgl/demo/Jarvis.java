package mlos.sgl.demo;

import static mlos.sgl.core.Geometry.distSq;
import static mlos.sgl.core.Geometry.orient2d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mlos.sgl.core.Vec2d;

public class Jarvis extends ConvexHullAlgorithm {
    
    public interface EventsListener {
        
        void foundBase(Vec2d v);
        
        void beforeIter();
        
        void firstCandidate(Vec2d v);
        
        void newCandidate(Vec2d v);
        
        void wasWorse();
        
        void wasBetter();
        
        void foundNextPoint(Vec2d v);
        
        void afterIter();
        
        void finished();
        
    }
    
    private EventsListener listener;
    
    public Jarvis(Collection<Vec2d> objects) {
        super(objects);    
    }
    
    public void setListener(EventsListener listener) {
        this.listener = listener;
    }
    
    private boolean betterBase(Vec2d a, Vec2d b) {
        return a.y < b.y || a.y == b.y && a.x < b.x;
    }
    
    private int findLowest() {
        int idx = 0;
        for (int i = 1; i < n; ++ i) {
            if (betterBase(V[i], V[idx])) {
                idx = i;
            }
        }
        return idx;
    }
    
    private boolean cwOrOnRay(Vec2d a, Vec2d b, Vec2d p) {
        double r = orient2d(a, b, p);
        return r < 0 || r == 0 && distSq(a, b) < distSq(a, p);
    }
    
    public Collection<Vec2d> compute() {
        int first = findLowest();
        
        List<Vec2d> hull = new ArrayList<>();
        hull.add(V[first]);
        listener.foundBase(V[first]);

        int i = first;
        do {
            listener.beforeIter();
            int k = (i + 1) % n;
            listener.firstCandidate(V[k]);
            for (int c = 2; c < n; ++ c) {
                int j = (i + c) % n;
                listener.newCandidate(V[j]);
                if (cwOrOnRay(V[i], V[k], V[j])) {
                    k = j;
                    listener.wasBetter();
                } else {
                    listener.wasWorse();
                }
            }
            hull.add(V[k]);
            i = k;
            listener.foundNextPoint(V[k]);
            listener.afterIter();
        } while (i != first);
        
        listener.finished();
        return hull;
    }

}
