package ACO_Index;

import org.apache.commons.lang3.SystemUtils;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Richard on 2017-12-21.
 *
 * This file was taken from an answer on StackOverflow, only for testing.
 */

public class DataSampleGenerator {
    static void generateDataSample(long nrOfAttr, long nrOfTrans, String filename){

        String path = String.valueOf(ClassLoader.getSystemClassLoader().getResource(filename).getPath());
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.replaceFirst("/", "");
        }

        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(
                    new BufferedOutputStream(new FileOutputStream(path)), "UTF-8"));
            for(int i = 0; i < nrOfTrans; i++) {
                //out.println(String.format("Line %d", i));
                String line = "";
                int transactionLength = ThreadLocalRandom.current().nextInt(0, 10 + 1);
                for(int j = 0; j < transactionLength; j++) {
                    //out.println(String.format("Line %d", i));
                    line += ThreadLocalRandom.current().nextLong(0, nrOfAttr + 1) + " ";
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
