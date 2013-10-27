package mlos.sgl.demo.monotonic;

import static mlos.sgl.core.Geometry.ccw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mlos.sgl.core.Vec2d;

public class ClassifyVertices {
    
    public interface EventListener {
        
        void start();
        
        void examining(Vec2d v, Vec2d prev, Vec2d next);
        
        void classified(Vec2d v, VertexType type);
        
        void finished();
        
    }
    
    private EventListener listener;
    
    private final int count;
    private final List<Vec2d> vertices;
    private final List<VertexType> types;

    public ClassifyVertices(Collection<Vec2d> polygon) {
        count = polygon.size();
        vertices = new ArrayList<>(polygon);
        types = new ArrayList<>(count);
    }
    
    public void setListener(EventListener listener) {
        this.listener = listener;
    }
    
    private int prev(int n) {
        return (n + count - 1) % count;
    }
    
    private int next(int n) {
        return (n + 1) % count;
    }
    
    private static VertexType classify(Vec2d v, Vec2d prev, Vec2d next) {
        if (prev.y > v.y && next.y > v.y) {
            return ccw(prev, v, next) ? VertexType.FINAL : VertexType.JOIN;
        } else if (prev.y < v.y && next.y < v.y) {
            return ccw(prev, v, next) ? VertexType.INITIAL : VertexType.SPLIT;
        }
        return VertexType.NORMAL;
    }
    
    public List<VertexType> classify() {
        listener.start();
        
        for (int i = 0; i < count; ++ i) {
            Vec2d v = vertices.get(i);
            Vec2d prev = vertices.get(prev(i));
            Vec2d next = vertices.get(next(i));
            
            listener.examining(v, prev, next);
            
            VertexType type = classify(v, prev, next);
            types.add(type);
            
            listener.classified(v, type);
        }
        
        listener.finished();
        return types;
    }
    

}
