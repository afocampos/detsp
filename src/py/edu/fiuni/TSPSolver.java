package py.edu.fiuni;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TSPSolver {

	private Config config = null;
	private HashMap<String, List<Edge>> distancesMap = null;

	List<Vector> elites = new ArrayList<>();

	/**
	 * 
	 * @param config
	 */
	public TSPSolver(Config config) {
		this.config = config;
		this.distancesMap = buildMapFromWeightsMatrix();
		printDistancesMap(this.distancesMap);
	}

	/**
	 * 
	 * @return
	 */
	public List<Vector> solve() {

		List<Vector> population = initPopulation();
		evalPopulation(population);
		printPopulation(population);

		/////////////////////////////////////////////
		//////////////// TESTING ///////////////
		// Vector original = population.get(0);
		// System.out.println("Original=> " + original.toString());
		// Vector mutated = original.mutate();
		// mutated.setFitness(mutated.calculateFitness(this.config,
		///////////////////////////////////////////// this.distancesMap));
		// System.out.println("Mutated=> " + mutated.toString());
		// System.out.println("Original=> " + original.toString());
		/////////////////////////////////////////////

		int t = 0;
		while (t < this.config.getMaxIterNumber()) {

			List<Vector> mutatedPopulation = mutate(population);
			evalPopulation(mutatedPopulation);

			List<Vector> crossedPopulation = crossover(population, mutatedPopulation);
			evalPopulation(crossedPopulation);

			List<Vector> selectedPopulation = select(population, crossedPopulation);
			evalPopulation(selectedPopulation);

			population = selectedPopulation;
			// pick the best path in the population
			elites.add(getBestFromPopulation(population));
			t++;
		}

		//printPopulation(population);

		return population;
	}

	private List<Vector> mutate(List<Vector> population) {
		List<Vector> mutated = new ArrayList<>();
		for (Vector v : population) {
			mutated.add(v.mutate());
		}
		return mutated;
	}

	private List<Vector> crossover(List<Vector> population, List<Vector> mutatedPopulation) {
		List<Vector> crossed = new ArrayList<>();
		
		for (int i = 0; i < population.size(); i++) {

			Vector original = population.get(i);
			Vector mutated = mutatedPopulation.get(i);
			
			crossed.add(original.crossover(mutated, this.config.getCRSubTourLen()));
		}
		
		return crossed;
	}

	/**
	 * 
	 * @param population
	 * @param crossedPopulation
	 * @return
	 */
	private List<Vector> select(List<Vector> population, List<Vector> crossedPopulation) {

		List<Vector> selected = new ArrayList<>();

		for (int i = 0; i < population.size(); i++) {

			Vector original = population.get(i);
			Vector crossed = crossedPopulation.get(i);

			if (original.getFitness() < crossed.getFitness()) {
				selected.add(new Vector(original));
			} else {
				selected.add(new Vector(crossed));
			}
		}

		return selected;
	}

	/**
	 * 
	 * @param p
	 */
	private void evalPopulation(List<Vector> p) {
		for (Vector v : p) {
			v.setFitness(v.calculateFitness(this.config, this.distancesMap));
		}
	}

	/**
	 * 
	 * @param solution
	 */
	public void showGraphicsFor(Vector solution) {
		SolutionViewer viewer = new SolutionViewer(solution, config);
		viewer.setVisible(true);
	}

	/**
	 * It is used Nearest neighbor algorithm to initialize the population based
	 * on settings in config
	 */
	private List<Vector> initPopulation() {
		int counter = 0;
		List<Vector> result = new ArrayList<>();
		while (counter++ < config.getPopSize()) {
			result.add(buildPathWithNNAlgorithm());
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	private Vector buildPathWithNNAlgorithm() {
		Vector path = new Vector();

		// Used to confirm if a city is already in the path
		// Set<String> tempSet = new HashSet<>();

		String[] cities = config.getCitiesNames();
		// pick a random city as the starting point
		int pos = (int) (Math.random() * config.getCitiesNames().length);
		String currentCity = cities[pos];
		path.addNode(new Node(currentCity));
		// tempSet.add(cities[pos])

		while (path.getSize() < cities.length) {
			String nearest = getNearestTo(currentCity, path);
			path.addNode(new Node(nearest));
			currentCity = nearest;
		}

		return path;
	}

	/**
	 * 
	 * @param currentCity
	 * @param path
	 * @return
	 */
	private String getNearestTo(String currentCity, Vector path) {
		List<Edge> distances = this.distancesMap.get(currentCity);
		String selected = null;
		for (Edge e : distances) {
			if (!path.containsCity(e.getTo())) {
				selected = e.getTo();
				break;
			}
		}
		return selected;
	}

	/**
	 * 
	 * @return
	 */
	private HashMap<String, List<Edge>> buildMapFromWeightsMatrix() {
		HashMap<String, List<Edge>> map = new HashMap<>();

		String[] cities = config.getCitiesNames();
		double[][] weights = config.getWeightMatrix();

		for (int i = 0; i < weights.length; i++) {
			// create a map for the current city
			map.put(cities[i], new ArrayList<>());
			for (int j = 0; j < weights[i].length; j++) {
				if (i != j) {
					map.get(cities[i]).add(new Edge(cities[i], cities[j], weights[i][j]));
				}
			}
			// sort the nodes in ascending order of distance
			java.util.Collections.sort(map.get(cities[i]));
		}
		return map;
	}

	/**
	 * 
	 * @param map
	 */
	private void printDistancesMap(HashMap<String, List<Edge>> map) {
		for (String city : map.keySet()) {
			System.out.print(city + " -> ");
			for (Edge e : map.get(city)) {
				System.out.print(e.getTo() + ":" + e.getDistance() + "\t");
			}
			System.out.println();
		}
	}

	/**
	 * 
	 * @param pop
	 */
	public void printPopulation(List<Vector> pop) {
		//System.out.println("\n");
		System.out.println("Population:::>>");
		for (Vector v : pop) {
			System.out.println(v.toString());
		}
		//System.out.println("\n");
	}

	/**
	 * 
	 * @param population
	 * @return
	 */
	public Vector getBestFromPopulation(List<Vector> population) {
		java.util.Collections.sort(population);
		return population.get(0);
	}

	public void printEliteHistory() {
		//System.out.println("\n");
		System.out.println("Elites:::>>");
		for (Vector v : elites) {
			System.out.print(v.getFitness() + ",  ");
		}
		//System.out.println("\n");
	}
}
