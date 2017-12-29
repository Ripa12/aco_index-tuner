package ACO_Index;

import ACO_Index.DP.DynamicProgramming;
import ACO_Index.GA.GeneticAlgorithm;

/**
 * Hello world!
 */

// https://github.com/RoaringBitmap/RoaringBitmap or https://github.com/lemire/javaewah

public class App 
{
    final static int NR_OF_ANTS = 20;
    final static int NR_OF_ATTRIBUTES = 1000;
    final static long NR_OF_TRANSACTIONS = 300000;
    final static int TRANSACTION_LENGTH = 5;
    final static int WEIGHT_LIMIT = 10000;

    public static void main( String[] args )
    {
        long startTime = System.nanoTime();
        //DataSampleGenerator.generateDataSample(NR_OF_ATTRIBUTES, NR_OF_TRANSACTIONS, TRANSACTION_LENGTH, 1, WEIGHT_LIMIT/50, "TestData.txt");
        long transactionBuildTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        Graph graph = Graph.buildGraph("TestData.txt", NR_OF_ATTRIBUTES, NR_OF_TRANSACTIONS, NR_OF_ANTS);
        long graphBuildTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        //graph.debugFrequentItemSets(20);
        //new GeneticAlgorithm(graph.debugFrequentItemSets(10), WEIGHT_LIMIT);
        //DynamicProgramming.solveKP(graph.debugFrequentItemSets((int)NR_OF_TRANSACTIONS/NR_OF_ATTRIBUTES), WEIGHT_LIMIT);
        AntColony antColony = new AntColony(NR_OF_ANTS, 5,
                .05, .3, graph);
        antColony.start(2, 1,  100, (int)NR_OF_TRANSACTIONS/NR_OF_ATTRIBUTES, WEIGHT_LIMIT);
        long frequentItemSetBuildTime = System.nanoTime() - startTime;

        System.out.println("\nTime to generate transactions: " + transactionBuildTime / 1000000000.0);
        System.out.println("Time to build graph: " + graphBuildTime / 1000000000.0);
        System.out.println("Time to mine for frequent item sets: " + frequentItemSetBuildTime / 1000000000.0);

        System.out.println("Total time: " + ((frequentItemSetBuildTime / 1000000000.0) + (graphBuildTime / 1000000000.0)));
    }
}
