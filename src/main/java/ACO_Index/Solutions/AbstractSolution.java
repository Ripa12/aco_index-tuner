package ACO_Index.Solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Richard on 2018-01-03.
 */
public class AbstractSolution {

    public enum DominationStatus{
        superior,
        equal,
        inferior
    }

    //MyAbstractObjective[] objectives;
    double[] qualities;
    //double quality;
    List<Integer> solution;

    public AbstractSolution(Integer[] solution, int objectives){
        this.solution = Arrays.asList(solution);
        this.qualities = new double[objectives];
        //this.quality = 0;
    }

    public AbstractSolution(int objectives){
        this.solution = new ArrayList<>();
        this.qualities = new double[objectives];
        //this.quality = 0;
    }

    public void add(Integer item){
        solution.add(item);
    }


    public void incrementQuality(int objective, double value){
        this.qualities[objective] += value;
    }

    public void clear(){
        solution.clear();
    }

    public Integer[] getSolution(){
        return (Integer[]) solution.toArray();
    }

    public double getQuality(int objective){
        return qualities[objective];
    }

    public void setQuality(int objective, double quality) {
        this.qualities[objective] = quality;
    }

    public DominationStatus dominates(AbstractSolution other){
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
}
