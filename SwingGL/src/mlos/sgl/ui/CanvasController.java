package mlos.sgl.ui;

import static com.google.common.base.Preconditions.checkNotNull;
import static mlos.sgl.core.Geometry.diff;
import static mlos.sgl.core.Geometry.neg;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    
    private final class Handler implements InputHandler {
        
        @Override
        public void mouseMoved(MouseEvent e) {
            Vec2d screenPos = getScreenPos(e);
            update(screenPos);
            
            ObjectController hit = findHit(screenPos);
            if (hovered != hit) {
                if (hovered != null) {
                    hovered.mouseExited(e);
                }
                if (hit != null) {
                    hit.mouseEntered(e);
                }
                hovered = hit;
            }
            prevPos = screenPos;
        }

        private void update(Vec2d pos) {
            properties.put("cursor", pos);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Vec2d screenPos = getScreenPos(e);
            update(screenPos);
            
            Transform planeToScreen = view.planeToScreen();
            if (drag.hasObject()) {
                drag.update(screenPos, planeToScreen);
            } else {
                Vec2d prevPlanePos = planeToScreen.invert(prevPos);
                Vec2d planePos = getPlanePos(e);
                Vec2d d = diff(planePos, prevPlanePos);
                view.prepend(Transforms.t(d));
            }
            prevPos = screenPos;
        }
    
        @Override
        public void mouseClicked(MouseEvent e) {
            
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Vec2d screenPos = getScreenPos(e);
            if (e.getButton() == MouseEvent.BUTTON3) {
                ObjectController hit = findHit(screenPos);
                boolean expand = e.isShiftDown();

                if (hit != null) {
                    if (expand) {
                        if (! selection.contains(hit)) {
                            selection.add(hit, screenPos);
                            drag.begin(hit, screenPos);
                        } else {
                            selection.remove(hit);
                        }
                    } else {
                        selection.clear();
                        selection.add(hit, screenPos);
                        drag.begin(hit, screenPos);
                    }
                    view.refresh();
                }
            }
            prevPos = screenPos;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                if (drag.hasObject()) {
                    Vec2d screenPos = getScreenPos(e);
                    Transform planeToScreen = view.planeToScreen();
                    drag.end(screenPos, planeToScreen);
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
        

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
        }

        @Override
        public void keyReleased(KeyEvent e) {
            System.out.println(KeyEvent.getKeyText(e.getKeyCode()));
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                selection.clear();
                view.refresh();
            }
        }
        
    }
    
    
    private class Selection {
        
        private final Set<ObjectController> selected = new HashSet<>();
        
        public void add(ObjectController object, Vec2d screenPos) {
            selected.add(object);
            object.selected(screenPos, view.planeToScreen());
            handlers.push(object);
        }
        
        public boolean contains(ObjectController object) {
            return selected.contains(object);
        }
        
        public void remove(ObjectController object) {
            prepareForRemoval(object);
            selected.remove(object);
        }
        
        private void prepareForRemoval(ObjectController object) {
            object.unselected();
            handlers.remove(object);
        }
        
        public void clear() {
            for (ObjectController object : selected) {
                prepareForRemoval(object);
            }
            selected.clear();
        }
    }
    
    private class Drag {
        
        private Vec2d startPos;
        private ObjectController active;
        private boolean draggingInProcess = false;
        
        public void begin(ObjectController object, Vec2d startPos) {
            this.active = object;
            this.startPos = startPos;
            draggingInProcess = false;
        }
        
        public boolean hasObject() {
            return active != null;
        }
        
        public void update(Vec2d screenPos, Transform planeToScreen) {
            if (!draggingInProcess) {
                active.dragBegin(startPos, planeToScreen);
                draggingInProcess = true;
            }
            active.drag(screenPos, planeToScreen);
        }
        
        public void end(Vec2d screenPos, Transform planeToScreen) {
            if (draggingInProcess) {
                active.dragEnd(screenPos, planeToScreen);
                draggingInProcess = false;
            }
            active = null;
        }
        
    }
    
    private final CanvasView view;
    
    private final Handler handler = new Handler();
    private final HandlerChain handlers = new HandlerChain();
    private final InputHandlerWrapper listener = new InputHandlerWrapper(handlers);
    
    private final PropertyMap properties;

    private ObjectControllerFactory controllerFactory;

//    private final Map<CanvasObject, ObjectController> controllerMap = new HashMap<>();
    private final Set<ObjectController> objects = new HashSet<>();
    
    private Vec2d prevPos;
    private ObjectController hovered;
    
    private final Selection selection = new Selection();
    private final Drag drag = new Drag();
    

    public CanvasController(CanvasView view, PropertyMap properties, 
            ObjectControllerFactory controllerFactory) {
        this.view = checkNotNull(view);
        this.properties = checkNotNull(properties);
        this.controllerFactory = checkNotNull(controllerFactory);
        
        handlers.push(handler);
    }

    @Override
    public void objectAdded(CanvasObject object) {
        ObjectController controller = controllerFactory.createController(object);
        objects.add(controller);
//        controllerMap.put(object, controller);
    }

    @Override
    public void objectRemoved(CanvasObject object) {
//        controllerMap.remove(object);
    }
    
    private Vec2d getScreenPos(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        return new Vec2d(p.x, p.y);
    }
    
    private Vec2d getPlanePos(MouseEvent e) {
        Vec2d screenPos = getScreenPos(e);
        return view.planeToScreen().invert(screenPos);
    }
    
    
    public ObjectController findHit(Vec2d p) {
        Transform planeToScreen = view.planeToScreen();
        
        ObjectController closest = null;
        double minDist = DEFAULT_TRESHOLD;
        Comparator<CanvasObject> cmp = ObjectZComparator.INSTANCE;
        
        for (ObjectController controller : objects) {
            double d = controller.distance(p, planeToScreen);
            CanvasObject canvasObjet = controller.getObject();
            if (d < minDist) {
                closest = controller;
                minDist = d;
            } else if (d == minDist) {
                if (closest == null || 
                        cmp.compare(closest.getObject(), canvasObjet) < 0) {
                    closest = controller;    
                }
            }
        }
        return closest;
    }
    
    
    
    public MouseMotionListener getMouseMotionListener() {
        return listener;
    }
    
    public MouseListener getMouseListener() {
        return listener;
    }
    
    public MouseWheelListener getMouseWheelListener() {
        return listener;
    }
    
    public KeyListener getKeyListener() {
        return listener;
    }

}
