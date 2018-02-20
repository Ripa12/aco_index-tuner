package ACO_Index.Colony;

import ACO_Index.Knapsack.Knapsack;
import ACO_Index.Knapsack.KnapsackSolution;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyAntColony {

    private KnapsackSolution bestSolution;

    private int nrOfAnts;
    private int nrOfIterations;

    private MyAnt[] ants; // ToDo: Simple array instead
    private Knapsack knapsack;

    public MyAntColony(Knapsack knapsack){

        this.knapsack = knapsack;
        this.nrOfAnts = 0;
        this.nrOfIterations = 0;

        this.bestSolution = knapsack.newSolution();//new KnapsackSolution(knapsack.getNumberOfObjectives());
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

        bestSolution.clear();


        while(nrOfIterations > 0) {
            nrOfIterations--;

            knapsack.reset();
            for (MyAnt ant : ants) {
                ant.findSolution(knapsack);


                // ToDo: this is where pareto front should be
                bestSolution.isGreater(ant.getSolution());
//                if (bestSolution.getQuality(0) < ant.getSolution().getQuality(0)) {
//                    bestSolution.setQuality(0, ant.getSolution().getQuality(0));
//                    ant.getSolution().copy(bestSolution);
//                    //localBestSupportCountSolution = ant.getSolution();
//                }
//                if (bestSolution.getQuality(1) > ant.getSolution().getQuality(1)) {
//                    bestSolution.setQuality(1, ant.getSolution().getQuality(1));
//                    //localBestSupportCountSolution = ant.getSolution();
//                }
            }

            knapsack.evaporate();
            knapsack.updatePheromones(bestSolution);
        }


        bestSolution.print();
        //bestSolution.getQuality(0);
    }
}
