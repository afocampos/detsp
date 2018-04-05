package py.edu.fiuni;

import java.util.LinkedHashMap;

/**
 * It represents a path (a possible solution for the TSP problem)
 * 
 * @author Arnaldo
 *
 */
public class Vector {
	
	private LinkedHashMap<String, Node> path = new LinkedHashMap<>();
	
	public Vector(){
		
	}
	
	@Override
	public String toString(){
		String result = "Path: ";
		for (String city : path.keySet()) {
			result += city + " -> ";
		}
		return result;
	}
}
