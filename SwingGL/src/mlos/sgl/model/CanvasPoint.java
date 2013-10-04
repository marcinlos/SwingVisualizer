package mlos.sgl.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Color;

import mlos.sgl.core.Point;

public class CanvasPoint extends CanvasObject {
    
    public static final Color DEFAULT_COLOR = Color.red;

    public static final int DEFAULT_SIZE = 10;

    /** Position in virtual coordinates */
    private Point point;

    /** Color of the point */
    private Color color = DEFAULT_COLOR;

    /** Diameter of the point */
    private int size = DEFAULT_SIZE;
    
    public CanvasPoint(Point point) {
        this(point, DEFAULT_Z);
    }

    public CanvasPoint(Point point, double z) {
        super(z);
        setPoint(point);
    }
    
    public Point getPoint() {
        return point;
    }
    
    public void setPoint(Point point) {
        this.point = checkNotNull(point);
        notifyListeners();
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = checkNotNull(color);
        notifyListeners();
    }
    
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        checkArgument(size > 0, "Point size must be positive");
        this.size = size;
        notifyListeners();
    }

    @Override
    public void accept(CanvasVisitor visitor) {
        visitor.visit(this);
    }

}
