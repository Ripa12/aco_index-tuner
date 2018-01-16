package ACO_Index.Knapsack;

import ACO_Index.Graph;
import ACO_Index.Objectives.SupportCountObjective;
import ACO_Index.Objectives.WriteRatioObjective;

import java.util.List;

/**
 * Created by Richard on 2018-01-04.
 */
public class KnapsackFactory implements IKnapsackFactory{

    private static final double DEFAULT_PERSISTENCE = .3;
    private static final double DEFAULT_ALPHA = 1;
    private static final double DEFAULT_Beta = 2;
    private static final double DEFAULT_CAPACITY = 4000;

    private List<Graph.ItemSet> data;
    private double persistence;
    private double alpha;
    private double beta;
    private double capacity;

    public KnapsackFactory(List<Graph.ItemSet> data) {
        this.data = data;
        this.persistence = DEFAULT_PERSISTENCE;
        this.alpha = DEFAULT_ALPHA;
        this.beta = DEFAULT_Beta;
        this.capacity = DEFAULT_CAPACITY;
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
    public Knapsack buildKnapsack(int capacity){
        Knapsack knapsack = null;

        int nrOfNodes = data.size();

        double[] writeRatio = new double[nrOfNodes];
        double[] supportCount = new double[nrOfNodes];
        double[] weights = new double[nrOfNodes];
        for (int i = 0; i < nrOfNodes; i++){
            supportCount[i]=(data.get(i).getKey());
            weights[i]=(data.get(i).getValue());
            writeRatio[i]=(data.get(i).getWriteToRead());
        }
        WriteRatioObjective wObjective = new WriteRatioObjective(writeRatio);
        SupportCountObjective scObjective = new SupportCountObjective(supportCount);

        knapsack = new Knapsack(weights, capacity, alpha, beta);
        knapsack.addObjective(wObjective, persistence);
        knapsack.addObjective(scObjective, persistence);

        return knapsack;
    }
}
