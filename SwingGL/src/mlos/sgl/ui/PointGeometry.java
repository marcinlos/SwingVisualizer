package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.ScreenTransform;
import mlos.sgl.view.ScreenPoint;

public class PointGeometry implements ObjectGeometry {
    
    private final CanvasPoint point;
    
    private final ScreenTransform transform;

    public PointGeometry(CanvasPoint point, ScreenTransform transform) {
        this.point = point;
        this.transform = transform;
    }

    @Override
    public boolean hit(ScreenPoint p, int treshold) {
        ScreenPoint s = transform.toScreen(point.getPoint());
        int distSq = ScreenPoint.distSq(p, s);
        
        int r = point.getSize() / 2 + treshold;
        return distSq < r * r;
    }

    @Override
    public CanvasObject getObject() {
        return point;
    }

}
