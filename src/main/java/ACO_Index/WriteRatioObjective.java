package ACO_Index;

import java.util.Arrays;

/**
 * Created by Richard on 2018-01-04.
 */
public class WriteRatioObjective extends MyAbstractObjective {
    private double maxWriteRatio;
    private double sumWriteRatio;

    public WriteRatioObjective(double[] vArray) {
        super(vArray);

        double sumWriteRatio = Arrays.stream(valueArray).reduce(Double::sum).getAsDouble();
        double maxWriteRatio = Arrays.stream(valueArray).reduce(Double::max).getAsDouble();
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
    public double calculatePheromoneFactor() {
        return 0;
    }

    @Override
    public double calculateHeuristic(int index) {
        return (maxWriteRatio - valueArray[index]);
    }
}
