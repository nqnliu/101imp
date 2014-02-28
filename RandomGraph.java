import java.util.*;
import java.io.*;

/* 
 * Graph is represented by an ArrayList of Vertex objects, each Vertex object
 * contains an adjacency list.
 */
public class RandomGraph {

    private class Vertex {
        boolean visited;
        HashMap<Vertex, Double> adjacencyList; // holds Vertex and weight

        public Vertex() {
            visited = false;
            adjacencyList = new HashMap<Vertex, Double>();
        }
    }

    ArrayList<Vertex> vertices; // list of all vertices in the Graph
    
    public RandomGraph( int n, double p ) {
        // initialization
        Random generator = new Random();
        vertices = new ArrayList<Vertex>();

        // create/add N amount of vertices into the ArrayList
        for ( int i = 0; i < n; i++ ) {
            vertices.add( new Vertex() );
        }

        // generate random edges
        for ( int i = 0; i < n; i++ ) {
            for ( int j = 0; j < n; j++ ) {
                if ( i != j && generator.nextDouble() < p ) {
                    addEdge(i, j, generator.nextDouble());
                }
            }
        }
    }

    // Adds an edge using the INDICES of two vertices in the ArrayList
    public void addEdge( int i, int j, double weight ) {
        Vertex u = vertices.get( i );
        Vertex v = vertices.get( j );
        u.adjacencyList.put( v, weight );
        v.adjacencyList.put( u, weight );
    }

    // Returns the number of connected components in the graph
    public int getCC(){
        int cc = 0;

        // Everything below is DFS
        for ( Vertex v : vertices ) {
            v.visited = false;
        }

        for ( Vertex v : vertices ) {
            if ( v.visited == false ) {
                dfs(v);
                cc++;
            }
        }

        return cc;
    }

    // Perform DFS on a vertex to explore all reachable nodes
    public void dfs( Vertex s ) {
        Stack<Vertex> stack = new Stack<Vertex>();
        
        stack.push( s );
        while( stack.empty() == false ) {
            Vertex u = stack.pop();
            if ( u.visited == false )
                u.visited = true;
            else 
                continue;
            
            for ( Vertex v : u.adjacencyList.keySet() ) {
                stack.push(v);
            }
        }        
    }

    /*
    private class Edge {
        double weight;

        Vertex u;
        Vertex v;
    }
    */

    // Calculate mean
    private static double compute_mean(double a[], int n) {
        if(n== 0)
            return 0.0;
        double sum= 0;
        for(int i= 0; i< n; i++) {
            sum+= a[i];
        }
        double mean= sum/ n;
        return mean;
    }

    // Calculate standard deviation
    private static double std_dev(double a[], int n, double mean) {
        if(n == 0)
            return 0.0;
        double sq_diff_sum = 0;
        for(int i = 0; i < n; ++i) {
            double diff = a[i] - mean;
            sq_diff_sum += diff * diff;
        }
        double variance = sq_diff_sum / n;
        return Math.sqrt(variance);
} 

/********************************** MAIN ****************************************/

    // Main
    public static void main( String [] args ) {
        // I/O stuff
        File outFile= new File("n20.txt");
        PrintWriter out= null;
        try {
            out= new PrintWriter(new FileWriter(outFile));
        } catch (Exception e) {
            System.out.println("SOMETHING WRONG~");
        }

        /***** For n = 20 *****/
        int n= 20;
        int k= 200;
        // an array to store each k, varies for each p
        double[] kCC= new double[200];
        // an array to store mean for n= 20
        double[] meanTwenty= new double[51];
        // an array to store sd for n= 20
        double[] sdTwenty= new double[51];
        // an array to store runtime for mean, n= 20
        double[] runtimeMeanTwenty= new double[51];
        // an array to store runtime for sd, n= 20
        double[] runtimeSdTwenty= new double[51];
        // some counter for the arrays
        int arrIndex= 0;

        out.println("Constructing Graph G(n,p), n = " + n);

        // for increments of p
        for (double p= 0; p<= 1.00; p+= 0.02) {
            // Make k instances of the graph
            int sumCC= 0;
            for (int i= 0; i< k; i++) {
                RandomGraph rg = new RandomGraph(n, p);
                int numCC= rg.getCC();
                // store current k
                kCC[i]= numCC;
            }

            long startTime= System.currentTimeMillis();
            // store mean
            meanTwenty[arrIndex]= compute_mean(kCC, k);
            long endTime= System.currentTimeMillis();
            double meanTime= (endTime- startTime)/ (double)(1000);
            runtimeMeanTwenty[arrIndex]= meanTime;

            // store sd
            startTime= System.currentTimeMillis();
            sdTwenty[arrIndex]= std_dev(kCC, k, meanTwenty[arrIndex]);
            endTime= System.currentTimeMillis();
            double sdTime= (endTime- startTime)/ (double)(1000);
            runtimeSdTwenty[arrIndex]= sdTime;

            // inc index
            arrIndex++;
        }
        // Output data 
        out.println("--- MEAN ---");
        for(int i= 0; i< meanTwenty.length; i++) {
            out.println(meanTwenty[i]);
        }
        
        out.println("--- STANDARD DEVIATION ---");
        for(int i= 0; i< sdTwenty.length; i++) {
            out.println(sdTwenty[i]);
        }

        out.println("--- RUNTIME: Mean ---");
        for(int i= 0; i< runtimeMeanTwenty.length; i++) {
            out.println(runtimeMeanTwenty[i]);
        }

        out.println("--- RUNTIME: Standard Deviation ---");
        for(int i= 0; i< runtimeSdTwenty.length; i++) {
            out.println(runtimeSdTwenty[i]);
        }

        

        /***** For n = 100 *****
        int n= 100;
        int k= 200;
        // an array to store mean for n= 100
        double[] meanHundred= new double[50];
        // an array to store sd for n= 100
        double[] sdHundred= new double[50];
        // an array to store runtime for n= 100
        double[] sdHundred= new double[50];

        System.out.println("Constructing Graph G(n,p), n = " + n);

        // for increments of p
        for (double p= 0; p<= 1.00; j+= 0.02) {
            // Make k instances of the graph
            for (int i= 0; i< k; i++) {
                RandomGraph rg = new RandomGraph(n, p);
                int numCC= rg.getCC;
            }
        }
        // Print data
        */

    // close the file
    out.close();
    }
}


