package mlos.sgl.ui;

import static com.google.common.base.Preconditions.checkNotNull;
import static mlos.sgl.core.Geometry.diff;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Map;

import mlos.sgl.canvas.CanvasListener;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Transforms;
import mlos.sgl.core.Vec2d;
import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.CanvasPanel;
import mlos.sgl.view.CanvasView;

public class CanvasController implements CanvasListener {

    public static final int DEFAULT_TRESHOLD = 5;

    private final class MotionListener implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
            update(e);
            onMouseHover(getPosition(e));
        }

        private void update(MouseEvent e) {
            properties.put("cursor", getPosition(e));
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            update(e);
            Vec2d p = diff(getPosition(e), dragBase);
            int dx = (int) p.x;
            int dy = (int) p.y;
            if (captured != null) {
//                captured.
            }
        }
    }
    
    private final class ButtonListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                CanvasObject hit = findHit(getPosition(e));
                if (hit != null) {
                    hit.setSelected(true);
                    canvasPanel.refresh();
                }
            }
            dragBase = getPosition(e);
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            properties.remove("cursor");
        }
        
    }
    
    private Vec2d getPosition(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        return new Vec2d(p.x, p.y);
    }
    
    private final CanvasView view;
    
    private final CanvasPanel canvasPanel;
    
    private final PropertyMap properties;

    private ObjectControllerFactory geometryFactory;

    private final Map<CanvasObject, ObjectController> geometryMap = new HashMap<>();
    
    
    private Vec2d dragBase;
    
    private CanvasObject captured;

    public CanvasController(CanvasView view, CanvasPanel canvasPanel, 
            PropertyMap properties, ObjectControllerFactory geometryFactory) {
        this.view = checkNotNull(view);
        this.canvasPanel = checkNotNull(canvasPanel);
        this.properties = checkNotNull(properties);
        this.geometryFactory = checkNotNull(geometryFactory);
        
        canvasPanel.addMouseMotionListener(new MotionListener());
        canvasPanel.addMouseListener(new ButtonListener());
    }

    @Override
    public void objectAdded(CanvasObject object) {
        ObjectController geometry = geometryFactory.createGeometry(object);
        geometryMap.put(object, geometry);
    }

    @Override
    public void objectRemoved(CanvasObject object) {
        geometryMap.remove(object);
    }
    
    
    private void onMouseHover(Vec2d p) {
        for (CanvasObject object : geometryMap.keySet()) {
            object.setHover(false);
        }
        CanvasObject hit = findHit(p);
        hit.setHover(true);
    }
    
    public CanvasObject findHit(Vec2d p) {
        Transform normToScreen = canvasPanel.normToScreen();
        Transform planeToNorm = view.getTransform();
        Transform planeToScreen = Transforms.compose(planeToNorm, normToScreen);
        
        CanvasObject closest = null;
        double minDist = DEFAULT_TRESHOLD;
        for (ObjectController geometry : geometryMap.values()) {
            double d = geometry.distance(p, planeToScreen);
            if (d < minDist) {
                closest = geometry.getObject();
                minDist = d;
            }
        }
        return closest;
    }

}
