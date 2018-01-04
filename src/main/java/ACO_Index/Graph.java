package ACO_Index;

import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Richard on 2017-12-18.
 */
public class Graph {
    public class ItemSet{
        private int weight;
        private int supportCount;
        private int writeToRead;
        private List<String> attributes;

        public ItemSet(int weight, int supportCount, int writeToRead){
            this.supportCount = supportCount;
            this.weight = weight;
            this.writeToRead = writeToRead;
            this.attributes = new LinkedList<>();
        }

        public ItemSet(int weight, int supportCount, int writeToRead, List<String> attributes){
            this.supportCount = supportCount;
            this.weight = weight;
            this.writeToRead = writeToRead;
            this.attributes = attributes;
        }

        public int getKey(){
            return supportCount;
        }
        public int getValue(){
            return weight;
        }
        public int getWriteToRead(){
            return writeToRead;
        }
        public void add(String a){
            this.attributes.add(a);
        }

    }

    private ArrayList<Node> nodes;
    Node root;

    private Graph(ArrayList<Node> nodes, Node root) {
        this.nodes = nodes;
        this.root = root;
        // Debugging
        //debugString(transactionMatrix);
    }

    public int getNumberOfNodes(){
        return nodes.size();
    }


    public ArrayList<Node> getNodeArrayClone(){
        return (ArrayList<Node>)nodes.clone();
    }

    // ToDo: Make graph bi-directed and let all ants have their own list of nodes, so that
    // duplicate indexes are removed by removing an element from the node list every cycle, or block and unblock edges,
    // which would not require a bi-directed graph.
    public Node.Connection getRandomNode(double alpha, double beta, int minsup, int currentWeight, int weightLimit, int antIndex){
        return this.root.getNextProbableItem(alpha, beta, minsup,currentWeight,weightLimit, this.root.getTransactionsClone(), antIndex);
        //return nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size()));
    }

    // ToDo: Ugly solution, it would be better to move solutionTreeNode class to graph class
    public Node getRoot(){
        return this.root;
    }

//    // ToDo: Kinda ugly solution, but in the right direction
//    public int getRootIndex(){
//        return this.root.getIndex();
//    }

    public void evaporatePheromones(double minPheromone, double pheromonePersistence){
        for (Node node : nodes) {
            node.evaporatePheromone(minPheromone, pheromonePersistence);
        }
    }

