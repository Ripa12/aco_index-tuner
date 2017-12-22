package ACO_Index.FPGrowth;

import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FPGrowth {

    public static void main(String[] args) {
//        if (args.length != 2) {
//            System.out.println("FPGrowth take exactly two arguments");
//            System.exit(1);
//        }
        String path = String.valueOf(ClassLoader.getSystemClassLoader().getResource("TestData.txt").getPath());
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.replaceFirst("/", "");
        }

        System.out.println("Running FPGrowth on file: " + path);
        double minSupport = Double.parseDouble("0.01");
        File file = new File(path);
        Preprocessor preprocessor = new DefaultPreprocessor();
        try {
            FileReader fileReader = new FileReader(file);
            preprocessor.loadDataFile(fileReader);
            fileReader.close();
            System.out.println("Preprocessed the file.");
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the file.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Could not read the file.");
            System.exit(1);
        }

        long startTime = System.nanoTime();
        Map<Integer, Integer> oneItemsetCounts = getOneItemsetCounts(preprocessor.getTransactions());

        preprocessor.getFrequentItemsetWithLabels(
                findFrequentItemsetWithSuffix(
                        buildFPTree(preprocessor.getTransactions(), oneItemsetCounts, true, minSupport),
                        new ArrayList<>(),
                        ((int) minSupport * preprocessor.getTransactions().size())
                )
        ).forEach(set -> System.out.println(set.toString()));

        long frequentItemSetBuildTime = System.nanoTime() - startTime;
        System.out.println("\nTime to mine for frequent item sets: " + frequentItemSetBuildTime / 1000000000.0);
    }

    public static Map<Integer, Integer> getOneItemsetCounts(List<int[]> transactions) {
        Map<Integer, Integer> counts = new HashMap<Integer, Integer>();
        return addToOneItemsetCounts(counts, transactions);
    }

    public static Map<Integer, Integer> addToOneItemsetCounts(Map<Integer, Integer> counts, List<int[]> transactions) {
        for (int[] itemset : transactions) {
            for (int item : itemset) {
                Integer count = counts.get(item);
                if (count == null) count = 0;
                count++;
                counts.put(item, count);
            }
        }
        return counts;
    }

    public static FPTree buildFPTree(List<int[]> transactions, Map<Integer, Integer> oneItemsetCounts, boolean pruneBeforeInsert, double minSupport) {
        FPTree tree = new FPTree();
        tree.addTransactions(transactions, oneItemsetCounts, pruneBeforeInsert, minSupport);
        return tree;
    }


    public static int[] sortTransaction(int[] transaction, Map<Integer, Integer> oneItemsetCounts) {
        List<Integer> list = Arrays.stream(transaction).boxed().collect(Collectors.toList());
        Collections.sort(list, new ItemsetComparator(oneItemsetCounts));
        return list.stream().mapToInt(i -> i).toArray();
    }


    public static List<List<Integer>> findFrequentItemsetWithSuffix(FPTree tree, List<Integer> suffix, int minSupportCount) {
        List<List<Integer>> frequentItemset = new ArrayList<List<Integer>>();
        for (Integer item : tree.getItems().keySet()) {
            int support = tree.getSupportForItem(item);
            if (support >= minSupportCount && !suffix.contains(item)) {
                List<Integer> found = new ArrayList<Integer>();
                found.addAll(suffix);
                found.add(item);
                frequentItemset.add(found);

                FPTree conditionalTree = FPTree.conditionalTree(tree.getPrefixPaths(item), minSupportCount);
                frequentItemset.addAll(findFrequentItemsetWithSuffix(conditionalTree, found, minSupportCount));
            }
        }
        return frequentItemset;
    }

}