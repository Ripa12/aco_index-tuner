package ACO_Index.Solutions;

import ACO_Index.Knapsack.Knapsack;
import ACO_Index.Knapsack.KnapsackSolution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Richard on 2018-01-03.
 */
public class ParetoFront extends LinkedList<KnapsackSolution>{

    public void insert(KnapsackSolution solution){
        boolean stop = false;
        for(int i = size()-1; i >= 0 && !stop; i--){
            KnapsackSolution.DominationStatus status = solution.dominates(get(i));
            if(status == KnapsackSolution.DominationStatus.inferior){
                stop = true;
            }
            else if(status == KnapsackSolution.DominationStatus.superior){
                remove(i);
            }
        }

        if(!stop){
            add(solution.clone());
            KnapsackSolution.updateBestQuality(solution);
        }
    }

    public void printAll(){
        forEach(KnapsackSolution::print);
    }
}
