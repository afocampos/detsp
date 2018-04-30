package py.edu.fiuni;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

/**
 * 
 * @author Arnaldo Ocampo
 * @author Nestor Tapia
 * @author Aldo Medina
 *
 */

public class Main {

	public static void main(String args[]) {

		try {
			
			// String citiesName2[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" };
			// double[][] matrix2 = FileHandler.load(buildFilePath("p02_d.txt"));			
			// Config config2 = new Config(1000, 16, 4, citiesName2, matrix2);
		
			TSPInstance instance = new TSPInstance(new File(buildFilePath("eil51.tsp")));
			// Map<String, Node> nodes = instance.getNodesMap();

			Config config = new Config(1000, 100, 2, instance, 5, 240);
			TSPSolver solver1 = new TSPSolver(config);

			StringBuilder buffer = new StringBuilder();
			buffer.append("Round, Best Found\n");
			
			
			double value = 0;
			for (int round = 1; round <= config.getRounds(); round++) {
				
				System.out.println("Round #" + round);
				buffer.append(String.valueOf(round)+",");
				List<Vector> lastPopulation = solver1.solve(buffer);
				// solver1.printPopulation(lastPopulation);
				//solver1.printEliteHistory();

				Vector bestPath = solver1.getBestFromPopulation(lastPopulation);
				System.out.println("Best Path=> " + bestPath.toString() + "\n");
				
				value += bestPath.getFitness();
				// solver1.showGraphicsFor(bestPath);
				
				buffer.append(String.valueOf(bestPath.getFitness())+"\n");
			}
			
			System.out.println("Mean:" + value / 5 + "\n");
			
			
			System.out.println(buffer.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String buildFilePath(String fileName) throws UnsupportedEncodingException {
		URL url = Main.class.getClassLoader().getResource("TSPFiles/" + fileName);
		return URLDecoder.decode(url.getPath(), "UTF-8");
	}
}