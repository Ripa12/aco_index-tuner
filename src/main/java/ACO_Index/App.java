package ACO_Index;

/**
 * Hello world!
 *
 */
public class App 
{
    final static int NR_OF_ATTRIBUTES = 50;
    final static int NR_OF_TRANSACTIONS = 30000;

    public static void main( String[] args )
    {
        DataSampleGenerator.generateDataSample(NR_OF_ATTRIBUTES, NR_OF_TRANSACTIONS, "TestData.txt");
        Graph graph = Graph.buildGraph("TestData.txt", NR_OF_ATTRIBUTES, NR_OF_TRANSACTIONS);
        //graph.debugFrequentItemSets();
        graph.debugFrequentItemSets(3000); // ToDo: All individual attributes are output regardless of their support count, need fix!
        System.out.println( "Hello World!" );
    }
}
