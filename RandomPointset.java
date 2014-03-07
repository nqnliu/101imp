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

    private class PointData implements Comparable<PointData> {
        double angle;
        Point p;

        public PointData(double angle, Point p) {
            this.angle= angle;
            this.p= p;
        }

        // for sorting
        public int compareTo(PointData pd) {
            if (this.angle < pd.angle)
                return -1;
            else if (this.angle > pd.angle)
                return 1;
            else
                return 0;
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


    // Graham Scan
    public Point[] grahamScan(Set<Point> pointSet) {
        Point[] myPS= pointSet.toArray(new Point[pointSet.size()]);
        
        // (0): find the lowest point
        int min = 0;
        for (int i= 1; i < myPS.length; i++) {
            if (myPS[i].y == myPS[min].y) {
                if (myPS[i].x > myPS[min].x)
                    min= i;
            }
            else if (myPS[i].y < myPS[min].y)
                min= i;
        }
        // move min to beginning of array
        swapPoints(myPS, 0, min);

        // (1): find angles from base (pointSet[min])
        // make array size n-1
        ArrayList<PointData> ccw= new ArrayList<PointData>();
        double ang;
        for (int i= 2; i < myPS.length; i++) {
            ang= isLeft(myPS[0], myPS[1], myPS[i]);
            ccw.add(new PointData(ang, myPS[i]));
        }
        // add the pivot point
        ccw.add(new PointData(0, myPS[1]));

        // (2): Sort ccw by ccw angles
        Collections.sort(ccw);     // DOES THIS USE QUICKSORT?
        
        // testing
	    for (Iterator<PointData> i=ccw.iterator(); i.hasNext(); ) {
	    PointData data = i.next();
	    System.out.println(data.angle + " --- " + data.p.stringPoint());
	    }

        // (3): create the stack(convex hull) of Points
        // initialize
        Stack<Point> pointStack= new Stack<Point>();
        // push base
        pointStack.push(myPS[0]);
        // push rightmost ccw element
        pointStack.push(ccw.get(0).p);
        // then we look for CH points
        int n= 1;
        while (n < ccw.size()) {
            System.out.println("entering STACK");
            Point c= pointStack.pop();
            System.out.println("POPPING " + c.stringPoint() );
            Point p= pointStack.peek();
            Point nPoint= ccw.get(n).p;
            System.out.println("ISLEFT: "+ isLeft(p, c, nPoint) );
            if (isLeft(p, c, nPoint) > 0) {
               pointStack.push(c);
               pointStack.push(nPoint);
               n++;
            }
            // MUST CHECK IS COLLINEAR, ERROR SOMEWHERE HERE!!!
        }

        // stack to array
        Point[] convexhull= new Point[pointStack.size()];
        int z= 0;
        System.out.println("THE CONVEX HULL:");
        while(pointStack.empty()==false) {
            Point hullpoint= pointStack.pop();
            System.out.println(hullpoint.stringPoint());
            convexhull[z]= hullpoint;
            z++;
        }


        // return the array pf the stack
        return convexhull;
    }

    public static double isLeft(Point p0, Point p1, Point p2) {
        return ( (p1.x - p0.x) * (p2.y - p0.y)
                - (p2.x - p0.x) * (p1.y - p0.y) );
    }

    public static void swapPoints(Point[] ps, int a, int b) {
        Point hold= ps[a];
        ps[a]= ps[b];
        ps[b]= hold;
    }

    // Count # of hulls in a point set
    public int findConvexHulls() {
        Set<Point> mySet = new HashSet<Point>(Arrays.asList(this.getPointSet()));
        Point[] convexhull;
        int numhulls= 0;
        while(mySet.isEmpty()==false) {
            convexhull= this.grahamScan(mySet);
            numhulls++;
            for (int i= 0; i < convexhull.length; i++) {
                mySet.remove(convexhull[i]);
            }
        }
        return numhulls;
    }

    // get method to get the pointset
    public Point[] getPointSet() {
        return this.pointSet;
    }



/*********** MAIN *********/

    public static void main(String[] args) {
        RandomPointset rps= new RandomPointset(20);
        rps.printPoints();
        //rps.grahamScan(rps.pointSet);
        System.out.println("# OF CONVEX HULLS: "+rps.findConvexHulls());
    }

}