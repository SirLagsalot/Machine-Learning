
import java.util.ArrayList;
//TAN operates the following algorithm
//1. Create a Node for each attribute in trainingData
//2. Calculate weight between each Node to create a Graph
//3. Remove edges between nodes until you have the maximally spanning tree
//4. Arbitrarily choose a root node(always 0 in this case) and make the tree directed
//Normally from this point a Class Node is created and has an arc to each Node,
//this is not done hear as that relationship is always constant.
//When calculating the probability of a class, the equation 
//P(C|features) = P(class)*P(rootFeature | class) * MULT(P(nonRootFeature | class ^ pa(nonRootFeature))--for each nonRootFeature
//Note that for the TAN Model every nonRootFeature has EXACTLY 1 parent Node
//

public class TAN extends NaiveBayes {

    private Node root;

    public TAN(ArrayList<Instance> trainingData) {
        super(trainingData);
        //create the tree that's used as the basis for the TAN Baysian Net
        createTree();
    }

    @Override
    public int classify(ArrayList<Integer> featureVector) {

        int bestClass = 0;
        double bestProbability = 0;

        //Algorithm: every node will have 1 parent(we didn't add the class node) except the root. 
        //Calculate root probability seperately
        //compare probabilities for each class, and then take the best class.
        for (int i = 0; i < numOfClassifications; i++) {
            double probability = probabilityOfClass(i, featureVector);
            if (probability > bestProbability) {
                bestClass = i;
                bestProbability = probability;
            }
        }
        return bestClass;
    }

    private void createTree() {

        ArrayList<Node> nodes = new ArrayList<>();
        for (int i = 0; i < numberOfFeatures; i++) {
            Node node = new Node(i);
            nodes.add(node);
        }
        //Connect nodes with correlation weights
        connectNodes(nodes);

        //make maximum spanning tree
        makeMaximumSpanningTree(nodes);
        //choose root node and have all edges go away from it ie, go from graph to tree.

        directTree(nodes);
        //Add a reference to each node for it's parent
        addParents(nodes);
        //Store every combination of P(nodesFeatureValue | classValue ^ parentsFeatureValue) for each node
        createProbabilityCharts(nodes);
        //assuming directTree makes 0 the root node.
        root = nodes.get(0);
    }

    //create the probability chart for each node
    private void createProbabilityCharts(ArrayList<Node> nodes) {

        //do not do for root node as that only has class as a parent
        for (int i = 1; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            createProbabilityChart(node);
        }
    }

    private void createProbabilityChart(Node node) {

        Node parent = node.parent;
        int nodeRange = getDistinctValueCount(getColumn(node.attrPosition));
        int parentRange = getDistinctValueCount(getColumn(parent.attrPosition));
        //numOfClassifications already calculated.
        //probabilityChart is classification, probabilityChart[0] is node is parentValue, probabilityChart[][] is given value.
        //Example, probabilityChart[2][0][1] = P(node = 1 | class=2 ^ parentValue = 0)
        node.probabilityChart = new double[numOfClassifications][parentRange][nodeRange];
        //for the case there is no scenario of class and parentValue being equal, we will add 1 as a buffer.
        for (int i = 0; i < numOfClassifications; i++) {
            for (int j = 0; j < parentRange; j++) {
                for (int k = 0; k < nodeRange; k++) {
                    node.probabilityChart[i][j][k] = probabilityGivenClassAndFeature(i, j, k, getColumn(node.attrPosition), getColumn(parent.attrPosition));
                }
            }
        }

    }

    //Returns P(curNodeVal | class ^ parentNodeVal)
    private double probabilityGivenClassAndFeature(int curClass, int parentNodeVal, int curNodeVal, int[] nodeColumn, int[] parentColumn) {

        int totalCount = 1;//just start at 1 to offset chance of dividing by 0
        int partialCount = 1;//also want to avoid chance of straight out 0 probability
        for (int i = 0; i < classColumn.length; i++) {
            if (parentColumn[i] == parentNodeVal && classColumn[i] == curClass) {
                totalCount++;
                if (curNodeVal == nodeColumn[i]) {
                    partialCount++;
                }
            }
        }
        return partialCount / (double) totalCount;
    }

    //Adds reference to each nodes parent to the node
    //this is determined by viewing the edges 'parents'
    //have to the children, if such an edge exists to a 
    //child, add a reference to its parent.
    private void addParents(ArrayList<Node> nodes) {

        for (Node node : nodes) {
            for (Edge edge : node.edges) {
                edge.endNode.parent = node;
            }
        }
    }

