package mlos.sgl.core;

class Schewchuck {
    
    static {
        System.loadLibrary("schewchuck");
    }

    private Schewchuck() {
        // non-instantiable
    }
    
    static native double schewchuckOrient2d(double ax, double ay, 
            double bx, double by, double cx, double cy);

}
