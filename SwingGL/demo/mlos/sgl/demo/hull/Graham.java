package mlos.sgl.demo.hull;

import static mlos.sgl.core.Geometry.orient2d;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import mlos.sgl.core.Vec2d;

public class Graham extends ConvexHullAlgorithm {
    
    public interface EventsListener {
        
        void foundBase(Vec2d v);
        
        void sorted(Vec2d[] points, int n);
        
        void initStack(Vec2d p0, Vec2d p1, Vec2d p2);
        
        void beforeIter();
        
        void nextPoint(Vec2d p);
        
        void push(Vec2d p);
        
        void pop();
        
        void afterIter();
        
        void finished();
        
    }
    
    private static final EventsListener NOOP = new EventsListener() {
        
        @Override
        public void sorted(Vec2d[] points, int n) { }
        
        @Override
        public void push(Vec2d p) { }
        
        @Override
        public void pop() { }
        
        @Override
        public void nextPoint(Vec2d p) { }
        
        @Override
        public void initStack(Vec2d p0, Vec2d p1, Vec2d p2) { }
        
        @Override
        public void foundBase(Vec2d v) { }
        
        @Override
        public void finished() { }
        
        @Override
        public void beforeIter() { }
        
        @Override
        public void afterIter() { }
    };
    
    private EventsListener listener = NOOP;

    private final Deque<Vec2d> stack = new ArrayDeque<>();

    public Graham(Collection<Vec2d> objects) {
        super(objects); 
    }
    
    public void addListener(EventsListener listener) {
        this.listener = listener;
    }
    
    private boolean betterBase(Vec2d a, Vec2d b) {
        return a.y < b.y || a.y == b.y && a.x < b.x;
    }
    
    private int findMinIndex() {
        int idx = 0;
        for (int i = 1; i < n; ++ i) {
            if (betterBase(V[i], V[idx])) {
                idx = i;
            }
        }
        return idx;
    }
    
    private void swap(int a, int b) {
        Vec2d tmp = V[a];
        V[a] = V[b];
        V[b] = tmp;
    }
    
    private void push(Vec2d point) {
        stack.push(point);
        listener.push(point);
    }

    private void pop() {
        stack.pop();
        listener.pop();
    }

    
    public Collection<Vec2d> compute() {
        swap(0, findMinIndex());
        
        Vec2d base = V[0];
        listener.foundBase(base);
        
        CCWComparator cmp = new CCWComparator(base);
        Arrays.sort(V, 1, n, cmp);

        int j = removeRedundant();
        initStack();
        
        for (int i = 3; i <= j; ) {
            listener.beforeIter();
            listener.nextPoint(V[i]);
            
            Iterator<Vec2d> it = stack.iterator();
            Vec2d b = it.next(), a = it.next();

            if (orient2d(a, b, V[i]) > 0) {
                push(V[i++]);
            } else  {
                pop();
            }
            listener.afterIter();
        }
        listener.finished();
        return stack;
    }

    private int removeRedundant() {
        int j = 1;
        for (int i = 2; i < n; ++ i) {
            if (orient2d(V[0], V[j], V[i]) != 0) {
                ++ j;
            }
            V[j] = V[i];
        }
        return j;
    }

    private void initStack() {
        for (int i = 0; i < 3; ++ i) {
            stack.push(V[i]);
        }
        listener.initStack(V[0], V[1], V[2]);
    }

}