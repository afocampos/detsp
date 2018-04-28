package py.edu.fiuni;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TSPProblem represents a TSPLIB problem instance.
 */

public class TSPInstance {

	private String problemName;
	private String problemComment;
	private String problemType;

	private Map<String, Node> nodes = null;

	/**
	 * 
	 * @param file
	 * @throws IOException
	 */
	public TSPInstance(File file) throws IOException {
		nodes = new LinkedHashMap<>();
		parseFile(file);
	}

	/**
	 * Parse a problem from a TSPLIB file
	 * 
	 * @param file
	 * @throws IOException
	 */
	private void parseFile(File file) throws IOException {
		BufferedReader reader = null;
		String line = null;

		try {
			reader = new BufferedReader(new FileReader(file));

			while ((line = reader.readLine()) != null) {
				line = line.trim();

				if (!line.isEmpty() && !line.equals("NODE_COORD_SECTION")) {
					if (line.equals("EOF")) {
						break;
					}

					else if (line.contains(":")) {
						String[] tokens = line.split(":");
						String token_0 = tokens[0].trim();
						String token_1 = tokens[1].trim();

						if (token_0.equals("NAME")) {
							problemName = "Problem Name: " + token_1;
						}

						if (token_0.equals("COMMENT")) {
							problemComment = "Comment: " + token_1;
						}

						if (token_0.equals("TYPE")) {
							problemType = "Problem Type: " + token_1;
						}
					}

					else {
						String[] tokens = line.trim().split("\\s+");

						if (tokens.length != 3) {
							System.err.println("Wrong info for node entry: " + line);
							continue;
						}

						int id = Integer.parseInt(tokens[0].trim());

						int position[];
						if (tokens[1].contains("e")) {
							int position_1 = new BigDecimal(tokens[1].trim()).intValue();
							int position_2 = new BigDecimal(tokens[2].trim()).intValue();
							position = new int[] { position_1, position_2 };
						} else {
							position = new int[] { Integer.parseInt(tokens[1].trim()),
									Integer.parseInt(tokens[2].trim()) };
						}

						Node node = new Node(String.valueOf(id), position);
						nodes.put(node.getName(), node);
					}
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public double[][] getDistancesMatrix(){
		double[][] distances = new double[nodes.size()][nodes.size()];
		int i = 0;
		int j = 0;
		for(Node n : nodes.values()){
			j = 0;
			for(Node other : nodes.values()){
				distances[i][j++] = n.distanceTo(other);
			}	
			i++;
		}		
		return distances;
	}
	
	/**
	 * 
	 * @return
	 */
	public String[] getNodeNames(){
		String[] names = new String[nodes.size()];
		int i = 0;
		for (String s : nodes.keySet()) {
			names[i++] = s;
		}
		return names;		
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Node> getNodesMap() {
		return nodes;
	}
}