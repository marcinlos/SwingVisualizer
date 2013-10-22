package mlos.sgl.ui.modes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import mlos.sgl.Scene;
import mlos.sgl.canvas.CanvasPoint;
import mlos.sgl.core.Geometry;
import mlos.sgl.core.Rect;
import mlos.sgl.core.Transform;
import mlos.sgl.core.Transforms;
import mlos.sgl.core.Vec2d;
import mlos.sgl.ui.CanvasController;
import mlos.sgl.ui.InputHandler;
import mlos.sgl.util.Randomizer;
import mlos.sgl.view.CanvasView;
import mlos.sgl.view.Painter;

public class RandomPoints implements Mode, InputHandler {

    private final CanvasView view;

    private final CanvasController controller;

    private JPanel optionsPanel;
    
    private final Scene scene;

    private Vec2d startPos;
    private Vec2d currentPos;
    private boolean dragging = false;


    private final Painter selectionPainter = new Painter() {

        @Override
        public void paint(Transform toScreen, Graphics2D ctx) {
            Transform planeToNorm = view.planeToNorm();
            Vec2d startNorm = planeToNorm.apply(startPos);
            Vec2d currentNorm = planeToNorm.apply(currentPos);

            Rect box = Geometry.aabb(startNorm, currentNorm);
            Transform normToScreen = view.normToScreen();
            Vec2d lt = normToScreen.apply(box.leftTop());
            Vec2d rb = normToScreen.apply(box.rightBottom());

            int left = (int) lt.x;
            int right = (int) rb.x;
            int top = (int) lt.y;
            int bottom = (int) rb.y;
            int w = right - left;
            int h = bottom - top;
            
            ctx.setColor(new Color(0.9f, 0.1f, 0.1f, 0.4f));
            ctx.fillRect(left, top, w, h);

            ctx.setColor(Color.black);
            Stroke old = ctx.getStroke();
            
            BasicStroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, 
                    BasicStroke.JOIN_BEVEL, 1, new float[]{3f, 5f}, 0);
            
            ctx.setStroke(dashed);
            ctx.drawRect(left - 1, top - 1, w + 1, h + 1);
            ctx.setStroke(old);
        }
    };

    public RandomPoints(Scene scene, CanvasView view, CanvasController controller) {
        this.scene = scene;
        this.view = view;
        this.controller = controller;
        setupUI();
    }

    private void setupUI() {
        optionsPanel = new JPanel();

        ButtonGroup selectionTypes = new ButtonGroup();
        JRadioButton radioRect = new JRadioButton("rectangle");
        JRadioButton radioCircle = new JRadioButton("circle");
        selectionTypes.add(radioRect);
        selectionTypes.add(radioCircle);
        radioRect.setSelected(true);

        JCheckBox onBorder = new JCheckBox("border only");

        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
        optionsPanel.add(radioRect);
        optionsPanel.add(radioCircle);
        optionsPanel.add(onBorder);
    }

    @Override
    public String getName() {
        return "Random";
    }

    @Override
    public InputHandler getHandler() {
        return this;
    }

    @Override
    public Component getOptionPanel() {
        return optionsPanel;
    }

    private Vec2d getScreenPos(MouseEvent e) {
        java.awt.Point p = e.getPoint();
        return new Vec2d(p.x, p.y);
    }

    private Vec2d getPlanePos(MouseEvent e) {
        Vec2d screenPos = getScreenPos(e);
        return view.planeToScreen().invert(screenPos);
    }

    @Override
    public void mouseClicked(MouseEvent e) {  }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            startPos = getPlanePos(e);
            e.consume();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            Vec2d endScreen = getScreenPos(e);
            Vec2d endNorm = view.normToScreen().invert(endScreen);
            Vec2d startNorm = view.planeToNorm().apply(startPos);
            
            addRandomPoints(startNorm, endNorm);
            
            
            startPos = null;
            dragging = false;
            view.removePostPainter(selectionPainter);
            view.refresh();
            e.consume();
        }
    }

    private void addRandomPoints(Vec2d startNorm, Vec2d endNorm) {
        Rect bounds = Geometry.aabb(startNorm, endNorm);
        int n = 20;
        List<Vec2d> points = Randomizer.inRect(bounds).list(n);
        
        List<Vec2d> planePoints = new ArrayList<>(points.size());
        Transform normToPlane = Transforms.invert(view.planeToNorm());
        for (Vec2d p : points) {
            planePoints.add(normToPlane.apply(p));
        }
        
        for (Vec2d p : planePoints) {
            CanvasPoint canvasPoint = new CanvasPoint(p);
            scene.addObject(canvasPoint);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) { }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!dragging) {
            dragging = true;
            view.addPostPainter(selectionPainter);
        }
        currentPos = getPlanePos(e);
        view.refresh();
        e.consume();
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

}
