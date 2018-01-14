package ACO_Index;

import java.util.Arrays;

/**
 * Created by Richard on 2018-01-04.
 */
public class SupportCountObjective extends MyAbstractObjective {

    private double sumSupportCount;

    public SupportCountObjective(double[] array) {
        super(array);

        sumSupportCount = Arrays.stream(valueArray).reduce(Double::sum).getAsDouble();
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
        return valueArray[index];
    }


}
