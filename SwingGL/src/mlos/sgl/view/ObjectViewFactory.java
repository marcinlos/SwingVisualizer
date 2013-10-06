package mlos.sgl.view;

import mlos.sgl.canvas.CanvasObject;

public interface ObjectViewFactory {

    ObjectPainter createView(CanvasObject object);
    
}
