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

	private TSPInstance problemInstance = null;
	private String[] names;
	private double[][] weightMatrix;
	
	
	private int rounds = 1;
	private long maxRunningTime = 0;
	

	/**
	 * 
	 * @param iterations
	 * @param popSize
	 * @param crSubtourLen
	 * @param problemInstance
	 */
	public Config(int iterations, int popSize, int crSubtourLen, TSPInstance problemInstance, int rounds, long howLong) {
		this.T = iterations;
		this.N = popSize;
		this.crLen = crSubtourLen;
		this.problemInstance = problemInstance;
		this.names = problemInstance.getNodeNames();
		this.weightMatrix = problemInstance.getDistancesMatrix();	
		this.rounds = rounds;
		this.maxRunningTime = howLong;
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
	
	public TSPInstance getTSPInstance(){
		return this.problemInstance;
	}

	public String[] getCitiesNames() {
		return this.names;
	}

	public double[][] getWeightMatrix() {
		return this.weightMatrix;
	}

	public int getRounds() {
		return rounds;
	}

	public long getMaxRunningTime() {
		return maxRunningTime;
	}	
}
