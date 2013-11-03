package mlos.sgl.ui.modes;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
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
import mlos.sgl.util.Randomizer;
import mlos.sgl.view.Drawer;
import mlos.sgl.view.Painter;

public class RandomPoints extends AbstractMode {

    private JPanel optionsPanel;

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
            
            Drawer d = new Drawer(ctx);
            d.color(0.9f, 0.1f, 0.1f, 0.4f);
            
            ctx.fillRect(left, top, w, h);
            
            d.color(Color.black).dashed(1, 3f, 5f);
            ctx.drawRect(left - 1, top - 1, w + 1, h + 1);
            
            d.restore();
        }
    };

    public RandomPoints(Scene scene, CanvasController controller) {
        super("Random points", scene);
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
    public Component getOptionPanel() {
        return optionsPanel;
    }

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
    public void mouseDragged(MouseEvent e) {
        if (!dragging) {
            dragging = true;
            view.addPostPainter(selectionPainter);
        }
        currentPos = getPlanePos(e);
        view.refresh();
        e.consume();
    }

}
