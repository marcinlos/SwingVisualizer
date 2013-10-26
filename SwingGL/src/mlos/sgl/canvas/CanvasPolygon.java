package mlos.sgl.canvas;

import java.util.Collections;
import java.util.List;

import mlos.sgl.core.Vec2d;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class CanvasPolygon extends CanvasObject {
    
    private final List<Vec2d> points;
    
    public CanvasPolygon() {
        this(Collections.<Vec2d>emptyList());
    }

    public CanvasPolygon(Iterable<Vec2d> points) {
        this.points = Lists.newArrayList(points);
    }
    
    public synchronized List<Vec2d> getPoints() {
        return ImmutableList.copyOf(points);
    }
    
    public synchronized void setPoints(Iterable<Vec2d> points) {
        this.points.clear();
        Iterables.addAll(this.points, points);
    }

}
