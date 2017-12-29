package ACO_Index;

import sun.awt.image.ImageWatched;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Richard on 2017-12-18.
 */
public class AntColony {
//    private double alpha = 1;
//    private double beta = 5;

    private Graph graph;
    private LinkedList<Node> globalSolution;

    private double maxPheromone;
    private double minPheromone;

    private double pheromonePersistence;

    private ArrayList<Ant> ants;
    
    public AntColony(int nrOfAnts, double initialMaxP,
                     double initialMinP, double persistence, Graph graph){
        this.pheromonePersistence = persistence;
        this.maxPheromone = initialMaxP;
        this.minPheromone = initialMinP;
        this.graph = graph;
        ants = new ArrayList<>(nrOfAnts);
        globalSolution = null;
        
        for (int i = 0; i < nrOfAnts; i++){
            ants.add(new Ant(i));
        }
    }

    private void updateMaxPheromoneLimit(double bestQuality){
        // ToDo: PHEROMONE_PERSISTENCE should probably be a local constant
        maxPheromone = bestQuality / (1 - pheromonePersistence);
    }

    private void updateMinPheromoneLimit(double bestQuality){
        final double pBest = .9; // ToDo: Temporary solution
        // ToDo: PHEROMONE_PERSISTENCE should probably be a local constant
//        minPheromone = (1 - Math.pow(Math.E, Math.log(1/pBest)/graph.getNumberOfNodes())) /
//                ((1-(graph.getNumberOfNodes()/2)))*Math.pow(Math.E, Math.log(pBest));

        minPheromone = maxPheromone / 10.0;
    }

    private void distributeAntPheromones(double bestQuality){
        // ToDo: Only one ant should update(globally best)!
        for (Ant ant : ants) {
            ant.updatePheromoneLevel(bestQuality, maxPheromone, pheromonePersistence);
        }
    }
    
    public void start(double alpha, double beta, int remainingIterations, int minsup, int weightLimit){
        //boolean finished = false;

        // ToDo: Maybe solution should be a tree instead of a LinkedList to avoid duplicate indexes
        LinkedList<LinkedList<Node.Connection>> bestLocalSolution = null;

        double solutionQuality = 0;
        Ant bestAnt = null;
        while (remainingIterations > 0){
            remainingIterations--;

            for (Ant ant : ants) {
                ant.findSolution(alpha, beta, graph, weightLimit, minsup); // ToDo: Maybe return solution here? Should graph really be passed to Ant? weightLimit should not be static!

                if(ant.getLocalQuality() > solutionQuality) {
                    bestAnt = ant;
                    bestLocalSolution = ant.getClonedSolution();
                    solutionQuality = ant.getLocalQuality();
                }
            }

            // Max Pheromone Limit
            updateMaxPheromoneLimit(solutionQuality);
            // Min Pheromone Limit
            updateMinPheromoneLimit(solutionQuality);
            // Spread Pheromone for every found solution
            //distributeAntPheromones(solutionQuality);
            if(bestAnt != null) {
                bestAnt.updatePheromoneLevel(solutionQuality, maxPheromone, pheromonePersistence);
            }

            // Evaporate all pheromones
            graph.evaporatePheromones(minPheromone, pheromonePersistence);

            bestAnt = null;
        }

//        bestLocalSolution.entrySet().forEach(n -> n.getValue()
//                .forEach(t -> System.out.println(t.getAttribute())));
        for(LinkedList<Node.Connection> list : bestLocalSolution){
            System.out.print("[");
            for(Node.Connection node : list){
                System.out.print(" " + node.getKey().getAttribute() + " ");
            }
            System.out.println("]");
        }
        System.out.println("Quality: " + solutionQuality);

    }

}
