package ACO_Index.Colony;

import ACO_Index.Knapsack.Knapsack;
import ACO_Index.Knapsack.KnapsackSolution;
import ACO_Index.Solutions.ParetoFront;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyAntColony {

    private ParetoFront paretoFront;

    private int nrOfAnts;
    private int nrOfIterations;

    private MyAnt[] ants; // ToDo: Simple array instead
    private Knapsack knapsack;

    public MyAntColony(Knapsack knapsack){

        this.knapsack = knapsack;
        this.nrOfAnts = 0;
        this.nrOfIterations = 0;

        this.paretoFront = new ParetoFront();
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

    // ToDo: Reset pheromone matrix after stagnation (check stagnation stopping criterion for mop)
    // ToDo: Or just regular stagnation for each individual pheromone matrix
    public void start(){

        //bestSolution.clear();


        while(nrOfIterations > 0) {
            nrOfIterations--;

            knapsack.reset();
            for (MyAnt ant : ants) {
                ant.findSolution(knapsack);

                // ToDo: this is where pareto front should be
                //bestSolution.isGreater(ant.getSolution());
                this.paretoFront.insert(ant.getSolution());
            }

            knapsack.evaporate();
            knapsack.updatePheromones();
        }


        this.paretoFront.printAll();
        //bestSolution.print();
        //bestSolution.getQuality(0);
    }
}
