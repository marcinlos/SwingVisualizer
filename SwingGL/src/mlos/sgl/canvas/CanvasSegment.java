package mlos.sgl.canvas;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;

import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;

public class CanvasSegment extends CanvasObject {
    
    public static final Color DEFAULT_COLOR = Color.black;
    
    public static final Color DEFAULT_HOVER_COLOR = Color.orange;
    
    public static final Color DEFAULT_SELECTED_COLOR = Color.magenta;
    
    public static final int DEFAULT_THICKNESS = 2;
    
    private Segment segment;
    
    private Color color = DEFAULT_COLOR;
    
    private Color hoverColor = DEFAULT_HOVER_COLOR;
    
    private Color selectedColor = DEFAULT_SELECTED_COLOR;

    private int thickness = DEFAULT_THICKNESS;
    
    private boolean dashed = false;
    
    public CanvasSegment() {
        this(new Segment(Vec2d.ZERO, Vec2d.ZERO));
    }
    
    public CanvasSegment(Segment segment) {
        this.segment = checkNotNull(segment);
    }
    
    public synchronized Segment getSegment() {
        return segment;
    }
    
    public synchronized void setSegment(Segment segment) {
        this.segment = checkNotNull(segment);
        signalUpdate();
    }
    
    public synchronized Color getColor() {
        return color;
    }
    
    public synchronized void setColor(Color color) {
        this.color = checkNotNull(color);
        signalUpdate();
    }
    
    public synchronized Color getHoverColor() {
        return hoverColor;
    }

    public synchronized void setHoverColor(Color hoverColor) {
        this.hoverColor = checkNotNull(hoverColor);
        signalUpdate();
    }

    public synchronized Color getSelectedColor() {
        return selectedColor;
    }

    public synchronized void setSelectedColor(Color selectedColor) {
        this.selectedColor = checkNotNull(selectedColor);
        signalUpdate();
    }

    public synchronized int getThickness() {
        return thickness;
    }
    
    public synchronized void setThickness(int thickness) {
        checkArgument(thickness > 0, "Line thickness must be positive");
        this.thickness = thickness;
        signalUpdate();
    }
    
    public synchronized boolean isDashed() {
        return dashed;
    }
    
    public synchronized void setDashed(boolean dashed) {
        this.dashed = dashed;
        signalUpdate();
    }

}
