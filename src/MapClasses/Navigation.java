package MapClasses;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Navigation {
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

    HashMap<Integer, LinkedList<Coordinate>> waypointMap = new HashMap<>();
    HashMap<Integer, Node> nodeMap = new HashMap<>();

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
                //nodes.get(firstNodeID).connections.add(new Connection(linkID, firstNodeID, lastNodeID, length));
                if (way == 2) {
                    //nodes.get(lastNodeID).connections.add(new Connection(-linkID, lastNodeID, firstNodeID, length));
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

    public void drawGraph(Graphics2D pen, int width, int height){
        double maxlat = -99999999;
        double maxlong = -99999999;
        double minlat = 99999999;
        double minlong = 99999999;
        double scalex;
        double scaley;

        for(LinkedList<Coordinate> assList : waypointMap.values()){
            for(Coordinate asshole : assList){
                if(maxlat < asshole.latitude){
                    maxlat = asshole.latitude;
                }
                if(maxlong < asshole.longitude){
                    maxlong = asshole.longitude;
                }
                if(minlat > asshole.latitude){
                    minlat = asshole.latitude;
                }
                if(minlong > asshole.longitude){
                    minlong = asshole.longitude;
                }
            }
        }

        scalex = width/(maxlong-minlong);
        scaley = height/(maxlat-minlat);

        for(LinkedList<Coordinate> assList : waypointMap.values()){
            for(int i = 0; i < assList.size() - 1; i++){
                int x1 = convertCoords(assList.get(i).longitude, scalex, minlong, width);
                int x2 = convertCoords(assList.get(i+1).longitude, scalex, minlong, width);
                int y1 = convertCoords(assList.get(i).latitude, scaley, minlat, height);
                int y2 = convertCoords(assList.get(i+1).latitude, scaley, minlat, height);
                pen.drawLine(x1, y1, x2, y2);
            }
        }
        //pen.drawLine(0, 0, 169, 420);
    }

    public int convertCoords(double coordinate, double scale, double min, double dimensions){
        return (int) (dimensions - ((coordinate - min) * scale));
    }

    class Node {
        ArrayList<Path> nodeConnections = new ArrayList<>();
        int nodeID;
        double nodeLong;
        double nodeLat;

        public Node(int nodeID, double nodeLong, double nodeLat) {
            this.nodeID = nodeID;
            this.nodeLong = nodeLong;
            this.nodeLat = nodeLat;
        }
    }

    class Path {
        double length;
        Node startNode;
        Node endNode;

        Path(double length, Node start, Node end) {
            this.length = length;
            this.startNode = start;
            this.endNode = end;
        }
    }

    public void addPoint(int x, int y, int height, int width) {
        Node closestNode;

    }
}
