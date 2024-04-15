/* Taha Alkattan 
   Dr. Steinberg
   COP3503 Spring 2024
   Programming Assignment 5
*/

import java.io.*;
import java.util.*;

class Edge{
    protected String src, dest;
    protected int weight;

    public Edge(String src, String dest, int weight){
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
    
}

class DisjointSet{

    int [] rank;
	int [] parent;
    int n;

    public DisjointSet(int n){
        rank = new int[n];
        parent = new int[n];
        this.n = n;
        makeSet();
    }

    public void makeSet(){
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    public int find(int x){
        if (parent[x] != x){
            parent[x] = find(parent[x]);
        }
 
        return parent[x];
    }

    public void union(int x, int y){
        int xRoot = find(x), yRoot = find(y);
 
        if (xRoot == yRoot)
            return;
		
        if (rank[xRoot] < rank[yRoot])
            parent[xRoot] = yRoot;
		
        else if (rank[yRoot] < rank[xRoot])
            parent[yRoot] = xRoot;
        else {
            parent[yRoot] = xRoot;
            rank[xRoot] = rank[xRoot] + 1;
        }
    }
}

public class Railroad{

    protected int numTracks, numOfVerticies = 0, totalCost = 0;
    protected String fileName;
    protected List<Edge> railroad;
    protected Map<String, Integer> indexOfVertex;
    protected List<Edge> minimumSpanningTree; 
    protected DisjointSet set;
 
    public Railroad(int numTrack, String file){
        this.numTracks = numTrack;
        this.fileName = file;
        this.railroad = new ArrayList<>();
        this.indexOfVertex = new HashMap<>();
        this.minimumSpanningTree = new ArrayList<>();
        this.set = new DisjointSet(numTracks);
        
        fileReader();
    }

    public void fileReader(){
        try{
            File rails = new File(fileName);
            Scanner railroadF = new Scanner(rails);
            
            while(railroadF.hasNext()){
                String src = railroadF.next();
                String dest = railroadF.next();
                int weight = railroadF.nextInt();

                if (!indexOfVertex.containsKey(src)) {
                    indexOfVertex.put(src, numOfVerticies++);
                }
                if (!indexOfVertex.containsKey(dest)) {
                    indexOfVertex.put(dest, numOfVerticies++);
                }

                railroad.add(new Edge(src, dest, weight));
            }

            railroadF.close();
        }catch (FileNotFoundException e) {
            System.out.println("File went boom");
            e.printStackTrace();
        }
    }

    public String buildRailroad() {
        
        Collections.sort(railroad, Comparator.comparingInt(Edge::getWeight));
        
        for (int i = 0; i < railroad.size(); i++) {
            Edge edge = railroad.get(i);
            int srcIndex = indexOfVertex.get(edge.src);
            int destIndex = indexOfVertex.get(edge.dest);
    
            
            if (set.find(srcIndex) != set.find(destIndex)) {
                
                minimumSpanningTree.add(edge);
                totalCost += edge.weight;
                set.union(srcIndex, destIndex);
            }
        }
    
        // Construct the result string
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < minimumSpanningTree.size(); i++) {
            Edge edge = minimumSpanningTree.get(i);
            result.append(edge.src).append("---").append(edge.dest).append("\t$").append(edge.weight).append("\n");
        }
        result.append("Total Cost: $").append(totalCost);
    
        return result.toString();
    }
}

