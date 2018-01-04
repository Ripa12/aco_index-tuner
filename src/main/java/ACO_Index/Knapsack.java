package ACO_Index;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard on 2018-01-04.
 */
public class Knapsack {
    private static final int NUMBER_OF_OBJECTIVES = 2;

    List<MyAbstractObjective> objectives;
    List<MyPheromone> pheromones;

    private int nrOfNodes;

//    private double profitR;
//    private double writesR;
    private double weightR; // Total constraint value

    private int capacity;

    public Knapsack(List<Graph.ItemSet> data, int cap){
        nrOfNodes = data.size();

        pheromones = new ArrayList<>();
        objectives = new ArrayList<>();
//        pheromones[0].reset(0);
//        pheromones[1].reset(0);

        this.capacity = cap;

    }

    public void addObjective(MyAbstractObjective obj, double percipience){
        objectives.add(obj);
        pheromones.add(new MyPheromone(nrOfNodes, percipience));
    }


}
