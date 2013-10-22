package mlos.sgl.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import mlos.sgl.ui.modes.Mode;

public class ToolPanel extends JPanel {
    
    private final class ModeAction extends AbstractAction {
        
        private final Mode mode;
        
        public ModeAction(Mode mode) {
            super(mode.getName());
            this.mode = mode;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            handlers.push(mode.getHandler());
            optionsPanel.removeAll();
            optionsPanel.add(mode.getOptionPanel());
            optionsPanel.revalidate();
            optionsPanel.repaint();
        }

    }

    private JPanel itemsPanel;
    
    private JPanel optionsPanel;
    
    private final List<Mode> modes = new ArrayList<>();

    private final Map<Mode, Component> buttons = new HashMap<>();
    
    private final HandlerStack handlers;
    
    public ToolPanel(HandlerStack handlers) {
        this.handlers = checkNotNull(handlers);
        setupUI();
    }


    private void setupUI() {
        setLayout(new BorderLayout(1, 1));
        setPreferredSize(new Dimension(200, 0));
        
        itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.PAGE_AXIS));
        itemsPanel.setBorder(createBorder());

        add(itemsPanel, BorderLayout.PAGE_START);
        
        optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBorder(createBorder());
        add(optionsPanel, BorderLayout.CENTER);
    }
    
    private Border createBorder() {
        Border outside = BorderFactory.createBevelBorder(BevelBorder.RAISED);
        Border inside = BorderFactory.createEmptyBorder(3, 3, 1, 1);
        return BorderFactory.createCompoundBorder(inside, outside);
    }
    
    public void addMode(Mode mode) {
        modes.add(mode);
        JButton button = new JButton(new ModeAction(mode));
        buttons.put(mode, button);
        itemsPanel.add(button);
    }

}
