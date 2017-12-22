/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ACO_Index.AnotherFPGrowth;

import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Code taken from https://github.com/goodinges/FP-Growth-Java/tree/master/src/fpgrowth
 * @author Kamran
 */
public class Main {

    static int threshold = 20;
    static String file = "census.dat";


    public static void main(String[] args) throws FileNotFoundException {
        String path = String.valueOf(ClassLoader.getSystemClassLoader().getResource("TestData.txt").getPath());
        if (SystemUtils.IS_OS_WINDOWS) {
            path = path.replaceFirst("/", "");
        }

        long start = System.nanoTime();
        new FPGrowth(new File(path), threshold);
        System.out.println((System.nanoTime() - start) / 1000000000.0);
    }
}