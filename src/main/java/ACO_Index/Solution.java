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


    MyAbstractObjective[] objectives;
    List<Integer> solution;

    public Solution(MyAbstractObjective[] objectives, Integer[] solution){
        this.solution = Arrays.asList(solution);

        this.objectives = new MyAbstractObjective[objectives.length];
        for (int o = 0; o < objectives.length; o++){
            this.objectives[o] = (objectives[o].clone());
        }
    }

    public void add(Integer item){
        solution.add(item);
    }

    public void clear(){
        solution.clear();
        for(MyAbstractObjective obj : objectives){
            obj.reset();
        }
    }

    public Integer[] getSolution(){
        return (Integer[]) solution.toArray();
    }

    public DominationStatus dominates(Solution other){
        DominationStatus status = DominationStatus.inferior;

        boolean stop = false;
        for(int i = 0; i < objectives.length && !stop; i++){
            if(objectives[i].equals(other.objectives[i])){
                // Do nothing
            }
            else if(objectives[i].isBetter(other.objectives[i])){
                status = DominationStatus.superior;
            }
            else{
                status = DominationStatus.inferior;
                stop = true;
            }
        }
        if(!stop && status == DominationStatus.inferior){
            status = DominationStatus.equal;
        }

        return status;
    }
}
