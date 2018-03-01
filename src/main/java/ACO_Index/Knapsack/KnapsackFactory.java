package ACO_Index.Knapsack;

import ACO_Index.DataMining.ItemSet;
import ACO_Index.MyPheromone;
import ACO_Index.Objectives.SupportCountObjective;
import ACO_Index.Objectives.WriteRatioObjective;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by Richard on 2018-01-04.
 */
public class KnapsackFactory implements IKnapsackFactory{

    private static final double DEFAULT_PERSISTENCE = .3;
    private static final double DEFAULT_ALPHA = 1;
    private static final double DEFAULT_Beta = 2;
    private static final double DEFAULT_CAPACITY = 4000;

    private Map<String, List<ItemSet>> data;
    private double persistence;
    private double alpha;
    private double beta;
    private double capacity;

    private KnapsackFactory(Map<String, List<ItemSet>> data) {
        this.data = data;
        this.persistence = DEFAULT_PERSISTENCE;
        this.alpha = DEFAULT_ALPHA;
        this.beta = DEFAULT_Beta;
        this.capacity = DEFAULT_CAPACITY;
    }

    public static KnapsackFactory getFactory(Map<String, List<ItemSet>> data){
        return new KnapsackFactory(data);
    }

    public KnapsackFactory setPersisitence(double p){
        persistence = p;
        return this;
    }
    public KnapsackFactory setAlpha(double a){
        alpha = a;
        return this;
    }
    public KnapsackFactory setBeta(double b){
        beta = b;
        return this;
    }
    public KnapsackFactory setCapacity(double c){
        capacity = c;
        return this;
    }

    @Override
    public Knapsack buildKnapsack(){
        Knapsack knapsack;

        int nrOfNodes = data.entrySet().stream().mapToInt(i -> i.getValue().size()).sum();

        double[] writeRatio = new double[nrOfNodes];
        double[] supportCount = new double[nrOfNodes];
        double[] weights = new double[nrOfNodes];

        NestedKnapsack[] nestedKnapsacks = new NestedKnapsack[1];
        nestedKnapsacks[0] = new NestedKnapsack((ArrayList<Integer>)IntStream.rangeClosed(0, nrOfNodes).boxed().collect(Collectors.toList()));

        for (Object o : data.entrySet()) {
            Map.Entry obj = (Map.Entry) o;
            List itemSet = (List) obj.getValue();

            int offset = 0;
            for (int i = offset; i < itemSet.size() + offset; i++) {
                supportCount[i] = (((ItemSet) itemSet.get(i)).getKey());
                weights[i] = (((ItemSet) itemSet.get(i)).getValue());
                writeRatio[i] = (((ItemSet) itemSet.get(i)).getWriteToRead());
                offset++;
            }
        }

        WriteRatioObjective wObjective = new WriteRatioObjective(writeRatio, new MyPheromone(nrOfNodes, persistence));
        SupportCountObjective scObjective = new SupportCountObjective(supportCount, new MyPheromone(nrOfNodes, persistence));

        knapsack = new Knapsack(nestedKnapsacks, weights, capacity, alpha, beta);
        knapsack.addObjective(wObjective);
        knapsack.addObjective(scObjective);

        return knapsack;
    }

    @Override
    public Knapsack buildNestedKnapsack() {
        Knapsack knapsack;

        int nrOfNodes = data.entrySet().stream().mapToInt(i -> i.getValue().size()).sum();

        double[] writeRatio = new double[nrOfNodes];
        double[] supportCount = new double[nrOfNodes];
        double[] weights = new double[nrOfNodes];

        NestedKnapsack[] nestedKnapsacks = new NestedKnapsack[data.size()];

        int currentTable = 0;
        int offset = 0;
        for (Object o : data.entrySet()) {
            Map.Entry obj = (Map.Entry) o;
            List itemSet = (List) obj.getValue();

            List<Integer> indexes = IntStream.rangeClosed(offset, (itemSet.size() + offset)).boxed().collect(Collectors.toList());
            nestedKnapsacks[currentTable++] = new NestedKnapsack((ArrayList<Integer>) indexes);

            for (int i = offset; i < itemSet.size() + offset; i++) {
                supportCount[i] = (((ItemSet) itemSet.get(i)).getKey());
                weights[i] = (((ItemSet) itemSet.get(i)).getValue());
                writeRatio[i] = (((ItemSet) itemSet.get(i)).getWriteToRead());
                offset++;
            }
        }

        WriteRatioObjective wObjective = new WriteRatioObjective(writeRatio, new MyPheromone(nrOfNodes, persistence));
        SupportCountObjective scObjective = new SupportCountObjective(supportCount, new MyPheromone(nrOfNodes, persistence));

        knapsack = new Knapsack(nestedKnapsacks, weights, capacity, alpha, beta);
        knapsack.addObjective(wObjective);
        knapsack.addObjective(scObjective);

        return knapsack;
    }
}
