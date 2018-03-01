package ACO_Index.Knapsack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NestedKnapsack{

    public class TableIndex{
        private NestedKnapsack nestedKnapsack;
        int index;
        private TableIndex(NestedKnapsack nestedKnapsack, int index){
            this.index = index;
            this.nestedKnapsack = nestedKnapsack;
        }

        public int getIndex(){
            return nestedKnapsack.updateMaintenanceCost(index);
        }
    }

    private double[] weights;
    private ArrayList<Integer> indexes;
    private int currentCost;

    static double ConstraintLimit = 0;

    NestedKnapsack(ArrayList<Integer> indexes, double[] weights){
        restart();
        this.indexes = indexes;
        this.weights = weights;
        // ToDo: Temporarily set to null
        //this.writeOperations = null;
    }

    List<TableIndex> getNeighbours(int currentPosition, double currentWeight){
        restart();

        List<TableIndex> neighbours = new ArrayList<>();

        for (int i = 0; i < indexes.size(); i++) {
            if (indexes.get(i) != currentPosition && currentWeight + weights[i] < Knapsack.capacity) {
                neighbours.add(new TableIndex(this, indexes.get(i)));
            }
        }
        return neighbours;

        //return indexes.stream().map(i -> new TableIndex(this, i)).collect(Collectors.toList());
    }

    private void restart(){
        this.currentCost = 0;
    }

    private int updateMaintenanceCost(int index){
        // ToDo: Only readToWriteRatio Objective!
        currentCost += Knapsack.objectives.get(1).getValue(index);
        return index;
    }

    void prune(List<TableIndex> neighbour, double currentWeight){
        if(currentCost > ConstraintLimit){
            neighbour.removeIf(n -> n.nestedKnapsack == this);
        }
        else{
            neighbour.removeIf(n -> weights[n.index] + currentWeight > Knapsack.capacity);
        }
    }
}
