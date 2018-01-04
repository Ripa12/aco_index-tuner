package ACO_Index;

/**
 * Created by Richard on 2018-01-03.
 */
public class MySupportObjective extends MyAbstractObjective {


    public MySupportObjective(double total) {
        super(total);
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
}
