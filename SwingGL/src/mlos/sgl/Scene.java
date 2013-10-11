package mlos.sgl;

import static com.google.common.base.Preconditions.checkArgument;
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
import mlos.sgl.ui.ObjectControllerFactory;
import mlos.sgl.util.PropertyListener;
import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.CanvasPanel;
import mlos.sgl.view.CanvasView;
import mlos.sgl.view.CompositePainter;
import mlos.sgl.view.DefaultObjectPainterFactory;
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
    
    private CompositePainter painter;

    private final CanvasView view;

    private final CanvasController controller;

    private final PropertyMap properties = new PropertyMap();
    

    public Scene(String name) {
        this(name, 1, 1);
    }

    public Scene(String name, double width, double height) {
        checkArgument(width > 0, "Width must be positive, is %f", width);
        checkArgument(height > 0, "Height must be positive, is %f", height);

        this.name = checkNotNull(name);
        this.canvas = new Canvas();
        this.view = new CanvasView(createViewFactory());
        
        this.painter = new CompositePainter()
            .add(view)
            .add(new CursorPositionPainter(properties));
        
        this.canvasPanel = new CanvasPanel(painter);
        
        ObjectControllerFactory geomFactory = createGeometryFactory();
        this.controller = new CanvasController(view, canvasPanel, properties, geomFactory);
        
        canvas.addListener(view);
        canvas.addListener(controller);
        properties.addListener(new PanelRefresher());
    }

    public void addObject(CanvasObject object) {
        canvas.add(object);
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
    
    protected ObjectPainterFactory createViewFactory() {
        return new DefaultObjectPainterFactory();
    }

    protected ObjectControllerFactory createGeometryFactory() {
        return new DefaultObjectControllerFactory();
    }

    public String getName() {
        return name;
    }

    protected Canvas canvas() {
        return canvas;
    }

    protected void refresh() {
        canvasPanel.refresh();
    }

    protected Component getUI() {
        return canvasPanel;
    }

}
