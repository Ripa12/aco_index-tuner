package ACO_Index.Knapsack;

import ACO_Index.Objectives.MyAbstractObjective;
import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Richard on 2018-01-04.
 */
public class Knapsack {

//    private List<MyAbstractObjective> objectives;
    static List<MyAbstractObjective> objectives;
    //private ObjectiveIterator objIterator;

    private NestedKnapsack[] nestedKnapsacks;


    private double alpha;
    private double beta;

    private int nrOfNodes;

    static double capacity;

    //MyConstraint globalConstraint;

    private double[] weights;

    // ToDo: MyConstraint instead of double[] weights
    Knapsack(NestedKnapsack[] nestedKnapsacks, double[] weights, double cap, double alpha, double beta) {

        this.nestedKnapsacks = nestedKnapsacks;

        this.alpha = alpha;
        this.beta = beta;

        this.weights = weights;

        this.nrOfNodes = weights.length;
        this.capacity = cap;

        objectives = new ArrayList<>();

    }

    public KnapsackSolution newSolution(){
//        return new KnapsackSolution(objectives.toArray(new MyAbstractObjective[objectives.size()]), objectives.size());
        return new KnapsackSolution(objectives.size());
    }

    public int getNumberOfItems(){
        return nrOfNodes;
    }

    public int getNumberOfObjectives() {
        return objectives.size();
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

    public void updatePheromones(){
        for(int objective = 0; objective < objectives.size(); objective++) {
            objectives.get(objective).updatePheromone();
        }
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

//    public void pruneNeighbours(List<Integer> neighbour, double currentWeight) {
//        neighbour.removeIf(n -> weights[n] + currentWeight > capacity);
//
//        for(NestedKnapsack nestedKnapsack : nestedKnapsacks){
//            nestedKnapsack.prune(neighbour);
//        }
//    }

    public void pruneNeighbours(List<NestedKnapsack.TableIndex> neighbour, double currentWeight) {
        for(NestedKnapsack nestedKnapsack : nestedKnapsacks){
            nestedKnapsack.prune(neighbour, currentWeight);
        }
    }

    public List<NestedKnapsack.TableIndex> getNeighbours(int currentPosition, double currentWeight) {
        List<NestedKnapsack.TableIndex> neighbours = new ArrayList<>();

        for(NestedKnapsack nestedKnapsack : nestedKnapsacks){
            neighbours.addAll(nestedKnapsack.getNeighbours(currentPosition, currentWeight));
        }
        return neighbours;
    }

//    public List<Integer> getNeighbours(int currentPosition, double currentWeight) {
//        List<Integer> neighbours = new ArrayList<>();
//
//        for (int i = 0; i < nrOfNodes; i++) {
//            if (i != currentPosition && currentWeight + weights[i] < capacity) {
//                neighbours.add(i);
//            }
//        }
//        return neighbours;
//    }

    private double calculateProbabilities(int currentIndex, MyAbstractObjective objective, List<NestedKnapsack.TableIndex> neighbours, double[] outProbabilities){
        double total = 0;

        NestedKnapsack.TableIndex neighbourIndex = null;
        for (int i = 0; i < neighbours.size(); i++) {
            double heuristics = 0;

            neighbourIndex = neighbours.get(i);
            for (MyAbstractObjective obj :
                    objectives) {
                heuristics += Math.pow(obj.calculateHeuristic(neighbourIndex.index)/weights[neighbourIndex.index], beta);
            }

            double p = Math.pow(objective.getPheromone(currentIndex, neighbourIndex.index), alpha);

            outProbabilities[i] = heuristics * p;

            total += outProbabilities[i];
        }

        double sumProbability = 0.0;

        for (int i = 0; i < neighbours.size(); i++) {
            outProbabilities[i] /= total;
            sumProbability += outProbabilities[i];
        }

        return sumProbability;
    }

    public NestedKnapsack.TableIndex getNextItemRandomObjective(int currentPosition, List<NestedKnapsack.TableIndex> neighbours) {

        int object = Math.random() < 0.5 ? 0 : 1; // ToDo: Only two objectives as of now

        double[] probabilities = new double[neighbours.size()];
        double sumProbability = calculateProbabilities(currentPosition, objectives.get(object), neighbours, probabilities);

        double rand = ThreadLocalRandom.current().nextDouble(sumProbability);

        double total = 0;

        for (int i = 0; i < neighbours.size(); i++) {
            total += probabilities[i];
            if (total >= rand) {
                return neighbours.get(i);
            }
        }

        return null;
    }

//    public NestedKnapsack.TableIndex getNextItemRandomObjective(int currentPosition, List<NestedKnapsack.TableIndex> neighbours) {
//
//        int object = Math.random() < 0.5 ? 0 : 1; // ToDo: Only two objectives as of now
//
//        double[] probabilities = new double[neighbours.size()];
//        double sumProbability = calculateProbabilities(currentPosition, objectives.get(object), neighbours, probabilities);
//
//        double rand = ThreadLocalRandom.current().nextDouble(sumProbability);
//
//        double total = 0;
//
//        for (int i = 0; i < neighbours.size(); i++) {
//            total += probabilities[i];
//            if (total >= rand) {
//                return i;
//            }
//        }
//
//        return -1;
//    }


}
