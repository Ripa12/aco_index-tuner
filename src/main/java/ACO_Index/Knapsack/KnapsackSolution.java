package ACO_Index.Knapsack;

import ACO_Index.Objectives.MyAbstractObjective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Richard on 2018-01-03.
 */
public class KnapsackSolution {

    public enum DominationStatus{
        superior,
        equal,
        inferior
    }

    MyAbstractObjective[] objectives;
    double[] qualities;
    List<Integer> solution;
    int nrOfObjectives;

    KnapsackSolution(MyAbstractObjective[] objectives, int nrOfObj){
        this.solution = new ArrayList<>();
        this.qualities = new double[nrOfObj];
        this.objectives = objectives;
        this.nrOfObjectives = nrOfObj;
    }

    public void add(Integer item){
        solution.add(item);
    }


    public void incrementQuality(int position){
        for (int i = 0; i < objectives.length; i++){
            this.qualities[i] += objectives[i].getValue(position);
        }
    }

    public void clear(){
        solution.clear();
        Arrays.fill(qualities, 0);

//        for(int obj = 0; obj < objectives.length; obj++){
//            this.qualities[obj] = objectives[obj].getInitialValue();
//        }
    }

    public List<Integer> getSolution(){
        return solution;
    }

    public double getQuality(int objective){
        return qualities[objective];
    }

    public void setQuality(int objective, double quality) {
        this.qualities[objective] = quality;
        objectives[objective].setBestQuality(quality);
    }

    public boolean isGreater(KnapsackSolution other){

        boolean isGreater = false;

        for(int obj = 0; obj < objectives.length; obj++){
            if(this.objectives[obj].isBetter(other.getQuality(obj))){
                this.objectives[obj].setBestQuality(other.getQuality(obj));
                other.copy(this);

                isGreater = true;
            }
        }


//        if (bestSolution.getQuality(0) < ant.getSolution().getQuality(0)) {
//            bestSolution.setQuality(0, ant.getSolution().getQuality(0));
//            ant.getSolution().copy(bestSolution);
//            //localBestSupportCountSolution = ant.getSolution();
//        }
//        if (bestSolution.getQuality(1) > ant.getSolution().getQuality(1)) {
//            bestSolution.setQuality(1, ant.getSolution().getQuality(1));
//            //localBestSupportCountSolution = ant.getSolution();
//        }

        return isGreater;
    }

    public DominationStatus dominates(KnapsackSolution other){
        DominationStatus status = DominationStatus.inferior;

//        boolean stop = false;
//        for(int i = 0; i < objectives.length && !stop; i++){
//            if(objectives[i].equals(other.objectives[i])){
//                // Do nothing
//            }
//            else if(objectives[i].isBetter(other.objectives[i])){
//                status = DominationStatus.superior;
//            }
//            else{
//                status = DominationStatus.inferior;
//                stop = true;
//            }
//        }
//        if(!stop && status == DominationStatus.inferior){
//            status = DominationStatus.equal;
//        }

        return status;
    }

    public void copy(KnapsackSolution other){
        other.solution = new ArrayList<>(this.solution);
        other.qualities = this.qualities.clone();
//        for (int i = 0; i < this.qualities.length; i++){
//            other.qualities[i] = this.qualities[i];
//        }
    }

    public void print(){
        System.out.println(solution);
        System.out.println(qualities[0]);
    }
}
