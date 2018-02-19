package ACO_Index.Solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard on 2018-01-03.
 */
public class Pareto {

//    private List<Solution> pareto;
//
//    public Pareto(){
//        pareto = new ArrayList<>();
//    }
//
//    public void update(Solution solution){
//        boolean stop = false;
//        for(int i = 0; i < pareto.size() && !stop; i++){
//            Solution.DominationStatus status = solution.dominates(pareto.get(i));
//            if(status == Solution.DominationStatus.superior){
//                pareto.remove(i);
//            }
//            else if(status == Solution.DominationStatus.inferior){
//                stop = true;
//            }
//        }
//
//        if(!stop){
//            pareto.add(solution);
//        }
//    }
//
//    public void updateBestSoFar(){
//        // Is this function really necessary?
//    }
}
