package mlos.sgl.ui;

import static mlos.sgl.core.Geometry.dist;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public class PointController implements ObjectController {
    
    private final CanvasPoint point;
    
    public PointController(CanvasPoint point) {
        this.point = point;
    }

    @Override
    public CanvasObject getObject() {
        return point;
    }

    @Override
    public double distance(Vec2d p, Transform planeToScreen) {
        Vec2d s = planeToScreen.apply(point.getPoint());
        double d = dist(p, s);
        double r = point.getSize();
        return Math.max(0, d - r);
    }


}
