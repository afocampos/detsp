package py.edu.fiuni;

public class Config {

	// Number of generations
	private int T = 100;

	// Population size
	private int N = 10;

	// Crossover probability
	private double pc = 0.5;
	
	private double[][] weightMatrix;
	
	public Config(int iterations, int popSize, double[][] matrix){
		this.T = iterations;
		this.N = popSize;
		this.weightMatrix = matrix;
	}
	
	public int getMaxIterNumber(){
		return this.T;
	}
	
	public int getPopSize(){
		return this.N;
	}

}
