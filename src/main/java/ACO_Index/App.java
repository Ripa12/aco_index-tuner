package ACO_Index;

import ACO_Index.ALT_ACO.ExecutionStats;
import ACO_Index.ALT_ACO.KnapsackProblem;
import ACO_Index.ALT_ACO.MaxMinAntSystem;
import ACO_Index.ALT_ACO.Problem;
import ACO_Index.DP.DynamicProgramming;
import ACO_Index.GA.Genetic;
import ACO_Index.GA.GeneticAlgorithm;
import org.apache.log4j.BasicConfigurator;

/**
 * Hello world!
 */

// https://github.com/RoaringBitmap/RoaringBitmap or https://github.com/lemire/javaewah

public class App 
{
    final static int NR_OF_ANTS = 10;
    final static int NR_OF_ATTRIBUTES = 1000;
    final static long NR_OF_TRANSACTIONS = 300000;
    final static int TRANSACTION_LENGTH = 5;
    final static int WEIGHT_LIMIT = 1000;

    public static void main( String[] args )
    {
        BasicConfigurator.configure();

        long startTime = System.nanoTime();
        //DataSampleGenerator.generateDataSample(NR_OF_ATTRIBUTES, NR_OF_TRANSACTIONS, TRANSACTION_LENGTH, 5, 10, "TestData.txt");
        long transactionBuildTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
        Graph graph = Graph.buildGraph("TestData.txt", NR_OF_ATTRIBUTES, NR_OF_TRANSACTIONS, NR_OF_ANTS);
        long graphBuildTime = System.nanoTime() - startTime;

        startTime = System.nanoTime();
//        AntColony antColony = new AntColony(NR_OF_ANTS, 5,
//                .05, .3, graph);
//        antColony.start(2, 1,  10, (int)500, WEIGHT_LIMIT);
        new MyAntColony(new MyGraph(graph.debugFrequentItemSets((int)500), WEIGHT_LIMIT), 20,
                .3, 10, WEIGHT_LIMIT, 1, 2).start();
        long antColonyTime = System.nanoTime() - startTime;
        //        Problem kp = new KnapsackProblem(graph.debugFrequentItemSets(500), WEIGHT_LIMIT);
//        MaxMinAntSystem aco = new MaxMinAntSystem(kp);
//        aco.setNumberOfAnts(10);
//        aco.setNumberOfIterations(5);
//        aco.setAlpha(1.0);
//        aco.setBeta(2.0);
//        aco.setRho(0.1);
//        aco.setStagnation(100);
//        ExecutionStats es = ExecutionStats.execute(aco, kp);
//        es.printStats();
        //graph.debugFrequentItemSets(20);
        //new GeneticAlgorithm(graph.debugFrequentItemSets(10), WEIGHT_LIMIT);
        //Genetic.run(graph.debugFrequentItemSets(500), WEIGHT_LIMIT);
        startTime = System.nanoTime();
        DynamicProgramming.solveKP(graph.debugFrequentItemSets((int)10), WEIGHT_LIMIT);
        long dpTime = System.nanoTime() - startTime;
        //DynamicProgramming.calcSolution(graph.debugFrequentItemSets((int)30), WEIGHT_LIMIT);
//        long frequentItemSetBuildTime = System.nanoTime() - startTime;

        System.out.println("\nTime to generate transactions: " + transactionBuildTime / 1000000000.0);
        System.out.println("Time to build graph: " + graphBuildTime / 1000000000.0);
        System.out.println("ACO Time: " + antColonyTime / 1000000000.0);
        System.out.println("DP Time: " + dpTime / 1000000000.0);

        //System.out.println("Total time: " + ((frequentItemSetBuildTime / 1000000000.0) + (graphBuildTime / 1000000000.0)));
    }
}
