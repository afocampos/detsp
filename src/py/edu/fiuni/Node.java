package py.edu.fiuni;

public class Node {
	private String name = null;
	
	public Node(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(Node n){
		if(n == null) return false;
		return this.name.equals(n.name);
	}
	
	public int hashCode(){
		return this.name.hashCode();
	}
}
