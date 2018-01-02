package ACO_Index;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Richard on 2017-12-30.
 */
public class MyGraph {
    private int capacity;
    private int nrOfNodes;
    private double[] weights;
    private double[] profits;
    //private boolean[][] edges;
    private double[][] pheromoneMatrix; // ToDo: Maybe put in Node class instead!
    private BitSet[] transactionMatrix;

    private double profitR;
    private double weightR;
    public MyGraph(List<Map.Entry<Integer, Integer>> data, int cap){
        nrOfNodes = data.size();

        this.pheromoneMatrix = new double[nrOfNodes][nrOfNodes];
        resetPheromoneMatrix(0.0);

        this.profits=new double[nrOfNodes];
        this.weights=new double[nrOfNodes];
        for (int i = 0; i < nrOfNodes; i++){
            this.profits[i]=(data.get(i).getKey());
            this.weights[i]=(data.get(i).getValue());

        }
        this.capacity = cap;

        this.profitR = Arrays.stream(profits).reduce(Double::sum).getAsDouble();
        this.weightR = Arrays.stream(weights).reduce(Double::sum).getAsDouble();
    }

    public double getTotalProfit(){
        return profitR;
    }

    public int getRandomPosition(){
        return ThreadLocalRandom.current().nextInt(0, nrOfNodes);
    }

    public int getNumberOfNodes(){
        return nrOfNodes;
    }

    public double getPheromone(int from, int to){
        return pheromoneMatrix[from][to];
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

    public void resetPheromoneMatrix(double value){
        for (double[] row: this.pheromoneMatrix)
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

    public void evaporatePheromones(double pheromonePersistence, double max){
        for (int i = 0; i < nrOfNodes; i++) {
            for (int j = i; j < nrOfNodes; j++) {
                pheromoneMatrix[i][j] = (1.0 - pheromonePersistence) * pheromoneMatrix[i][j];
                pheromoneMatrix[i][j] = Math.min(pheromoneMatrix[i][j], max);
                pheromoneMatrix[j][i] = pheromoneMatrix[i][j];
            }
        }
    }

    public void updatePheromoneMatrix(List<Integer> solution, double solutionQuality, double min){
        for (int i : solution) {
            for (int j = 0; j < nrOfNodes; j++) {
//                if (i != j) {
                    // Do Evaporation
//                    pheromoneMatrix[i][j]=(1.0 - pheromonePersistence) * pheromoneMatrix[i][j];
//                    pheromoneMatrix[i][j] = Math.min(pheromoneMatrix[i][j], max);
//                    pheromoneMatrix[j][i]=pheromoneMatrix[i][j];
                    // Do Deposit


                    pheromoneMatrix[i][j] = pheromoneMatrix[i][j] + 1 * (solutionQuality / profitR);
                    pheromoneMatrix[i][j] = Math.max(pheromoneMatrix[i][j], min);
                    pheromoneMatrix[j][i] = pheromoneMatrix[i][j];
                    //graph.setTau(i, j, deposit.getTheNewValue(i, j));
                    //graph.setTau(j, i, graph.getTau(i, j));

//                }
            }
        }
    }

//    public void limitPheromoneMatrix(double max, double min){
//        for (int i = 0; i < nrOfNodes; i++) {
//            for (int j = i; j < nrOfNodes; j++) {
//                if (i != j) {
//                    pheromoneMatrix[i][j] = Math.min(pheromoneMatrix[i][j], max);
//                    pheromoneMatrix[i][j] = Math.max(pheromoneMatrix[i][j], min);
//                    pheromoneMatrix[j][i] = pheromoneMatrix[i][j];
//                }
//            }
//        }
//    }

}
