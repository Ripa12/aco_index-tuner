package ACO_Index;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;
import sun.awt.image.ImageWatched;

import javax.swing.text.StyledEditorKit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Richard on 2017-12-18.
 */
public class Node {
    public static class Connection{
        Node node;
        Double pheromone;
        BitSet antBlockades;
        public Connection(Node node, Double pheromone, BitSet antBlockades){
            this.node = node;
            this.pheromone = pheromone;
            this.antBlockades = antBlockades;
        }

        public Node getKey(){
            return node;
        }

        public void setValue(double value){
            pheromone = value;
        }

        public double getValue(){
            return pheromone;
        }
    }

    private static final int WEIGHT = 5; // ToDo: Temporary, should be read from file!

    private ArrayList<Connection> neighbours; // ToDo: Pheromone on every edge instead of node!

    private BitSet transactions;
    private String attribute;
    private long supportCount;
    private int writeToRead;

    private int weight;
    //private double pheromone; // ToDo: Maybe float?

    private int index;
    private boolean visited;

//    public Node(BitSet transactions, String attribute) {
    public Node(int writeToRead, int weight, String attribute) {
        this.weight = weight; // ToDo: Temporary solution
        this.writeToRead = writeToRead;

        //this.pheromone = 1;
//        this.transactions = transactions;
        this.transactions = null;
        this.attribute = attribute;
        this.visited = false;

//        supportCount = transactions.cardinality();
        supportCount = 0;

        neighbours = new ArrayList<>(); // ToDo: Possible to Reserve Memory?
    }

//    public Node(BitSet transactions, String attribute, ArrayList<Node> neighbours) {
    public Node(int nrOfAnts, int writeToRead, int weight, String attribute, ArrayList<Node> neighbours) {
        this.weight = weight; // ToDo: Temporary solution
        this.writeToRead = writeToRead;

        //this.pheromone = 1;
//        this.transactions = transactions;
        this.transactions = null;
        this.attribute = attribute;
        this.visited = false;

//        supportCount = transactions.cardinality();
        supportCount = 0;

        this.neighbours = new ArrayList<>(neighbours.size());
        neighbours.forEach((n)->this.neighbours.add(new Connection(n, 1.0, new BitSet(nrOfAnts))));
    }

    public void setTransactions(BitSet transactions){
        this.transactions = transactions;
        supportCount = transactions.cardinality();
    }

    public void unvisit(){
        this.visited = false;
    }

    private void unblockEdges(){
        for (Connection neighbour:
        neighbours){
            neighbour.setValue(1.0);
        }
    }

    public int getIndex(){
        int result = index;
        index = -1;
        return result;
    }

    public int getNrOfNeighbours() {
        return neighbours.size();
    }

    public int getWeight(){
        return weight;
    }

    public int getWriteToRead() {
        return writeToRead;
    }

    public BitSet getTransactionsClone() {
        return (BitSet) transactions.clone();
    }

    public BitSet getTransactions() {
        return transactions;
    }

    public String getAttribute() {
        return attribute;
    }

    public long getSupportCount() {
        return supportCount;
    }

    public void connect(int nrOfAnts, Node to) {
        BitSet difference = ((BitSet) this.transactions.clone());
        difference.and(to.transactions);
        if (difference.cardinality() > 0) {
            if (this.supportCount >= to.supportCount) // ToDo: Check for minimum support between items, maybe?
            {
                neighbours.add(new Connection(to, 1.0, new BitSet(nrOfAnts)));
            } else {
                to.neighbours.add(new Connection(this, 1.0, new BitSet(nrOfAnts)));
            }
        }
    }

    private int getItemSetFrequency(BitSet other) {
        BitSet clone = (BitSet) transactions.clone();
        clone.and(other);
        return clone.cardinality();
    }

    private int getItemSetFrequency(BitSet clone, BitSet other) {
        clone.and(other);
        return clone.cardinality();
    }

