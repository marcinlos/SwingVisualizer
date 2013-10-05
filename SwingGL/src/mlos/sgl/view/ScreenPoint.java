package mlos.sgl.view;

import java.util.Objects;

public class ScreenPoint {

    public final int x;
    public final int y;
    
    public ScreenPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ScreenPoint) {
            ScreenPoint other = (ScreenPoint) o;
            return x == other.x && y == other.y;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
    
    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);
    }
    
    
    public static int distSq(ScreenPoint a, ScreenPoint b) {
        int dx = a.x - b.x;
        int dy = a.y - b.y;
        return dx * dx + dy * dy;
    }
}
