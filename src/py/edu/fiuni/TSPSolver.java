package py.edu.fiuni;

import java.util.ArrayList;
import java.util.List;

public class TSPSolver {
	
	private Config config = null;
	private List<Vector> population = new ArrayList<>();
	
	public TSPSolver(Config config){
		this.config = config;
		
		initPopulation();
	}
	
	public Vector solve(){
	
		return null;
	}
	
	/**
	 * It is used Nearest neighbor algorithm to initialize the population
	 * based on settings in config
	 */
	private void initPopulation(){
		int counter = 0;
		while(counter < config.getPopSize()){
			this.population.add(buildPathWithNNAlgorithm());
		}
	}
	
	private Vector buildPathWithNNAlgorithm(){
		Vector path = new Vector();
		
		
		return path;
	}
}
