package mlos.sgl.ui;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.util.GenericFactory;

public class GenericObjectGeometryFactory extends
        GenericFactory<ObjectGeometry> implements ObjectGeometryFactory {

    @Override
    public ObjectGeometry createGeometry(CanvasObject object) {
        return create(object);
    }

}
