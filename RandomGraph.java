import java.util.*;

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

    // Test main for now..
    public static void main( String [] args ) {
        /***** For n = 20 *****/
        int n= 20;
        int k= 200;
        // an array to store mean for n= 20
        double[] meanTwenty= new double[50];
        // an array to store sd for n= 20
        double[] sdTwenty= new double[50];
        // an array to store runtime for n= 20
        double[] rtTwenty= new double[50]

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

        
        /***** For n = 100 *****/
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


    }
}


