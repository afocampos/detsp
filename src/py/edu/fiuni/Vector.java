package py.edu.fiuni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * It represents a path (a possible solution for the TSP problem)
 * 
 * @author Arnaldo
 *
 */
public class Vector implements Comparable<Vector> {

	private List<String> path = new ArrayList<>();

	private double fitness = 0;

	/**
	 * 
	 */
	public Vector() { }

	/**
	 * 
	 * @param v
	 */
	public Vector(Vector v) {
		for (String node : v.getTour()) {
			this.addNode(node);
		}
		this.fitness = v.getFitness();
	}

	/**
	 * 
	 * @param nodeName
	 */
	public void addNode(String nodeName) {
		this.path.add(nodeName);
	}

	/**
	 * 
	 * @param list
	 */
	public void addNodes(List<String> list) {
		for (String node : list) {
			this.path.add(node);
		}
	}

	/**
	 * 
	 * @return
	 */
	public double getFitness() {
		return fitness;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getTour() {
		return this.path;
	}

	/**
	 * 
	 * @return
	 */
	public int getSize() {
		return this.path.size();
	}

	/**
	 * 
	 * @param city
	 * @return
	 */
	public boolean containsCity(String city) {
		return this.path.contains(city);
	}

	/**
	 * 
	 * @param f
	 */
	public void setFitness(double f) {
		this.fitness = f;
	}

	/**
	 * Mutate the current vector using Inversion algorithm
	 * 
	 * @return a new Vector, mutation of the current one
	 */
	public Vector mutate() {

		// TODO: Try to use the instance of perform a copy
		List<String> current = new ArrayList<>(this.path); 

		Vector mutated = new Vector();

		int p1Index = /*1 +*/ (int) (Math.random() * (current.size() - 1));
		int p2Index = /*1 + */(int) (Math.random() * (current.size() - 1));

		int lower = 0;
		int upper = 0;
		if (p1Index < p2Index) {
			lower = p1Index;
			upper = p2Index;
		} else {
			lower = p2Index;
			upper = p1Index;
		}

		// System.out.println("Lower: " + lower + " Upper: " + upper);

		// copy elements before the lower bound
		for (int i = 0; i < lower; i++) {
			mutated.addNode(current.get(i));
		}

		// copy elements between the lower and upper bound in reverse order
		List<String> subListtoInvert = current.subList(lower, upper);
		Collections.reverse(subListtoInvert);
		mutated.addNodes(subListtoInvert);

		// copy elements after the upper bound
		for (int i = upper; i < current.size(); i++) {
			mutated.addNode(current.get(i));
		}

		return mutated;
	}

	/**
	 * Perform a Crossover operation using the "Order Crossover (OX)" algorithm
	 * 
	 * @param mutated
	 * @param subTourLen
	 * @return the crossed population
	 */
	public Vector crossover(Vector mutated, int subTourLen) {
		
		// TODO: Try to use the instance of perform a copy
		List<String> originalPath = new ArrayList<>(this.path);
		List<String> mutatedPath = new ArrayList<>(mutated.path);

		Vector crossed = new Vector();

		int startIndex = (int) (Math.random() * (originalPath.size() - 1 - subTourLen));

		// System.out.println("Start Index: " + startIndex + " SubTour len: " + subTourLen);

		// take subTourLen elements starting at startIndex
		List<String> subList = originalPath.subList(startIndex, startIndex + subTourLen);
		List<String> pendingNodes = new ArrayList<>();

		for (String n : mutatedPath) {
			if (!subList.contains(n)) {
				pendingNodes.add(n);
			}
		}

		// copy elements before the start index
		for (int i = 0; i < startIndex; i++) {
			crossed.addNode(pendingNodes.get(i));
		}

		crossed.addNodes(subList);

		// add remaining nodes in pending list
		for (int i = startIndex; i < pendingNodes.size(); i++) {
			crossed.addNode(pendingNodes.get(i));
		}

		return crossed;
	}

	/**
	 * The fitness of the vector is given by the sum of the distances of the
	 * entire path
	 * 
	 * @param config
	 * @param map
	 * @return
	 */
	public double calculateFitness(Config config, HashMap<String, List<Edge>> map) {
		List<String> nodes = new ArrayList<>(this.path);
		double fit = 0;
		for (int i = 0; i < nodes.size(); i++) {
			String n1 = nodes.get(i);
			String n2 = i == nodes.size() - 1 ? nodes.get(0) : nodes.get(i + 1);
			fit += getDistanceBetween(n1, n2, config, map);
		}
		return fit;
	}

	@Override
	public String toString() {
		
		String result = "Fit:" + this.fitness + " Tour: " + String.join(" - ", path);
				
		if (!path.isEmpty()) {
			result += path.get(0);
		}		

		return result;
	}

	
	@Override
	public int compareTo(Vector v) {
		if (this.fitness < v.fitness)
			return -1;
		else if (this.fitness > v.fitness)
			return 1;
		else
			return 0;
	}

	/**
	 * 
	 * @param n1
	 * @param n2
	 * @param config
	 * @param map
	 * @return
	 */
	private double getDistanceBetween(String n1, String n2, Config config, HashMap<String, List<Edge>> map) {
		List<Edge> distances = map.get(n1);
		for (Edge e : distances) {
			if (e.getTo().equals(n2))
				return e.getDistance();
		}
		return Double.MAX_VALUE;
	}
}
