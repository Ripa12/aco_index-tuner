package ACO_Index;

import java.util.Arrays;

/**
 * Created by Richard on 2018-01-12.
 */
public class MyConstraint {
    private double[] values;
    private double totalValue;

    public MyConstraint(double[] values){
        this.values = values;
        this.totalValue = Arrays.stream(values).reduce(Double::sum).getAsDouble();
    }

    public double getValue(int index){
        return values[index];
    }
}
