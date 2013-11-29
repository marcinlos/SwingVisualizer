package mlos.sgl.core;

class Schewchuck {

    static {
        String arch = System.getProperty("os.arch");
        String libName = "schewchuck";
        if (arch.contains("86")) {
            libName += "32";
        } else {
            libName += "64";
        }
        System.loadLibrary(libName);
        init();
    }

    private Schewchuck() {
        // non-instantiable
    }

    private static native void init();

    static native double schewchuckOrient2d(double ax, double ay,
            double bx, double by, double cx, double cy);

    static native double schewchuckIncircle(double ax, double ay, double bx,
            double by, double cx, double cy, double dx, double dy);

}
