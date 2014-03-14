import java.util.*;
import java.io.*;

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
            if (this.angle > pd.angle)
                return -1;
            else if (this.angle < pd.angle)
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

        // the base point
        //System.out.println("BASE POINT: " + myPS[0].stringPoint());

        // (1): find angles from base (pointSet[min])
        // make array size n-1
        ArrayList<PointData> ccw= new ArrayList<PointData>();
        double ang;
        for (int i= 1; i < myPS.length; i++) {
            ang= findAngle(myPS[0], myPS[i]);
            ccw.add(new PointData(ang, myPS[i]));
        }
        // add the pivot point
        //ccw.add(new PointData(0, myPS[1]));

        // (2): Sort ccw by ccw angles
        Collections.sort(ccw);     // DOES THIS USE QUICKSORT?
        
        // testing
	    for (Iterator<PointData> i=ccw.iterator(); i.hasNext(); ) {
	        PointData data = i.next();
	        //System.out.println(data.angle + " --- " + data.p.stringPoint());
	    }

        // (3): create the stack(convex hull) of Points
        // initialize
        Stack<Point> pointStack= new Stack<Point>();
        // push base
        pointStack.push(myPS[0]);
        // if the base ONLY exists
        if (ccw.isEmpty()) {
            //System.out.println("A BASE HULL!");
            Point[] basehull = new Point[1];
            basehull[0]= pointStack.pop();
            return basehull;
        }

        // push rightmost ccw element
        pointStack.push(ccw.get(0).p);
        // then we look for CH points
        int n= 1;
        while (n < ccw.size()) {
            //System.out.println("entering STACK");
            Point c= pointStack.pop();
            //System.out.println("POPPING " + c.stringPoint() );
            Point p= pointStack.peek();
            Point nPoint= ccw.get(n).p;
            //System.out.println("CHECKING POINTS: \n" +
                    //p.stringPoint() + "\n" +
                    //c.stringPoint() + "\n" +
                    //nPoint.stringPoint());
            //System.out.println("ISLEFT: "+ isLeft(p, c, nPoint) );
            if (isLeft(p, c, nPoint) < 0) {
               pointStack.push(c);
               pointStack.push(nPoint);
               n++;
            }
        }

        // stack to array
        Point[] convexhull= new Point[pointStack.size()];
        int z= 0;
        //System.out.println("THE CONVEX HULL:");
        while(pointStack.empty()==false) {
            Point hullpoint= pointStack.pop();
            //System.out.println(hullpoint.stringPoint());
            convexhull[z]= hullpoint;
            z++;
        }


        // return the array pf the stack
        return convexhull;
    }

    // find an angle based on a base point base and a point p
    // the greater, the more cw
    public static double findAngle(Point b, Point p) {
        double dx= p.x - b.x;
        double dy= p.y - b.y;
        
        if (dy == 0) 
            return -10000;
        return  dx / dy;
    }

    // check if point p2, is left of line p0-p1
    public static double isLeft(Point p0, Point p1, Point p2) {
        return (p1.y - p0.y) * (p2.x - p1.x) -
              (p1.x - p0.x) * (p2.y - p1.y);
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


    // THE TRIALS
    public static void rpsTrials(PrintWriter out, int n, int k) {
        // array to store # of hulls for each k
        int[] hulls= new int[k];
        // array to store runtime of rps construction
        //double[] runtimeRPS= new double[k];
        // array to store runtime of hull finding
        //double[] runtimeCHulls= new double[k];
        // array runtime all
        double[] runtimeAll= new double[k];

        for (int i= 0; i < k; i++) {

            long startTime= System.currentTimeMillis();
            RandomPointset rps= new RandomPointset(n);
            //long endTime= System.currentTimeMillis();
            //runtimeRPS[i]= (double)(endTime-startTime);

            //startTime= System.currentTimeMillis();
            hulls[i]= rps.findConvexHulls(); 
            long endTime= System.currentTimeMillis();
            //runtimeCHulls[i]= (double)(endTime-startTime);
            runtimeAll[i]= (double)(endTime-startTime);

        } 
        double meanhulls= find_mean(hulls, k);
        double stddevhulls= std_dev(hulls, k, meanhulls);
        double meanruntime= find_mean(runtimeAll, k);

        // Output findings
        out.format("%4d\t%10f\t%10f\t%10.3f%n", n, meanhulls, stddevhulls, meanruntime);
        
        /*
        System.out.println("Mean # of SHELLS for n="+n +": "+ mean );
        System.out.println("std_dev of SHELLS for n="+n +": "+ stddev );

        double runtimeRPSmean= find_mean(runtimeRPS, k);
        double runtimeRPSsd= std_dev(runtimeRPS, k, runtimeRPSmean);
        System.out.println("Mean # of SHELLS for n="+n +": "+ runtimeRPSmean );
        System.out.println("std_dev of SHELLS for n="+n +": "+ runtimeRPSsd );

        double runtimeCHmean= find_mean(runtimeCHulls, k);
        double runtimeCHsd= std_dev(runtimeCHulls, k, runtimeCHmean);
        System.out.println("Mean # of SHELLS for n="+n +": "+ runtimeCHmean );
        System.out.println("std_dev of SHELLS for n="+n +": "+ runtimeCHsd );
        */

    }

    private static double find_mean(int a[], int n) {
        if(n== 0)
            return 0.0;
        double sum= 0.0;
        for(int i= 0; i< n; i++) {
            sum+= a[i];
        }
        double mean= sum/ n;
        return mean;
    }

    private static double find_mean(double a[], int n) {
        if(n== 0)
            return 0.0;
        double sum= 0.0;
        for(int i= 0; i< n; i++) {
            sum+= a[i];
        }
        double mean= sum/ n;
        return mean;
    }


    // Calculate standard deviation
    private static double std_dev(int a[], int n, double mean) {
        if(n == 0)
            return 0.0;
        double sq_diff_sum = 0.0;
        for(int i = 0; i < n; ++i) {
            double diff = a[i] - mean;
            sq_diff_sum += diff * diff;
        }
        double variance = sq_diff_sum / n;
        return Math.sqrt(variance);
    } 

    private static double std_dev(double a[], int n, double mean) {
        if(n == 0)
            return 0.0;
        double sq_diff_sum = 0.0;
        for(int i = 0; i < n; ++i) {
            double diff = a[i] - mean;
            sq_diff_sum += diff * diff;
        }
        double variance = sq_diff_sum / n;
        return Math.sqrt(variance);
    } 



/*********** MAIN *********/

    public static void main(String[] args) {
        File outFile= new File("2a.txt");
        PrintWriter out= null;
        try {
            out= new PrintWriter(new FileWriter(outFile));
        } catch (Exception e) {
            System.out.println("SOMETHING WRONG~");
        }





        // number of trials k
        int k= 200;
        
        // for n = 20
        rpsTrials(out, 20, k);

        // for n = 50
        rpsTrials(out, 50, k);

        // for n = 100
        rpsTrials(out, 100, k);

        // for n = 200
        rpsTrials(out, 200, k);
        
        // for n = 500
        rpsTrials(out, 500, k);
        
        // for n = 1000
        rpsTrials(out, 1000, k);
        
        // for n = 2000
        rpsTrials(out, 2000, k);

        // for n = 3000
        rpsTrials(out, 3000, k);

        out.close();
    }

}
