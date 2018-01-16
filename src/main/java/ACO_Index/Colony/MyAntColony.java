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

    private List<MyPheromone> pheromones;
    private List<MyAbstractObjective> objectives;

    private double localBestWritesQuality;
    private List<Integer> localBestWritesSolution;

    private double localBestSupportCountQuality;
    private List<Integer> localBestSupportCountSolution;

    private int nrOfAnts;

    MyAnt[] ants; // ToDo: Simple array instead
    private Knapsack knapsack;

    public MyAntColony(Knapsack knapsack){

        objectives = new ArrayList<>();
        pheromones = new ArrayList<>();

        this.knapsack = knapsack;
        this.nrOfAnts = 0;

        ants = null;
    }

    public MyAntColony setNrOfAnts(int nr){
        this.nrOfAnts = nr;
        ants = new MyAnt[nrOfAnts];
        for(int i = 0; i < nrOfAnts; i++){
            ants[i] = new MyAnt(i, graph);
        }
        return this;
    }

    public void addObjective(MyAbstractObjective obj, double percipience) {
        objectives.add(obj);
        pheromones.add(new MyPheromone(knapsack.getNumberOfItems(), percipience)); // ToDo: Maybe combine objectives and pheromones into same class
    }

    public List<Integer> getLocalBestSupportCountSolution(){
        return new ArrayList<>(localBestSupportCountSolution);
    }
    public List<Integer> getLocalBestWritesSolution(){
        return new ArrayList<>(localBestWritesSolution);
    }

    public double getLocalBestSupportCountQuality(){
        return localBestSupportCountQuality;
    }
    public double getLocalBestWritesQuality(){
        return localBestWritesQuality;
    }


    //ToDo: Rethink design here!
    private void updatePheromones(int objective, Solution globalBestSolution){
        objectives.get(objective).updatePheromone(globalBestSolution.getSolution());
    }

    public void start(){

        for(MyAbstractObjective obj : objectives){
            obj.reset();
        }

        localBestSupportCountSolution = null;
        localBestWritesSolution = null;
        for(MyAnt ant : ants){
            ant.findSolution(capacity, alpha, beta);

            if(localBestSupportCountQuality < ant.getSolutionSupportCountQuality()){
                localBestSupportCountQuality = ant.getSolutionSupportCountQuality();
                localBestSupportCountSolution = ant.getSolution();
            }
            if(localBestWritesQuality > ant.getSolutionWritesQuality()){
                localBestWritesQuality = ant.getSolutionWritesQuality();
                localBestWritesSolution = ant.getSolution();
            }
        }

        for(MyPheromone p : pheromones){
            p.evaporate(null);
        }
    }
}
