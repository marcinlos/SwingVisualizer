package mlos.sgl.ui;

import static mlos.sgl.core.Geometry.diff;
import static mlos.sgl.core.Geometry.dist;
import static mlos.sgl.core.Geometry.move;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public class PointController implements ObjectController {
    
    private final CanvasPoint point;
    
    private Vec2d beforeDrag;
    private Vec2d dragBegin;
    
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
        double r = point.getSize() / 2.0;
        return Math.max(0, d - r);
    }

    @Override
    public void dragBegin(Vec2d pos, Transform planeToScreen) {
        this.dragBegin = planeToScreen.invert(pos);
        this.beforeDrag = point.getPoint();
    }

    @Override
    public void drag(Vec2d pos, Transform planeToScreen) {
        Vec2d planePos = planeToScreen.invert(pos);
        Vec2d disp = diff(planePos, dragBegin);
        point.setPoint(move(beforeDrag, disp));
    }

    @Override
    public void dragEnd(Vec2d pos, Transform planeToScreen) {
        drag(pos, planeToScreen);
        beforeDrag = dragBegin = null;
    }



}
