package mlos.sgl.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.Map;

import mlos.sgl.canvas.Canvas;
import mlos.sgl.canvas.CanvasListener;
import mlos.sgl.canvas.CanvasObject;

public class CanvasController implements CanvasListener {

    public static final int DEFAULT_TRESHOLD = 4;
    

    private final Canvas canvas;

    private ObjectGeometryFactory geometryFactory;

    private final Map<CanvasObject, ObjectGeometry> geometryMap = new HashMap<>();

    public CanvasController(Canvas canvas, ObjectGeometryFactory geometryFactory) {
        this.canvas = checkNotNull(canvas);
        this.geometryFactory = checkNotNull(geometryFactory);
    }

    @Override
    public void objectAdded(CanvasObject object) {
        ObjectGeometry geometry = geometryFactory.createGeometry(object);
        geometryMap.put(object, geometry);
    }

    @Override
    public void objectRemoved(CanvasObject object) {
        geometryMap.remove(object);
    }

}
