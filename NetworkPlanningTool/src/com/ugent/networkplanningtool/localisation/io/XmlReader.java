package com.ugent.networkplanningtool.localisation.io;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.data.Location;
import com.ugent.networkplanningtool.data.enums.LocationType;
import com.ugent.networkplanningtool.localisation.Fingerprint;
import com.ugent.networkplanningtool.localisation.Measure;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Alexander on 20/11/2015.
 * Class used to write XML formats.
 */
public class XmlReader {

    public XmlReader() {

    }

    /**
     * Read all measures from a given log file.
     * @param file the file containing the measures.
     * @return the list of measures.
     */
    public List<Measure> readMeasuresFromLogXml(File file) {
        List<Measure> results = new ArrayList<Measure>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);


            NodeList measures = document.getElementsByTagName("measure");
            for ( int i = 0; i < measures.getLength(); i++) {
                Node measure = measures.item(i);
                if (measure.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) measure;

                    int x1 = Integer.parseInt(element.getElementsByTagName("marked_x").item(0).getTextContent());
                    int y1 = Integer.parseInt(element.getElementsByTagName("marked_y").item(0).getTextContent());

                    int x2 = Integer.parseInt(element.getElementsByTagName("found_x").item(0).getTextContent());
                    int y2 = Integer.parseInt(element.getElementsByTagName("found_y").item(0).getTextContent());

                    Location current = new Location(LocationType.CURRENT, x2,y2);
                    Location marked = new Location(LocationType.MARKED,x1,y1);
                    Measure result = new Measure(current,marked);

                    results.add(result);
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong in printing: " + e);
        }
        return results;

    }


    /**
     * read the parameters matching a given measure file.
     * @param file the selected measure file.
     * @return the used parameters.
     */
    public HashMap<String, String> readParametersFromLogXml(File file) {

        HashMap<String, String> results = new HashMap<>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            NodeList parameters = document.getElementsByTagName("parameters");
            Node parameter = parameters.item(0);


            NodeList nodes = parameter.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                results.put(node.getNodeName(), node.getTextContent());
            }

        } catch (Exception e) {
            System.out.println("Something went wrong in printing: " + e);
        }
        return results;
    }


    /**
     * Read the fingerprints from the XML file.
     * @return the fingerprints.
     */
    public List<Fingerprint> readFingerprintsFromXml() {
        File file = new File(LocalisationActivity.path + "fingerprint/fingerprint.xml");
        List<Fingerprint> results = new ArrayList<Fingerprint>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            NodeList fingerprints = document.getElementsByTagName("fingerprint");
            for ( int i = 0; i < fingerprints.getLength(); i++) {
                Node selected = fingerprints.item(i);
                if (selected.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) selected;

                    Node markedX = element.getElementsByTagName("marked_x").item(0);
                    int x  = Integer.parseInt(markedX.getTextContent());

                    Node markedY = element.getElementsByTagName("marked_y").item(0);
                    int y = Integer.parseInt(markedY.getTextContent());

                    Node rssi = element.getElementsByTagName("rssi").item(0);
                    NodeList rssiList = rssi.getChildNodes();

                    List<Double> rssiValues = new ArrayList<>();
                    for (int j = 0; j < rssiList.getLength(); j++) {
                        rssiValues.add(Double.parseDouble(rssiList.item(j).getTextContent()));
                    }
                    results.add(new Fingerprint(x, y, rssiValues));
                }
            }
        } catch (Exception e) {
            System.out.println("Something went wrong in printing: " + e);
        }
        return results;

    }
}
