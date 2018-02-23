package ACO_Index.Objectives;

import ACO_Index.MyPheromone;
import ACO_Index.Objectives.MyAbstractObjective;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Richard on 2018-01-04.
 */
public class WriteRatioObjective extends MyAbstractObjective {
    private static double maxWriteRatio;
    private static double sumWriteRatio;

    public WriteRatioObjective(double[] vArray, MyPheromone pheromone) {
        super(vArray, pheromone);

        sumWriteRatio = Arrays.stream(valueArray).reduce(Double::sum).getAsDouble(); // ToDo: + 1 maybe to avoid division by zero
        maxWriteRatio = Arrays.stream(valueArray).reduce(Double::max).getAsDouble();
        this.bestQuality = Double.MAX_VALUE;
    }

    private WriteRatioObjective(double[] vArray, MyPheromone pheromone, double sumWriteRatio, double maxWriteRatio) {
        super(vArray, pheromone);

        this.sumWriteRatio = sumWriteRatio; // ToDo: + 1 maybe to avoid division by zero
        this.maxWriteRatio = maxWriteRatio;
        this.bestQuality = Double.MAX_VALUE;
    }

    @Override
    public void reset() {
        pheromone.reset(sumWriteRatio);
        //bestQuality = Double.MAX_VALUE;
    }

    @Override
    public double getInitialValue() {
        return Double.MAX_VALUE;
    }


//    @Override
//    public boolean isEqual(double other) {
//        return other == bestQuality;
//    }

    @Override
    public boolean isBetter(double other) {
        return other < bestQuality;
    }

    @Override
    public boolean isBetter(double first, double second) {
        return second < first;
    }

    @Override
    public void updatePheromone()  {
        pheromone.update((1/(sumWriteRatio - bestQuality)), bestQuality, bestSolution); // ToDo: Maybe 1/bestQuality is enough?
    }

    @Override
    public double calculateHeuristic(int index) {
        return (maxWriteRatio - valueArray[index]);
    }

    @Override
    public MyAbstractObjective clone() {
        return new WriteRatioObjective(this.valueArray, this.pheromone, sumWriteRatio, maxWriteRatio);
    }
}
