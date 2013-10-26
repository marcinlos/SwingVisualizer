package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasPolygon;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public class PolygonController extends InputAdapter implements ObjectController {

    private final CanvasPolygon polygon;

    public PolygonController(CanvasPolygon polygon) {
        this.polygon = polygon;
    }

    @Override
    public double distance(Vec2d p, Transform planeToScreen) {
        return 10;
    }

    @Override
    public void selected(Vec2d p, Transform planeToScreen) {
        polygon.setSelected(true);
    }

    @Override
    public void unselected() {
        polygon.setSelected(false);
    }

    @Override
    public void dragBegin(Vec2d pos, Transform planeToScreen) {
        // empty
    }

    @Override
    public void drag(Vec2d pos, Transform planeToScreen) {
        // empty
    }

    @Override
    public void dragEnd(Vec2d pos, Transform planeToScreen) {
        // empty
    }

    @Override
    public CanvasPolygon getObject() {
        return polygon;
    }

}
