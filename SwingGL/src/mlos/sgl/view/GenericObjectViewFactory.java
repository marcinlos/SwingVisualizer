package mlos.sgl.view;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.util.GenericFactory;

public class GenericObjectViewFactory extends GenericFactory<ObjectPainter>
        implements ObjectViewFactory {

    @Override
    public ObjectPainter createView(CanvasObject object) {
        return create(object);
    }
}
