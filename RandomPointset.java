import java.util.*;

public class RandomPointset {

    private class Point {
        double x;   // x coordinate
        double y;   // y coordinate

        public Point(double x, double y) {
            // initialize coordinates
            this.x= x;
            this.y= y;
        }

        // testing
        public String stringPoint() {
            return "("+ this.x+", "+ this.y+ ")";
        }
    }



    Point[] pointSet;   // list of points in the pointset

    public RandomPointset(int n) {
        // initialize
        Random generator= new Random();
        this.pointSet= new Point[n];

        // add n points to pointset
        for(int i= 0; i< n; i++) {
            double theta= 2*Math.PI*generator.nextDouble();
            double u= generator.nextDouble() + generator.nextDouble();
            double r= 0;
            // check if point exceeds the circle
            if (u> 1)
                r= 2-u;
            else
                r= u;
            // add the point to the pointset
            double x= r*Math.cos(theta);
            double y= r*Math.sin(theta);
            this.addPoint(x, y, i);
        }
    }

    public void addPoint(double x, double y, int n) {
        Point p= new Point(x, y);
        this.pointSet[n]= p;
    }

    // for testing
    public void printPoints() {
        for (int i= 0; i< this.pointSet.length; i++) {
            System.out.println(this.pointSet[i].stringPoint());
        }
    }



/*********** MAIN *********/

    public static void main(String[] args) {
        RandomPointset rps= new RandomPointset(20);
        rps.printPoints();
    }

}
