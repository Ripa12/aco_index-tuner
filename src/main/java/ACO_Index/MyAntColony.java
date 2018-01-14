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

    MyAbstractObjective objective;

    private double localBestWritesQuality;
    private List<Integer> localBestWritesSolution;

    private double localBestSupportCountQuality;
    private List<Integer> localBestSupportCountSolution;

    private int capacity;

    private double pheromonePersistence;

    private double writesMinPheromone;
    private double writesMaxPheromone;

    private double supportCountMaxPheromone;
    private double supportCountMinPheromone;

    private double alpha;
    private double beta;
    private int nrOfAnts;

    MyAnt[] ants; // ToDo: Simple array instead
    private MyGraph graph;

    public MyAntColony(MyGraph graph, MyAbstractObjective objective){
        //this.remainingIterations = nrOfIterations;
        this.pheromonePersistence = 0;
        this.nrOfAnts = 0;
        this.alpha = 0;
        this.beta = 0;
        this.graph = graph;
        this.objective = objective;
//        this.stagnation = 8;

        //this.globalBestQuality = 0;

        this.capacity = 0;

        ants = null;
    }

    public MyAntColony setBeta(double b){
        this.beta = b;
        return this;
    }

    public MyAntColony setAlpha(double a){
        this.alpha = a;
        return this;
    }

    public MyAntColony setPheromonePersistence(double p){
        this.pheromonePersistence = p;
        return this;
    }

    public MyAntColony setCapacity(int cap){
        this.capacity = cap;
        return this;
    }

    public MyAntColony setNrOfAnts(int nr){
        this.nrOfAnts = nr;
        ants = new MyAnt[nrOfAnts];
        for(int i = 0; i < nrOfAnts; i++){
            ants[i] = new MyAnt(i, graph);
        }
        return this;
    }

    public List<Integer> getLocalBestSupportCountSolution(){
        return new ArrayList<>(localBestSupportCountSolution);
    }
    public List<Integer> getLocalBestWritesSolution(){
        return new ArrayList<>(localBestWritesSolution);
    }

    public double getLocalBestSupportCountQuality(){
        return localBestSupportCountQuality;
    }
    public double getLocalBestWritesQuality(){
        return localBestWritesQuality;
    }

    public void updatePheromoneMatrix(int objective, List<Integer> globalBestSolution, double globalBestQuality){
        if(objective == 0)
            graph.updatePheromoneMatrix(objective, globalBestSolution, globalBestQuality, supportCountMinPheromone);
        else
            graph.updatePheromoneMatrix(objective, globalBestSolution, globalBestQuality, writesMinPheromone);
    }

    public void updateWMaximumPheromone(double globalBestQuality){
        // initial: pheromonePersistence * profits of all frequent item-sets
        writesMaxPheromone = 1.0 / (pheromonePersistence * globalBestQuality);
    }

    public void updateSCMaximumPheromone(double globalBestQuality){
        // initial: pheromonePersistence * profits of all frequent item-sets
        supportCountMaxPheromone = 1.0 / (pheromonePersistence * globalBestQuality);
    }

    public void updateSCMinimumPheromone(){
        supportCountMinPheromone = supportCountMaxPheromone / 10.0;
    }
    public void updateWMinimumPheromone(){
        writesMinPheromone = writesMaxPheromone / 10.0;
    }

    public void start(){

        graph.resetPheromoneMatrix(0,1.0/(pheromonePersistence*graph.getTotalProfit()));
        graph.resetPheromoneMatrix(1, 1.0/(pheromonePersistence*graph.getTotalWrites()));
//        graph.resetPheromoneMatrix(1, 1.0/(pheromonePersistence*graph.getTotalWrites()));

            localBestSupportCountQuality = 0;
            localBestWritesQuality = Double.MAX_VALUE;
            localBestSupportCountSolution = null;
            localBestWritesSolution = null;
            for(MyAnt ant : ants){
                ant.findSolution(capacity, alpha, beta);

                if(localBestSupportCountQuality < ant.getSolutionSupportCountQuality()){
                    localBestSupportCountQuality = ant.getSolutionSupportCountQuality();
                    localBestSupportCountSolution = ant.getSolution();
                }
                if(localBestWritesQuality > ant.getSolutionWritesQuality()){
                    localBestWritesQuality = ant.getSolutionWritesQuality();
                    localBestWritesSolution = ant.getSolution();
                }
            }
        graph.evaporatePheromones(0, pheromonePersistence, supportCountMaxPheromone);
        graph.evaporatePheromones(1, pheromonePersistence, writesMaxPheromone);
    }
}
