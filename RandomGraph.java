import java.util.*;
import java.io.*;

/* 
 * Graph is represented by an ArrayList of Vertex objects, each Vertex object
 * contains an adjacency list.
 */
public class RandomGraph {

    private class Vertex {
        boolean visited;
        ArrayList<Edge> adjacencyList; // holds Vertex and weight

        public Vertex() {
            visited = false;
            adjacencyList = new ArrayList<Edge>();
        }
    }

    private class Edge implements Comparable<Edge>{
        Vertex u;
        Vertex v;
        double weight;

        public Edge( Vertex u, Vertex v, double weight ) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }
    
        public int compareTo( Edge other ) {
            if (other.weight > this.weight)
                return -1;
            else if (this.weight > other.weight) 
                return 1;
            else
                return 0;
        }

    }

    Vertex[] vertices; // list of all vertices in the Graph
    
    public RandomGraph( int n, double p ) {
        // initialization
        Random generator = new Random();
        vertices = new Vertex[n];

        // create/add N amount of vertices into the ArrayList
        for ( int i = 0; i < n; i++ ) {
            vertices[i] = new Vertex();
        }

        // generate random edges
        for ( int i = 0; i < n; i++ ) {
            for ( int j = 0; j < n; j++ ) {
                if ( i < j && generator.nextDouble() < p ) {
                    addEdge(i, j, generator.nextDouble());
                }
            }
        }
    }

    // Adds an edge using the INDICES of two vertices in the ArrayList
    public void addEdge( int i, int j, double weight ) {
        Vertex u = vertices[i];
        Vertex v = vertices[j];
        Edge e = new Edge( u, v, weight );
        u.adjacencyList.add( e );
        v.adjacencyList.add( e );
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
            
            for ( Edge e : u.adjacencyList ) {
                stack.push(e.v);
            }
        }        
    }

    // Calculate mean
    private static double compute_mean(int a[], int n) {
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

    private static void getCCStats( int nVertices, int nTrials, String fileName) {
        // I/O stuff
        File outFile= new File(fileName);
        File outFile2= new File("n" + nVertices + "graphCCruntime.txt");
        PrintWriter out= null;
        PrintWriter out2 = null;
        try {
            out= new PrintWriter(new FileWriter(outFile));
            out2 = new PrintWriter(new FileWriter(outFile2));
        } catch (Exception e) {
            System.out.println("SOMETHING WRONG~");
        }

        /***** For n = 20 *****/
        int n= nVertices;
        int k= nTrials;
        // an array to store each k, varies for each p
        int[] kCC= new int[k];
        // an array to store mean for n= 20
        double[] mean= new double[51];
        // an array to store sd for n= 20
        double[] sd= new double[51];
        // an array to store runtime for mean, n= 20
        double[] runtimeGraph= new double[51];
        // an array to store runtime for sd, n= 20
        double[] runtimeCC= new double[51];
        // some counter for the arrays
        double[] runtime = new double[51];

        int arrIndex= 0;

        out.println("Constructing Graph G(n,p), n = " + n);

        // for increments of p
        for (int p= 0; p<= 100; p+= 2) {
            // Make k instances of the graph
            int sumCC= 0;

            for (int i= 0; i< k; i++) {
                long startTime= System.currentTimeMillis();
                RandomGraph rg = new RandomGraph(n, p/100.0);
                long endTime= System.currentTimeMillis();
                runtimeGraph[arrIndex] += (endTime-startTime);

                startTime = System.currentTimeMillis();
                int numCC= rg.getCC();
                endTime = System.currentTimeMillis();
                runtimeCC[arrIndex] += (endTime-startTime);

                // store current k
                kCC[arrIndex] = numCC;
            }

            // store mean
            mean[arrIndex] = compute_mean(kCC, k);

            // store runtime of creating graph
            runtimeGraph[arrIndex] /= k*1.0;
            runtimeCC[arrIndex] /= k*1.0;

            // store sd
            sd[arrIndex]= std_dev(kCC, k, mean[arrIndex]);

            // inc index
            arrIndex++;
        }

        // Output data 
        /*out.println("--- MEAN: # of Connected Components ---");
        out.format("%s\t%10s\t%10s\t%10s\n", "p", "mean", "stdev", "runtime" );*/
        for(int i= 0; i< mean.length; i++) {
            out.format("%.2f\t%10f\t%10f\t%n", (i*2)/100.0, mean[i], sd[i]);
            out2.format("%10f\t%10f%n", runtimeGraph[i], runtimeCC[i]);
        }

        // close the writer
        out.close();
        out2.close();
    }

    public void getPrim() {
        // Pq of edges
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
        
        for( Vertex v : vertices ) {
            for( Edge e: v.adjacencyList ) {
                pq.add( e );
            }
        }

        
    }

/********************************** MAIN ****************************************/

    // Main
    public static void main( String [] args ) {
        getCCStats( 20, 500, "n20.txt" );
        getCCStats( 100, 500, "n100.txt" );
        getCCStats( 500, 500, "n500.txt" );
        getCCStats( 1000, 500, "n1000.txt" );
    }
}


