package ACO_Index;

/**
 * Hello world!
 */

// https://github.com/RoaringBitmap/RoaringBitmap or https://github.com/lemire/javaewah

public class App 
{
    final static int NR_OF_ATTRIBUTES = 1000;
    final static long NR_OF_TRANSACTIONS = 200000;
    final static int TRANSACTION_LENGTH = 5;

    public static void main( String[] args )
    {
        long startTime = System.nanoTime();
        DataSampleGenerator.generateDataSample(NR_OF_ATTRIBUTES, NR_OF_TRANSACTIONS, TRANSACTION_LENGTH, "TestData.txt");
        long transactionBuildTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        Graph graph = Graph.buildGraph("TestData.txt", NR_OF_ATTRIBUTES, NR_OF_TRANSACTIONS);
        long graphBuildTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        graph.debugFrequentItemSets(20);
        long frequentItemSetBuildTime = System.nanoTime() - startTime;

        System.out.println("\nTime to generate transactions: " + transactionBuildTime / 1000000000.0);
        System.out.println("Time to build graph: " + graphBuildTime / 1000000000.0);
        System.out.println("Time to mine for frequent item sets: " + frequentItemSetBuildTime / 1000000000.0);

        System.out.println("Total time: " + ((frequentItemSetBuildTime / 1000000000.0) + (graphBuildTime / 1000000000.0)));
    }
}
