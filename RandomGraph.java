import java.util.*;
import java.io.*;

/* 
 * Graph is represented by an ArrayList of Vertex objects, each Vertex object
 * contains an adjacency list.
 */
public class RandomGraph {

    Vertex[] vertices; // list of all vertices in the Graph
    ArrayList<ConnectedComponent> ccs;
    int nCC;
    double weights = 0.0;
    
    public RandomGraph( int n, double p ) {
        // initialization
        Random generator = new Random();
        vertices = new Vertex[n];

        // create/add N amount of vertices into the ArrayList
        for ( int i = 0; i < n; i++ ) {
            vertices[i] = new Vertex(i);
        }

        // generate random edges
        for ( int i = 0; i < n; i++ ) {
            for ( int j = 0; j < n; j++ ) {
                if ( i < j && generator.nextDouble() < p ) {
                    addEdge(i, j, generator.nextDouble());
                }
            }
        }
        //System.out.println( weights/edges );
        ccs = new ArrayList<ConnectedComponent>();
    }

    // Adds an edge using the INDICES of two vertices in the ArrayList
    public void addEdge( int i, int j, double weight ) {
        weights += weight;
        Vertex u = vertices[i];
        Vertex v = vertices[j];
        Edge e = new Edge( u, v, weight );
        u.adjacencyList.add( e );
        v.adjacencyList.add( e );
    }

    // Returns the number of connected components in the graph
    public int getCC(){
        nCC = 0;

        for ( Vertex v : vertices ) {
            if ( v.visited == false ) {
                ConnectedComponent cc = new ConnectedComponent();
                ccs.add( cc );
                dfs(v, cc);
                nCC++;
            }
        }
        return nCC;
    }

    public double getMSTCost() {
        double sumCost = 0.0;

        for( ConnectedComponent cc : ccs ) {
            MST mst = cc.getMST();
            sumCost += mst.cost;
        }
        return sumCost/(nCC*1.0);
    }

    public double getMSTDiam() {
        double sumCost = 0.0;

        for( ConnectedComponent cc : ccs ) {
            MST mst = cc.mst;
            int diam = mst.getDiameter();
            sumCost += diam;
        }
        return sumCost/(nCC*1.0);
    }

    // Perform DFS on a vertex to explore all reachable nodes
    public void dfs( Vertex s, ConnectedComponent cc ) {
        Stack<Vertex> stack = new Stack<Vertex>();
        
        stack.push( s );
        while( stack.empty() == false ) {
            Vertex u = stack.pop();
            if ( u.visited == false )
                u.visited = true;
            else 
                continue;
                
            cc.ccVertices.add(u);
            for ( Edge e : u.adjacencyList ) {
                stack.push(e.other(u));
            }
        }
    }

    private class Vertex {
        boolean visited;
        ArrayList<Edge> adjacencyList; // holds Vertex and weight
        int i;
        int level;

        public Vertex( int index ) {
            i = index;
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

        public Vertex other( Vertex v ) {
            if ( v == this.u ) 
                return this.v;
            else
                return this.u;
        }

    }

    public class ConnectedComponent {
        ArrayList<Vertex> ccVertices;
        PriorityQueue<Edge> pq;
        MST mst;

        public ConnectedComponent() {
            ccVertices = new ArrayList<Vertex>();
            pq = new PriorityQueue<Edge>();
            mst = null;
        }

        public MST getMST() {
            mst = new MST();
            for( Vertex v : ccVertices ) {
                if (v.visited)
                    prim(mst, v);
            }
            
            return mst;
        }

        public void prim( MST mst, Vertex v ) {
            scan( v );
            // Pq of edges
            while( pq.isEmpty() == false ) {
                Edge top = pq.poll();
                if (top.u.visited == false && top.v.visited == false)
                    continue;

                mst.edges.add( top );
                mst.cost += top.weight;

                if (top.u.visited) 
                    scan( top.u );

                if (top.v.visited)
                    scan( top.v );
            }
        }

        public void scan( Vertex v ) {
            v.visited = false;
            for( Edge e: v.adjacencyList ) {
                Vertex w = e.other(v);
                if( !w.visited ) continue;
                pq.add(e);
            }
        }
    }

    public class MST {
        ArrayList<Edge> edges;
        HashMap<Integer,Vertex> vertices;

        double cost;

        public MST() {
            edges = new ArrayList<Edge>();
            vertices = new HashMap<Integer, Vertex>();
            cost = 0.0;
        }

        public void createMST() {
            for ( Edge e : edges ) {
                Vertex w1;
                if( vertices.get( e.u.i ) == null ) {
                    w1 = new Vertex( e.u.i );
                    vertices.put( e.u.i, w1 );
                } else {
                    w1 = vertices.get( e.u.i );
                }

                Vertex w2;
                if( vertices.get( e.v.i ) == null ) {
                    w2 = new Vertex( e.v.i );
                    vertices.put( e.v.i, w2 );
                } else {
                    w2 = vertices.get( e.v.i );
                }
                Edge e2 = new Edge( w1, w2, e.weight );
                //System.out.println( w1.i + "--" + w2.i );
                w1.adjacencyList.add( e2 );
                w2.adjacencyList.add( e2 );                
            }
        }

        public int getDiameter() {
            if( edges.size() == 0 )
                return 0;

            createMST();

            Queue<Vertex> queue = new LinkedList<Vertex>();

            queue.add( vertices.get(edges.get(0).u.i) );
            Vertex w = null;
            while ( !queue.isEmpty() ) {
                Vertex u = queue.remove();
                u.visited = true;

                for( Edge e: u.adjacencyList ) {
                    w = e.other(u);
                    //System.out.println( u.i + "--" + w.i );
                    if( !w.visited )
                        queue.add(w);
                }
            }

            queue.add( w );
            w.level = 0;
            boolean branch = false;

            while ( !queue.isEmpty() ) {
                Vertex u = queue.remove();
                u.visited = false;
                branch = true;

                for( Edge e: u.adjacencyList ) {
                    w = e.other(u);
                    if( w.visited ) {
                        queue.add(w);
                        w.level = u.level + 1;
                        branch = false;
                    }
                }
            }

            return w.level;
        }
    }
}


