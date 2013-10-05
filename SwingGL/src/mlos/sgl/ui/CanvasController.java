package mlos.sgl.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

import mlos.sgl.canvas.CanvasListener;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.CanvasPanel;
import mlos.sgl.view.ScreenPoint;

public class CanvasController implements CanvasListener {

    public static final int DEFAULT_TRESHOLD = 4;

    private final class MotionListener implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
            Point p = e.getPoint();
            ScreenPoint cursor = new ScreenPoint(p.x, p.y);
            properties.put("cursor", cursor);
        }

        @Override
        public void mouseDragged(MouseEvent e) {

        }
    }
    
    private final PropertyMap properties;

    private ObjectGeometryFactory geometryFactory;

    private final Map<CanvasObject, ObjectGeometry> geometryMap = new HashMap<>();

    public CanvasController(CanvasPanel panel, PropertyMap properties, 
            ObjectGeometryFactory geometryFactory) {
        this.properties = checkNotNull(properties);
        this.geometryFactory = checkNotNull(geometryFactory);
        
        panel.addMouseMotionListener(new MotionListener());
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
