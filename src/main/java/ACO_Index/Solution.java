package ACO_Index;

import ACO_Index.Objectives.MyAbstractObjective;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Richard on 2018-01-03.
 */
public class Solution {

    public enum DominationStatus{
        superior,
        equal,
        inferior
    }

    //MyAbstractObjective[] objectives;
    double[] qualities;
    List<Integer> solution;

    public Solution(int objectives, Integer[] solution){
        this.solution = Arrays.asList(solution);
        this.qualities = new double[objectives];
    }

    public Solution(int objectives){
        this.solution = new ArrayList<>();
        this.qualities = new double[objectives];
    }

    public void add(Integer item){
        solution.add(item);
    }

    public void incrementQuality(int objective, double value){
        qualities[objective] += value;
    }

    public void clear(){
        solution.clear();
        Arrays.fill(qualities, 0.0);
    }

    public Integer[] getSolution(){
        return (Integer[]) solution.toArray();
    }


    // ToDo: Temporary solution
    public double getWriteQuality(){
        return qualities[1];
    }

    public double getSupportCountQuality(){
        return qualities[0];
    }

    public void setSupportCountQuality(double quality){
        qualities[0] = quality;
    }

    public void setWriteQuality(double quality){
        qualities[1] = quality;
    }

    public DominationStatus dominates(Solution other){
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
