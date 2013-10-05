package mlos.sgl.view;

import mlos.sgl.model.CanvasObject;

public interface ObjectView extends Painter {

    CanvasObject getObject();
    
}
