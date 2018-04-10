package py.edu.fiuni;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SolutionViewer extends JFrame {

	private Vector path = null;
	private Config config = null;
	
	public SolutionViewer(Vector path, Config cfg){
		this.path = path;
		this.config = cfg;
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(50, 50, 600, 600);
		this.setTitle("Path viewer");
		this.getContentPane().add(new JPanel());
	}
	
	/*public void paint(Graphics g){
		
		int px = 20;
		
		for(Node n : path.getTour().values()){
			g.drawOval(px, (int)(Math.random()*500), 10, 10);
		}
	}
	
	public void update(Graphics g){
		repaint();
	}*/
}
