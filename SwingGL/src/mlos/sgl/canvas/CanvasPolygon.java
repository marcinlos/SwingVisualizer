package mlos.sgl.canvas;

import static com.google.common.base.Preconditions.checkArgument;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import mlos.sgl.core.Vec2d;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class CanvasPolygon extends CanvasObject {
    
    private final List<Vec2d> points;
    
    private boolean opaque;
    
    private int thickness = CanvasSegment.DEFAULT_THICKNESS;
    
    private Color fillColor = new Color(0, 1, 0, 0.1f);
    
    private Color borderColor = Color.black;
    
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
    
    public synchronized boolean isOpaque() {
        return opaque;
    }
    
    public synchronized void setOpaque(boolean opaque) {
        this.opaque = opaque;
    }
    
    public synchronized Color getFillColor() {
        return fillColor;
    }
    
    public synchronized void setFillColor(Color borderColor) {
        this.fillColor = borderColor;
    }
    
    public synchronized Color getBorderColor() {
        return borderColor;
    }
    
    public synchronized void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
    

    public synchronized int getThickness() {
        return thickness;
    }
    
    public synchronized void setThickness(int thickness) {
        checkArgument(thickness > 0, "Line thickness must be positive");
        this.thickness = thickness;
        signalUpdate();
    }

}
