package ACO_Index;

import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Richard on 2017-12-21.
 *
 * This file was taken from an answer on StackOverflow, only for testing.
 */

public class DataSampleGenerator {
    static void generateDataSample(int nrOfAttr, long nrOfTrans, int transLength, int minWeight, int maxWeight, String filename){

        String path = String.valueOf(ClassLoader.getSystemClassLoader().getResource(filename).getPath());
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.replaceFirst("/", "");
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(
                    new BufferedOutputStream(new FileOutputStream(path)), "UTF-8"));


            List<List<Integer>> sourceTables = new ArrayList<>();

            int nrOfTables = ThreadLocalRandom.current().nextInt(50, 150);
            int minBoundary = 0;
            int maxBoundary = nrOfAttr / nrOfTables;
            for(int i = 0; i < nrOfTables; i++){
                sourceTables.add(IntStream.rangeClosed(minBoundary, maxBoundary-1).boxed().collect(Collectors.toList()));
                minBoundary = maxBoundary;
                maxBoundary += (nrOfAttr / nrOfTables);
            }

            out.println(nrOfAttr);
            int nrOfTransHalf = (int)nrOfTrans/100;

            for(int i = 0; i < nrOfAttr; i++) {
//                List<Integer> tempTableList = sourceTables.get(i);
//                int tableSize = tempTableList.size();
//                out.println(tableSize);
//                for(int j = 0; j < tableSize; j++) {

                int weight = ThreadLocalRandom.current().nextInt(minWeight, maxWeight + 1);

                int writeToRead = ThreadLocalRandom.current().nextInt(0, nrOfTransHalf + 1);

                String line = "";
                line += i + " " + weight + " " + writeToRead;
                out.println(line);
//                }
            }


            for(int i = 0; i < nrOfTrans; i++) {
                int tableIndex = ThreadLocalRandom.current().nextInt(0, nrOfTables);

                StringBuilder line = new StringBuilder(Integer.toString(tableIndex) + " ");

                List<Integer> pool = new ArrayList<>(sourceTables.get(tableIndex));
                int transactionLength = ThreadLocalRandom.current().nextInt(1, transLength + 1);
                for(int j = 0; j < transactionLength; j++) {
                    line.append(pool.remove(ThreadLocalRandom.current().nextInt(0, pool.size()))).append(" ");
                }

                out.println(line);
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(out != null) {
                out.flush();
                out.close();
            }
        }
    }
}

