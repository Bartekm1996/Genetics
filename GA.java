import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Point2D;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

    class GraphVisualisation extends JFrame{
        private String TITLE;
        private final int HEIGHT = 400;
        private final int WIDTH = 400;
        private int[][] adjacencyMatrix;
        private int numberOfVerticies;
        private int[] ordering;
        private double chunk;
		private String text;

        public GraphVisualisation(String title){
            this.TITLE = title;
            setTitle(title);
            setSize(WIDTH, HEIGHT);
            setVisible(true);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
			text = "";
        }

		public void updateFunc(int[][] adjacencyMatrix, int[] ordering, int numberOfVerticies) {
			this.adjacencyMatrix = adjacencyMatrix;
            this.ordering = ordering;
            this.numberOfVerticies = numberOfVerticies;
            this.chunk = (Math.PI * 2)/((double) numberOfVerticies);
			
		}
		public void setText(String text) {
			this.text = text;
		}
		
        @Override
        public void paint(Graphics g){
			super.paintComponents(g);
            int radius = 100;
            int mov = 200;
			g.drawString(text, 10, 350);
            for(int i = 0; i < numberOfVerticies; i++){
					int xx = (int) (Math.cos(i*chunk)*radius) + mov;
					int yy = (int) (Math.sin(i*chunk)*radius) + mov;
					g.drawOval(xx,yy, 5, 5);
					g.drawString("" + ordering[i], xx, yy);
                for(int j = 0; j < numberOfVerticies; j++){
                    if(adjacencyMatrix[ordering[i]][ordering[j]] == 1){			
                       g.drawLine(
                                (int) (Math.cos(i*chunk)*radius) + mov,
                                (int) (Math.sin(i*chunk)*radius) + mov,
                                (int) (Math.cos(j*chunk)*radius) + mov,
                                (int) (Math.sin(j *chunk)*radius) +mov
                                );
                    }

                }

            }
        }
    }
	
public class GA {
	public static void main(String[] args) {
		List<int[]> edges = readFile("edges.txt");
		for(int[] edge : edges) {
			System.out.println(Arrays.toString(edge));
		}
		
		int[] len = edges.remove(edges.size()-1);
		int x = len[0];
		int y = len[1];
		int max = Math.max(x,y);
		int[][] adj = new int[max][max];
		for(int i = 0; i < y; i++) {
			for(int j = 0; j < x; j++) {
				adj[i][j] = contains(edges, i, j) ? 1 : 0;
			}
		}
		
		GraphVisualisation test = new GraphVisualisation("Test");
		int[] ordering = {5,4,2,0,6,1,3};
		
		test.updateFunc(adj, ordering, ordering.length);
	}
	

	
	private static boolean contains(List<int[]> edges, int x, int y) {
		for(int[] edge : edges) {
			if(edge[0] == x && edge[1] == y) return true;
		}
		
		return false;
	}
	
	private static List<int[]> readFile(String filePath){
		List<int[]> items = new ArrayList<>();
		int x = 0;
		int y = 0;
		
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)))) {
            ArrayList<String> arrayList = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                arrayList.add(line);
            }

            for(String pts : arrayList){
                String[] split = pts.split(" ");

                int numOne = Integer.parseInt(split[0]);
                int numTwo = Integer.parseInt(split[1]);

				if(numOne > x) x = numOne;
				if(numTwo > x) y = numTwo;
                items.add(new int[]{numOne, numTwo});

            }

			items.add(new int[]{x, y});
        }catch (IOException e){
            System.out.println("Failed to read file, exiting");
            System.exit(1);
        }
		
		return items;
		
    }
}
