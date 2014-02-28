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
        double sum= 0.0;
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
        File outFile20= new File("n20.txt");
        PrintWriter out20= null;
        try {
            out20= new PrintWriter(new FileWriter(outFile20));
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
        double[] runtime = new double[51];
        int arrIndex= 0;

        out20.println("Constructing Graph G(n,p), n = " + n);

        // for increments of p
        for (int p= 0; p<= 100; p+= 2) {
            System.out.println( p );
            // Make k instances of the graph
            int sumCC= 0;

            long startTime= System.currentTimeMillis();
            for (int i= 0; i< k; i++) {
                RandomGraph rg = new RandomGraph(n, p/100.0);
                int numCC= rg.getCC();
                if ( p == 1.00 ) 
                    System.out.println( numCC );
                // store current k
                kCC[i]= numCC;
            }

            // store mean
            meanTwenty[arrIndex]= compute_mean(kCC, k);
            long endTime= System.currentTimeMillis();

            // store sd
            sdTwenty[arrIndex]= std_dev(kCC, k, meanTwenty[arrIndex]);
            double sdTime= (endTime- startTime);
            runtime[arrIndex] = sdTime;


            // inc index
            arrIndex++;
        }
        // Output data 
        out20.println("--- MEAN: # of Connected Components ---");
        for(int i= 0; i< meanTwenty.length; i++) {
            out20.format("%f\n", meanTwenty[i]);
        }
            
        out20.println("--- STANDARD DEVIATION: # of Connected Components ---");
        for(int i= 0; i< sdTwenty.length; i++) {
            out20.println(sdTwenty[i]);
        }

        out20.println("--- RUNTIME: Mean (in Milliseconds) ---");
        for(int i= 0; i< runtimeMeanTwenty.length; i++) {
            out20.println(runtime[i]);
        }

        out20.println("--- RUNTIME: Standard Deviation (in Milliseconds) ---");
        for(int i= 0; i< runtimeSdTwenty.length; i++) {
            out20.println(runtimeSdTwenty[i]);
        }
        // close the writer
        out20.close();
        


         // I/O stuff
        File outFile100= new File("n100.txt");
        PrintWriter out100= null;
        try {
            out100= new PrintWriter(new FileWriter(outFile100));
        } catch (Exception e) {
            System.out.println("SOMETHING WRONG~");
        }

        /***** For n = 100 *****/
        n= 100;
        //int k= 200;
        // an array to store each k, varies for each p
        //double[] kCC= new double[200];
        // an array to store mean for n= 100
        double[] meanHundred= new double[51];
        // an array to store sd for n= 100
        double[] sdHundred= new double[51];
        // an array to store runtime for mean, n= 100
        double[] runtimeMeanHundred= new double[51];
        // an array to store runtime for sd, n= 100
        double[] runtimeSdHundred= new double[51];

        // some counter for the arrays
        arrIndex= 0;

        out100.println("Constructing Graph G(n,p), n = " + n);

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
            meanHundred[arrIndex]= compute_mean(kCC, k);
            long endTime= System.currentTimeMillis();
            double meanTime= (endTime- startTime);
            runtimeMeanHundred[arrIndex]= meanTime;

            // store sd
            startTime= System.currentTimeMillis();
            sdHundred[arrIndex]= std_dev(kCC, k, meanHundred[arrIndex]);
            endTime= System.currentTimeMillis();
            double sdTime= (endTime- startTime);
            runtimeSdHundred[arrIndex]= sdTime;

            // inc index
            arrIndex++;
        }
        // Output data 
        out100.println("--- MEAN: # of Connected Components ---");
        for(int i= 0; i< meanHundred.length; i++) {
            out100.println(meanHundred[i]);
        }
        
        out100.println("--- STANDARD DEVIATION: # of Connected Components ---");
        for(int i= 0; i< sdHundred.length; i++) {
            out100.println(sdHundred[i]);
        }

        out100.println("--- RUNTIME: Mean (in Milliseconds) ---");
        for(int i= 0; i< runtimeMeanHundred.length; i++) {
            out100.println(runtimeMeanHundred[i]);
        }

        out100.println("--- RUNTIME: Standard Deviation (in Milliseconds) ---");
        for(int i= 0; i< runtimeSdHundred.length; i++) {
            out100.println(runtimeSdHundred[i]);
        }
        // close the file
        out100.close();
    }
}


