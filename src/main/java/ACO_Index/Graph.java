package ACO_Index;

import org.apache.commons.lang3.SystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Richard on 2017-12-18.
 */
public class Graph {
    private ArrayList<Node> nodes;

    private Graph(ArrayList<Node> nodes) {
        this.nodes = nodes;

        // Debugging
        //debugString(transactionMatrix);
    }

    public int getNumberOfNodes(){
        return nodes.size();
    }


    // ToDo: Would be better to return random node based on probability
    public Node getRandomNode(){
        return nodes.get(ThreadLocalRandom.current().nextInt(0, nodes.size()));
    }

    public void evaporatePheromones(double minPheromone){
        for (Node node : nodes) {
            node.evaporatePheromone(minPheromone);
        }
    }

    public static Graph buildGraph(String filename, int nrOfAttributes, long nrOfTransactions) {
        Map<String, BitSet> transactionMatrix = new HashMap<>();

        String path = String.valueOf(ClassLoader.getSystemClassLoader().getResource(filename).getPath());
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.replaceFirst("/", "");
        }

        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(new Consumer<String>() {
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

        return new Graph(generateConnections(transactionMatrix, nrOfAttributes));
    }

    private static void processTransaction(String transaction, long entry, Map<String, BitSet> transactionMatrix, long nrOfTransactions) {
        Arrays.stream(transaction.split("\\s+"))
                .forEach(attr -> {
                    if (!transactionMatrix.containsKey(attr))
                        transactionMatrix.put(attr, new BitSet((int)nrOfTransactions));
                    transactionMatrix.get(attr).set((int)entry);
                });
    }

    private static ArrayList<Node> generateConnections(Map<String, BitSet> transactionMatrix, int nrOfAttributes) {

        ArrayList<Node> nodes = new ArrayList<>(nrOfAttributes);

        //ToDo: Try to improve from O(n^2). Is it possible without nested loop?
        Iterator itFirst = transactionMatrix.entrySet().iterator();
        int index = 0;
        while (itFirst.hasNext()) {
            Map.Entry firstPair = (Map.Entry) itFirst.next();
            Node newNode = new Node((BitSet) firstPair.getValue(), (String) firstPair.getKey());

            int reverseIndex = index - 1;
            while (reverseIndex > -1) {
                newNode.connect(nodes.get(reverseIndex));
                reverseIndex--;
            }
            nodes.add(index, newNode);
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


    public void debugFrequentItemSets(long minsup) {
        LinkedList<LinkedList<String>> itemsets = new LinkedList<>();

        for (Node node: nodes) {
            boolean itemsetsComplete = false;
            while(!itemsetsComplete) {
                LinkedList<String> itemset = new LinkedList<>();

                Map.Entry<Node, Boolean> currentNode = new AbstractMap.SimpleEntry<>(node, true);
                Map.Entry<Node, Boolean> lastEdge;

                BitSet supportCount = currentNode.getKey().getTransactionsClone();

                boolean itemsetComplete = false;
                while (!itemsetComplete) {
                    lastEdge = currentNode;
                    currentNode = currentNode.getKey().getNextItem(minsup, supportCount);
                    if (currentNode != null) {
                        itemset.add(currentNode.getKey().getAttribute());
                        supportCount.and(currentNode.getKey().getTransactions());
                    } else {
                        itemsetComplete = true;
                        lastEdge.setValue(false);
                    }
                }
                if(!itemset.isEmpty()) {
                    itemset.addFirst(node.getAttribute());
                    itemsets.add(itemset);
                }
                else{
                    if(node.getSupportCount() >= minsup) {
                        itemset.add(node.getAttribute());
                        itemsets.add(itemset);
                    }
                    itemsetsComplete = true;
                }
            }
            //resetSkip();
        }
        // Debugging
        //itemsets.forEach(set -> System.out.print(set.toString()));
        itemsets.forEach(set -> System.out.println(set.toString()));
    }

}
