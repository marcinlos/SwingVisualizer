package mlos.sgl.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

class InputHandlerWrapper implements MouseListener, MouseMotionListener, 
        MouseWheelListener, KeyListener {
    
    private final InputHandler handler;

    public InputHandlerWrapper(InputHandler handler) {
        this.handler = checkNotNull(handler);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        handler.keyTyped(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        handler.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        handler.keyReleased(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        handler.mouseWheelMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        handler.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        handler.mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        handler.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        handler.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        handler.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        handler.mouseEntered(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        handler.mouseExited(e);
    }

}
