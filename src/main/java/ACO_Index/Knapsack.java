package ACO_Index;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Richard on 2018-01-04.
 */
public class Knapsack {

    private double alpha;
    private double beta;

    private int nrOfNodes;

    private double capacity;

    private List<MyAbstractObjective> objectives;
    private List<MyPheromone> pheromones;

    private double[] weights;

    // ToDo: MyConstraint instead of double[] weights
    public Knapsack(double[] weights, double cap, double alpha, double beta) {

        this.alpha = alpha;
        this.beta = beta;

        this.weights = weights;

        this.nrOfNodes = weights.length;
        this.capacity = cap;

        objectives = new ArrayList<>();

    }

    public void addObjective(MyAbstractObjective obj, double percipience) {
        objectives.add(obj);
        pheromones.add(new MyPheromone(nrOfNodes, percipience));
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

    private double calculateAggregateProbabilities(int pheromoneIndex, int currentIndex, double[] probabilities, List<Integer> neighbours) {

        double total = 0;

        // nodes to visit
        for (int i = 0; i < neighbours.size(); i++) {
            double heuristics = 0;
            for (MyAbstractObjective obj :
                    objectives) {
                heuristics += obj.calculateHeuristic(neighbours.get(i));
            }
            heuristics = Math.pow(heuristics, beta);

            double pheromone = Math.pow(pheromones.get(pheromoneIndex).getPheromone(currentIndex, neighbours.get(i)), alpha);

            probabilities[i] = heuristics * pheromone;

            total += probabilities[i];
        }

        double sumProbability = 0.0;

        for (int i = 0; i < neighbours.size(); i++) {
            sumProbability += probabilities[i] / total;
        }

        return sumProbability;
    }

    private int getNextItem(int pheromoneIndex, int currentIndex, List<Integer> neighbours) {

        double[] probabilities = new double[neighbours.size()];
        double sumProbability = calculateAggregateProbabilities(pheromoneIndex, currentIndex, probabilities, neighbours);

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
