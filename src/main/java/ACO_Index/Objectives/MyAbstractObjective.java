package ACO_Index.Objectives;

import ACO_Index.MyPheromone;

import java.util.List;

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
        this.pheromone = pheromone;

        this.bestQuality = Double.MIN_VALUE;
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
    public void setBestQuality(double quality){
        bestQuality = quality;
    }

    public double getPheromone(int from, int to){
        return pheromone.getPheromone(from, to);
    }

    public void evaporate(){
        pheromone.evaporate(this);
    }

    public abstract void reset();

    public abstract double getInitialValue();

    // ToDo: maybe make classes below Static!
    public abstract boolean isEqual(double other);
    public abstract boolean isBetter(double other);

    public abstract void updatePheromone(List<Integer> solution);

    public abstract double calculateHeuristic(int index);

    public abstract MyAbstractObjective clone();

}
