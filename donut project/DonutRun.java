import java.util.*;
import java.io.*;

public class DonutRun {
	HashMap<String, String> name;
	List<String> geocode;
	Graph graph;

	DonutRun(String fileName, String from, String l1, String to, String l2){
		name = new HashMap<>();
		geocode = new ArrayList<>();
		read_file(fileName);
		graph = create_graph(from, l1, to, l2);
	}

	public double coor_distances(double x1, double y1, double x2, double y2) {
		double distance = 0;
		distance += Math.pow(x1-x2, 2) + Math.pow(y1-y2, 2);
		return Math.sqrt(distance);
	}

	public Graph create_graph(String from, String l1, String to, String l2) {
		geocode.add(0, from);
    	String[] s = from.split(",");
    	name.put(s[0], l1);

    	geocode.add(geocode.size(), to); //to
    	s = to.split(",");
    	name.put(s[0], l2);

		Graph graph = new Graph(geocode.size());
        for(int i = 0; i < geocode.size(); i++) {
            for(int j = 0; j < geocode.size(); j++) {
            	String[] fst = geocode.get(i).split(",");
            	String[] snd = geocode.get(j).split(",");
	            double d = coor_distances(Double.parseDouble(fst[0]), Double.parseDouble(fst[1]), Double.parseDouble(snd[0]), Double.parseDouble(snd[1]));
	            graph.setUndirectedEdge(i, j, d);
            }
        }
        return graph;
	}

	public List<String> getShortestPath(int source, int target) {
	    boolean found = false;
	    List<String> output = new ArrayList<>();
	    PriorityQueue<VertexDistancePair> queue = new PriorityQueue<>();
	    Integer[] previous = new Integer[graph.size()];
	    double[] distances = new double[graph.size()];
	    Set<Integer> visited = new HashSet<>();

	    init(distances, previous, target);

	   	queue.add(new VertexDistancePair(target, 0));

	   	while(!queue.isEmpty()) {
	        VertexDistancePair curr = queue.poll();
	        if (curr.vertex == source) {
	            found = true;
	            break;
	        }
	        visited.add(curr.vertex);
	        for (Edge edge : graph.getIncomingEdges(curr.vertex)) {
	            int v = edge.getSource();
	            if (!visited.contains(v)) {
	                double dist = distances[curr.vertex] + edge.getWeight();
	                if (dist < distances[v] && !(v == source && curr.vertex == target)) {
	                    distances[v] = dist;
	                    previous[v] = curr.vertex;
	                    queue.add(new VertexDistancePair(v, dist));
	                }
	            }

	        }
	    }
	    if(found) {
	        int index = source;
	        String[] s = new String[2];
	        while (index != target) {
	          	s = geocode.get(index).split(",");
	            output.add(name.get(s[0])); 
	            index = previous[index];
	        }
	       	s = geocode.get(target).split(",");
	        output.add(name.get(s[0])); 
	    }
	    return output;
	}

	private void init(double[] distances, Integer[] previous, int target) {
	    for (int i = 0; i < distances.length; i++) {
	       	if (i == target)
	            distances[i] = 0;
	      	else {
	           	distances[i] = Double.MAX_VALUE;
	            previous[i] = null;
	        }
	    }
	}

	public void read_file(String fileName) {
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			boolean hasLine = true;
			String line = "";
			while(hasLine) {
				line = br.readLine();
				if(line == null) {
					hasLine = false;
					break;
				}
				if(line.length() > 0 && isCharInt(line.charAt(0))) {
					String[] s = line.split(",");
					geocode.add(s[0]+","+s[1]);
					name.put(s[0], s[2].substring(0, s[2].length()-1));
				}
			}
			fr.close();
		}
		catch(IOException io) {
			System.out.println("Error: " + io);
		}
	}

	public boolean isCharInt(char c) {	
	    try {
	    	if (c == '-') return true;
	        Integer.parseInt(String.valueOf(c));
	        return true;
	    } catch (NumberFormatException ex) {
	        return false;
	    }
	}

	public String shortenURL(String s) {
		String[] array = s.split("/");
		String out = array[array.length-1];
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < out.length(); i++) {
			if (out.charAt(i)!='.') {
				sb.append(out.charAt(i));
			}
			else return sb.toString();
		}
		return sb.toString();
	}

    public static void main(String[] args) {
    	String fileName = "";
    	String to = ""; String from = "";
    	String l1 = ""; String l2 = "";
    	if(args.length == 5) {
    		fileName = args[0];
    		from = args[1];
    		l1 = args[2];
    		to = args[3];
    		l2 = args[4];
    	}
    	else {
    		System.out.println("Format: java test fileName.rtf lat,long l1 lat,long l2");
    		System.exit(0);
    	}
    	DonutRun d = new DonutRun(fileName, from, l1, to, l2);
    	List<String> path = d.getShortestPath(0, d.graph.size()-1); //source, target
    	System.out.println("");
    	System.out.println("Go to the '" + d.shortenURL(fileName) + "' location in: " + path.get(1));
    	System.out.println("");
    }
}

class VertexDistancePair implements Comparable<VertexDistancePair> {
   	public int vertex;
  	public double distance;

   	public VertexDistancePair(int vertex, double distance) {
        this.vertex = vertex;
        this.distance = distance;
    }

    @Override
    public int compareTo(VertexDistancePair p) {
        double diff = this.distance - p.distance;
        if (diff > 0) return 1;
        if (diff < 0) return -1;
        return 0;
    }
}