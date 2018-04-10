package py.edu.fiuni;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * It represents a path (a possible solution for the TSP problem)
 * 
 * @author Arnaldo
 *
 */
public class Vector implements Comparable<Vector> {

	private LinkedHashMap<String, Node> path = new LinkedHashMap<>();

	private double fitness = 0;

	public Vector() {

	}

	public Vector(Vector v) {
		for (Node n : v.getTour().values()) {
			this.addNode(new Node(n.getName()));
		}
		this.fitness = v.getFitness();
	}

	public void addNode(Node n) {
		this.path.put(n.getName(), n);
	}

	private void addNodes(List<Node> list) {
		for (Node n : list) {
			this.path.put(n.getName(), new Node(n.getName()));
		}
	}

	public double getFitness() {
		return fitness;
	}

	public Map<String, Node> getTour() {
		return this.path;
	}

	public int getSize() {
		return this.path.size();
	}

	public boolean containsCity(String city) {
		return this.path.containsKey(city);
	}

	public void setFitness(double f) {
		this.fitness = f;
	}

	/**
	 * Mutate the current vector using Inversion algorithm
	 * 
	 * @return a new Vector, mutation of the current one
	 */
	public Vector mutate() {

		List<Node> current = new ArrayList<>(this.path.values());

		Vector mutated = new Vector();

		int p1Index = /* 1 + */(int) (Math.random() * (current.size() - 1));
		int p2Index = /* 1 + */(int) (Math.random() * (current.size() - 1));

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
			mutated.addNode(new Node(current.get(i).getName()));
		}

		// copy elements between the lower and upper bound in reverse order
		List<Node> subListtoInvert = current.subList(lower, upper);
		Collections.reverse(subListtoInvert);
		mutated.addNodes(subListtoInvert);

		// copy elements after the upper bound
		for (int i = upper; i < current.size(); i++) {
			mutated.addNode(new Node(current.get(i).getName()));
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
		List<Node> originalPath = new ArrayList<>(this.path.values());
		List<Node> mutatedPath = new ArrayList<>(mutated.path.values());

		Vector crossed = new Vector();

		int startIndex = (int) (Math.random() * (originalPath.size() - 1 - subTourLen));

		//System.out.println("Start Index: " + startIndex + " 	SubTour len: " + subTourLen);

		// take subTourLen elements starting at startIndex
		List<Node> subList = originalPath.subList(startIndex, startIndex + subTourLen);
		List<Node> pendingNodes = new ArrayList<>();

		for (Node n : mutatedPath) {
			if (!subList.contains(n)){
				pendingNodes.add(n);
			}
		}

		// copy elements before the start index
		for (int i = 0; i < startIndex; i++) {
			crossed.addNode(new Node(pendingNodes.get(i).getName()));
		}

		crossed.addNodes(subList);

		// add remaining nodes in pending list
		for (int i = startIndex; i < pendingNodes.size(); i++) {
			crossed.addNode(new Node(pendingNodes.get(i).getName()));
		}
		
		return crossed;
	}

	/**
	 * The fitness of the vector is given by the sum of the distances of the entire path
	 * @param config
	 * @param map
	 * @return
	 */
	public double calculateFitness(Config config, HashMap<String, List<Edge>> map) {
		List<Node> nodes = new ArrayList<Node>(this.path.values());
		double fit = 0;
		for (int i = 0; i < nodes.size(); i++) {
			Node n1 = nodes.get(i);
			Node n2 = i == nodes.size() - 1 ? nodes.get(0) : nodes.get(i + 1);
			fit += getDistanceBetween(n1, n2, config, map);
		}
		return fit;
	}

	@Override
	public String toString() {
		String result = "Path: ";
		for (String city : path.keySet()) {
			result += city + " -> ";
		}
		if (!path.isEmpty()) {
			result += new ArrayList<>(path.values()).get(0).getName(); // ((Node)(path.values().toArray()[0])).getName();
		}
		result += "\tFit:" + this.fitness;

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

	private double getDistanceBetween(Node n1, Node n2, Config config, HashMap<String, List<Edge>> map) {
		List<Edge> distances = map.get(n1.getName());
		for (Edge e : distances) {
			if (e.getTo().equals(n2.getName()))
				return e.getDistance();
		}
		return Double.MAX_VALUE;
	}
}
