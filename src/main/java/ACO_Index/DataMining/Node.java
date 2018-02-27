package ACO_Index.DataMining;

import java.util.*;

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

    private ArrayList<Connection> neighbours; // ToDo: Pheromone on every edge instead of node!

    private BitSet transactions;
    private String attribute;
    private long supportCount;
    private int writeToRead;

    private int weight;

    private int index;

    private String tableName;


    public Node(int nrOfTransactions, String attribute) {
        this.weight = 0;
        this.writeToRead = 0;
        this.tableName = "";

        this.transactions = new BitSet(nrOfTransactions);
        this.attribute = attribute;

        supportCount = 0;

        neighbours = new ArrayList<>();
    }

    public void setTransactionBit(int bit){
        this.transactions.set(bit);
    }

    public Node setNeighbours(int nrOfAnts, ArrayList<Node> neighbours){
        this.neighbours = new ArrayList<>(neighbours.size());
        neighbours.forEach((n)->this.neighbours.add(new Connection(n, 1.0, new BitSet(nrOfAnts))));
        return this;
    }

    public Node setWriteToRead(int writeToRead){
        this.writeToRead = writeToRead;
        return this;
    }

    public Node setAttribute(String attribute){
        this.attribute = attribute;
        return this;
    }

    public Node setWeight(int weight){
        this.weight = weight;
        return this;
    }

    public Node setTableName(String index){
        this.tableName = index;
        return this;
    }

    public void setTransactions(BitSet transactions){
        this.transactions = transactions;
        supportCount = transactions.cardinality();
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

    public String getTableName(){
        return tableName;
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
        this.supportCount = transactions.cardinality();
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

    public void printDebugString() {
        String str = attribute + " " + supportCount + "{ ";
        for (int i = 0; i < neighbours.size(); i++) {
            str += neighbours.get(i).getKey().attribute + " ";
        }
        str += " }";
        System.out.println(str);
    }
}
