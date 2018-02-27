package ACO_Index.DataMining;

import java.util.LinkedList;
import java.util.List;

public class ItemSet{
    private int weight;
    private int supportCount;
    private int writeToRead;
    private String tableName;
    private List<String> attributes;


    public ItemSet(int weight, int supportCount, int writeToRead, String tableName, List<String> attributes){
        this.weight = weight;
        this.supportCount = supportCount;
        this.writeToRead = writeToRead;
        this.tableName = tableName;
        this.attributes = attributes;
    }

    public String getTableName() {return tableName;}
    public int getKey(){
        return supportCount;
    }
    public int getValue(){
        return weight;
    }
    public int getWriteToRead(){
        return writeToRead;
    }
    public void add(String a){
        this.attributes.add(a);
    }

}
