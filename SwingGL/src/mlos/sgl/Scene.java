package mlos.sgl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Component;

import mlos.sgl.canvas.Canvas;
import mlos.sgl.canvas.CanvasObject;
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
import mlos.sgl.ui.modes.RandomPoints;
import mlos.sgl.util.PropertyListener;
import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.CanvasPanel;
import mlos.sgl.view.CanvasView;
import mlos.sgl.view.DefaultObjectPainterFactory;
import mlos.sgl.view.ObjectPainter;
import mlos.sgl.view.ObjectPainterFactory;

public abstract class Scene {
    
    private final class PanelRefresher implements PropertyListener {
        
        @Override
        public void removed(String name) {
            canvasPanel.refresh();
        }

        @Override
        public void changed(String name, Object newValue) {
            canvasPanel.refresh();
        }
    }

    private String name;

    private final Canvas canvas;
    
    private final CanvasPanel canvasPanel;
    
    private final ToolPanel sidePanel;
    
    private final CanvasView view;
    
    private final HandlerStack handlerStack;

    private final CanvasController canvasController;
    
    
    private final ObjectControllerFactory controllerFactory;
    
    private final ObjectPainterFactory painterFactory;
    

    private final PropertyMap properties = new PropertyMap();
    

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
        
        this.canvasController = new CanvasController(view, properties, handlerStack);
        
        establishInputListeners();
        
        properties.addListener(new PanelRefresher());
        
        sidePanel.addMode(new RandomPoints(view, canvasController));
    }

    private void establishInputListeners() {
        canvasPanel.addMouseListener(canvasController.getMouseListener());
        canvasPanel.addMouseMotionListener(canvasController.getMouseMotionListener());
        canvasPanel.addMouseWheelListener(canvasController.getMouseWheelListener());
        canvasPanel.addKeyListener(canvasController.getKeyListener());
    }

    public void addObject(CanvasObject object) {
        canvas.add(object);
        ObjectPainter painter = painterFactory.createPainter(object);
        view.add(painter);
        
        ObjectController controller = controllerFactory.createController(object);
        canvasController.add(controller);
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

    protected Canvas canvas() {
        return canvas;
    }
    
    protected CanvasView view() {
        return view;
    }

    protected void refresh() {
        view.refresh();
    }
}
