package mlos.sgl.ui;

import static mlos.sgl.core.Geometry.diff;
import static mlos.sgl.core.Geometry.dist;
import static mlos.sgl.core.Geometry.move;

import java.awt.event.MouseEvent;

import mlos.sgl.canvas.CanvasObject;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Vec2d;

public class PointController extends InputAdapter implements ObjectController {
    
    private final CanvasPoint point;
    
    private Vec2d beforeDrag;
    private Vec2d dragBegin;
    
    public PointController(CanvasPoint point) {
        this.point = point;
    }

    @Override
    public CanvasObject getObject() {
        return point;
    }

    @Override
    public double distance(Vec2d p, Transform planeToScreen) {
        Vec2d s = planeToScreen.apply(point.getPoint());
        double d = dist(p, s);
        double r = point.getSize() / 2.0;
        return Math.max(0, d - r);
    }

    @Override
    public void dragBegin(Vec2d pos, Transform planeToScreen) {
        this.dragBegin = planeToScreen.invert(pos);
        this.beforeDrag = point.getPoint();
    }

    @Override
    public void drag(Vec2d pos, Transform planeToScreen) {
        Vec2d planePos = planeToScreen.invert(pos);
        Vec2d disp = diff(planePos, dragBegin);
        point.setPoint(move(beforeDrag, disp));
    }

    @Override
    public void dragEnd(Vec2d pos, Transform planeToScreen) {
        drag(pos, planeToScreen);
        beforeDrag = dragBegin = null;
    }

    @Override
    public void selected(Vec2d p, Transform planeToScreen) {
        point.setSelected(true);
    }

    @Override
    public void unselected() {
        point.setSelected(false);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        point.setHover(true);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        point.setHover(false);
    }



}
