package ACO_Index;

/**
 * Created by Richard on 2018-01-03.
 */
public class Solution {

    public enum DominationStatus{
        superior,
        equal,
        inferior
    }

    MyAbstractObjective[] objectiveQualities;
    int[] solution;

    public Solution(){

    }

    public int[] getSolution(){
        return solution;
    }

    public DominationStatus dominates(Solution other){
        DominationStatus status = DominationStatus.inferior;

        boolean stop = false;
        for(int i = 0; i < objectiveQualities.length && !stop; i++){
            if(objectiveQualities[i].equals(other.objectiveQualities[i])){
                // Do nothing
            }
            else if(objectiveQualities[i].isBetter(other.objectiveQualities[i])){
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
