package ACO_Index;

/**
 * Created by Richard on 2017-12-31.
 */
public abstract class MyAbstractObjective {

    // http://ieeexplore.ieee.org/document/4630948/
    // https://www.sciencedirect.com/science/article/pii/S0307904X13001157 Shows that PACO is a god approach to MOP
    private int value;

    private double totalValue;

    public MyAbstractObjective(double total){
        totalValue = total;
    }
    public int getValue(){
        return value;
    }

    public abstract boolean isEqual(MyAbstractObjective other);
    public abstract boolean isBetter(MyAbstractObjective other);

    public abstract double calculatePheromoneFactor();

}
