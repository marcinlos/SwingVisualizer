package mlos.sgl.ui;

import static mlos.sgl.core.Geometry.distSq;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.Vec2d;
import mlos.sgl.core.ScreenTransform;

public class PointGeometry implements ObjectGeometry {
    
    private final CanvasPoint point;
    
    public PointGeometry(CanvasPoint point) {
        this.point = point;
    }

    @Override
    public boolean hit(Vec2d p, ScreenTransform transform, int treshold) {
        Vec2d s = transform.toScreen(point.getPoint());
        double distSq = distSq(p, s);
        
        int r = point.getSize() / 2 + treshold;
        return distSq < r * r;
    }

    @Override
    public CanvasObject getObject() {
        return point;
    }

}
