package mlos.sgl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

import mlos.sgl.canvas.Canvas;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasObjectListener;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.canvas.CanvasSegment;
import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;
import mlos.sgl.decorators.CursorPositionPainter;
import mlos.sgl.ui.CanvasController;
import mlos.sgl.ui.DefaultObjectControllerFactory;
import mlos.sgl.ui.HandlerStack;
import mlos.sgl.ui.ObjectController;
import mlos.sgl.ui.ObjectControllerFactory;
import mlos.sgl.ui.ToolPanel;
import mlos.sgl.ui.modes.PolyCreation;
import mlos.sgl.ui.modes.RandomPoints;
import mlos.sgl.ui.modes.SegmentCreation;
import mlos.sgl.util.PropertyListener;
import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.CanvasPanel;
import mlos.sgl.view.CanvasView;
import mlos.sgl.view.DefaultObjectPainterFactory;
import mlos.sgl.view.ObjectPainter;
import mlos.sgl.view.ObjectPainterFactory;

public abstract class Scene {
    
    private final class Refresher implements PropertyListener, CanvasObjectListener {
        
        @Override
        public void removed(String name) {
            view.refresh();
        }

        @Override
        public void changed(String name, Object newValue) {
            view.refresh();
        }

        @Override
        public void updated(CanvasObject object) {
            view.refresh();
        }
    }

    private String name;

    protected final Canvas canvas;
    
    protected final CanvasPanel canvasPanel;
    
    protected final ToolPanel sidePanel;
    
    protected final CanvasView view;
    
    protected final HandlerStack handlerStack;

    protected final CanvasController canvasController;
    
    
    private final ObjectControllerFactory controllerFactory;
    
    private final ObjectPainterFactory painterFactory;
    
    protected final Map<CanvasObject, ObjectPainter> painters = new HashMap<>();
    protected final Map<CanvasObject, ObjectController> controllers = new HashMap<>();
    

    protected final PropertyMap properties = new PropertyMap();

    private final Refresher refresher;
    

    public Scene(String name) {

        this.name = checkNotNull(name);
        this.canvas = new Canvas();
        this.canvasPanel = new CanvasPanel();
        this.handlerStack = new HandlerStack();
        this.sidePanel = new ToolPanel(handlerStack);
        this.view = new CanvasView(canvasPanel);
        
        this.controllerFactory = createControllerFactory();
        this.painterFactory = createPainterFactory();
        
        view.addPostPainter(new CursorPositionPainter(properties));
        
        this.canvasController = new CanvasController(this);
        
        establishInputListeners();
        
        refresher = new Refresher();
        properties.addListener(refresher);
        
        sidePanel.addMode(new RandomPoints(this, view, canvasController));
        sidePanel.addMode(new SegmentCreation(this, view));
        sidePanel.addMode(new PolyCreation(this, view));
    }

    private void establishInputListeners() {
        canvasPanel.addMouseListener(canvasController.getMouseListener());
        canvasPanel.addMouseMotionListener(canvasController.getMouseMotionListener());
        canvasPanel.addMouseWheelListener(canvasController.getMouseWheelListener());
        canvasPanel.addKeyListener(canvasController.getKeyListener());
    }

    public synchronized boolean addObject(CanvasObject object) {
        boolean added = canvas.add(object);
        if (added) {
            ObjectPainter painter = painterFactory.createPainter(object);
            view.add(painter);
            painters.put(object, painter);
            object.addListener(refresher);
            
            ObjectController controller = controllerFactory.createController(object);
            canvasController.add(controller);
            controllers.put(object, controller);
        }
        return added;
    }
    
    public CanvasPoint addPoint(Vec2d v) {
        CanvasPoint p = new CanvasPoint(v);
        addObject(p);
        return p;
    }
    
    public CanvasPoint addPoint(double x, double y) {
        return addPoint(new Vec2d(x, y));
    }
    
    public CanvasSegment addSegment(Segment seg) {
        CanvasSegment s = new CanvasSegment(seg);
        addObject(s);
        return s;
    }
    
    public CanvasObject addSegment(Vec2d a, Vec2d b) {
        return addSegment(new Segment(a, b));
    }
    
    public synchronized void removeObject(CanvasObject object) {
        if (canvas.remove(object)) {

            ObjectPainter painter = painters.remove(object);
            view.remove(painter);
            
            ObjectController controller = controllers.remove(object);
            canvasController.remove(controller);
            view.refresh();
        }
    }
    
    protected ObjectPainterFactory createPainterFactory() {
        return new DefaultObjectPainterFactory();
    }

    protected ObjectControllerFactory createControllerFactory() {
        return new DefaultObjectControllerFactory();
    }

    public Component getCanvasGui() {
        return canvasPanel;
    }
    
    public Component getSideGui() {
        return sidePanel;
    }


    public String getName() {
        return name;
    }

    public Canvas getCanvas() {
        return canvas;
    }
    
    public CanvasController getCanvasController() {
        return canvasController;
    }
    
    public HandlerStack getHandlerStack() {
        return handlerStack;
    }
    
    public CanvasView getView() {
        return view;
    }
    
    public PropertyMap getProperties() {
        return properties;
    }
    
    public CanvasPanel getPanel() {
        return canvasPanel;
    }

    protected void refresh() {
        view.refresh();
    }
}
