package com.ugent.networkplanningtool.localisation.io;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.data.Node;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Alexander on 10/11/2015.
 * NodeReader will manage to load and read the available nodes on the floorplan.
 */
public class NodeReader {

    private ArrayList<String> nodes;
    private ArrayList<Node> allNodes;
    private String text;
    private Node node;

    public NodeReader() {
        nodes = new ArrayList<>();
        allNodes = new ArrayList<>();
        try {
            XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();
            InputStream is = LocalisationActivity.getInstance().getAssets().open("nodes.xml");
            parser.setInput(is, null);
            parseNodes(parser);
        } catch (IOException e) {
            System.out.println("file not found");
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            System.out.println("Problem in parsing");
            e.printStackTrace();
        }
    }

    /**
     * This method will read the nodes from an xml fromat and saves them.
     * @param parser the used XML-parser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void parseNodes(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase("node")) {
                        node = new Node();
                    }
                    break;
                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase("name")) {
                        node.setName(text);
                        nodes.add(text);
                    }
                    else if (tagname.equalsIgnoreCase("xcoord")) {
                        node.setX(Integer.parseInt(text));
                    }
                    else if (tagname.equalsIgnoreCase("ycoord")) {
                        node.setY(Integer.parseInt(text));
                    }else if (tagname.equalsIgnoreCase("node")) {
                        allNodes.add(node);
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
    }

    /**
     * Return the list of all node names.
     * @return the list of node names.
     */
    public ArrayList<String> getNodes() {
        return nodes;
    }

    /**
     * Returns the list of all node objects.
     * @return the list of objects.
     */
    public ArrayList<Node> getNodeObjects() {
        return allNodes;
    }
}
