package ACO_Index;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Richard on 2017-12-18.
 */
public class Graph {
    final static short NR_OF_ATTRIBUTES = 5000; // ToDo: Read this value dynamically from file
    final static short NR_OF_TRANSACTIONS = 30000; // ToDo: Read this value dynamically from file

    private Map<String, BitSet> transactionMatrix;
    private ArrayList<Node> nodes;

    private Graph(Map<String, BitSet> transactionMatrix, ArrayList<Node> nodes) {
        this.transactionMatrix = transactionMatrix;
        this.nodes = nodes;

        debugString(transactionMatrix);
    }

    public static Graph buildGraph(String filename){
        Map<String, BitSet> transactionMatrix = new HashMap<>();

        try (Stream<String> stream = Files.lines(Paths.get(ClassLoader.getSystemClassLoader().getResource(filename).getPath()))) {
            stream.forEach(new Consumer<String>() {
                short entry = 0;
                public void accept(String trans) {
                    try {
                        processTransaction(trans, entry++, transactionMatrix);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {e.printStackTrace(); }

        return new Graph(transactionMatrix, generateConnections(transactionMatrix));
    }

    private static void processTransaction(String transaction, short entry, Map<String, BitSet> transactionMatrix){
        Arrays.stream(transaction.split("\\s+"))
                .forEach(attr -> {if(!transactionMatrix.containsKey(attr))transactionMatrix.put(attr, new BitSet(NR_OF_TRANSACTIONS)); transactionMatrix.get(attr).set(entry);});
    }

    private static ArrayList<Node> generateConnections(Map<String, BitSet> transactionMatrix){

        ArrayList<Node> nodes = new ArrayList<>(NR_OF_ATTRIBUTES);

        //ToDo: Try to improve from O(n^2). Is it possible without nested loop?
        Iterator itFirst = transactionMatrix.entrySet().iterator();
        int index = 0;
        while (itFirst.hasNext()) {
            Map.Entry firstPair = (Map.Entry)itFirst.next();
            //itFirst.remove(); // avoids a ConcurrentModificationException
            Node newNode = new Node((BitSet) firstPair.getValue(), (String)firstPair.getKey());

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
    private void debugString(Map<String, BitSet> transactionMatrix){
//        transactionMatrix.forEach((attr, trans) -> System.out.println(attr + " " + trans.stream().mapToObj(i -> trans.get(i) ? '1' : '0')
//                .collect(
//                        () -> new StringBuilder(NR_OF_TRANSACTIONS),
//                        (buffer, characterToAdd) -> buffer.append(characterToAdd),
//                        StringBuilder::append
//                )
//                .toString()));
        //transactionMatrix.forEach((attr, trans)-> System.out.println(attr + " " + trans.toString()));
        ;

        for (int i = 0; i < nodes.size(); i++){
            nodes.get(i).printDebugString();
        }
    }

}
