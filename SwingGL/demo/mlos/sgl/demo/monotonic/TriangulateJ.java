package mlos.sgl.demo.monotonic;

import static mlos.sgl.core.Geometry.ccw;
import static mlos.sgl.core.Geometry.cw;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import mlos.sgl.core.Polygon;
import mlos.sgl.core.Vec2d;

public class TriangulateJ {
    
    public interface EventListener {
        void foundInit(Vec2d v);
        void foundFinal(Vec2d v);
        void foundLeft(List<Vec2d> vs);
        void foundRight(List<Vec2d> vs);
        void start();
        void next(Vec2d v);
        void push(Vec2d v);
        void pop();
        void addSegment(Vec2d a, Vec2d b);
    }
    
    private enum Side {
        LEFT,
        RIGHT
    }
    
    private static final class VertexWithSide {
        public final Vec2d v;
        public final Side side;
        
        public VertexWithSide(Vec2d v, Side side) {
            this.v = v;
            this.side = side;
        }
    }
    
    private static final Comparator<VertexWithSide> CMP = new Comparator<VertexWithSide>() {
        @Override
        public int compare(VertexWithSide o1, VertexWithSide o2) {
            return -Double.compare(o1.v.y, o2.v.y);
        }
    };
    
    private EventListener listener;
    private final Polygon poly;
    
    private int bottom;
    private int top;
    
    private final PriorityQueue<VertexWithSide> queue;
    private final Deque<VertexWithSide> stack = new ArrayDeque<>();
    
    public TriangulateJ(Polygon poly) {
        this.poly = poly;
        this.queue = new PriorityQueue<>(poly.vertexCount(), CMP);
    }
    
    public void setListener(EventListener listener) {
        this.listener = listener;
    }
    
    private int idx(int n) {
        return n % poly.vertexCount();
    }
    
    private void findExtrema() {
        for (int i = 1; i < poly.vertexCount(); ++ i) {
            if (poly.v(i).y > poly.v(top).y) {
                top = i;
            }
            if (poly.v(i).y < poly.v(bottom).y) {
                bottom = i;
            }
        }
        
        Vec2d topVertex = poly.v(top);
        Vec2d bottomVertex = poly.v(bottom);
        
        queue.add(new VertexWithSide(topVertex, null));
        queue.add(new VertexWithSide(bottomVertex, null));
        
        listener.foundInit(topVertex);
        listener.foundFinal(bottomVertex);
        
    }

    private void findSides() {
        List<Vec2d> lefts = new ArrayList<>();
        List<Vec2d> rights = new ArrayList<>();
        
        int i = top + 1;
        while (idx(i) != bottom) {
            Vec2d v = poly.v(idx(i++));
            lefts.add(v);
            queue.add(new VertexWithSide(v, Side.LEFT));
        }
        ++ i;
        while (idx(i) != top) {
            Vec2d v = poly.v(idx(i++));
            rights.add(v);
            queue.add(new VertexWithSide(v, Side.RIGHT));
        }
        
        listener.foundRight(rights);
        listener.foundLeft(lefts);
    }
    
    private VertexWithSide pop() {
        listener.pop();
        return stack.pop();
    }
    
    private void push(VertexWithSide vertex) {
        listener.push(vertex.v);
        stack.push(vertex);
    }
    
    private boolean inside(Vec2d a, Vec2d b, Vec2d c, Side side) {
        return side == Side.LEFT ? ccw(a, b, c) : cw(a, b, c);
    }
    
    public void run() {
        findExtrema();
        findSides();
        
        listener.start();
        
        for (int i = 0; i < 2; ++ i) {
            VertexWithSide next = queue.poll();
            push(next);
        }
        
        while (!queue.isEmpty()) {
            VertexWithSide next = queue.poll();
            listener.next(next.v);
            
            VertexWithSide top = stack.peek();
            
            if (next.side != top.side) {
                while (!stack.isEmpty()) {
                    VertexWithSide p = stack.peek();
                    if (stack.size() > 1) {
                        listener.addSegment(next.v, p.v);
                    }
                    pop();
                }
                push(top);
                push(next);
            } else {
                while (stack.size() > 1) {
                    Iterator<VertexWithSide> it = stack.iterator();
                    top = it.next();
                    VertexWithSide prev = it.next();
                    if (inside(prev.v, top.v, next.v, next.side)) {
                        listener.addSegment(next.v, prev.v);
                        pop();
                    } else {
                        break;
                    }
                }
                push(next);
            }
        }
        
    }

}
