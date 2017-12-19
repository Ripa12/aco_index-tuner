package ACO_Index;

import java.util.ArrayList;
import java.util.BitSet;

/**
 * Created by Richard on 2017-12-18.
 */
public class Node {
    public ArrayList<Node> neighbours;
    public BitSet transactions;
    public String attribute;
    public int supportCount;

    public Node(){
        transactions = new BitSet();
    }
}
