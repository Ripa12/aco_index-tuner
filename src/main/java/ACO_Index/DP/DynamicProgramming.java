package ACO_Index.DP;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

// A Dynamic Programming based solution for 0-1 Knapsack problem
public class DynamicProgramming {

    // A utility function that returns maximum of two integers
    static int max(int a, int b) {
        return (a > b) ? a : b;
    }

    // Returns the maximum value that can be put in a knapsack of capacity W
    static int knapSack(int W, int wt[], int val[], int n) {
            // val[] is for storing maximum profit for each weight
            // wt[] is for storing weights
            // n number of item
            // W maximum capacity of bag
            // dp[W+1] to store final result
            // array to store final result
            //dp[i] stores the profit with KnapSack capacity "i"
            int dp[] = new int[W + 1];

            //initially profit with 0 to W KnapSack capacity is 0
            //memset(dp, 0, sizeof(dp));
            Arrays.fill(dp, 0);


            // iterate through all items
            for (int i = 0; i < n; i++)
                //traverse dp array from right to left
                for (int j = W; j >= wt[i]; j--)
                    dp[j] = max(dp[j], val[i] + dp[j - wt[i]]);
            /*above line finds out maximum of  dp[j](excluding ith element value)
            and val[i] + dp[j-wt[i]] (including ith element value and the
            profit with "KnapSack capacity - ith element weight") */
            return dp[W];


// This code is contributed by Gaurav Mamgain

//        int i, w;
//        int K[][] = new int[n+1][W+1];
//
//        // Build table K[][] in bottom up manner
//        for (i = 0; i <= n; i++)
//        {
//            for (w = 0; w <= W; w++)
//            {
//                if (i==0 || w==0)
//                    K[i][w] = 0;
//                else if (wt[i-1] <= w)
//                    K[i][w] = max(val[i-1] + K[i-1][w-wt[i-1]],  K[i-1][w]);
//                else
//                    K[i][w] = K[i-1][w];
//            }
//        }
//
//        return K[n][W];
    }


    // Driver program to test above function
    public static void solveKP(List<Map.Entry<Integer, Integer>> data, int capacity) {

        int n = data.size();
        int val[] = new int[n];
        int wt[] = new int[n];
        int W = capacity;


        for (int i = 0; i < n; i++) {
            val[i] = (data.get(i).getKey());
            wt[i] = (data.get(i).getValue());
        }

        System.out.println(knapSack(W, wt, val, n));
    }
}
/*This code is contributed by Rajat Mishra */