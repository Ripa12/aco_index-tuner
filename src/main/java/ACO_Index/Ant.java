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

    public Ant(){}

    public void prepareAnt(Node node){
        currentNode = node;
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

//    graph = generateRandomMatrix(noOfCities);
//    numberOfCities = graph.length;
//    numberOfAnts = (int) (numberOfCities * antFactor);
//
//    trails = new double[numberOfCities][numberOfCities];
//    probabilities = new double[numberOfCities];
//    ants = new Ant[numberOfAnts];
//    IntStream.range(0, numberOfAnts).forEach(i -> ants.add(new Ant(numberOfCities)));
//
//
//    public void visitCity(int currentIndex, int city) {
//        trail[currentIndex + 1] = city;
//        visited[city] = true;
//    }
//
//    public boolean visited(int i) {
//        return visited[i];
//    }
//
//    public double trailLength(double graph[][]) {
//        double length = graph[trail[trailSize - 1]][trail[0]];
//        for (int i = 0; i < trailSize - 1; i++) {
//            length += graph[trail[i]][trail[i + 1]];
//        }
//        return length;
//    }
}