    public static Graph buildGraph(String filename, int nrOfAttributes, long nrOfTransactions, int nrOfAnts) {
        Map<String, BitSet> transactionMatrix = new HashMap<>();
        ArrayList<Node> nodes = null;

        String path = String.valueOf(ClassLoader.getSystemClassLoader().getResource(filename).getPath());
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.replaceFirst("/", "");
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            List<String> lineList = stream.collect(Collectors.toList());

            Iterator<String> lineIt = lineList.iterator();
            int nrOfAttr = Integer.parseInt(lineIt.next());
            nodes = new ArrayList<>(nrOfAttributes);
            for (int i = 0; i < nrOfAttr; i++){
                String[] strArr = lineIt.next().split("\\s+");
                nodes.add(i, new Node(Integer.parseInt(strArr[2]), Integer.parseInt(strArr[1]), strArr[0]));
            }
            lineIt.forEachRemaining(new Consumer<String>() {
                long entry = 1;

                public void accept(String trans) {
                    try {
                        processTransaction(trans, entry++, transactionMatrix, nrOfTransactions);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Node> connections = generateConnections(nodes, transactionMatrix, nrOfAttributes);
        BitSet tempBitSet = connections.get(0).getTransactionsClone();
        tempBitSet.set(0, tempBitSet.length(), true);

        Node root = new Node(nrOfAnts, 0, 0, "root", connections);
        root.setTransactions(tempBitSet);

        return new Graph(connections, root);
    }

    private static void processTransaction(String transaction, long entry, Map<String, BitSet> transactionMatrix, long nrOfTransactions) {
        Arrays.stream(transaction.split("\\s+"))
                .forEach(attr -> {
                    if (!transactionMatrix.containsKey(attr))
                        transactionMatrix.put(attr, new BitSet((int)nrOfTransactions));
                    transactionMatrix.get(attr).set((int)entry);
                });
    }

    private static ArrayList<Node> generateConnections(ArrayList<Node> nodes, Map<String, BitSet> transactionMatrix, int nrOfAnts) {

        //ToDo: Try to improve from O(n^2). Is it possible without nested loop?
        Iterator itFirst = transactionMatrix.entrySet().iterator();
        int index = 0;
        while (itFirst.hasNext()) {
            Map.Entry firstPair = (Map.Entry) itFirst.next();
            //Node newNode = new Node((BitSet) firstPair.getValue(), (String) firstPair.getKey());
            nodes.get(index).setTransactions((BitSet) firstPair.getValue());

            int reverseIndex = index - 1;
            while (reverseIndex > -1) {
                nodes.get(index).connect(nrOfAnts, nodes.get(reverseIndex));
                //newNode.connect(nodes.get(reverseIndex));
                reverseIndex--;
            }
            //nodes.add(index, newNode);
            index++;
        }

        return nodes;
    }

    // Temporary method for debugging
    private void debugString(Map<String, BitSet> transactionMatrix) {
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).printDebugString();
        }
    }


//    public void debugFrequentItemSets(int minsup) {
    public List<ItemSet> debugFrequentItemSets(int minsup) {
        LinkedList<ItemSet> itemsets = new LinkedList<>();

        for (Node node: nodes) {
            boolean itemsetsComplete = false;
            while(!itemsetsComplete) {
                LinkedList<String> itemset = new LinkedList<>();

                Node.Connection currentNode = new Node.Connection(node, 1.0, null);
                Node.Connection lastEdge;

                BitSet supportCount = currentNode.getKey().getTransactionsClone();
                int quality = supportCount.cardinality();
                int totalWeight = currentNode.getKey().getWeight();
                int TotalWriteToRead =  currentNode.getKey().getWriteToRead();

                boolean itemsetComplete = false;
                while (!itemsetComplete) {
                    lastEdge = currentNode;
                    currentNode = currentNode.getKey().getNextItem(minsup, supportCount);
                    if (currentNode != null) {
                        itemset.add(currentNode.getKey().getAttribute());
                        supportCount.and(currentNode.getKey().getTransactions());
                        quality += supportCount.cardinality();
                        totalWeight += currentNode.getKey().getWeight();
                        TotalWriteToRead += currentNode.getKey().getWriteToRead();
                    } else {
                        itemsetComplete = true;
                        lastEdge.setValue(0.0);
                    }
                }
                if(!itemset.isEmpty()) {
                    itemset.addFirst(node.getAttribute());
                    itemsets.add(new ItemSet(totalWeight, quality, TotalWriteToRead, itemset));
//                    itemsets.add(new AbstractMap.SimpleEntry<LinkedList<String>, ItemSet>(itemset,
//                    new AbstractMap.SimpleEntry<Integer, Integer>(quality, totalWeight)));
                }
                else{
                    if(node.getSupportCount() >= minsup) {
                        itemset.add(node.getAttribute());
                        itemsets.add(new ItemSet(totalWeight, quality, TotalWriteToRead, itemset));
//                        itemsets.add(new AbstractMap.SimpleEntry<LinkedList<String>, ItemSet>(itemset,
//                                new AbstractMap.SimpleEntry<Integer, Integer>(quality, totalWeight)));
                    }
                    itemsetsComplete = true;
                }
            }
            //resetSkip();
        }
        // Debugging
        //itemsets.forEach(set -> System.out.println(set.toString()));
//        return itemsets.stream().map(s->s.getValue()).collect(Collectors.toList());
        return itemsets;
    }

}
