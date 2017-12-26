package ACO_Index;

import sun.awt.image.ImageWatched;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.stream.IntStream;

/**
 * Created by Richard on 2017-12-18.
 */
public class Ant {

    private LinkedList<LinkedList<Node>> localSolution;
    //LinkedList<Node> globalSolution;
    //double bestQuality;
    private double localQuality;

    private Node currentNode;

    public Ant(){

    }

    public double getLocalQuality(){
        return localQuality;
    }

    public LinkedList<LinkedList<Node>> getClonedSolution(){
        return (LinkedList<LinkedList<Node>>)localSolution.clone(); // ToDo: Should probably return a copy instead!
    }

    public void updatePheromoneLevel(double bestQuality, double maxPheromone){
        double pheromone = 1 / (1+((bestQuality-localQuality)/bestQuality));

        localSolution.forEach(l -> l.forEach(s->s.increasePheromone(pheromone, maxPheromone)));
    }

    public void findSolution(Graph graph, int weightLimit){
        boolean localSolutionFound = false;

        localSolution = new LinkedList<>();

        int totalWeight = 0;
        while(!localSolutionFound) {
            Node currentNode = graph.getRandomNode();

            totalWeight += currentNode.getWeight();
            if (totalWeight < weightLimit) {
                LinkedList<Node> partialSolution = new LinkedList<>();
                partialSolution.addFirst(currentNode);

                BitSet supportCount = currentNode.getTransactionsClone();
                localQuality = supportCount.cardinality();

                boolean finished = false;
                while (!finished) {
                    currentNode = currentNode.getNextProbableItem(3, totalWeight, supportCount);
                    if (currentNode != null) {
                        supportCount.and(currentNode.getTransactions());
                        localQuality += supportCount.cardinality();
                        totalWeight += currentNode.getWeight();
                        partialSolution.add(currentNode);
                    } else {
                        finished = true;
                    }
                }

                localSolution.add(partialSolution);
            }
            else
            {
                localSolutionFound = true;
            }
        }
    }
}
