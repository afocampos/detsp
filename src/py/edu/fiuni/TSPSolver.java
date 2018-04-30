package py.edu.fiuni;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
		//printDistancesMap(this.distancesMap);
	}

	/**
	 * 
	 * @return
	 */
	public List<Vector> solve(StringBuilder buffer) {
		
		long startTime = System.currentTimeMillis();
		long endTime = startTime + (config.getMaxRunningTime()*1000);
		
		List<Vector> population = initPopulation();
		evalPopulation(population);
		// TODO: Make this cofigurable
		//printPopulation(population);

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
		long currentTime;
		do {

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
			
			currentTime = System.currentTimeMillis();
			
		}while ( currentTime < endTime /*t < this.config.getMaxIterNumber()*/);
				
		System.out.println("Elapsed time: " + (System.currentTimeMillis() - startTime) + " Generations: " + t);
		
		//printPopulation(population);

		return population;
	}
	
	/**
	 * 
	 * @param pop
	 */
	public void printPopulation(List<Vector> pop) {		
		System.out.println("Population: ");
		for (Vector v : pop) {
			System.out.println(v.toString());
		}
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
		System.out.println("Elites: ");
		for (Vector v : elites) {
			System.out.print(v.getFitness() + ",  ");
		}
		//System.out.println("\n");
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
	 * 
	 * @param population
	 * @return
	 */
	private List<Vector> mutate(List<Vector> population) {
		List<Vector> mutated = new ArrayList<>();
		for (Vector v : population) {
			mutated.add(v.mutate());
		}
		return mutated;
	}

	/**
	 * 
	 * @param population
	 * @param mutatedPopulation
	 * @return
	 */
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
	 * Initialize the population based on the settings in the config.
	 * One vector is built using Nearest neighbor algorithm
	 * The rest of the population is built randomly
	 * @return The list of vectors that represents the population
	 */
	private List<Vector> initPopulation() {
		List<Vector> result = new ArrayList<>();
		//
		result.add(buildPathWithNNAlgorithm());
		//result.add(buildPathRandomly());
		// starts in 1 because the first is for the generated with NNAlgorithm 
		int counter = 1;
		while (counter++ < config.getPopSize()) {
			result.add(buildPathRandomly());
		}
		return result;
	}

	/**
	 * Build a vector using nearest neighbor algoritm
	 * @return
	 */
	private Vector buildPathWithNNAlgorithm() {
		Vector path = new Vector();

		String[] cities = config.getCitiesNames();
		
		// pick a random city as the starting point
		int pos = (int) (Math.random() * config.getCitiesNames().length);
		String currentCity = cities[pos];
		path.addNode(currentCity);

		while (path.getSize() < cities.length) {
			String nearest = getNearestTo(currentCity, path);
			path.addNode(nearest);
			currentCity = nearest;
		}

		return path;
	}
	
	private Vector buildPathRandomly() {
		Vector path = new Vector();
	
		List<String> listOfCities = Arrays.asList(config.getCitiesNames());
		Collections.shuffle(listOfCities);
		path.addNodes(listOfCities);
		
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
}
