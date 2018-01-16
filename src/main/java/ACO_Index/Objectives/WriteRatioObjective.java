package ACO_Index.Objectives;

import ACO_Index.MyPheromone;
import ACO_Index.Objectives.MyAbstractObjective;

import java.util.Arrays;

/**
 * Created by Richard on 2018-01-04.
 */
public class WriteRatioObjective extends MyAbstractObjective {
    private double maxWriteRatio;
    private double sumWriteRatio;

    public WriteRatioObjective(double[] vArray, MyPheromone pheromone) {
        super(vArray, pheromone);

        double sumWriteRatio = Arrays.stream(valueArray).reduce(Double::sum).getAsDouble(); // ToDo: + 1 maybe to avoid division by zero
        double maxWriteRatio = Arrays.stream(valueArray).reduce(Double::max).getAsDouble();
    }

    @Override
    public void reset() {
        pheromone.reset(sumWriteRatio);
        bestQuality = Double.MAX_VALUE;
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
    public void updatePheromone(int[] solution)  {
        pheromone.update((1/(sumWriteRatio - bestQuality)), bestQuality, solution); // ToDo: Maybe 1/bestQuality is enough?
    }

    @Override
    public double calculateHeuristic(int index) {
        return (maxWriteRatio - valueArray[index]);
    }
}
