package mlos.sgl.view;

import mlos.sgl.model.CanvasObject;

public interface View extends Painter {

    CanvasObject getObject();
    
}
