package ACO_Index.Colony;

import ACO_Index.Knapsack.Knapsack;
import ACO_Index.Solutions.AbstractSolution;
import ACO_Index.Solutions.Solution;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyAnt {
    private int index;
    private AbstractSolution solution;

    public MyAnt(int index, Knapsack knapsack){
        this.index = index;
        this.solution = new AbstractSolution(knapsack.getNumberOfObjectives());
    }

    public AbstractSolution getSolution(){
        return solution;
    }

    public int getIndex(){
        return index;
    }

    public void findSolution(Knapsack knapsack){
        solution.clear();

        int currentPosition = knapsack.getRandomPosition();

        solution.add(currentPosition);
        knapsack.incrementQuality(currentPosition, solution);
//        writesQuality = knapsack.getWrites(currentPosition);
//        supportCountQuality = knapsack.getProfit(currentPosition);
        double currentWeight = knapsack.getWeight(currentPosition);

        List<Integer> neighbours = knapsack.getNeighbours(currentPosition, currentWeight);

        while (neighbours.size()>0){
            // ToDo: Only supports 2 objectives as of now
            int nextPosition = knapsack.getNextItemRandomObjective(currentPosition, neighbours);

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

}
