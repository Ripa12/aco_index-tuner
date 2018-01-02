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
    private BitSet[] blockages;
    private BitSet supportCount;
    private int index;
    private double quality;
    private MyGraph graph;
    private List<Integer> solution;

    //public int[][] path;

    private int currentPosition;

    public MyAnt(int index, MyGraph graph){
        this.graph = graph;
        this.index = index;
        this.solution = new LinkedList<>(); // ArrayList

    }

    public List<Integer> getSolution(){
        return solution;
    }

    public double getSolutionQuality(){
        return quality;
    }

    public int getIndex(){
        return index;
    }

    public BitSet getSupportCount(){
        return supportCount;
    }

    public void findSolution(int capacity, double alpha, double beta){

        solution.clear();

        currentPosition = graph.getRandomPosition();
        solution.add(currentPosition);
        quality = graph.getProfit(currentPosition);
        double currentWeight = graph.getWeight(currentPosition);

        //path = new int[graph.getNumberOfNodes()][graph.getNumberOfNodes()];


        List<Integer> neighbours = graph.getNeighbours(currentPosition, capacity, currentWeight);

        while (neighbours.size()>0){
            int nextPosition = getNextItem(neighbours.size(), currentPosition, alpha, beta);

            neighbours.remove(nextPosition);

//            path[currentPosition][nextPosition] = 1;
//            path[nextPosition][currentPosition] = 1;

            graph.pruneNeighbours(neighbours, capacity, currentWeight);

            currentPosition = nextPosition;
            solution.add(nextPosition);
            quality += graph.getProfit(nextPosition);
            currentWeight += graph.getWeight(nextPosition);
        }

    }

    private int getNextItem(int nrOfNodes, int index, double alpha, double beta){

        //int nrOfNodes = graph.getNumberOfNodes();
        double[] tij = new double[nrOfNodes];
        double[] nij = new double[nrOfNodes];

        double total = 0;

        // nodes to visit
        for (int i = 0; i < nrOfNodes; i++){
            tij[i] = Math.pow(graph.getPheromone(index, i), alpha);
            //double heuristic = graph.getHeuristic(supportCount, i).cardinality()/graph.getWeight(i);
            double heuristic = graph.getProfit(index)/graph.getWeight(i);
            nij[i] = Math.pow(heuristic, beta);

            total += tij[i] * nij[i];
        }

        double[] probability = new double[nrOfNodes];

        double sumProbability = 0.0;

        for (int i = 0; i < nrOfNodes; i++){
            probability[i] = (tij[i] * nij[i]) / total;

            sumProbability += probability[i];
        }

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
