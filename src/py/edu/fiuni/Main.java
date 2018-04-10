package py.edu.fiuni;

import java.net.URL;
import java.net.URLDecoder;
import java.util.List;

public class Main {

	public static void main(String args[]) {

		try {
			
			String citiesName1[] = { "W", "Y", "B", "R" };
			double matrix1[][] = { { Double.MAX_VALUE, 10, 17, 15 }, { 20, Double.MAX_VALUE, 19, 18 },
					{ 50, 44, Double.MAX_VALUE, 22 }, { 45, 40, 20, Double.MAX_VALUE } };
			Config config1 = new Config(100, 10, 2, citiesName1, matrix1);
			
			String citiesName2[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" };
			URL url = Main.class.getClassLoader().getResource("p01_d.txt");
			double[][] matrix2 = FileHandler.load(URLDecoder.decode(url.getPath(), "UTF-8"));
			Config config2 = new Config(1000, 16, 4, citiesName2, matrix2);

			TSPSolver solver1 = new TSPSolver(config2);
			List<Vector> lastPopulation = solver1.solve();
			solver1.printPopulation(lastPopulation);
			solver1.printEliteHistory();

			Vector bestPath = solver1.getBestFromPopulation(lastPopulation);
			System.out.println("\n\nBest Path=> " + bestPath.toString());
			
			solver1.showGraphicsFor(bestPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}