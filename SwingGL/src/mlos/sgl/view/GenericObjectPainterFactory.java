package mlos.sgl.view;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.util.GenericFactory;

public class GenericObjectPainterFactory extends GenericFactory<ObjectPainter>
        implements ObjectPainterFactory {

    @Override
    public ObjectPainter createPainter(CanvasObject object) {
        return create(object);
    }
}
