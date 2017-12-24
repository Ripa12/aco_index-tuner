package ACO_Index;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Richard on 2017-12-18.
 */
public class AntColony {
//    private double c = 1.0;
//    private double alpha = 1;
//    private double beta = 5;
//    private double evaporation = 0.5;
//    private double Q = 500;
//    private double antFactor = 0.8;
//    private double randomFactor = 0.01;

    private Graph graph;
    //private int nrOfAnts;
    private int remainingIterations;
    private LinkedList<Node> globalSolution;

    private double maxPheromone;
    private double minPheromone;

    private ArrayList<Ant> ants;
    
    public AntColony(int nrOfAnts, int remainingIterations, double initialMaxP, double initialMinP, Graph graph){
        //this.nrOfAnts = nrOfAnts;
        this.maxPheromone = initialMaxP;
        this.minPheromone = initialMinP;
        this.graph = graph;
        this.remainingIterations = remainingIterations;
        ants = new ArrayList<>(nrOfAnts);
        globalSolution = null;
        
        for (int i = 0; i < nrOfAnts; i++){
            ants.add(new Ant());
        }
    }

    private void updateMaxPheromoneLimit(double bestQuality){
        // ToDo: PHEROMONE_PERSISTENCE should probably be a local constant
        maxPheromone = bestQuality / (1 - Node.PHEROMONE_PERSISTENCE);
    }

    private void updateMinPheromoneLimit(double bestQuality){
        // ToDo: PHEROMONE_PERSISTENCE should probably be a local constant
        maxPheromone = bestQuality / (1 - Node.PHEROMONE_PERSISTENCE);
    }

    private void distributeAntPheromones(double bestQuality){
        for (Ant ant : ants) {
            ant.updatePheromoneLevel(bestQuality, maxPheromone);
        }
    }
    
    public void start(){
        //boolean finsihed = false;

        LinkedList<Node> bestLocalSolution = null;
        
        while (remainingIterations > 0){
            remainingIterations--;
            
            double solutionQuality = 0;
            
            for (Ant ant : ants) {
                ant.prepareAnt(graph.getRandomNode());
                
                ant.findSolution(); // ToDo: Maybe return solution here?
                
                if(ant.getLocalQuality() > solutionQuality) {
                    bestLocalSolution = (LinkedList<Node>)ant.getSolution().clone();
                    solutionQuality = ant.getLocalQuality();
                }
            }

            // Max Pheromone Limit
            updateMaxPheromoneLimit(solutionQuality);
            // Spread Pheromone for every found solution
            distributeAntPheromones(solutionQuality);
            // Evaporate all pheromones
            graph.evaporatePheromones(minPheromone);
        }

        bestLocalSolution.forEach(System.out::println);
    }

}
