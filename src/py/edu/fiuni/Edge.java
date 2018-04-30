package py.edu.fiuni;

/**
 * 
 * 
 * @author Arnaldo
 *
 */
public class Edge implements Comparable<Edge> {

	private String from = null;
	private String to = null;
	private double distance = Double.MAX_VALUE;

	/**
	 * 
	 * @param from
	 * @param to
	 * @param distance
	 */
	public Edge(String from, String to, double distance) {
		this.from = from;
		this.to = to;
		this.distance = distance;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(Edge arg0) {
		if (this.distance < arg0.distance)
			return -1;
		else if (this.distance > arg0.distance)
			return 1;
		else
			return 0;
	}
}
