package ACO_Index.Constraints;

import java.util.List;

public class NestedKnapsack {

    /* ToDo: Generalize this? MyConstraint class?
     * ToDo: Or should it contain indices instead? */
    private List<Integer> indices;

    private static double localConstraintLimit = 0;

    public NestedKnapsack(List<Integer> indices){
        this.indices = indices;
    }

    public static void setLocalConstraintLimit(double limit){
        localConstraintLimit = limit;
    }
}
