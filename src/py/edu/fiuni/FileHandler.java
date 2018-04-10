package py.edu.fiuni;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static double[][] load(String file) {
		File aFile = new File(file);
		StringBuffer contents = new StringBuffer();
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(aFile));
			String line = null;
			//int i = 0;
			while ((line = input.readLine()) != null) {
				contents.append(line);
				//i++;
				contents.append(System.getProperty("line.separator"));
			}
		} catch (FileNotFoundException ex) {
			System.out.println("Can't find the file: " + file);
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Input output exception while processing file");
			ex.printStackTrace();
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				System.out.println("IO exception while processing file");
				ex.printStackTrace();
			}
		}
		String[] strArray = contents.toString().split("\n");
		double[][] matrix = new double[strArray.length][];
		
		// loop over the tab separated values
		int i = 0;
		for(String tsv : strArray){
			String[] distances = tsv.split("\t");
			matrix[i] = new double[distances.length];
			for(int j = 0; j<distances.length; j++){
				matrix[i][j] = Double.valueOf(distances[j]);
			}
			i++;
		}
		
		return matrix;
	}

	/*public void save(String file, String[] array) throws FileNotFoundException, IOException {

		File aFile = new File(file);
		Writer output = null;
		try {
			output = new BufferedWriter(new FileWriter(aFile));
			for (int i = 0; i < array.length; i++) {
				output.write(array[i]);
				output.write(System.getProperty("line.separator"));
			}
		} finally {
			if (output != null)
				output.close();
		}
	}*/
}
