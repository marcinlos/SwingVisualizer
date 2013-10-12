package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public interface ObjectController {
    
    double distance(Vec2d p, Transform planeToScreen);
    
    void dragBegin(Vec2d pos, Transform planeToScreen);
    
    void drag(Vec2d pos, Transform planeToScreen);
    
    void dragEnd(Vec2d pos, Transform planeToScreen);

    CanvasObject getObject();
    
}
