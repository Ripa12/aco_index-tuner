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


    //SolutionTreeNode solutionTreeRoot;
    //private LinkedList<SolutionTreeNode> solutionTreeLeaves;

    private TreeMap<String, LinkedList<Node>> localSolution;
    //LinkedList<Node> globalSolution;
    //double bestQuality;
    private double localQuality;
    private int totalWeight;

    private Node currentNode;

    //public Ant(){}

    public double getLocalQuality(){
        return localQuality;
    }

    public Map<String, LinkedList<Node>> getClonedSolution(){
        return (Map<String, LinkedList<Node>>)localSolution.clone(); // ToDo: Should probably return a copy instead!
    }

    public void updatePheromoneLevel(double bestQuality, double maxPheromone, double pheromonePersistence){
        double pheromone = 1 / (1+((bestQuality-localQuality)/bestQuality));

        localSolution.entrySet().forEach(l -> l.getValue()
                .forEach(s->s.increasePheromone(pheromone, maxPheromone, pheromonePersistence)));
    }

    public void findSolution(double alpha, double beta, Graph graph, int weightLimit, int minsup){
        boolean localSolutionFound = false;

        localSolution = new TreeMap<>();
        //localSolution = new LinkedList<>();
//        solutionTreeRoot = new SolutionTreeNode(graph.getRoot(), null);
//        SolutionTreeNode solutionTreeNode = solutionTreeRoot;

//        solutionTreeLeaves = new LinkedList<>();

        //ArrayList<Node> nodeArray = graph.getNodeArrayClone();
        SolutionTreeNode solutionRoot = null;
        Node currentNode = null;
        Node parentNode = null;

        localQuality = 0;
        totalWeight = 0;
        while(!localSolutionFound) {
            parentNode = currentNode;
            currentNode = graph.getRandomNode(alpha, beta, minsup, totalWeight, weightLimit);

            int partialWeight = 0;
            int partialQuality = 0;
            if (currentNode != null) {
                //solutionTreeNode = solutionTreeNode.updateSolutionTree(parentNode, currentNode);
                partialWeight += currentNode.getWeight();

                LinkedList<Node> partialSolution = new LinkedList<>();
                partialSolution.addFirst(currentNode);

                BitSet supportCount = currentNode.getTransactionsClone();
                partialQuality += supportCount.cardinality();

                boolean finished = false;
                while (!finished) {
                    currentNode = currentNode.getNextProbableItem(alpha, beta, minsup, totalWeight,
                            weightLimit, supportCount);//, solutionTreeNode.edgeStates);
                    if (currentNode != null) {
                        supportCount.and(currentNode.getTransactions());
                        partialQuality += supportCount.cardinality();
                        partialWeight += currentNode.getWeight();
                        partialSolution.add(currentNode);
                        //solutionTreeNode = solutionTreeNode.updateSolutionTree(parentNode, currentNode);
                    } else {
                        finished = true;
                    }
                }
                String key = "";
                for (Node node :
                        partialSolution) {
                    key += node.getAttribute()+",";
                }
                if(!localSolution.containsKey(key)){
                    localSolution.put(key, partialSolution);
                    totalWeight += partialWeight;
                    localQuality += partialQuality;
                }
                //localSolution.add(partialSolution);
            }
            else
            {
                //solutionTreeLeaves.add(solutionTreeNode);
                //solutionTreeNode.edgeStates.set(parentNode.getIndex());
                localSolutionFound = true;
            }
        }
    }
}
