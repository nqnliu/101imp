import java.util.*;

public class RandomGraph {

    private class Vertex {
        int label;
        boolean visited;

        HashMap<Vertex, Double> adjacencyList;

        public Vertex( int i ) {
            label = i;
            visited = false;
            adjacencyList = new HashMap<Vertex, Double>();
        }
    }

    ArrayList<Vertex> vertices;
    
    public RandomGraph( int n, double p ) {
        Random generator = new Random();
        vertices = new ArrayList<Vertex>();

        for ( int i = 0; i < n; i++ ) {
            vertices.add( new Vertex(i) );
        }

        for ( int i = 0; i < n; i++ ) {
            for ( int j = 0; j < n; j++ ) {
                if ( i != j && generator.nextDouble() < p ) {
                    addEdge(i, j, generator.nextDouble());
                }
            }
        }
    }

    public void addEdge( int i, int j, double weight ) {
        Vertex u = vertices.get( i );
        Vertex v = vertices.get( j );
        u.adjacencyList.put( v, weight );
        v.adjacencyList.put( u, weight );
    }

    public int getCC(){
        int cc = 0;

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

    private class Edge {
        double weight;

        Vertex u;
        Vertex v;
    }

    public static void main( String [] args ) {
        RandomGraph rg = new RandomGraph(100, .01);
        System.out.println( rg.getCC() );
    }
}


