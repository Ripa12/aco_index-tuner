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
    
    private ArrayList<Ant> ants;
    
    public AntColony(int nrOfAnts, int remainingIterations, Graph graph){
        //this.nrOfAnts = nrOfAnts;
        this.graph = graph;
        this.remainingIterations = remainingIterations;
        ants = new ArrayList<>(nrOfAnts);
        globalSolution = null;
        
        for (int i = 0; i < nrOfAnts; i++){
            ants.add(new Ant());
        }
    }
    
    public void start(){
        //boolean finsihed = false;

        LinkedList<Node> bestLocalSolution = null;
        
        while (remainingIterations > 0){
            remainingIterations--;
            
            double solutionQuality = 0;
            
            for (Ant ant :
                    ants) {
                ant.prepareAnt(graph.getRandomNode());
                
                ant.findSolution(); // ToDo: Maybe return solution here?
                
                if(ant.localQuality > solutionQuality) {
                    bestLocalSolution = (LinkedList<Node>)ant.getSolution().clone();
                    solutionQuality = ant.localQuality;
                }
            }
        }

        bestLocalSolution.forEach(System.out::println);
    }

}
