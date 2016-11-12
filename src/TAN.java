
import java.util.ArrayList;

public class TAN extends NaiveBayes {


    public TAN(ArrayList<Instance> trainingData) {
        super(trainingData);
        createTree();
    }

    public void createTree(){
        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < numberOfFeatures; i++) {
            Node node = new Node(i);
        }
        connectNodes(nodes);
        
        //make maximum spanning tree
        makeMaximumSpanningTree(nodes);
        //choose root node and have all edges go away from it ie, go from graph to tree.
        //TODO
        directTree(nodes);
    }
    
    private void directTree(ArrayList<Node> nodes){
        //Always make the first node the root
        Node root = nodes.get(0);
        ArrayList<Node> toWork = new ArrayList<>();
        toWork.add(root);
        while(!toWork.isEmpty()){
            Node curNode = toWork.get(0);
            for (Edge edge : curNode.edges) {
                removeEdgeWithStart(edge.endNode, edge.startNode);
                toWork.add(edge.endNode);
            }
            toWork.remove(curNode);
        }
    }
    
    private void removeEdgeWithStart(Node node, Node toRemove){
        for (Edge edge: node.edges) {
            if(edge.endNode == toRemove){
                node.edges.remove(edge);
                return;
            }
        }
    }
    
    public void connectNodes(ArrayList<Node> nodes){
        for (int i = 0; i < nodes.size(); i++) {
            Node node1 = nodes.get(i);
            for (int j = i+1; j < nodes.size(); j++) {
                Node node2 = nodes.get(j);
                //calculate weight between node i and j
                double weight = getWeight(i,j);
                //connect i and j
                Edge edge = new Edge(node1,node2,weight);
                node1.edges.add(edge);
                //connect j and i
                Edge edge2 = new Edge(node2, node1, weight);
                node2.edges.add(edge2);
            }
        }
    }
    
    public double getWeight(int firstIndex, int secondIndex){
        int[] feature1 = getColumn(firstIndex);
        int[] feature2 = getColumn(secondIndex);
        int[] classValues = getColumn(data[0].length - 1);//assuming class is last column in data
        int feature1Range = getDistinctValueCount(feature1);
        int feature2Range = getDistinctValueCount(feature2);
        int classRange = getDistinctValueCount(classValues);
        
        double sum = 0;
        //We want sum_(x,y,c) P(x,y,c)*log(P(x,y|c)/(P(x|c)*P(y|c))
        for (int curFeature1 = 0; curFeature1 < feature1Range; curFeature1++) {
            for (int curFeature2 = 0; curFeature2 < feature2Range; curFeature2++) {
                for (int curClass = 0; curClass < classRange; curClass++) {
                    double probabilityXYZ = getProbabilityXYZ(feature1,feature2,classValues,curFeature1,curFeature2,curClass);
                    double probabilityXYGivenZ = getProbabilityGivenClass(feature1,feature2,classValues,curFeature1,curFeature2,curClass);
                    double probabilityXGivenZ = getProbabilityGivenClass(feature1,classValues,curFeature1,curClass);
                    double probabilityYGivenZ = getProbabilityGivenClass(feature2,classValues,curFeature2,curClass);
                    sum += probabilityXYZ*Math.log(probabilityXYGivenZ/(probabilityXGivenZ*probabilityYGivenZ));
                }
            }
        }
        
        return sum;
    }

    @Override
    public int classify(ArrayList<Integer> featureVector) {
        //Todo
        return -1;
    }

    @Override
    public void train(ArrayList<Instance> trainingSet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //returns the probability of x and y and z from their respective columns. Sum of all matching/whole
    private double getProbabilityXYZ(int[] feature1, int[] feature2, int[] classValues, int curFeature1, int curFeature2, int curClass) {
        int count = 0;
        for (int i = 0; i < feature1.length; i++) {
            if(feature1[i] == curFeature1 && feature2[i] == curFeature2 && classValues[i]==curClass)
                count++;
        }
        return count/feature1.length;
    }

    //this will use prims algorithm
    private void makeMaximumSpanningTree(ArrayList<Node> nodes) {
        ArrayList<Node> visited = new ArrayList<>();
        Node startNode = nodes.get(0);
        ArrayList<Edge> edgesToWorkWith = startNode.edges;
        while(!edgesToWorkWith.isEmpty()){
            Edge largestEdge = getLargestEdge(edgesToWorkWith);
            edgesToWorkWith.remove(largestEdge);
            if(!visited.contains(largestEdge.endNode)){
                visited.add(largestEdge.endNode);
                edgesToWorkWith.addAll(largestEdge.endNode.edges);
            }
            else{
                //remove edge from it's startNode, algo will handle endNode
                largestEdge.startNode.edges.remove(largestEdge);
            }
        }
        
        
    }
    
    private Edge getLargestEdge(ArrayList<Edge> edges){
        Edge largestEdge = edges.get(0);
        for (Edge edge : edges) {
            if(edge.weight > largestEdge.weight)
                largestEdge=edge;
        }
        return largestEdge;
    }
    
    private class Node{
        int attrPosition;
        ArrayList<Edge> edges = new ArrayList<>();
        public Node(int position){
            attrPosition = position;
        }
        
    }
    private class Edge{
        Node startNode;
        Node endNode;
        double weight;
        public Edge(Node startNode, Node endNode, double weight){
            this.startNode = startNode;
            this.endNode = endNode;
            this.weight = weight;
        }
    }
}
