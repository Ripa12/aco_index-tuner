package ACO_Index.Knapsack;

import ACO_Index.Objectives.MyAbstractObjective;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    //private static MyAbstractObjective[] objectives = null;
    private double[] qualities;
    private List<Integer> solution;

//    KnapsackSolution(MyAbstractObjective[] objectives, int nrOfObj){
//        this.solution = new ArrayList<>();
//        this.qualities = new double[nrOfObj];
//
//        Knapsack.objectives = objectives;
//    }

    KnapsackSolution(int nrOfObj){
        this.solution = new ArrayList<>();
        this.qualities = new double[nrOfObj];
    }

    private KnapsackSolution(KnapsackSolution other){
        this.qualities = other.qualities.clone();
        this.solution = new ArrayList<>(other.solution);
    }

    public static void updateBestQuality(KnapsackSolution other){

        for(int obj = 0; obj < Knapsack.objectives.size(); obj++){
            if(Knapsack.objectives.get(obj).isBetter(other.qualities[obj])){
                Knapsack.objectives.get(obj).setBestQuality(other.qualities[obj]);
                Knapsack.objectives.get(obj).setBestSolution(ImmutableList.copyOf(other.solution)); // ToDo: Or simply a reference and not copy
            }
        }
    }

    public void add(Integer item){
        solution.add(item);
    }


    public void incrementQuality(int position){
        for (int i = 0; i < Knapsack.objectives.size(); i++){
            this.qualities[i] += Knapsack.objectives.get(i).getValue(position);
        }
    }

    public void clear(){
        solution.clear();
        Arrays.fill(qualities, 0);
    }

    public List<Integer> getSolution(){
        return solution;
    }

    public DominationStatus dominates(KnapsackSolution other){
        DominationStatus status = DominationStatus.inferior;

        short domination = 0;

        for(int i = 0; i < Knapsack.objectives.size(); i++){
            if(Knapsack.objectives.get(i).isBetter(qualities[i], other.qualities[i])){
                domination++;
            }
        }
        if(domination == Knapsack.objectives.size()){
            status = DominationStatus.superior;
        }
        else if(domination > 0){
            status = DominationStatus.equal;
        }

        return status;
    }

    public KnapsackSolution clone(){
        return new KnapsackSolution(this);
    }

    public void print(){
        //System.out.println(solution);
        System.out.println("WriteRatio: " + qualities[0]);
        System.out.println("SupportCount: " + qualities[1]);
    }
}
