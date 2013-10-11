package mlos.sgl.demo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mlos.sgl.core.Segment;
import mlos.sgl.core.Vec2d;
import mlos.sgl.io.Formats;
import mlos.sgl.io.Printer;
import mlos.sgl.util.Randomizer;

public class Lab1Data {

    private static void save(String path, List<Vec2d> points) {
        try (FileWriter writer = new FileWriter(path)) {
            Printer printer = new Printer(writer);
            printer.write(Formats.COORDS_AS_LONG, points);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        new File("data").mkdirs();
        save("data/1e2", Randomizer.inSquare(100).list(100000));
        save("data/1e14", Randomizer.inSquare(1e14).list(100000));
        save("data/circle", Randomizer.onCircle(100).list(1000));
        
        double r = 1000;
        Vec2d a = new Vec2d(r * -1, r * -0.05 + 0.05);
        Vec2d b = new Vec2d(r * 1, r * 0.05 + 0.05);
        
        
        List<Vec2d> points = Randomizer.onSegment(new Segment(a, b)).list(1000);
        Collections.sort(points, new Comparator<Vec2d>() {

            @Override
            public int compare(Vec2d a, Vec2d b) {
                return Double.compare(a.y, b.y);
            }
        });
        save("data/line", points);
    }

}
