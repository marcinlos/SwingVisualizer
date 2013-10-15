package mlos.sgl.ui.modes;

import java.awt.Component;

import mlos.sgl.ui.InputHandler;

public interface Mode {
    
    String getName();
    
    InputHandler getHandler();
    
    Component getOptionPanel();

}
