package ACO_Index.Colony;

import ACO_Index.Knapsack.Knapsack;
import ACO_Index.Knapsack.KnapsackSolution;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyAnt {
    private int index;
    private KnapsackSolution solution;

    public MyAnt(int index, Knapsack knapsack){
        this.index = index;
        this.solution = knapsack.newSolution();//new AbstractSolution(knapsack.getNumberOfObjectives());
    }

    public KnapsackSolution getSolution(){
        return solution;
    }

    public int getIndex(){
        return index;
    }

    public void findSolution(Knapsack knapsack){
        solution.clear();

        int currentPosition = knapsack.getRandomPosition();

        solution.add(currentPosition);
        solution.incrementQuality(currentPosition);
//        writesQuality = knapsack.getWrites(currentPosition);
//        supportCountQuality = knapsack.getProfit(currentPosition);
        double currentWeight = knapsack.getWeight(currentPosition);

        List<Integer> neighbours = knapsack.getNeighbours(currentPosition, currentWeight);

        while (neighbours.size()>0){
            // ToDo: Only supports 2 objectives as of now
            int nextPosition = knapsack.getNextItemRandomObjective(currentPosition, neighbours);
            currentPosition = neighbours.get(nextPosition);

            neighbours.remove(nextPosition);

            //nextPosition = currentPosition;
            solution.add(currentPosition);
            solution.incrementQuality(currentPosition);



            currentWeight += knapsack.getWeight(currentPosition);
            knapsack.pruneNeighbours(neighbours, currentWeight);

//            supportCountQuality += knapsack.getProfit(nextPosition);
//            writesQuality += knapsack.getWrites(nextPosition);

        }
        //System.out.println("Weight: " +  currentWeight);
        //solution.print();
    }

}
