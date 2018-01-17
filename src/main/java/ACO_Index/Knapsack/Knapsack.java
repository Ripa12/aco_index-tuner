package ACO_Index.Knapsack;

import ACO_Index.Constraints.MyConstraint;
import ACO_Index.MyPheromone;
import ACO_Index.Objectives.MyAbstractObjective;
import ACO_Index.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Richard on 2018-01-04.
 */
public class Knapsack {

    private List<MyAbstractObjective> objectives;

    private double alpha;
    private double beta;

    private int nrOfNodes;

    private double capacity;

    MyConstraint globalConstraint;


    private double[] weights;

    // ToDo: MyConstraint instead of double[] weights
    public Knapsack(double[] weights, double cap, double alpha, double beta) {

        this.alpha = alpha;
        this.beta = beta;

        this.weights = weights;

        this.nrOfNodes = weights.length;
        this.capacity = cap;

    }

    public int getNumberOfItems(){
        return nrOfNodes;
    }

    public double getWeight(int index){
        return weights[index];
    }

    public int getRandomPosition(){
        return ThreadLocalRandom.current().nextInt(0, nrOfNodes);
    }

    public void addObjective(MyAbstractObjective obj) {
        objectives.add(obj);
    }

    public void updatePheromone(int objective, Solution globalBestSolution){
        objectives.get(objective).updatePheromone(globalBestSolution.getSolution());
    }

    public void reset(){
        for(MyAbstractObjective obj : objectives){
            obj.reset();
        }
    }

    public void evaporate(){
        for(MyAbstractObjective p : objectives){
            p.evaporate();
        }
    }

    public void incrementQuality(int position, Solution solution){

    }

    public void pruneNeighbours(List<Integer> neighbour, double currentWeight) {
        neighbour.removeIf(n -> weights[n] + currentWeight > capacity);
    }

    public List<Integer> getNeighbours(int currentPosition, double currentWeight) {
        List<Integer> neighbours = new ArrayList<>();
        for (int i = 0; i < nrOfNodes; i++) {
            if (i != currentPosition && currentWeight + weights[i] < capacity) {
                neighbours.add(i);
            }
        }
        return neighbours;
    }

    private int getNextItem(List<MyAbstractObjective> objectives, MyPheromone pheromone, int currentIndex, List<Integer> neighbours) {

        double[] probabilities = new double[neighbours.size()];
        double total = 0;

        int neighbourIndex = 0;
        for (int i = 0; i < neighbours.size(); i++) {
            double heuristics = 0;

            neighbourIndex = neighbours.get(i);
            for (MyAbstractObjective obj :
                    objectives) {
                heuristics += Math.pow(obj.calculateHeuristic(neighbourIndex)/weights[neighbourIndex], beta);
            }

            double p = Math.pow(pheromone.getPheromone(currentIndex, neighbourIndex), alpha);

            probabilities[i] = heuristics * p;

            total += probabilities[i];
        }

        double sumProbability = 0.0;

        for (int i = 0; i < neighbours.size(); i++) {
            sumProbability += probabilities[i] / total;
        }

        double rand = ThreadLocalRandom.current().nextDouble(sumProbability);

        total = 0;

        for (int i = 0; i < neighbours.size(); i++) {
            total += probabilities[i];
            if (total >= rand) {
                return i;
            }
        }

        return -1;
    }

}
