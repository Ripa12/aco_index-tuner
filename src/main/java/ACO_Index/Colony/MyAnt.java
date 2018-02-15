package ACO_Index.Colony;

import ACO_Index.Knapsack.Knapsack;
import ACO_Index.Solution;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyAnt {
    private int index;
    private Knapsack knapsack;

    Solution solution;
    private int currentPosition;

    public MyAnt(int index, Knapsack knapsack){
        this.knapsack = knapsack;
        this.index = index;
        this.solution = new Solution(knapsack.getNumberOfObjectives());

    }

    public Solution getSolution(){
        return solution;
    }

    public int getIndex(){
        return index;
    }

    public void findSolution(){
        solution.clear();

        currentPosition = knapsack.getRandomPosition();
        solution.add(currentPosition);
//        writesQuality = knapsack.getWrites(currentPosition);
//        supportCountQuality = knapsack.getProfit(currentPosition);
        double currentWeight = knapsack.getWeight(currentPosition);

        List<Integer> neighbours = knapsack.getNeighbours(currentPosition, currentWeight);

        while (neighbours.size()>0){
            // ToDo: Only supports 2 objectives as of now
            int nextPosition = getNextItem(currentPosition, Math.random() < 0.5 ? 0 : 1, neighbours);

            neighbours.remove(nextPosition);

            knapsack.pruneNeighbours(neighbours, currentWeight);

            currentPosition = nextPosition;
            solution.add(nextPosition);

            knapsack.incrementQuality(nextPosition, solution);
//            supportCountQuality += knapsack.getProfit(nextPosition);
//            writesQuality += knapsack.getWrites(nextPosition);
            currentWeight += knapsack.getWeight(nextPosition);
        }
        System.out.println("Weight: " +  currentWeight);
        System.out.println(solution);
    }

    // ToDo: Pass MyAbstractObjective instance as parameter
    private int getNextItem(int currentIndex, int objective, List<Integer> neighbours) {

        double[] probabilities = new double[neighbours.size()];
        double sumProbability = knapsack.calculateProbabilities(currentIndex, objective, neighbours, probabilities);

        double rand = ThreadLocalRandom.current().nextDouble(sumProbability);

        double total = 0;

        for (int i = 0; i < neighbours.size(); i++) {
            total += probabilities[i];
            if (total >= rand) {
                return i;
            }
        }

        return -1;
    }

}
