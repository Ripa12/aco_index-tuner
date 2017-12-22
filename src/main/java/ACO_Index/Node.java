package ACO_Index;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;
import sun.awt.image.ImageWatched;

import javax.swing.text.StyledEditorKit;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Richard on 2017-12-18.
 */
public class Node {
    private ArrayList<Map.Entry<Node, Boolean>> neighbours;

    private BitSet transactions;
    private String attribute;
    private long supportCount;

    private float pheromone;

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

    public void generateFrequentItemsets(long minsup, LinkedList<String> itemset, LinkedList<LinkedList<String>> itemsets) {
        itemset.add(attribute);
        for (Map.Entry<Node, Boolean> neighbour : neighbours) {
            //boolean isLeaf = true;
            LinkedList<String> newSet = (LinkedList<String>) itemset.clone();
            long tempValue = getItemSetFrequency(neighbour.getKey().transactions);
            if (minsup <= tempValue) {
                //isLeaf = false;
                itemsets.add(newSet);

                neighbour.getKey().generateFrequentItemsets(minsup, newSet, itemsets);
            }
            //itemsets.add(newSet);
        }
    }

//    public void calculateProbability() {
//
//        ArrayList<Double> probabilities = new ArrayList<>(neighbours.size());
//        double neighbourHeuristics = 0;
//        for (Node neighbour: neighbours) {
//            double tempValue = (double)getItemSetFrequency(neighbour.transactions);
//            probabilities.add(tempValue);
//            neighbourHeuristics += tempValue;
//        }
//        double sumHeuristics = 0;
//        int i;
//        for(i = 0; i < neighbours.size(); i++){
//            double tempValue =  (probabilities.get(i) / neighbourHeuristics) * neighbours.get(i).pheromone;
//            probabilities.set(i, tempValue);
//            sumHeuristics += tempValue;
//        }
//        for(i = 0; i < neighbours.size(); i++){
//            double tempValue =  probabilities.get(i) / sumHeuristics;
//            probabilities.set(i, tempValue);
//        }
//
//        int i = ant.trail[currentIndex];
//        double pheromone = 0.0;
//        for (int l = 0; l < numberOfCities; l++) {
//            if (!ant.visited(l)){
//                pheromone
//                        += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);
//            }
//        }
//        for (int j = 0; j < numberOfCities; j++) {
//            if (ant.visited(j)) {
//                probabilities[j] = 0.0;
//            } else {
//                double numerator
//                        = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
//                probabilities[j] = numerator / pheromone;
//            }
//        }
//    }

    public void printDebugString() {
        String str = attribute + " " + supportCount + "{ ";
        for (int i = 0; i < neighbours.size(); i++) {
            str += neighbours.get(i).getKey().attribute + " ";
        }
        str += " }";
        System.out.println(str);
    }
}
