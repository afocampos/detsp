package py.edu.fiuni;

/**
 * 
 * @author Arnaldo
 *
 */
public class Node {
	
	private String name = null;
	
	//The position of the node.
    private final int[] location;
	
    /**
     * 
     * @param name
     * @param pos
     */
	public Node(String name, int[] pos){
		this.name = name;
		this.location = pos;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int[] getLocation(){
		return this.location;
	}
	
	public boolean equals(Node n){
		if(n == null) return false;
		return this.name.equals(n.name);
	}
	
	public int hashCode(){
		return this.name.hashCode();
	}	
	
	/**
	 * Calculate the distance to a given node 
	 * @param node the other node to get the distance from
	 * @return the distance between the nodes
	 */
    public double distanceTo (Node node) {
        int nodeLocation[] = node.getLocation();

        int dx = Math.abs(this.location[0] - nodeLocation[0]);
        int dy = Math.abs(this.location[1] - nodeLocation[1]);
        double result = Math.pow(dx, 2.0) + Math.pow(dy, 2.0);

        return Math.sqrt(result);
    }
}
