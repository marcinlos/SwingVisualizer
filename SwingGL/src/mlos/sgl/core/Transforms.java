package mlos.sgl.core;

public class Transforms {

    private Transforms() {
        // non-instantiable
    }
    
    public static final Transform ID = new Transform();
    

    public static Transform invert(Transform t) {
        return new Transform.Builder(t).invert().create();
    }
    
    public static Transform compose(Transform first, Transform... ts) {
        Transform.Builder builder = new Transform.Builder(first);
        for (Transform t : ts) {
            builder.apply(t);
        }
        return builder.create();
    }
}
