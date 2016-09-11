package com.ugent.networkplanningtool.localisation.io;


import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.data.Location;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Alexander on 19/11/2015.
 * Class handling everything that has to be written to XML-files.
 */
public class XmlWriter {

    public XmlWriter() {

    }

    /**
     * Prints the measures to a selected file.
     * @param file the selected file.
     * @param currentLocation the location calculated by the algorithm.
     * @param markedLocation the location marked by the user.
     * @param parameters the parameters used to conduct the localisation.
     */
    public void printMeasuresToXmlFile(File file, Location currentLocation, Location markedLocation, HashMap<String, String> parameters) {

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document;
            if (!file.exists()) {
                document = documentBuilder.newDocument();
            } else {
                document = documentBuilder.parse(file);
            }

            Node root = document.getFirstChild();
            if (root == null) {
                root = document.createElement("measures");
                document.appendChild(root);

                Element parameter = document.createElement("parameters");

                for (String type : parameters.keySet()) {
                    Element temp = document.createElement(type);
                    temp.appendChild(document.createTextNode(parameters.get(type)));
                    parameter.appendChild(temp);
                }

                root.appendChild(parameter);
            }

            Element newMeasure = document.createElement("measure");

            Element markedX = document.createElement("marked_x");
            markedX.appendChild(document.createTextNode(markedLocation.getPoint().x + ""));
            newMeasure.appendChild(markedX);

            Element markedY = document.createElement("marked_y");
            markedY.appendChild(document.createTextNode(markedLocation.getPoint().y + ""));
            newMeasure.appendChild(markedY);

            Element foundX = document.createElement("found_x");
            foundX.appendChild(document.createTextNode(currentLocation.getPoint().x + ""));
            newMeasure.appendChild(foundX);

            Element foundY = document.createElement("found_y");
            foundY.appendChild(document.createTextNode(currentLocation.getPoint().y + ""));
            newMeasure.appendChild(foundY);

            root.appendChild(newMeasure);

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            System.out.println("Something went wrong in printing: " + e);
        }
    }

    /**
     * Print the fingerprint to a given file.
     * @param markedLocation the location marked by the user.
     * @param rssi the list of RSSI-values measured on that location.
     * @param nodes the lidt of node names matching the rssi values.
     */
    public void printFingerprintToXmlFile(Location markedLocation, List<Double> rssi, List<String> nodes) {

        try {
            File file = new File(LocalisationActivity.path + "/fingerprint/fingerprint.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document;
            if (!file.exists()) {
                document = documentBuilder.newDocument();
            } else {
                document = documentBuilder.parse(file);
            }

            Node root = document.getFirstChild();
            if (root == null) {
                root = document.createElement("fingerprints");
                document.appendChild(root);
            }

            Element newFingerprint = document.createElement("fingerprint");

            Element markedX = document.createElement("marked_x");
            markedX.appendChild(document.createTextNode(markedLocation.getPoint().x + ""));
            newFingerprint.appendChild(markedX);

            Element markedY = document.createElement("marked_y");
            markedY.appendChild(document.createTextNode(markedLocation.getPoint().y + ""));
            newFingerprint.appendChild(markedY);

            Element rssiValues = document.createElement("rssi");
            newFingerprint.appendChild(rssiValues);

            for ( int i = 0; i < rssi.size(); i++ ) {
                Element node = document.createElement(nodes.get(i));
                node.appendChild(document.createTextNode(rssi.get(i).toString()));
                rssiValues.appendChild(node);
            }

            root.appendChild(newFingerprint);

            DOMSource source = new DOMSource(document);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        } catch (Exception e) {
            System.out.println("Something went wrong in printing: " + e);
        }
    }
}
