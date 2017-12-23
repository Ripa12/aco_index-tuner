package ACO_Index;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;
import sun.awt.image.ImageWatched;

import javax.swing.text.StyledEditorKit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Created by Richard on 2017-12-18.
 */
public class Node {
    private static final double PHEROMONE_PERSISTENCE = 0.5; // ToDo: Friend to Ant Colony class
    private static final double WEIGHT_LIMIT = 100; // ToDo: Move somewhere else!

    private static double maxPheromone;
    private static double minPheromone;

    private ArrayList<Map.Entry<Node, Boolean>> neighbours; // ToDo: Pheromone on every edge instead of node!

    private BitSet transactions;
    private String attribute;
    private long supportCount;

    private int weight;
    private double pheromone; // ToDo: Maybe float?

    public Node(BitSet transactions, String attribute) {
        this.pheromone = 1;
        this.transactions = transactions;
        this.attribute = attribute;

        supportCount = transactions.cardinality();

        neighbours = new ArrayList<>(); // ToDo: Possible to Reserve Memory?
    }

    private void unblockEdges(){
        for (Map.Entry<Node, Boolean> neighbour:
        neighbours){
            neighbour.setValue(true);
        }
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
    public Map.Entry<Node, Boolean> getNextItem(long minsup, BitSet bitset) {

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

    public Node getNextProbableItem(long minsup, int currentWeight, BitSet bitset) {

        Node result = null;

        // Code snippet below taken from http://www.baeldung.com/java-ant-colony-optimization
        ArrayList<Double> probabilities = calculateProbabilities(minsup, currentWeight, bitset);
        double rand = ThreadLocalRandom.current().nextDouble();
        double total = 0;
        for (int i = 0; i < neighbours.size(); i++) {
            total += probabilities.get(i);
            if (total >= rand) {
                result = neighbours.get(i).getKey();
            }
        }

        return result;
    }

    private ArrayList<Double> calculateProbabilities(long minsup, int currentWeight, BitSet bitset){
        ArrayList<Double> probabilities = calculateHeuristics(minsup, bitset);

        double totalHeuristics = 0;
        double totalPheromone = 0;
        for(int i = 0; i < probabilities.size(); i++){
            totalPheromone += neighbours.get(i).getKey().pheromone;
            totalHeuristics += probabilities.get(i);
        }
        for(int i = 0; i < probabilities.size(); i++){
            // ToDo: Alpha and Beta
            if(WEIGHT_LIMIT >= (currentWeight + weight)) {
                probabilities.set(i, ((neighbours.get(i).getKey().pheromone * probabilities.get(i)) /
                        (totalPheromone * totalHeuristics)/neighbours.get(i).getKey().weight));
            }
            else{
                probabilities.set(i, 0.0);
            }
        }

        return probabilities;
    }

    // ToDo: move this function to Ant and treat as an executable argument when invoking getNextItem
    private ArrayList<Double> calculateHeuristics(long minsup, BitSet bitset) {
        ArrayList<Double> heuristics = new ArrayList<>(neighbours.size());

        double total = 0;
        for (Map.Entry<Node, Boolean> neighbour: neighbours) {
            double tempValue = (double)getItemSetFrequency((BitSet) bitset.clone(), neighbour.getKey().transactions);
            heuristics.add(tempValue);
            total += tempValue;
        }
        for(int i = 0; i < heuristics.size(); i++){
            if(heuristics.get(i) >= minsup) {
                heuristics.set(i, heuristics.get(i) / total);
            }
            else{
                heuristics.set(i, 0.0);
            }
        }

        return heuristics;
    }

    public void increasePheromone(double delta){
        pheromone = ((PHEROMONE_PERSISTENCE) * pheromone) + delta;
        if(pheromone > maxPheromone)
            pheromone = maxPheromone;
    }

    public void evaporatePheromone(){
        pheromone = pheromone * (1 - PHEROMONE_PERSISTENCE);
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
