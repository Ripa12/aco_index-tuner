package ACO_Index.Objectives;

import ACO_Index.MyPheromone;
import ACO_Index.Objectives.MyAbstractObjective;

import java.util.Arrays;

/**
 * Created by Richard on 2018-01-04.
 */
public class SupportCountObjective extends MyAbstractObjective {

    private double sumSupportCount;

    public SupportCountObjective(double[] array, MyPheromone pheromone) {
        super(array, pheromone);

        sumSupportCount = Arrays.stream(valueArray).reduce(Double::sum).getAsDouble();
    }

    private SupportCountObjective(double[] array, MyPheromone pheromone, double sumSupportCount){
        super(array, pheromone);

        this.sumSupportCount = sumSupportCount;
    }

    @Override
    public void reset() {
        pheromone.reset(sumSupportCount);
        bestQuality = 0;
    }

    @Override
    public boolean isEqual(MyAbstractObjective other) {
        return false;
    }

    @Override
    public boolean isBetter(MyAbstractObjective other) {
        return false;
    }

    @Override
    public void updatePheromone(int[] solution) {
        pheromone.update((1/bestQuality), bestQuality, solution);
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
