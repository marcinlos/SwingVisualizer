package mlos.sgl.view;

import mlos.sgl.canvas.CanvasObject;

public interface ObjectViewFactory {

    ObjectView createView(CanvasObject object);
    
}
