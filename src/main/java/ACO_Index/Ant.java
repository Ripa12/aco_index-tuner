package ACO_Index;

import sun.awt.image.ImageWatched;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.stream.IntStream;

/**
 * Created by Richard on 2017-12-18.
 */
public class Ant {

    private LinkedList<Node> localSolution;
    //LinkedList<Node> globalSolution;
    //double bestQuality;
    private double localQuality;

    private Node currentNode;

    public Ant(){

    }

    public void prepareAnt(Node node){
        currentNode = node;
        localSolution = new LinkedList<>();
    }

    public double getLocalQuality(){
        return localQuality;
    }

    public LinkedList<Node> getSolution(){
        return localSolution; // ToDo: Should probably return a copy instead!
    }

    public void updatePheromoneLevel(double bestQuality, double maxPheromone){
        double pheromone = 1/(1+localQuality-bestQuality);

        for (Node node : localSolution) {
            node.increasePheromone(pheromone, maxPheromone);
        }
    }

    public void findSolution(){
        localSolution.addFirst(currentNode);

        BitSet supportCount = currentNode.getTransactionsClone();
        localQuality = supportCount.cardinality();

        boolean finished = false;
        while(!finished){
            currentNode = currentNode.getNextProbableItem(5, 1, supportCount);
            if(currentNode != null) {
                supportCount.and(currentNode.getTransactions());
                localQuality += supportCount.cardinality();
            }
            else{
                finished = true;
            }
        }
    }
}
