import java.util.*;
import java.io.*;

public class Main1 {

    // Calculate mean
    private static double compute_mean(double a[], int n) {
        if(n== 0)
            return 0.0;
        double sum= 0.0;
        for(int i= 0; i< n; i++) {
            sum+= a[i];
        }
        double mean= sum/ (n*1.0);
        return mean;
    }

    // Calculate mean
    private static double compute_mean(int a[], int n) {
        if(n== 0)
            return 0.0;
        double sum= 0.0;
        for(int i= 0; i< n; i++) {
            sum+= a[i];
        }
        double mean= sum/ (n*1.0);
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

    private static void getStats( int nVertices, int nTrials, String fileName) {
        // I/O stuff
        File outFile= new File(fileName);
        File outFile2= new File("n" + nVertices + "runtime.txt");
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
        double[] MSTcost = new double[k];
        double[] MSTdiam = new double[k];
        // an array to store mean for n= 20
        double[] mean= new double[51];
        // an array to store sd for n= 20
        double[] sd = new double[51];
        double[] mean2 = new double[51]; 
        double[] mean3 = new double[51];
        // an array to store runtime for mean, n= 20
        double[] runtimeGraph= new double[51];
        // an array to store runtime for sd, n= 20
        double[] runtimeCC= new double[51];
        // some counter for the arrays
        double[] runtimeMST = new double[51];
        double[] runtimeDiam = new double[51];

        int arrIndex= 0;

        out.println("Constructing Graph G(n,p), n = " + n);

        // for increments of p
        for (int p= 0; p <= 100; p+= 2) {
            // Make k instances of the graph
            
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
                kCC[i] = numCC;

                startTime = System.currentTimeMillis();
                MSTcost[i] = rg.getMSTCost();
                endTime = System.currentTimeMillis();
                runtimeMST[arrIndex] += (endTime-startTime);

                startTime = System.currentTimeMillis();
                MSTdiam[i] = rg.getMSTDiam();
                endTime = System.currentTimeMillis();
                runtimeDiam[arrIndex] += (endTime-startTime);
            }

            // store mean
            mean[arrIndex] = compute_mean(kCC, k);
            mean2[arrIndex] = compute_mean(MSTcost, k);
            mean3[arrIndex] = compute_mean(MSTdiam, k);

            // store runtime of creating graph
            runtimeGraph[arrIndex] /= k*1.0;
            runtimeCC[arrIndex] /= k*1.0;
            runtimeMST[arrIndex] /= k*1.0;
            runtimeDiam[arrIndex] /= k*1.0;

            // store sd
            sd[arrIndex]= std_dev(kCC, k, mean[arrIndex]);

            // inc index
            arrIndex++;
        }

        // Output data 
        /*out.println("--- MEAN: # of Connected Components ---");
        out.format("%s\t%10s\t%10s\t%10s\n", "p", "mean", "stdev", "runtime" );*/
        for(int i= 0; i< mean.length; i++) {
            out.format("%.2f\t%10f\t%10f\t%10f\t%10f%n", (i*2)/100.0, mean[i], sd[i], mean2[i], mean3[i]);
            out2.format("%.2f\t%10f\t%10f\t%10f\t%10f%n", (i*2)/100.0, runtimeGraph[i], runtimeCC[i], runtimeMST[i], runtimeDiam[i]);
        }

        // close the writer
        out.close();
        out2.close();
    }

/********************************** MAIN ****************************************/

    // Main
    public static void main( String [] args ) {
        
        getStats( 20, 500, "n20.txt" );
        getStats( 100, 500, "n100.txt" );
        /*
        getCCStats( 500, 500, "n500.txt" );
        getCCStats( 1000, 500, "n1000.txt" );*/
    }
}
