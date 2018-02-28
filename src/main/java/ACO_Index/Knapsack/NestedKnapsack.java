package ACO_Index.Knapsack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NestedKnapsack{

    public class IndexPair{
        public int tableIndex;
        public int index;
        IndexPair(int tableIndex, int index){
            this.index = index;
            this.tableIndex = tableIndex;
        }
    }

    ArrayList<Integer> indexes;
//    double[] weights;
    private int currentCost;

    public NestedKnapsack(ArrayList<Integer> indexes){
        clean();
        this.indexes = indexes;
    }

    ArrayList<Integer> getNeighbours(){
        return indexes;
    }

    private void clean(){
        this.currentCost = 0;
    }

//    IndexPair[] getNeighbours() {
//        clean();
//        return (IndexPair[]) stream().map(i -> new IndexPair(i, this)).toArray();
//    }
}
