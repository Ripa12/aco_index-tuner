package ACO_Index;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by Richard on 2017-12-18.
 */
public class Node {
    private ArrayList<Node> neighbours;
    private BitSet transactions;
    private String attribute;
    private int supportCount;

    public Node(BitSet transactions, String attribute){
        this.transactions = transactions;
        this.attribute = attribute;

        supportCount = transactions.cardinality();

        neighbours = new ArrayList<>(); // ToDo: Possible to Reserve Memory?
    }

    public void connect(Node to){
        BitSet difference = ((BitSet) this.transactions.clone());
        difference.and(to.transactions);
        if(difference.cardinality() > 0)
        {
            if(this.supportCount >= to.supportCount) // ToDo: Check for minimum support between items, maybe?
            {
                neighbours.add(to);
            }
            else
            {
                to.neighbours.add(this);
            }


        }
    }

    public void printDebugString(){
        String str = attribute + " " + supportCount + "{ ";
        for (int i = 0; i < neighbours.size(); i++){
            str += neighbours.get(i).attribute + " ";
        }
        str += " }";
        System.out.println(str);
    }
}
