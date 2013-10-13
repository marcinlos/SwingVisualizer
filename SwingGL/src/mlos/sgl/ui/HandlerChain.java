package mlos.sgl.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayDeque;
import java.util.Deque;

public class HandlerChain implements InputHandler {

    private final Deque<InputHandler> handlers = new ArrayDeque<>();
    
    /**
     * Pushes the handler on the top of the stack.
     */
    public void push(InputHandler handler) {
        handlers.push(checkNotNull(handler));
    }

    /**
     * Adds the handler at the bottom of the stack.
     */
    public void pushBack(InputHandler handler) {
        handlers.offerLast(checkNotNull(handler));
    }
    
    public void pop() {
        handlers.pop();
    }
    
    public void remove(InputHandler handler) {
        handlers.remove(handler);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        for (InputHandler handler : handlers) {
            handler.mouseClicked(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (InputHandler handler : handlers) {
            handler.mousePressed(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (InputHandler handler : handlers) {
            handler.mouseReleased(e);
            if (e.isConsumed()) {
                break;
            }
        }  
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (InputHandler handler : handlers) {
            handler.mouseMoved(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for (InputHandler handler : handlers) {
            handler.mouseDragged(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        for (InputHandler handler : handlers) {
            handler.mouseEntered(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        for (InputHandler handler : handlers) {
            handler.mouseExited(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        for (InputHandler handler : handlers) {
            handler.mouseWheelMoved(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        for (InputHandler handler : handlers) {
            handler.keyTyped(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        for (InputHandler handler : handlers) {
            handler.keyPressed(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (InputHandler handler : handlers) {
            handler.keyReleased(e);
            if (e.isConsumed()) {
                break;
            }
        }
    }

}
