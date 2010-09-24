package com.xtremelabs.droidsugar.fakes;

import android.graphics.Path;
import com.xtremelabs.droidsugar.util.Implements;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(Path.class)
public class FakePath {
    private List<Point> points = new ArrayList<Point>();
    public Point wasMovedTo;

    public void moveTo(float x, float y) {
        Point p = new Point(x, y);
        points.add(p);
        wasMovedTo = p;
    }

    public void lineTo(float x, float y) {
        points.add(new Point(x, y));
    }

    public List<Point> getPoints() {
        return points;
    }

    public static class Point {
        float x, y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (Float.compare(point.x, x) != 0) return false;
            if (Float.compare(point.y, y) != 0) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
            result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Point(" + x + "," + y + ")";
        }
    }
}
