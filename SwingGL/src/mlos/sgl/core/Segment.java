package mlos.sgl.core;

import static com.google.common.base.Preconditions.checkNotNull;

public class Segment {
    
    public final Point a;
    public final Point b;

    public Segment(Point a, Point b) {
        this.a = checkNotNull(a);
        this.b = checkNotNull(b);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Segment) {
            Segment other = (Segment) o;
            return a.equals(other.a) && b.equals(other.b);
        } else {
            return false;
        }
    }
    
    @Override
    public String toString() {
        return String.format("[%s, %s]", a, b);
    }

}
