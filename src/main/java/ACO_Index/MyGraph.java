package ACO_Index;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyGraph {
    private static final int NUMBER_OF_OBJECTIVES = 2;

    private int capacity;
    private int nrOfNodes;
    private double[] weights;
    private double[] profits;
    private double[] writes;
    //private boolean[][] edges;
    //private double[][] pheromoneMatrix; // ToDo: Maybe put in Node class instead!
    private double[][][] pheromoneMatrices; // ToDo: Maybe put in Node class instead!
    private BitSet[] transactionMatrix;

    private double profitR;
    private double maxWritesR;
    private double sumWritesR;
    private double weightR;
    public MyGraph(List<Graph.ItemSet> data, int cap){
        nrOfNodes = data.size();

        this.pheromoneMatrices = new double[NUMBER_OF_OBJECTIVES][nrOfNodes][nrOfNodes];
        resetPheromoneMatrix(0, 0.0);
        resetPheromoneMatrix(1, 0.0);

        this.writes=new double[nrOfNodes];
        this.profits=new double[nrOfNodes];
        this.weights=new double[nrOfNodes];
        for (int i = 0; i < nrOfNodes; i++){
            this.profits[i]=(data.get(i).getKey());
            this.weights[i]=(data.get(i).getValue());
            this.writes[i]=(data.get(i).getWriteToRead());
        }
        this.capacity = cap;

        this.sumWritesR = Arrays.stream(writes).reduce(Double::sum).getAsDouble();
        this.maxWritesR = Arrays.stream(writes).reduce(Double::max).getAsDouble();
        this.profitR = Arrays.stream(profits).reduce(Double::sum).getAsDouble();
        this.weightR = Arrays.stream(weights).reduce(Double::sum).getAsDouble();
    }

    public double getTotalProfit(){
        return profitR;
    }

    public double getTotalWrites(){
        return sumWritesR;
    }

    public double getMaxWrites(){
        return maxWritesR;
    }

    public int getRandomPosition(){
        return ThreadLocalRandom.current().nextInt(0, nrOfNodes);
    }

    public int getNumberOfNodes(){
        return nrOfNodes;
    }

    public double getPheromone(int index, int from, int to){
        return pheromoneMatrices[index][from][to];
    }

    public BitSet getHeuristic(BitSet bitSet, int index){
        BitSet result = transactionMatrix[index].get(0, bitSet.size());
        result.and(bitSet);
        return result;
    }

    public double getProfit(int index){
        return profits[index];
    }

    public double getWeight(int index){
        return weights[index];
    }

    public double getWrites(int index) {
        return writes[index];
    }

    public void resetPheromoneMatrix(int objIndex, double value){
        //for(int i = 0; i < this.pheromoneMatrices.length; i++)
            for (double[] row: this.pheromoneMatrices[objIndex])
                Arrays.fill(row, value);
    }

    public void pruneNeighbours(List<Integer> neighbour, int capacity, double currentWeight){
        neighbour.removeIf(n->weights[n]+currentWeight > capacity);
    }

    public List<Integer> getNeighbours(int currentPosition, int capacity, double currentWeight){
        List<Integer> neighbours = new ArrayList<>();
        for(int i = 0; i < nrOfNodes; i++){
            if(i != currentPosition && currentWeight + weights[i] < capacity){//edges[currentPosition][i]){//currentWeight + weight[i] < capacity){
                neighbours.add(i);
            }
        }
        return neighbours;
    }

    public void evaporatePheromones(int objIndex, double pheromonePersistence, double max){

            for (int i = 0; i < nrOfNodes; i++) {
                for (int j = i; j < nrOfNodes; j++) {
                    pheromoneMatrices[objIndex][i][j] = (1.0 - pheromonePersistence) * pheromoneMatrices[objIndex][i][j];
                    pheromoneMatrices[objIndex][i][j] = Math.min(pheromoneMatrices[objIndex][i][j], max);
                    pheromoneMatrices[objIndex][j][i] = pheromoneMatrices[objIndex][i][j];
                }
            }

    }

    public void updatePheromoneMatrix(int objective, List<Integer> solution, double solutionQuality, double min){

        double factor = 0;

        // toDo: Temporary solution, should be moved somewhere else!
        if(objective == 0){
//            factor = (solutionQuality / profitR);
            factor = (1/solutionQuality); /// profitR);
        }
        else{
//            factor = ((sumWritesR - solutionQuality) / sumWritesR);
            factor = (1/(sumWritesR - solutionQuality));// / sumWritesR);
        }

        for (int i : solution) {
            for (int j = 0; j < nrOfNodes; j++) {
                    pheromoneMatrices[objective][i][j] = pheromoneMatrices[objective][i][j] + 1 * factor;
                    pheromoneMatrices[objective][i][j] = Math.max(pheromoneMatrices[objective][i][j], min);
                    pheromoneMatrices[objective][j][i] = pheromoneMatrices[objective][i][j];
            }
        }
    }
}
