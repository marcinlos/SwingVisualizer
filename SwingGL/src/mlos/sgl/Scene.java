package mlos.sgl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.Component;

import mlos.sgl.canvas.Canvas;
import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.decorators.CursorPositionPainter;
import mlos.sgl.ui.CanvasController;
import mlos.sgl.ui.DefaultObjectGeometryFactory;
import mlos.sgl.ui.ObjectGeometryFactory;
import mlos.sgl.util.PropertyListener;
import mlos.sgl.util.PropertyMap;
import mlos.sgl.view.CanvasView;
import mlos.sgl.view.CanvasPanel;
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
    
    private CompositePainter painter;

    private final CanvasPanel canvasPanel;

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
        this.controller = new CanvasController(canvasPanel, properties, 
                createGeometryFactory());
        
        canvas.addListener(view);
        canvas.addListener(controller);
        properties.addListener(new PanelRefresher());
    }

    public void addObject(CanvasObject object) {
        canvas.add(object);
    }

    protected ObjectPainterFactory createViewFactory() {
        return new DefaultObjectPainterFactory();
    }

    protected ObjectGeometryFactory createGeometryFactory() {
        return new DefaultObjectGeometryFactory();
    }

    public String getName() {
        return name;
    }

    protected Canvas canvas() {
        return canvas;
    }

    protected CanvasPanel panel() {
        return canvasPanel;
    }

    protected void refresh() {
        canvasPanel.refresh();
    }

    protected Component getUI() {
        return panel();
    }

}
