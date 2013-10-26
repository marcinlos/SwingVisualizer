package mlos.sgl.demo.monotonic;

import mlos.sgl.App;
import mlos.sgl.Scene;

public class MonotonicPolygons extends Scene {

    public MonotonicPolygons() {
        super("triangulation");
    }
    
    public static void main(String[] args) {
        App.create(new MonotonicPolygons());
    }

}
