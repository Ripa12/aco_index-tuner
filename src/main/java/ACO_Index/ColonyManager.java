package ACO_Index;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Richard on 2018-01-03.
 */
public class ColonyManager {
    private List<MyAntColony> colonies;

    private List<Integer> globalBestSupportCountSolution;
    private double globalBestSupportCountQuality;

    private List<Integer> globalBestWritesSolution;
    private double globalBestWritesQuality;

    public ColonyManager(){
        colonies = new ArrayList<>();
        globalBestSupportCountQuality = 0;
        globalBestWritesQuality = Double.MAX_VALUE;
    }

    public void addColony(MyAntColony colony){
        colonies.add(colony);
    }

    public void run(int iterations){
        while(iterations > 0){

            for (MyAntColony colony : colonies) {
                colony.start();
                if(colony.getLocalBestSupportCountQuality() > globalBestSupportCountQuality){
                    globalBestSupportCountQuality = colony.getLocalBestSupportCountQuality();
                    globalBestSupportCountSolution = colony.getLocalBestSupportCountSolution();//new ArrayList<>(localBestSolution);
                }
                if(colony.getLocalBestWritesQuality() < globalBestWritesQuality){
                    globalBestWritesQuality = colony.getLocalBestWritesQuality();
                    globalBestWritesSolution = colony.getLocalBestWritesSolution();//new ArrayList<>(localBestSolution);
                }
                colony.updateSCMaximumPheromone(globalBestSupportCountQuality);
                colony.updateSCMinimumPheromone();
                colony.updatePheromoneMatrix(0, globalBestSupportCountSolution, globalBestSupportCountQuality);

                colony.updateWMaximumPheromone(globalBestWritesQuality);
                colony.updateWMinimumPheromone();
                colony.updatePheromoneMatrix(1, globalBestWritesSolution, globalBestWritesQuality);
            }

            iterations--;
        }
        System.out.println("Best SupportCount found: " + globalBestSupportCountQuality);
        System.out.println("Best Writes found: " + globalBestWritesQuality);
    }
}
