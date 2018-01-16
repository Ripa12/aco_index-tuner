package ACO_Index;

import ACO_Index.Objectives.MyAbstractObjective;

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
    int[] solution;

    public Solution(MyAbstractObjective[] objectives, int[] solution){
        this.objectives = objectives;
        this.solution = solution;
    }

    public int[] getSolution(){
        return solution;
    }

    public MyAbstractObjective getObjective(int index) {
        return objectives[index];
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
