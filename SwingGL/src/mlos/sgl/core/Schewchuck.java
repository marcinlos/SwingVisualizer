package mlos.sgl.core;

class Schewchuck {
    
    static {
        System.loadLibrary("schewchuck");
        init();
    }

    private Schewchuck() {
        // non-instantiable
    }
    
    private static native void init(); 
    
    static native double schewchuckOrient2d(double ax, double ay, 
            double bx, double by, double cx, double cy);

}
