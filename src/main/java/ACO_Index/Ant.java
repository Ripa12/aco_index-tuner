package ACO_Index;

import sun.awt.image.ImageWatched;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Created by Richard on 2017-12-18.
 */
public class Ant {

    private class SolutionTreeNode{
        Node node;
        Node parent;
        BitSet edgeStates;
        Map<Node, SolutionTreeNode> children; // ToDo: or just regular array with same size as node.neighbours

        SolutionTreeNode(Node node, Node parent){
            this.node = node;
            this.parent = parent;
            edgeStates = new BitSet(node.getNrOfNeighbours());
            children = new HashMap<>();
        }

        SolutionTreeNode updateSolutionTree(Node node, Node parent){
            SolutionTreeNode result = null;
            if(!children.containsKey(node)){
                result = new SolutionTreeNode(node, parent);
                children.put(node, result);
                totalWeight += node.getWeight();
            }
            else{
                result = children.get(node);
            }
            return result;
        }
    }


    private BitSet visitedNodes;
    //SolutionTreeNode solutionTreeRoot;
    //private LinkedList<SolutionTreeNode> solutionTreeLeaves;
    private LinkedList<LinkedList<Node.Connection>> localSolution;
    //private TreeMap<String, LinkedList<Node>> localSolution;
    //LinkedList<Node> globalSolution;
    //double bestQuality;
    private double localQuality;
    private int totalWeight;
    private int index;

    private Node currentNode;

    public Ant(int index){
        this.index = index;
    }

    public int getTotalWeight(){
        return totalWeight;
    }

    public double getLocalQuality(){
        return localQuality;
    }

    public LinkedList<LinkedList<Node.Connection>> getClonedSolution(){
        return (LinkedList<LinkedList<Node.Connection>>)localSolution.clone(); // ToDo: Should probably return a copy instead!
    }

    public void updatePheromoneLevel(double bestQuality, double maxPheromone, double pheromonePersistence){
        double delta = 1 / (1+((bestQuality-localQuality)/bestQuality));

        for (LinkedList<Node.Connection> connections :
                localSolution) {
            for (Node.Connection connection :
                 connections) {
                connection.setValue(((pheromonePersistence) * connection.getValue()) + delta);
                if(connection.getValue() > maxPheromone)
                    connection.setValue(maxPheromone);
            }
        }
    }

    public void findSolution(double alpha, double beta, Graph graph, int weightLimit, int minsup){
        boolean localSolutionFound = false;

        //visitedNodes = new BitSet(graph.getNumberOfNodes());
        //visitedNodes.set(0, visitedNodes.size(), false);
        //localSolution = new TreeMap<>();
        localSolution = new LinkedList<>();
//        solutionTreeRoot = new SolutionTreeNode(graph.getRoot(), null);
//        SolutionTreeNode solutionTreeNode = solutionTreeRoot;

//        solutionTreeLeaves = new LinkedList<>();

        //ArrayList<Node> nodeArray = graph.getNodeArrayClone();
        SolutionTreeNode solutionRoot = null;
        Node.Connection currentNode = null;
        Node.Connection parentNode = null;

        localQuality = 0;
        totalWeight = 0;
        while(!localSolutionFound) {
            parentNode = currentNode;
            currentNode = graph.getRandomNode(alpha, beta, minsup, totalWeight, weightLimit, index);

            int partialWeight = 0;
            int partialQuality = 0;
            if (currentNode != null) {
                //solutionTreeNode = solutionTreeNode.updateSolutionTree(parentNode, currentNode);
//                partialWeight += currentNode.getKey().getWeight();
                totalWeight += currentNode.getKey().getWeight();

                LinkedList<Node.Connection> partialSolution = new LinkedList<>();
                partialSolution.addFirst(currentNode);

                BitSet supportCount = currentNode.getKey().getTransactionsClone();
                partialQuality = supportCount.cardinality();

                boolean finished = false;
                while (!finished) {
                    parentNode = currentNode;
                    currentNode = currentNode.getKey().getNextProbableItem(alpha, beta, minsup, totalWeight,
                            weightLimit, supportCount, index);//, solutionTreeNode.edgeStates);
                    if (currentNode != null) {
                        supportCount.and(currentNode.getKey().getTransactions());
                        partialQuality += supportCount.cardinality();
//                        partialWeight += currentNode.getKey().getWeight();
                        //localQuality += supportCount.cardinality();
                        totalWeight += currentNode.getKey().getWeight();
                        partialSolution.add(currentNode);
                        //solutionTreeNode = solutionTreeNode.updateSolutionTree(parentNode, currentNode);
                    } else {
                        finished = true;

                        parentNode.antBlockades.set(index);

                    }
                }
//                String key = "";
//                for (Map.Entry<Node, Double> node :
//                        partialSolution) {
//                    key += node.getKey().getAttribute()+",";
//                }
//                if(!localSolution.containsKey(key)){
//                    localSolution.put(key, partialSolution);
//                    totalWeight += partialWeight;
//                    localQuality += partialQuality;
//                }

                localQuality += partialQuality;
                localSolution.add(partialSolution);
            }
            else
            {
                for (LinkedList<Node.Connection> connections :
                        localSolution) {
                    for (Node.Connection connection :
                            connections) {
                        connection.antBlockades.clear(index);
                    }
                }
                //solutionTreeLeaves.add(solutionTreeNode);
                //solutionTreeNode.edgeStates.set(parentNode.getIndex());
                localSolutionFound = true;
            }
        }
    }
}
