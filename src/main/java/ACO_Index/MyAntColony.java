package ACO_Index;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyAntColony {
//    private int stagnationCounter; // Reference

    private int capacity;

    private double pheromonePersistence;

    private double maximumPheromone;
    private double minimumPheromone;

    private double alpha;
    private double beta;
    private int nrOfAnts;
    private int remainingIterations;

    MyAnt[] ants; // ToDo: Simple array instead
    private MyGraph graph;

    //private MyAnt globalBestAnt;
    private List<Integer> globalBestSolution;
    private double globalBestQuality;
    //private MyAnt localBestAnt;

    //private MyAnt localAnt; // Reference

//    private int stagnation; // Reference
    public MyAntColony(MyGraph graph, int nrOfIterations, double pheromonePersistence,
                       int nrOfAnts, int capacity, double alpha, double beta){
        this.remainingIterations = nrOfIterations;
        this.pheromonePersistence = pheromonePersistence;
        this.nrOfAnts = nrOfAnts;
        this.alpha = alpha;
        this.beta = beta;
        this.graph = graph;
//        this.stagnation = 8;

        this.globalBestQuality = 0;

        this.capacity = capacity;

        ants = new MyAnt[nrOfAnts];
        for(int i = 0; i < nrOfAnts; i++){
            ants[i] = new MyAnt(i, graph);
        }
    }

    private void updateMaximumPheromone(){
        // initial: pheromonePersistence * profits of all frequent item-sets
        maximumPheromone = 1.0 / (pheromonePersistence * globalBestQuality);
    }

    private void updateMinimumPheromone(){
        minimumPheromone = maximumPheromone / 10.0;
    }

    public void start(){

        graph.resetPheromoneMatrix(1.0/(pheromonePersistence*graph.getTotalProfit()));

        int quality = 0;
        // Ant at each node?
        while (remainingIterations > 0){

            double localBestQuality = 0;
            List<Integer> localBestSolution = null;
            for(MyAnt ant : ants){
                ant.findSolution(capacity, alpha, beta);

                if(localBestQuality < ant.getSolutionQuality()){
                    localBestQuality = ant.getSolutionQuality();
                    localBestSolution = ant.getSolution();
                }
            }
            if(localBestQuality > globalBestQuality){
                globalBestQuality = localBestQuality;
                globalBestSolution = new ArrayList<>(localBestSolution);
            }
                updateMaximumPheromone();
                updateMinimumPheromone();
                graph.updatePheromoneMatrix(globalBestSolution, globalBestQuality, minimumPheromone);
//            }
            graph.evaporatePheromones(pheromonePersistence, maximumPheromone);
//            restartPheromoneMatrix(localBestQuality);
            remainingIterations--;
        }

        System.out.println("Best solution found: " + globalBestQuality);
    }

//    private void restartPheromoneMatrix(double quality){
////        if (bestAnt == null) {
////            bestAnt = aco.getGlobalBest().clone();
////        }
//
//        if (quality == globalBestQuality) {
//            stagnationCounter++;
//        }else{
//            //localBest = globalBestAnt;
//            stagnationCounter = 0;
//        }
//
//        if (stagnationCounter == stagnation) {
//
//            //LOGGER.debug("The stagnation was reached. The pheromone matrix will be restarted");
//            graph.resetPheromoneMatrix(maximumPheromone);
//            stagnationCounter = 0;
//        }
//    }
}
