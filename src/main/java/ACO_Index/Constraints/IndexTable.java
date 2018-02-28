package ACO_Index.Constraints;

import java.util.ArrayList;
import java.util.List;

public abstract class IndexTable {

    /* ToDo: Generalize this? MyConstraint class?
     * ToDo: Or should it contain indices instead? */
    private List<Integer> indices;
    private List<Integer> copyOfIndices;

    private static double ConstraintLimit = 0;
    public static void setConstraintLimit(double limit){
        ConstraintLimit = limit;
    }

    public IndexTable(List<Integer> indices){
        this.indices = indices;
        this.copyOfIndices = new ArrayList<>(indices.size());
    }

    protected List<Integer> getCopyOfIndices(){
        this.copyOfIndices.clear();
        this.copyOfIndices.addAll(indices);
        return copyOfIndices;
    }

    public abstract List<Integer> getIndexes();
    public abstract void update();
}
