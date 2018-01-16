package ACO_Index.Objectives;

import ACO_Index.MyPheromone;

/**
 * Created by Richard on 2017-12-31.
 */
public abstract class MyAbstractObjective {

    // http://ieeexplore.ieee.org/document/4630948/
    // https://www.sciencedirect.com/science/article/pii/S0307904X13001157 Shows that PACO is a good approach to MOP

    protected MyPheromone pheromone;

    protected double[] valueArray;

    protected double bestQuality;

    //private double totalValue;

    public MyAbstractObjective(double[] vArray, MyPheromone pheromone){
        this.valueArray = vArray;
        this.bestQuality = Double.MIN_VALUE;

        this.pheromone = pheromone;
    }

    public boolean updateBestQuality(double other){
        boolean isGreater = false;

        if(bestQuality < other){
            bestQuality = other;
            isGreater = true;
        }

        return isGreater;
    }


    public double getValue(int index){
        return valueArray[index];
    }

    public double getBestQuality(){
        return bestQuality;
    }

    public abstract void reset();

    // ToDo: maybe make classes below Static!
    public abstract boolean isEqual(MyAbstractObjective other);
    public abstract boolean isBetter(MyAbstractObjective other);

    public abstract void updatePheromone(int[] solution);

    public abstract double calculateHeuristic(int index);

}
