package ACO_Index.Objectives;

import ACO_Index.MyPheromone;
import ACO_Index.Objectives.MyAbstractObjective;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Richard on 2018-01-04.
 */
public class SupportCountObjective extends MyAbstractObjective {

    private double sumSupportCount;

    public SupportCountObjective(double[] array, MyPheromone pheromone) {
        super(array, pheromone);

        sumSupportCount = Arrays.stream(valueArray).reduce(Double::sum).getAsDouble();
        this.bestQuality = 0;
    }

    private SupportCountObjective(double[] array, MyPheromone pheromone, double sumSupportCount){
        super(array, pheromone);

        this.sumSupportCount = sumSupportCount;
        this.bestQuality = 0;
    }

    @Override
    public void reset() {
        pheromone.reset(sumSupportCount);
        //bestQuality = 0;
    }

    @Override
    public double getInitialValue() {
        return 0;
    }


//    @Override
//    public boolean isEqual(double other) {
//        return bestQuality == other;
//    }

    @Override
    public boolean isBetter(double other) {
        return other > bestQuality;
    }

    @Override
    public boolean isBetter(double first, double second) {
        return second > first;
    }

    @Override
    public void updatePheromone() {
        pheromone.update((1/bestQuality), bestQuality, bestSolution);
    }

    @Override
    public double calculateHeuristic(int index) {
        return valueArray[index];
    }

    @Override
    public MyAbstractObjective clone() {
        return new SupportCountObjective(this.valueArray, pheromone, sumSupportCount);
    }


}
