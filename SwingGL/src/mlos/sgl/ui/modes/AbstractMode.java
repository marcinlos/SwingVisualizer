package mlos.sgl.ui.modes;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.event.MouseEvent;

import mlos.sgl.Scene;
import mlos.sgl.core.Vec2d;
import mlos.sgl.ui.InputAdapter;
import mlos.sgl.ui.InputHandler;
import mlos.sgl.view.CanvasView;

public abstract class AbstractMode extends InputAdapter implements Mode {
    
    protected final CanvasView view;
    protected final Scene scene;

    private final String name;


    public AbstractMode(String name, Scene scene) {
        this.name = checkNotNull(name);
        this.scene = checkNotNull(scene);
        this.view = scene.getView();
    }
    
    @Override
    public InputHandler getHandler() {
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    
    protected Vec2d getScreenPos(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        return new Vec2d(p.x, p.y);
    }

    protected Vec2d getPlanePos(MouseEvent e) {
        Vec2d screenPos = getScreenPos(e);
        return view.planeToScreen().invert(screenPos);
    }
    
    protected static boolean leftButtonDown(MouseEvent e) {
        return (e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0;
    }
    
    protected static boolean rightButtonDown(MouseEvent e) {
        return (e.getModifiersEx() & MouseEvent.BUTTON2_DOWN_MASK) != 0;
    }
    
}
