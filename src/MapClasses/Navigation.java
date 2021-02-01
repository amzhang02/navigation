package MapClasses;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Navigation {
    HashMap<Integer, LinkedList<Coordinate>> waypointMap = new HashMap<>();
    HashMap<Integer, Node> nodeMap = new HashMap<>();
    double scalex;
    double scaley;
    double maxlat;
    double maxlong;
    double minlat;
    double minlong;
    Node startNode;
    Node endNode;

    public Navigation() {
        readFiles("Catlin1-allroads");
    }

    class Coordinate {
        double latitude;
        double longitude;

        public Coordinate(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public void readFiles(String folder) {
        clear();

        // Read nodes
        try {
            String nodesFileName = folder + "/nodes.bin";
            DataInputStream inStream = new DataInputStream(new BufferedInputStream(new FileInputStream(nodesFileName)));
            int numNodes = inStream.readInt();
            for (int i=0; i<numNodes; i++) {
                int nodeID = inStream.readInt();
                double longitude = inStream.readDouble();
                double latitude = inStream.readDouble();
                Node node = new Node(nodeID, longitude, latitude);
                nodeMap.put(nodeID, node);
            }
            inStream.close();
        }
        catch (IOException e) {
            System.err.println("error on nodes.bin: " + e.getMessage());
        }

// Read links
        int numLinks = 0;
        try {
            String nodesFileName = folder + "/links.bin";
            DataInputStream inStream = new DataInputStream(new BufferedInputStream(new FileInputStream(nodesFileName)));
            numLinks = inStream.readInt();
            for (int i=0; i<numLinks; i++) {
                int linkID = inStream.readInt();
                int firstNodeID = inStream.readInt();
                int lastNodeID = inStream.readInt();
                String linkLabel = inStream.readUTF();
                double length = inStream.readDouble();
                byte way = inStream.readByte();
                nodeMap.get(firstNodeID).nodeConnections.add(new Path(length, nodeMap.get(firstNodeID), nodeMap.get(lastNodeID)));
                if (way == 2) {
                    nodeMap.get(lastNodeID).nodeConnections.add(new Path(length, nodeMap.get(firstNodeID), nodeMap.get(lastNodeID)));
                }
            }
            inStream.close();
        }
        catch (IOException e) {
            System.err.println("error on links.bin: " + e.getMessage());
        }

// Read waypoints
        try {
            String nodesFileName = folder + "/links-waypoints.bin";
            DataInputStream inStream = new DataInputStream(new BufferedInputStream(new FileInputStream(nodesFileName)));
            for(int i=0; i < numLinks; i++){
                int linkID = inStream.readInt();
                int numWaypoints = inStream.readInt();
                waypointMap.put(linkID, new LinkedList<>());
                for(int count = 0; count < numWaypoints; count += 1){
                    double waypointlong = inStream.readDouble();
                    double waypointlat = inStream.readDouble();
                    waypointMap.get(linkID).add(new Coordinate(waypointlat, waypointlong));
                }
            }
            inStream.close();
        }
        catch (IOException e) {
            System.err.println("error on waypoints.bin: " + e.getMessage());
        }
    }

    private void clear() {
    }

    public void scaling(int width, int height){
        maxlat = -99999999;
        maxlong = -99999999;
        minlat = 99999999;
        minlong = 99999999;
        for(LinkedList<Coordinate> waypointLinkedList : waypointMap.values()){
            for(Coordinate coordinate : waypointLinkedList){
                if(maxlat < coordinate.latitude){
                    maxlat = coordinate.latitude;
                }
                if(maxlong < coordinate.longitude){
                    maxlong = coordinate.longitude;
                }
                if(minlat > coordinate.latitude){
                    minlat = coordinate.latitude;
                }
                if(minlong > coordinate.longitude){
                    minlong = coordinate.longitude;
                }
            }
        }

        scalex = width/(maxlong-minlong);
        scaley = height/(maxlat-minlat);
    }
    public void drawGraph(Graphics2D pen, int width, int height){
        scaling(width, height);

        for(LinkedList<Coordinate> waypointLinkedList : waypointMap.values()){
            for(int i = 0; i < waypointLinkedList.size() - 1; i++){
                int x1 = convertCoords(waypointLinkedList.get(i).longitude, scalex, minlong, width);
                int x2 = convertCoords(waypointLinkedList.get(i+1).longitude, scalex, minlong, width);
                int y1 = convertCoords(waypointLinkedList.get(i).latitude, scaley, minlat, height);
                int y2 = convertCoords(waypointLinkedList.get(i+1).latitude, scaley, minlat, height);
                pen.drawLine(x1, y1, x2, y2);
            }
        }

        if(startNode != null){
            pen.setColor(Color.red);
            pen.drawOval(convertCoords(startNode.nodeLong, scalex, minlong, width) - 5,
                    convertCoords(startNode.nodeLat, scaley, minlat, height) - 5, 10, 10);
        }

        if(endNode != null){
            Node currentNode = endNode;
            System.out.println(currentNode.prev);
            while(currentNode.prev != null){
                pen.setColor(Color.blue);
                pen.drawOval(convertCoords(currentNode.nodeLong, scalex, minlong, width) - 5,
                        convertCoords(currentNode.nodeLat, scaley, minlat, height) - 5, 10, 10);
                currentNode = currentNode.prev;
            }
            pen.setColor(Color.green);
            pen.drawOval(convertCoords(endNode.nodeLong, scalex, minlong, width) - 5,
                    convertCoords(endNode.nodeLat, scaley, minlat, height) - 5, 10, 10);
        }
    }

    public int convertCoords(double coordinate, double scale, double min, double dimensions){
        return (int) (dimensions - ((coordinate - min) * scale));
    }

    class Node {
        ArrayList<Path> nodeConnections = new ArrayList<>();
        int nodeID;
        double nodeLong;
        double nodeLat;
        double distance;
        boolean visited;
        Node prev;

        public Node(int nodeID, double nodeLong, double nodeLat) {
            this.nodeID = nodeID;
            this.nodeLong = nodeLong;
            this.nodeLat = nodeLat;
        }
    }

    class Path {
        double length;
        Node from;
        Node to;

        Path(double length, Node start, Node end) {
            this.length = length;
            this.from = start;
            this.to = end;
        }
    }

    public void addPoint(int x, int y, int height, int width) {
        Node closestNode = null;
        double minDistance = 999999999;
        double currentDistance;
        scaling(width, height);
        for(Node node : nodeMap.values()) {
            currentDistance = Math.sqrt(Math.pow((x - convertCoords(node.nodeLong, scalex, minlong, width)), 2) +
                    Math.pow((y - convertCoords(node.nodeLat, scaley, minlat, height)), 2));
            if(currentDistance < minDistance) {
                minDistance = currentDistance;
                closestNode = node;
            }
        }

        if(startNode != null){
            endNode = closestNode;
        }
        else{
            startNode = closestNode;
        }
    }

    public void findPath(){
        for(Node node : nodeMap.values()){
            node.distance = Double.POSITIVE_INFINITY;
            node.visited = false;
        }
        startNode.distance = 0;
        startNode.visited = true;

        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingDouble(node -> node.distance));
        for(Path path : startNode.nodeConnections){
            path.to.distance = path.length;
            path.to.prev = startNode;
            frontier.add(path.to);
        }
        Node currentNode = null;
        while(!frontier.isEmpty()){
            System.out.println(frontier.size());
            currentNode = frontier.poll();
            currentNode.visited = true;
            if(currentNode == endNode){
                System.out.println("endNode found");
                break;
            }
            for(Path path : currentNode.nodeConnections){
                if(path.to.distance > path.length + currentNode.distance){
                    path.to.distance = path.length + currentNode.distance;
                    path.to.prev = currentNode;
                }
                if(!path.to.visited && !frontier.contains(path.to)){
                    frontier.add(path.to);
                }
            }
        }
        System.out.println(endNode.prev);
    }
}
