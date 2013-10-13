package mlos.sgl.ui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public interface InputHandler {
    
    void mouseClicked(MouseEvent e);
    
    void mousePressed(MouseEvent e);
    
    void mouseReleased(MouseEvent e);
    
    void mouseMoved(MouseEvent e);
    
    void mouseDragged(MouseEvent e);
    
    void mouseEntered(MouseEvent e);
    
    void mouseExited(MouseEvent e);
    
    void mouseWheelMoved(MouseWheelEvent e);
    
    void keyTyped(KeyEvent e);
    
    void keyPressed(KeyEvent e);
    
    void keyReleased(KeyEvent e);
}
