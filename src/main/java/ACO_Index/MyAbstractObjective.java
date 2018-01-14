package ACO_Index;

/**
 * Created by Richard on 2017-12-31.
 */
public abstract class MyAbstractObjective {

    // http://ieeexplore.ieee.org/document/4630948/
    // https://www.sciencedirect.com/science/article/pii/S0307904X13001157 Shows that PACO is a god approach to MOP
    protected double[] valueArray;

    //private double totalValue;

    public MyAbstractObjective(double[] vArray){
        this.valueArray = vArray;
    }
    public double getValue(int index){
        return valueArray[index];
    }

    public abstract boolean isEqual(MyAbstractObjective other);
    public abstract boolean isBetter(MyAbstractObjective other);

    public abstract double calculatePheromoneFactor();

    public abstract double calculateHeuristic(int index);

}
