package py.edu.fiuni;

public class Config {

	// Number of generations
	private int T = 100;

	// Population size
	private int N = 10;

	// len of the subtour taken in Order Crossover (OX)
	private int crLen = 4;

	// Crossover probability
	private double CR = 0.5;

	private String[] names;
	private double[][] weightMatrix;

	public Config(int iterations, int popSize, int crSubtourLen, String[] citiesName, double[][] matrix) {
		this.T = iterations;
		this.N = popSize;
		this.crLen = crSubtourLen;
		this.names = citiesName;
		this.weightMatrix = matrix;
	}

	public int getMaxIterNumber() {
		return this.T;
	}

	public int getPopSize() {
		return this.N;
	}

	public int getCRSubTourLen() {
		return this.crLen;
	}

	public String[] getCitiesNames() {
		return this.names;
	}

	public double[][] getWeightMatrix() {
		return this.weightMatrix;
	}
}
