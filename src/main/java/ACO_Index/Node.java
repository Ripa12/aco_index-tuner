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
    private static final int WEIGHT = 5; // ToDo: Temporary, should be read from file!

    private ArrayList<Map.Entry<Node, Boolean>> neighbours; // ToDo: Pheromone on every edge instead of node! maybe...

    private BitSet transactions;
    private String attribute;
    private long supportCount;

    private int weight;
    private double pheromone; // ToDo: Maybe float?

    public Node(BitSet transactions, String attribute) {
        this.weight = WEIGHT; // ToDo: Temporary solution

        this.pheromone = 1;
        this.transactions = transactions;
        this.attribute = attribute;

        supportCount = transactions.cardinality();

        neighbours = new ArrayList<>(); // ToDo: Possible to Reserve Memory?
    }

    public Node(BitSet transactions, String attribute, ArrayList<Node> neighbours) {
        this.weight = WEIGHT; // ToDo: Temporary solution

        this.pheromone = 1;
        this.transactions = transactions;
        this.attribute = attribute;

        supportCount = transactions.cardinality();

        this.neighbours = new ArrayList<>(neighbours.size());
        neighbours.forEach((n)->this.neighbours.add(new AbstractMap.SimpleEntry<Node, Boolean>(n, false)));
    }

    private void unblockEdges(){
        for (Map.Entry<Node, Boolean> neighbour:
        neighbours){
            neighbour.setValue(true);
        }
    }

    public int getWeight(){
        return weight;
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

    public void connect(Node to) {
        BitSet difference = ((BitSet) this.transactions.clone());
        difference.and(to.transactions);
        if (difference.cardinality() > 0) {
            if (this.supportCount >= to.supportCount) // ToDo: Check for minimum support between items, maybe?
            {
                neighbours.add(new AbstractMap.SimpleEntry<>(to, true));
            } else {
                to.neighbours.add(new AbstractMap.SimpleEntry<>(this, true));
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
    public Map.Entry<Node, Boolean> getNextItem(int minsup, BitSet bitset) {

        Map.Entry<Node, Boolean> result = null;

        long maxValue = minsup; // ToDo: Should be minsup
        for (Map.Entry<Node, Boolean> tempNode : neighbours) {
            if (tempNode.getValue()) {
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

    public Node getNextProbableItem(double alpha, double beta, int minsup, int currentWeight, int weightLimit, BitSet bitset) {

        // Code snippet below taken from http://www.baeldung.com/java-ant-colony-optimization
        ArrayList<Double> probabilities = calculateProbabilities(alpha, beta, minsup, currentWeight, weightLimit, bitset);
        double rand = ThreadLocalRandom.current().nextDouble();
        double total = 0;
        for (int i = 0; i < neighbours.size(); i++) {
            total += probabilities.get(i);
            if (total >= rand) {
                return neighbours.get(i).getKey();
            }
        }

        return null;
    }

    // ToDo: should probably be moved to Ant Colony
    private ArrayList<Double> calculateProbabilities(double alpha, double beta, int minsup, int currentWeight, int weightLimit, BitSet bitset){
        ArrayList<Double> probabilities = calculateHeuristics(minsup, bitset);

        double totalHeuristics = 0;
        double totalPheromone = 0;
        double sumProbability = 0;
        for(int i = 0; i < probabilities.size(); i++){
            totalPheromone += Math.pow(neighbours.get(i).getKey().pheromone, alpha);
            totalHeuristics += Math.pow(probabilities.get(i), beta);
        }

        for(int i = 0; i < probabilities.size(); i++){
            // ToDo: Alpha and Beta
            if(weightLimit > (currentWeight + neighbours.get(i).getKey().weight) && probabilities.get(i) > 0) {
                double tempValue = ((Math.pow(neighbours.get(i).getKey().pheromone, alpha) * Math.pow(probabilities.get(i), beta)) /
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
    private ArrayList<Double> calculateHeuristics(int minsup, BitSet bitset) {
        ArrayList<Double> heuristics = new ArrayList<>(neighbours.size());

        for (Map.Entry<Node, Boolean> neighbour: neighbours) {
            double tempValue = (double)getItemSetFrequency((BitSet) bitset.clone(), neighbour.getKey().transactions);
            heuristics.add(tempValue);
        }
        for(int i = 0; i < heuristics.size(); i++){
            if(heuristics.get(i) >= minsup) {
                heuristics.set(i, heuristics.get(i) / (weight * weight));
            }
            else{
                heuristics.set(i, 0.0);
            }
        }

        return heuristics;
    }

    public void increasePheromone(double delta, double maxPheromone, double pheromonePersistence){
        pheromone = ((pheromonePersistence) * pheromone) + delta;
        if(pheromone > maxPheromone)
            pheromone = maxPheromone;
    }

    public void evaporatePheromone(double minPheromone, double pheromonePersistence){
        pheromone = pheromone * (1 - pheromonePersistence);
        if(pheromone < minPheromone)
            pheromone = minPheromone;
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
