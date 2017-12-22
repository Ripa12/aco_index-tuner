package ACO_Index;

import org.apache.commons.lang3.SystemUtils;

import java.io.*;
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
    static void generateDataSample(int nrOfAttr, long nrOfTrans, int transLength, String filename){

        String path = String.valueOf(ClassLoader.getSystemClassLoader().getResource(filename).getPath());
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.replaceFirst("/", "");
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(
                    new BufferedOutputStream(new FileOutputStream(path)), "UTF-8"));

            List<Integer> source = IntStream.rangeClosed(0, nrOfAttr).boxed().collect(Collectors.toList());
            for(long i = 0; i < nrOfTrans; i++) {
                String line = "";

                List<Integer> pool = new LinkedList<>(source);
                int transactionLength = ThreadLocalRandom.current().nextInt(1, transLength + 1);
                for(int j = 0; j < transactionLength; j++) {
                    line += pool.remove(ThreadLocalRandom.current().nextInt(0, pool.size())) + " ";
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
