package ACO_Index;

import ACO_Index.Objectives.MyAbstractObjective;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Richard on 2018-01-04.
 */
public class MyPheromone {
    private double[][] pheromoneMatrix;
    private int size;

    private double persistence;

    private double minPheromone;
    private double maxPheromone;

    public MyPheromone(int size, double pheromonePersistence){
        this.pheromoneMatrix = new double[size][size];
        this.size = size;
        this.persistence = pheromonePersistence;
    }

    public double getPheromone(int from, int to){
        return pheromoneMatrix[from][to];
    }

    public void reset(double totalProfit){
        for (double[] row: this.pheromoneMatrix)
            Arrays.fill(row, 1.0/(persistence * totalProfit));
    }

    private void updatePheromoneLevel(double globalBestQuality){
        maxPheromone = 1.0 / (persistence * globalBestQuality);
        minPheromone = maxPheromone / 10.0;
    }

    public void evaporate(MyAbstractObjective objective){

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                pheromoneMatrix[i][j] = (1.0 - persistence) * pheromoneMatrix[i][j];
                pheromoneMatrix[i][j] = Math.min(pheromoneMatrix[i][j], maxPheromone);
                pheromoneMatrix[j][i] = pheromoneMatrix[i][j];
            }
        }

    }

    public void update(double factor, double bestQuality, List<Integer> solution){
        updatePheromoneLevel(bestQuality);

        for (int i : solution) {
            for (int j = 0; j < size; j++) {
                pheromoneMatrix[i][j] = pheromoneMatrix[i][j] + 1 * factor;
                pheromoneMatrix[i][j] = Math.max(pheromoneMatrix[i][j], minPheromone);
                pheromoneMatrix[j][i] = pheromoneMatrix[i][j];
            }
        }

    }
}
