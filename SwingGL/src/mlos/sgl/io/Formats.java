package mlos.sgl.io;

import java.util.Scanner;

import mlos.sgl.core.Vec2d;

public class Formats {

    private Formats() {
        // non-instantiable
    }
    
    public static final Format<Vec2d> COORDS_SPACE_SEPARATED = new Format<Vec2d>() {
        
        @Override
        public String print(Vec2d value) {
            return String.format("%.18f %.18f", value.x, value.y);
        }
        
        @Override
        public Vec2d parse(Scanner scanner) {
            double x = scanner.nextDouble();
            double y = scanner.nextDouble();
            return new Vec2d(x, y);
        }
    };
    
    public static final Format<Vec2d> COORDS_AS_LONG = new Format<Vec2d>() {
        
        private String printDouble(double x) {
            long val = Double.doubleToLongBits(x);
            return String.valueOf(val);
        }
        
        private double readDouble(Scanner scanner) {
            return Double.longBitsToDouble(scanner.nextLong());
        }
        
        @Override
        public String print(Vec2d v) {
            return String.format("%s %s", printDouble(v.x), printDouble(v.y));
        }
        
        @Override
        public Vec2d parse(Scanner scanner) {
            double x = readDouble(scanner);
            double y = readDouble(scanner);
            return new Vec2d(x, y);
        }
    }; 

}
