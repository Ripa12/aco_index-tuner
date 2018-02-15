package ACO_Index.Colony;

import ACO_Index.Knapsack.Knapsack;
import ACO_Index.MyPheromone;
import ACO_Index.Objectives.MyAbstractObjective;
import ACO_Index.Solution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyAntColony {

    private Solution bestSolution;

    private int nrOfAnts;

    MyAnt[] ants; // ToDo: Simple array instead
    private Knapsack knapsack;

    public MyAntColony(Knapsack knapsack){

        this.knapsack = knapsack;
        this.nrOfAnts = 0;

        ants = null;
    }

    public MyAntColony setNrOfAnts(int nr){
        this.nrOfAnts = nr;
        ants = new MyAnt[nrOfAnts];
        for(int i = 0; i < nrOfAnts; i++){
            ants[i] = new MyAnt(i, this.knapsack);
        }
        return this;
    }


    public void start(){

        knapsack.reset();

        bestSolution.clear();
        for(MyAnt ant : ants){
            ant.findSolution();

            // ToDo: this is where pareto front should be
            if(bestSolution.getSupportCountQuality() < ant.getSolution().getSupportCountQuality()){
                bestSolution.setSupportCountQuality(ant.getSolution().getSupportCountQuality());
                //localBestSupportCountSolution = ant.getSolution();
            }
            if(bestSolution.getWriteQuality() > ant.getSolution().getWriteQuality()){
                bestSolution.setWriteQuality(ant.getSolution().getWriteQuality());
                //localBestWritesSolution = ant.getSolution();
            }
        }

        knapsack.evaporate();
    }
}
