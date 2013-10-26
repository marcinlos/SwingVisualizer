package mlos.sgl.core;

import java.util.List;
import java.util.Objects;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

public class Polygon {
    
    public final List<Vec2d> vs;

    public Polygon(Iterable<Vec2d> vs) {
        this.vs = ImmutableList.copyOf(vs);
    }

    public int vertexCount() {
        return vs.size();
    }
    
    public Vec2d v(int i) {
        return vs.get(i);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Polygon) {
            Polygon other = (Polygon) o;
            return vs.equals(other.vs);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(vs);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Joiner.on(", ").appendTo(sb, vs).append(']');
        return sb.toString();
    }

}
