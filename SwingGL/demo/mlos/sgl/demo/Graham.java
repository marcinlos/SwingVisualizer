package mlos.sgl.demo;

import static mlos.sgl.core.Geometry.distSq;
import static mlos.sgl.core.Geometry.orient2d;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import mlos.sgl.Scene;
import mlos.sgl.core.Vec2d;

public class Graham extends ConvexHullAlgorithm {
    
    public interface EventsListener {
        
        void foundBase(Vec2d v);
        
        void sorted(Vec2d[] points);
        
        void initStack(Vec2d p0, Vec2d p1, Vec2d p2);
        
        void beforeIter();
        
        void nextPoint(Vec2d p);
        
        void push(Vec2d p);
        
        void pop();
        
        void afterIter();
        
        void finished();
        
    }
    
    private EventsListener listener;

    private final Deque<Vec2d> stack = new ArrayDeque<>();

    public Graham(Scene scene, Collection<Vec2d> objects) {
        super(objects); 
        addListener(new GrahamVisualizer(scene));
    }
    
    public void addListener(EventsListener listener) {
        this.listener = listener;
    }
    
    private boolean betterBase(Vec2d a, Vec2d b) {
        return a.y < b.y || a.y == b.y && a.x < b.x;
    }
    
    private int findMin() {
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
    
    public Collection<Vec2d> compute() {
        int idx = findMin();
        swap(0, idx);
        
        Vec2d base = V[0];
        listener.foundBase(base);
        
        CCWComparator cmp = new CCWComparator(base);
        Arrays.sort(V, 1, n, cmp);

//        int j = 1;
//        for (int i = 1; i < n; ++ i) {
//            if (Math.abs(orient2d(base, V[i], V[j - 1])) >= 1e-20) {
////            if (cmp.compare(V[i], V[j - 1]) != 0) {
//                V[j++] = V[i];
//            } else {
//                System.out.println("blaaah " + V[i]);
//            }
//        }
//        
//        Vec2d[] tmp = new Vec2d[j];
//        System.arraycopy(V, 0, tmp, 0, j);
//        
//        Vec2d[] V = tmp;
//        for (Vec2d v : V) {
//            System.out.println(v);
//        }
        
        int i = 3;
        
        initStack();
        
        while (i < n) {
            listener.beforeIter();
            listener.nextPoint(V[i]);
            
            Iterator<Vec2d> it = stack.iterator();
            Vec2d b = it.next(), a = it.next();

            double r = orient2d(a, b, V[i]);
            System.out.println("V[i] = " + V[i]);
//            if (r == 0) {
            if (Math.abs(r) < 1e-30) {
                double d1 = distSq(a, b);
                double d2 = distSq(a, V[i]);
                if (d2 > d1) {
                    pop();
                    push(V[i]);
                } 
                ++ i;
            } else if (r > 0) {
                push(V[i]);
                ++ i;
            } else if (r < 0) {
                pop();
            }
            listener.afterIter();
        }
        listener.finished();
        return stack;
    }

    private void initStack() {
        for (int i = 0; i < 3; ++ i) {
            stack.push(V[i]);
        }
        listener.initStack(V[0], V[1], V[2]);
    }

    private void push(Vec2d point) {
        stack.push(point);
        listener.push(point);
    }

    private void pop() {
        stack.pop();
        listener.pop();
    }

    
}