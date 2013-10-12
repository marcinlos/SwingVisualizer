package mlos.sgl.ui;

import static com.google.common.base.Preconditions.checkNotNull;
import static mlos.sgl.core.Geometry.diff;
import static mlos.sgl.core.Geometry.neg;

import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import mlos.sgl.canvas.CanvasListener;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.ObjectZComparator;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Transforms;
import mlos.sgl.core.Vec2d;
import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.CanvasView;

public class CanvasController implements CanvasListener {

    public static final int DEFAULT_TRESHOLD = 5;

    private final class MotionListener implements MouseMotionListener {
        @Override
        public void mouseMoved(MouseEvent e) {
            Vec2d screenPos = getScreenPos(e);
            update(screenPos);
            onMouseHover(screenPos);
            prevPos = screenPos;
        }

        private void update(Vec2d pos) {
            properties.put("cursor", pos);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Vec2d screenPos = getScreenPos(e);
            update(screenPos);
            
            if (captured != null) {
                ObjectController controller = controllerMap.get(captured);
                Transform planeToScreen = view.planeToScreen();
                if (!dragging) {
                    controller.dragBegin(dragBeginPos, planeToScreen);
                    dragging = true;
                }
                controller.drag(screenPos, planeToScreen);
            } else {
                Vec2d prevPlanePos = view.planeToScreen().invert(prevPos);
                Vec2d planePos = getPlanePos(e);
                Vec2d d = diff(planePos, prevPlanePos);
                view.prepend(Transforms.t(d));
            }
            prevPos = screenPos;
        }
    }
    
    private final class ButtonListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Vec2d screenPos = getScreenPos(e);
            if (e.getButton() == MouseEvent.BUTTON3) {
                CanvasObject hit = findHit(screenPos);
                if (hit != null) {
                    hit.setSelected(true);
                    view.refresh();
                    captured = hit;
                    dragBeginPos = screenPos;
                }
            }
            prevPos = screenPos;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (captured != null) {
                    if (dragging) {
                        Vec2d screenPos = getScreenPos(e);
                        ObjectController controller = controllerMap.get(captured);
                        Transform planeToScreen = view.planeToScreen();
                        controller.dragEnd(screenPos, planeToScreen);
                        dragging = false;
                    }
                    captured = null;
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            
        }

        @Override
        public void mouseExited(MouseEvent e) {
            properties.remove("cursor");
        }
        
    }
    
    private final class WheelListener implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            Vec2d planePos = getPlanePos(e);
            
            double f = e.getPreciseWheelRotation();
            double scale = Math.pow(0.95, f);
            
            Transform scaling = new Transform.Builder()
                .t(neg(planePos))
                .s(scale)
                .t(planePos)
                .create();
            view.prepend(scaling);
        }
        
    }
    
    private Vec2d getScreenPos(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        return new Vec2d(p.x, p.y);
    }
    
    private Vec2d getPlanePos(MouseEvent e) {
        Vec2d screenPos = getScreenPos(e);
        return view.planeToScreen().invert(screenPos);
    }
    
    private final CanvasView view;
    
    
    private final PropertyMap properties;

    private ObjectControllerFactory controllerFactory;

    private final Map<CanvasObject, ObjectController> controllerMap = new HashMap<>();
    
    private Vec2d prevPos;
    
    private Vec2d dragBeginPos;
    private CanvasObject captured;
    private boolean dragging = false;

    public CanvasController(CanvasView view, PropertyMap properties, 
            ObjectControllerFactory controllerFactory) {
        this.view = checkNotNull(view);
        this.properties = checkNotNull(properties);
        this.controllerFactory = checkNotNull(controllerFactory);
    }

    @Override
    public void objectAdded(CanvasObject object) {
        ObjectController controller = controllerFactory.createController(object);
        controllerMap.put(object, controller);
    }

    @Override
    public void objectRemoved(CanvasObject object) {
        controllerMap.remove(object);
    }
    
    
    private void onMouseHover(Vec2d p) {
        for (CanvasObject object : controllerMap.keySet()) {
            object.setHover(false);
        }
        CanvasObject hit = findHit(p);
        hit.setHover(true);
    }
    
    public CanvasObject findHit(Vec2d p) {
        Transform planeToScreen = view.planeToScreen();
        
        CanvasObject closest = null;
        double minDist = DEFAULT_TRESHOLD;
        Comparator<CanvasObject> cmp = ObjectZComparator.INSTANCE;
        
        for (ObjectController geometry : controllerMap.values()) {
            double d = geometry.distance(p, planeToScreen);
            if (d < minDist) {
                closest = geometry.getObject();
                minDist = d;
            } else if (d == minDist) {
                if (closest == null || cmp.compare(closest, geometry.getObject()) < 0) {
                    closest = geometry.getObject();    
                }
            }
        }
        return closest;
    }
    
    
    
    public MouseMotionListener getMouseMotionListener() {
        return new MotionListener();
    }
    
    public MouseListener getMouseListener() {
        return new ButtonListener();
    }
    
    public MouseWheelListener getMouseWheelListener() {
        return new WheelListener();
    }
    
    public KeyListener getKeyListener() {
        return null;
    }

}
