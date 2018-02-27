package ACO_Index.DataMining;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.apache.commons.lang3.SystemUtils;
import sun.applet.resources.MsgAppletViewer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Richard on 2017-12-18.
 */
public class Graph {

    private ArrayList<Node> nodes; // ToDo: Might not be necessary (Will only take up extra space! HashMap instead (i.e. transactionMatrix))
    private Node root;

    private Graph(ArrayList<Node> nodes, Node root) {
        this.nodes = nodes;
        this.root = root;

    }

    public static Graph buildGraph(String filename, int nrOfAttributes, long nrOfTransactions, int nrOfAnts) {
//        Map<String, ArrayList<Node>> tableMatrix = new HashMap<>();
        Map<String, Node> transactionMatrix = new HashMap<>();

        ArrayList<Node> nodes = null;

        String path = String.valueOf(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(filename)).getPath());
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
                Node tempNode = new Node((int) nrOfTransactions, strArr[0])
                        .setWeight(Integer.parseInt(strArr[1]))
                        .setWriteToRead(Integer.parseInt(strArr[2]));
                nodes.add(i, tempNode);


                transactionMatrix.put(Integer.toString(i), tempNode);

            }
            lineIt.forEachRemaining(new Consumer<String>() {
                long entry = 1;

                public void accept(String trans) {
                    try {
                        processTransaction(trans, entry++, transactionMatrix);

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

        Node root = new Node((int)nrOfTransactions, "root")
                .setNeighbours(nrOfAnts, connections);

        root.setTransactions(tempBitSet);

        return new Graph(connections, root);
    }

    private static void processTransaction(String transaction, long entry, Map<String, Node> transactionMatrix) {

        String tableName = transaction.substring(0, transaction.indexOf(" "));

        Arrays.stream(transaction.substring(transaction.indexOf(" ") + 1, transaction.length() - 1).split("\\s+"))
                .forEach(attr -> {
                    transactionMatrix.get(attr).setTransactionBit((int)entry);
                    transactionMatrix.get(attr).setTableName(tableName);
                });
    }

    private static ArrayList<Node> generateConnections(ArrayList<Node> nodes, Map<String, Node> transactionMatrix, int nrOfAnts) {

        //ToDo: Try to improve from O(n^2). Is it possible without nested loop?
        Iterator itFirst = transactionMatrix.entrySet().iterator();
        int index = 0;
//        while (itFirst.hasNext()) {
        while (index < nodes.size()) {

            int reverseIndex = index - 1;
            while (reverseIndex >= 0) {
                nodes.get(index).connect(nrOfAnts, nodes.get(reverseIndex));
                reverseIndex--;
            }
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


    public Map<String, List<ItemSet>> debugFrequentItemSets(int minsup) {
        Map<String, List<ItemSet>> tableMatrix = new HashMap<>();
        LinkedList<ItemSet> itemSets = new LinkedList<>();

        for (Node node: nodes) {
            boolean itemsetsComplete = false;
            while(!itemsetsComplete) {
                LinkedList<String> itemSet = new LinkedList<>();

                Node.Connection currentNode = new Node.Connection(node, 1.0, null);
                Node.Connection lastEdge;

                BitSet supportCount = currentNode.getKey().getTransactionsClone();
                int quality = supportCount.cardinality();
                int totalWeight = currentNode.getKey().getWeight();
                int totalWriteToRead =  currentNode.getKey().getWriteToRead();
                String tableName = currentNode.getKey().getTableName();

                boolean itemsetComplete = false;
                while (!itemsetComplete) {
                    lastEdge = currentNode;
                    currentNode = currentNode.getKey().getNextItem(minsup, supportCount);
                    if (currentNode != null) {
                        itemSet.add(currentNode.getKey().getAttribute());
                        supportCount.and(currentNode.getKey().getTransactions());
                        quality += supportCount.cardinality();
                        totalWeight += currentNode.getKey().getWeight();
                        totalWriteToRead += currentNode.getKey().getWriteToRead();
                    } else {
                        itemsetComplete = true;
                        lastEdge.setValue(0.0);
                    }
                }
                if(!itemSet.isEmpty()) {
                    itemSet.addFirst(node.getAttribute());
                    itemSets.add(new ItemSet(totalWeight, quality, totalWriteToRead, tableName, itemSet));

                    // ToDo: Better way? Lambda maybe?
                    if(!tableMatrix.containsKey(tableName)){
                        tableMatrix.put(tableName, new ArrayList<>());
                    }
                    tableMatrix.get(tableName).add(itemSets.getLast());

                }
                else{
                    if(node.getSupportCount() >= minsup) {
                        itemSet.add(node.getAttribute());
                        itemSets.add(new ItemSet(totalWeight, quality, totalWriteToRead, tableName, itemSet));

                        // ToDo: Better way? Lambda maybe?
                        if(!tableMatrix.containsKey(tableName)){
                            tableMatrix.put(tableName, new ArrayList<>());
                        }
                        tableMatrix.get(tableName).add(itemSets.getLast());
                    }
                    itemsetsComplete = true;
                }


            }
        }
        return tableMatrix;
    }

}
