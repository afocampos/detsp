package py.edu.fiuni;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String args[]) {

		try {
			
			// pr2392 -> time:43834   best: 466425.8    best known: 3         2500 iteraciones
			// pcb442 -> time:265089   best: 61323.69    best known: 50778	  5000 iteraciones	
			
			URL url = Main.class.getClassLoader().getResource("TSPFiles/pr2392.tsp");

			TSPInstance instance = new TSPInstance(new File(URLDecoder.decode(url.getPath(), "UTF-8")));
			Map<String, Node> nodes = instance.getNodesMap();
			double[][] matrix = instance.getDistancesMatrix();
			String[] citiesName = instance.getNodeNames();
			Config config = new Config(5000, 30, 5, citiesName, matrix);

			int i = 0;
			// String citiesName1[] = { "W", "Y", "B", "R" };
			// double matrix1[][] = { { Double.MAX_VALUE, 10, 17, 15 }, { 20,
			// Double.MAX_VALUE, 19, 18 },
			// { 50, 44, Double.MAX_VALUE, 22 }, { 45, 40, 20, Double.MAX_VALUE
			// } };
			// Config config1 = new Config(100, 10, 2, citiesName1, matrix1);
			//
			// String citiesName2[] = { "1", "2", "3", "4", "5", "6", "7", "8",
			// "9", "10", "11", "12", "13", "14", "15" };
			// URL url =
			// Main.class.getClassLoader().getResource("TSPFiles/p02_d.txt");
			// double[][] matrix2 =
			// FileHandler.load(URLDecoder.decode(url.getPath(), "UTF-8"));
			// Config config2 = new Config(1000, 16, 4, citiesName2, matrix2);

			TSPSolver solver1 = new TSPSolver(config);
			
			double value = 0;
			for(int times = 1; times < 6; times++){
				System.out.println("Round #" + times);
				List<Vector> lastPopulation = solver1.solve();
				//solver1.printPopulation(lastPopulation);
				//solver1.printEliteHistory();
	
				Vector bestPath = solver1.getBestFromPopulation(lastPopulation);
				System.out.println("Best Path=> " + bestPath.getFitness() + " >>> " + bestPath.toString()+ "\n");
				value += bestPath.getFitness();
				// solver1.showGraphicsFor(bestPath);
			}
			System.out.println("Mean:" + value/5 + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}