    //Take the maximally spanning tree and direct it
    //by making the first node encountered the root
    private void directTree(ArrayList<Node> nodes) {

        //Always make the first node the root
        Node root = nodes.get(0);
        ArrayList<Node> toWork = new ArrayList<>();
        toWork.add(root);
        //while we have roots to direct outwords
        while (!toWork.isEmpty()) {
            Node curNode = toWork.get(0);
            for (Edge edge : curNode.edges) {
                removeEdgeWithStart(edge.endNode, edge.startNode);
                toWork.add(edge.endNode);
            }
            toWork.remove(curNode);
        }
    }

    //Removed an edge with from node to toRemove
    private void removeEdgeWithStart(Node node, Node toRemove) {

        for (Edge edge : node.edges) {
            if (edge.endNode == toRemove) {
                node.edges.remove(edge);
                return;
            }
        }
    }

    //Create edges between every pair of nodes
    //with correlation weight
    private void connectNodes(ArrayList<Node> nodes) {

        for (int i = 0; i < nodes.size(); i++) {
            Node node1 = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node node2 = nodes.get(j);
                //calculate the correlation weight between node i and j
                double weight = getWeight(i, j);
                //connect i and j
                Edge edge = new Edge(node1, node2, weight);
                node1.edges.add(edge);
                //connect j and i
                Edge edge2 = new Edge(node2, node1, weight);
                node2.edges.add(edge2);
            }
        }
    }

    //returns P(X=x ^ Y=y)
    private double getProbabilityXAndY(int[] xColumn, int[] yColumn, int xVal, int yVal) {

        int sum = 1;//start from one to avoid 0 probabilities
        for (int i = 0; i < xColumn.length; i++) {
            if (xColumn[i] == xVal && yColumn[i] == yVal) {
                sum++;
            }
        }
        return sum / (double) xColumn.length;
    }

    public double getProbabilityGivenClass(int[] featureColumn, int featureValue, int classValue) {

        ArrayList<Integer> limitedFeatureColumn = new ArrayList<>();

        //We take the values of the feature column that have a match in the class column for the desired class value
        for (int i = 0; i < classColumn.length; i++) {
            if (classColumn[i] == classValue) {
                limitedFeatureColumn.add(featureColumn[i]);
            }
        }

        //Take the sum of the items in the limitedFeatureColumn and divide by the total size for the probability
        int match = 0;
        for (int feature : limitedFeatureColumn) {
            if (feature == featureValue) {
                match++;
            }
        }
        return match > 0 ? match / (double) limitedFeatureColumn.size() : 1;
    }
    
    public double getProbabilityGivenClass(int[] featureColumn, int[] featureColumn2, int featureValue, int featureValue2, int classValue) {

        ArrayList<Integer> limitedFeatureColumn = new ArrayList<>();
        ArrayList<Integer> limitedFeatureColumn2 = new ArrayList<>();

        //We take the values of the feature column that have a match in the class column for the desired class value
        for (int i = 0; i < classColumn.length; i++) {
            if (classColumn[i] == classValue) {
                limitedFeatureColumn.add(featureColumn[i]);
                limitedFeatureColumn2.add(featureColumn2[i]);
            }
        }

        //Take the sum of the items in the limitedFeatureColumn and divide by the total size for the probability
        int match = 0;
        for (int i = 0; i < limitedFeatureColumn.size(); i++) {
            int feature = limitedFeatureColumn.get(i);
            int feature2 = limitedFeatureColumn2.get(i);
            if (feature == featureValue && feature2 == featureValue2) {
                match++;
            }
        }
        return match > 0 ? match / (double) limitedFeatureColumn.size() : 1;
    }
    //returns the correlation weight between two features
    private double getWeight(int firstIndex, int secondIndex) {

        int[] feature1 = getColumn(firstIndex);
        int[] feature2 = getColumn(secondIndex);
        int feature1Range = getDistinctValueCount(feature1);
        int feature2Range = getDistinctValueCount(feature2);
        int classRange = numOfClassifications;

        double sum = 0;
        //We want sum_(x,y,c) P(x,y,c)*log(P(x,y|c)/(P(x|c)*P(y|c))
        for (int curFeature1 = 0; curFeature1 < feature1Range; curFeature1++) {
            for (int curFeature2 = 0; curFeature2 < feature2Range; curFeature2++) {
                for (int curClass = 0; curClass < classRange; curClass++) {
                    double probabilityXYZ = getProbabilityXYZ(feature1, feature2, curFeature1, curFeature2, curClass);
                    double probabilityXYGivenZ = getProbabilityGivenClass(feature1, feature2, curFeature1, curFeature2, curClass);
                    double probabilityXGivenZ = getProbabilityGivenClass(feature1, curFeature1, curClass);
                    double probabilityYGivenZ = getProbabilityGivenClass(feature2, curFeature2, curClass);
                    sum += probabilityXYZ * Math.log(probabilityXYGivenZ / (double) (probabilityXGivenZ * probabilityYGivenZ));
                }
            }
        }
        return sum;
    }

    //returns P(C=c|featureVector) via bayes rule
    //note that we ommit dividing by P(featureValue) at every point as this will
    //not add anything of value to the maximization equation as it's independent
    //of class chosen
    private double probabilityOfClass(int classValue, ArrayList<Integer> featureVector) {

        double probability = 1;
        //Algo: return P(c) * P(rootVal | c) * (for all other nodes)P(nodeVal|parent^c)
        probability *= probabilityOfClassValue(classValue);
        probability *= getProbabilityGivenClass(getColumn(root.attrPosition), featureVector.get(root.attrPosition), classValue);
        probability *= getChildrenProbabilities(root, classValue, featureVector);
        return probability;
    }

    //returns the multiplied probability of every child node of the given node
    private double getChildrenProbabilities(Node node, int classValue, ArrayList<Integer> featureVector) {

        //returns P(node value | parentValue ^ c) for all children nodes of node
        double probability = 1;
        for (Edge edge : node.edges) {

            probability *= getNodeProbability(edge.endNode, classValue, featureVector.get(node.attrPosition), featureVector.get(edge.endNode.attrPosition));//edge.endNode.probabilityChart[classValue][featureVector.get(node.attrPosition)][featureVector.get(edge.endNode.attrPosition)];
            probability *= getChildrenProbabilities(edge.endNode, classValue, featureVector);
        }
        return probability;
    }

    //returns P(Node=value | Class=classValue ^ Pa=parentValue)
    private double getNodeProbability(Node node, int classValue, int parentValue, int value) {

        double probability = 1;
        //Stay at 1 if there isn't an entry for the given value, it'll ignore the impact of that attribute as no other chart should have that value, thus we're using 1 across the board regardless of which class we try
        int length1 = node.probabilityChart[0].length;
        int length2 = node.probabilityChart[0][0].length;
        if (!(parentValue >= length1 || value >= length2)) {
            probability = node.probabilityChart[classValue][parentValue][value];
        }
        return probability;
    }

    //returns the probability of x and y and z from their respective columns. Sum of all matching/whole
    private double getProbabilityXYZ(int[] feature1, int[] feature2, int curFeature1, int curFeature2, int curClass) {

        //Want to avoid 0 probability so start with a count of 1
        int count = 0;
        for (int i = 0; i < feature1.length; i++) {
            if (feature1[i] == curFeature1 && feature2[i] == curFeature2 && classColumn[i] == curClass) {
                count++;
            }
        }
        return count / (double) feature1.length;
    }

    //Use Prim's algorithm to create a maximally spanning tree
    private void makeMaximumSpanningTree(ArrayList<Node> nodes) {

        ArrayList<Node> visited = new ArrayList<>();
        Node startNode = nodes.get(0);
        ArrayList<Edge> edgesToWorkWith = (ArrayList<Edge>) startNode.edges.clone();
        visited.add(startNode);

        while (!edgesToWorkWith.isEmpty()) {
            Edge largestEdge = getLargestEdge(edgesToWorkWith);
            edgesToWorkWith.remove(largestEdge);
            if (!visited.contains(largestEdge.endNode)) {
                visited.add(largestEdge.endNode);
                edgesToWorkWith.addAll((ArrayList<Edge>) largestEdge.endNode.edges.clone());
            } else {
                //remove edge from it's startNode, algo will handle endNode
                largestEdge.startNode.edges.remove(largestEdge);
            }
        }
    }

    //returns the edge with the largest weight in the list
    private Edge getLargestEdge(ArrayList<Edge> edges) {

        Edge largestEdge = edges.get(0);
        for (Edge edge : edges) {
            if (edge.weight > largestEdge.weight) {
                largestEdge = edge;
            }
        }
        return largestEdge;
    }

    class Node {

        int attrPosition;
        ArrayList<Edge> edges = new ArrayList<>();
        Node parent;
        double[][][] probabilityChart;
        //probabilityChart[class][parentval][nodeVal] returns P(nodeVal | parentVal^class)

        public Node(int position) {
            attrPosition = position;
        }
    }

    class Edge {

        Node startNode, endNode;
        double weight;

        public Edge(Node startNode, Node endNode, double weight) {
            this.startNode = startNode;
            this.endNode = endNode;
            this.weight = weight;
        }
    }
}
