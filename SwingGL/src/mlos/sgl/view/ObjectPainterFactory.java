package mlos.sgl.view;

import mlos.sgl.canvas.CanvasObject;

public interface ObjectPainterFactory {

    ObjectPainter createPainter(CanvasObject object);
    
}
