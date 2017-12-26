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
        //minPheromone = (1 - Math.pow(Math.E, Math.log(1/bestQuality)/graph.getNumberOfNodes())) /
        //        ((1-(graph.getNumberOfNodes()/2)))*Math.pow(Math.E, Math.log(bestQuality));

        minPheromone = .05;
    }

    private void distributeAntPheromones(double bestQuality){
        for (Ant ant : ants) {
            ant.updatePheromoneLevel(bestQuality, maxPheromone);
        }
    }
    
    public void start(){
        //boolean finished = false;

        // ToDo: Maybe solution should be a tree instead of a LinkedList to avoid duplicate indexes
        LinkedList<LinkedList<Node>> bestLocalSolution = null;

        double solutionQuality = 0;

        while (remainingIterations > 0){
            remainingIterations--;

            for (Ant ant : ants) {
                //ant.prepareAnt(graph.getRandomNode());
                
                ant.findSolution(graph, 100); // ToDo: Maybe return solution here? Should graph really be passed to Ant? weightLimit should not be static!

                if(ant.getLocalQuality() > solutionQuality) {
                    bestLocalSolution = ant.getClonedSolution();
                    solutionQuality = ant.getLocalQuality();
                }
            }

            // Max Pheromone Limit
            updateMaxPheromoneLimit(solutionQuality);
            // Min Pheromone Limit
            updateMinPheromoneLimit(solutionQuality);
            // Spread Pheromone for every found solution
            distributeAntPheromones(solutionQuality);
            // Evaporate all pheromones
            graph.evaporatePheromones(minPheromone);
        }

        bestLocalSolution.forEach(n -> n.forEach(t -> System.out.println(t.getAttribute())));
        for(LinkedList<Node> list : bestLocalSolution){
            System.out.print("[");
            for(Node node : list){
                System.out.print(" " + node.getAttribute() + " ");
            }
            System.out.println("]");
        }
        System.out.println("Quality: " + solutionQuality);

    }

}
