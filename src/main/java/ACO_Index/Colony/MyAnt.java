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
        this.solution = new Solution();

    }

    public Solution getSolution(){
        return solution;
    }

    public int getIndex(){
        return index;
    }

    public void findSolution(int capacity, double alpha, double beta){

        solution.clear();

        currentPosition = knapsack.getRandomPosition();
        solution.add(currentPosition);
//        writesQuality = knapsack.getWrites(currentPosition);
//        supportCountQuality = knapsack.getProfit(currentPosition);
        double currentWeight = knapsack.getWeight(currentPosition);

        List<Integer> neighbours = knapsack.getNeighbours(currentPosition, currentWeight);

        while (neighbours.size()>0){
            int nextPosition = getNextItem(neighbours, Math.random() < 0.5 ? 0 : 1, alpha, beta);

            neighbours.remove(nextPosition);

            knapsack.pruneNeighbours(neighbours, currentWeight);

            currentPosition = nextPosition;
            solution.add(nextPosition);


            supportCountQuality += knapsack.getProfit(nextPosition);
            writesQuality += knapsack.getWrites(nextPosition);
            currentWeight += knapsack.getWeight(nextPosition);
        }
        System.out.println("Weight: " +  currentWeight);
        System.out.println(solution);
    }

    private double calculateWriteHeuristic(int i){
        return ((knapsack.getMaxWrites() - knapsack.getWrites(i))/knapsack.getWeight(i));
    }

    private double calculateSupportHeuristic(int i){
        return knapsack.getProfit(i)/knapsack.getWeight(i);
    }

    // ToDo: Move to MyAbstractObjective!
    private double calculateAggregateProbabilities(double[] probability, int objective, List<Integer> neighbours,
                                                   double alpha, double beta){
//        double[] tij = new double[neighbours.size()];
//        double[] nij = new double[neighbours.size()]; // This is the reason why it took so long!
//        double[] nij = new double[nrOfNodes];
        double[] tijnij = new double[neighbours.size()];

        double scTotal = 0;
        double wTotal = 0;

        // nodes to visit
        for (int i = 0; i < neighbours.size(); i++){
//            tij[i] = Math.pow(knapsack.getPheromone(objective, index, neighbours.get(i)), alpha);
//            nij[i] = Math.pow(calculateSupportHeuristic(neighbours.get(i)), beta) + Math.pow(calculateWriteHeuristic(neighbours.get(i)), beta);
            tijnij[i] = Math.pow(knapsack.getPheromone(objective, index, neighbours.get(i)), alpha) *
                    (Math.pow(calculateSupportHeuristic(neighbours.get(i)), beta) +
                            Math.pow(calculateWriteHeuristic(neighbours.get(i)), beta));

            scTotal += tijnij[i];
            //scTotal += tij[i] * nij[i];
            //wTotal += tij[i] * nij[1][i];
        }

        double sumProbability = 0.0;

        for (int i = 0; i < neighbours.size(); i++){
//            probability[i] = ((tij[i] * nij[i]) / scTotal);
            probability[i] = tijnij[i] / scTotal;

            sumProbability += probability[i];
        }

        return sumProbability;
    }



    // ToDo: Pass MyAbstractObjective instance as parameter
    private int getNextItem(List<Integer> neighbours, int indexObj, double alpha, double beta){

        double[] probability = new double[neighbours.size()];
        double sumProbability = calculateAggregateProbabilities(probability, indexObj, neighbours, alpha, beta);

        int j = 0;

        double p = probability[j];

        double r = ThreadLocalRandom.current().nextDouble(sumProbability);

        while (p < r) {
            j = j + 1;
            p = p + probability[j];
        }

        return j;
    }

}
