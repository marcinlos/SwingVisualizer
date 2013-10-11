package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public interface ObjectController {
    
    double distance(Vec2d p, Transform planeToScreen);

    CanvasObject getObject();
    
}