    // Frequent Item-set
    public Connection getNextItem(int minsup, BitSet bitset) {

        Connection result = null;

        long maxValue = minsup; // ToDo: Should be minsup
        for (Connection tempNode : neighbours) {
            if (tempNode.getValue() > 0) {
                int tempValue = getItemSetFrequency((BitSet) bitset.clone(), tempNode.getKey().transactions);
                if (tempValue >= maxValue) {
                    maxValue = tempValue;
                    result = tempNode;
                }
            }
        }
        //sample data
        // http://www.philippe-fournier-viger.com/spmf/FPGrowth.php

        if (result == null) {
            unblockEdges();
        }

        return result;
    }

    public Connection getNextProbableItem(double alpha, double beta, int minsup, int currentWeight,
                                    int weightLimit, BitSet supportCount, int antIndex){
        ArrayList<Double> probabilities = calculateProbabilities(alpha, beta, minsup, currentWeight, weightLimit, supportCount);
        double rand = ThreadLocalRandom.current().nextDouble();
        double total = 0;
        for (int i = 0; i < neighbours.size(); i++) {
            total += probabilities.get(i);
            if (total >= rand && !neighbours.get(i).antBlockades.get(antIndex)){
                neighbours.get(i).getKey().index = i;
                return neighbours.get(i);
            }
        }

        return null;
    }

    // ToDo: should probably be moved to Ant Colony
    private ArrayList<Double> calculateProbabilities(double alpha, double beta, int minsup, int currentWeight, int weightLimit, BitSet supportCount){
        ArrayList<Double> probabilities = calculateHeuristics(minsup, supportCount);

        double totalHeuristics = 0;
        double totalPheromone = 0;
        double sumProbability = 0;
        for(int i = 0; i < probabilities.size(); i++){
            totalPheromone += Math.pow(neighbours.get(i).getValue(), alpha);
            totalHeuristics += Math.pow(probabilities.get(i), beta);
        }

        for(int i = 0; i < probabilities.size(); i++){
            // ToDo: Alpha and Beta
            if(weightLimit > (currentWeight + neighbours.get(i).getKey().weight) && probabilities.get(i) > 0) {
                double tempValue = ((Math.pow(neighbours.get(i).getValue(), alpha) * Math.pow(probabilities.get(i), beta)) /
                        (totalPheromone * totalHeuristics)/neighbours.get(i).getKey().weight);
                sumProbability += tempValue;
                probabilities.set(i, (tempValue));
            }
            else{
                probabilities.set(i, 0.0);
            }
        }

        for(int i = 0; i < probabilities.size(); i++) {
            probabilities.set(i, probabilities.get(i)/sumProbability);
        }

        return probabilities;
    }

    // ToDo: move this function to Ant and treat as an executable argument when invoking getNextItem
    private ArrayList<Double> calculateHeuristics(int minsup, BitSet supportCount) {
        ArrayList<Double> heuristics = new ArrayList<>(neighbours.size());

        for (Connection neighbour: neighbours) {
            double tempValue = (double)getItemSetFrequency((BitSet) supportCount.clone(), neighbour.getKey().transactions);
            heuristics.add(tempValue);
        }
        for(int i = 0; i < heuristics.size(); i++){
            if(heuristics.get(i) >= minsup) {
                // ToDo: accumulated weight + neighbours.get(i).getKey().weight (if necessary)
                heuristics.set(i, heuristics.get(i) / (Math.pow(neighbours.get(i).getKey().weight, 2)));
            }
            else{
                heuristics.set(i, 0.0);
            }
        }

        return heuristics;
    }

//    public void increasePheromone(double delta, double maxPheromone, double pheromonePersistence){
//        pheromone = ((pheromonePersistence) * pheromone) + delta;
//        if(pheromone > maxPheromone)
//            pheromone = maxPheromone;
//    }

    public void evaporatePheromone(double minPheromone, double pheromonePersistence){
        for (Connection connection :
                neighbours) {
            connection.setValue(connection.getValue() * (1 - pheromonePersistence));
            if(connection.getValue() < minPheromone)
                connection.setValue(minPheromone);
        }

    }

    public void printDebugString() {
        String str = attribute + " " + supportCount + "{ ";
        for (int i = 0; i < neighbours.size(); i++) {
            str += neighbours.get(i).getKey().attribute + " ";
        }
        str += " }";
        System.out.println(str);
    }
}
