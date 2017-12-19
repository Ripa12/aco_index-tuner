package ACO_Index;

/**
 * Created by Richard on 2017-12-18.
 */
public class Ant {

    graph = generateRandomMatrix(noOfCities);
    numberOfCities = graph.length;
    numberOfAnts = (int) (numberOfCities * antFactor);

    trails = new double[numberOfCities][numberOfCities];
    probabilities = new double[numberOfCities];
    ants = new Ant[numberOfAnts];
    IntStream.range(0, numberOfAnts).forEach(i -> ants.add(new Ant(numberOfCities)));


    public void visitCity(int currentIndex, int city) {
        trail[currentIndex + 1] = city;
        visited[city] = true;
    }

    public boolean visited(int i) {
        return visited[i];
    }

    public double trailLength(double graph[][]) {
        double length = graph[trail[trailSize - 1]][trail[0]];
        for (int i = 0; i < trailSize - 1; i++) {
            length += graph[trail[i]][trail[i + 1]];
        }
        return length;
    }
}
