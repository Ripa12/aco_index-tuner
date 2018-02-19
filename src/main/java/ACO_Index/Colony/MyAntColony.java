package ACO_Index.Colony;

import ACO_Index.Knapsack.Knapsack;
import ACO_Index.Solutions.AbstractSolution;
import ACO_Index.Solutions.Solution;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyAntColony {

    private AbstractSolution bestSolution;

    private int nrOfAnts;
    private int nrOfIterations;

    private MyAnt[] ants; // ToDo: Simple array instead
    private Knapsack knapsack;

    public MyAntColony(Knapsack knapsack){

        this.knapsack = knapsack;
        this.nrOfAnts = 0;
        this.nrOfIterations = 0;

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

    public MyAntColony setNrOfIterations(int nr){
        this.nrOfIterations = nr;
        return this;
    }

    public void start(){

        knapsack.reset();

        bestSolution.clear();
        for(MyAnt ant : ants){
            ant.findSolution(knapsack);

            // ToDo: this is where pareto front should be
            if(bestSolution.getQuality(0) < ant.getSolution().getQuality(0)){
                bestSolution.setQuality(0, ant.getSolution().getQuality(0));
                //localBestSupportCountSolution = ant.getSolution();
            }
            if(bestSolution.getQuality(1) > ant.getSolution().getQuality(0)){
                bestSolution.setQuality(1, ant.getSolution().getQuality(0));
                //localBestSupportCountSolution = ant.getSolution();
            }
        }

        knapsack.evaporate();
    }
}
