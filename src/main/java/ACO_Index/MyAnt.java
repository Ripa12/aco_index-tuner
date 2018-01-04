package ACO_Index;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyAnt {
    private int index;
    private double supportCountQuality;
    private double writesQuality;
    private MyGraph graph;
    private List<Integer> solution;

    private int currentPosition;

    public MyAnt(int index, MyGraph graph){
        this.graph = graph;
        this.index = index;
        this.solution = new LinkedList<>(); // ArrayList

    }

    public List<Integer> getSolution(){
        return solution;
    }

    public double getSolutionSupportCountQuality(){
        return supportCountQuality;
    }

    public double getSolutionWritesQuality(){
        return writesQuality;
    }

    public int getIndex(){
        return index;
    }

//    public BitSet getSupportCount(){
//        return supportCount;
//    }

    public void findSolution(int capacity, double alpha, double beta){

        solution.clear();

        currentPosition = graph.getRandomPosition();
        solution.add(currentPosition);
        writesQuality = graph.getWrites(currentPosition);
        supportCountQuality = graph.getProfit(currentPosition);
        double currentWeight = graph.getWeight(currentPosition);

        List<Integer> neighbours = graph.getNeighbours(currentPosition, capacity, currentWeight);

        while (neighbours.size()>0){
//            int nextPosition = getNextItem(neighbours.size(), currentPosition, alpha, beta);
            int nextPosition = getNextItem(neighbours, Math.random() < 0.5 ? 0 : 1, alpha, beta);
            //int nextPosition = getNextItem(neighbours, 0, alpha, beta);

            neighbours.remove(nextPosition);

            graph.pruneNeighbours(neighbours, capacity, currentWeight);

            currentPosition = nextPosition;
            solution.add(nextPosition);
            supportCountQuality += graph.getProfit(nextPosition);
            writesQuality += graph.getWrites(nextPosition);
            currentWeight += graph.getWeight(nextPosition);
        }

    }

    private double calculateWriteHeuristic(int i){
//        double debTotalwrites = graph.getTotalWrites();
//        double debWrites = graph.getWrites(index);
//        double weight = graph.getWeight(i);
        return ((graph.getTotalWrites() - graph.getWrites(index))/graph.getWeight(i));
    }

    private double calculateSupportHeuristic(int i){
//        double debProfit = graph.getProfit(index);
//        double weight = graph.getWeight(i);
        return graph.getProfit(index)/graph.getWeight(i);
    }

    // ToDo: Move to MyAbstractObjective!
    private double calculateAggregateProbabilities(double[] probability, int objective, int nrOfNodes,
                                                  double alpha, double beta){
        double[] tij = new double[nrOfNodes];
        double[][] nij = new double[2][nrOfNodes]; // This is the reason why it took so long!
//        double[] nij = new double[nrOfNodes];

        double scTotal = 0;
        double wTotal = 0;

        // nodes to visit
        for (int i = 0; i < nrOfNodes; i++){
            tij[i] = Math.pow(graph.getPheromone(objective, index, i), alpha);
            nij[0][i] = Math.pow(calculateSupportHeuristic(i), beta);
            nij[1][i] = Math.pow(calculateWriteHeuristic(i), beta);

            scTotal += tij[i] * nij[0][i];
            wTotal += tij[i] * nij[1][i];
        }

        double sumProbability = 0.0;

        for (int i = 0; i < nrOfNodes; i++){
            probability[i] = ((tij[i] * nij[0][i]) / scTotal) + ((tij[i] * nij[1][i]) / wTotal);

            sumProbability += probability[i];
        }

        return sumProbability;
    }



    // ToDo: Pass MyAbstractObjective instance as parameter
    private int getNextItem(List<Integer> neighbours, int indexObj, double alpha, double beta){

        //int nrOfNodes = graph.getNumberOfNodes();
//        double[] tij = new double[nrOfNodes];
//        double[] nij = new double[nrOfNodes];
//
//        double total = 0;
        //total += calculateSupportHeuristics(tij, nij, nrOfNodes, alpha, beta);


//        // nodes to visit
//        for (int i = 0; i < nrOfNodes; i++){
//            tij[i] = Math.pow(graph.getPheromone(index, i), alpha);
//            //double heuristic = graph.getHeuristic(supportCount, i).cardinality()/graph.getWeight(i);
//            double heuristic = graph.getProfit(index)/graph.getWeight(i);
//            nij[i] = Math.pow(heuristic, beta);
//
//            total += tij[i] * nij[i];
//        }

        double[] probability = new double[neighbours.size()];
        double sumProbability = calculateAggregateProbabilities(probability, indexObj, neighbours.size(), alpha, beta);

//        double sumProbability = 0.0;
//
//        for (int i = 0; i < nrOfNodes; i++){
//            probability[i] = (tij[i] * nij[i]) / total;
//
//            sumProbability += probability[i];
//        }

        int j = 0;

        double p = probability[j];

        //checkState(sumProbability , "Error");

        double r = ThreadLocalRandom.current().nextDouble(sumProbability);

        while (p < r) {
            j = j + 1;
            p = p + probability[j];
        }

        return j;
    }

}
