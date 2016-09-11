package com.ugent.networkplanningtool.localisation.tasks;

import com.ugent.networkplanningtool.io.AbstractASyncTask;
import com.ugent.networkplanningtool.localisation.Measure;
import com.ugent.networkplanningtool.localisation.io.FilePrinter;
import com.ugent.networkplanningtool.localisation.io.XmlReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Alexander on 21/11/2015.
 * ASync task to convert the log in the background.
 */
public class ConvertLogTask extends AbstractASyncTask<File, File> {

    @Override
    protected File performTaskInBackground(File parameter) throws Exception {

        String fileName = parameter.getName();
        String path = parameter.getPath();

        String name = fileName.substring(0, fileName.lastIndexOf('.'));
        String currentPath = path.substring(0, path.lastIndexOf('/'));

        XmlReader reader = new XmlReader();

        HashMap<String, String> parameters = reader.readParametersFromLogXml(parameter);
        String output1 = "Parameters:\n" +
                         "\n" +
                         "Fingerprint:\t"     + parameters.get("fingerprint") + "\n" +
                         "Calibration:\t"     + parameters.get("calibration") + "\n" +
                         "Amount of scans:\t" + parameters.get("scans")       + "\n" +
                         "Use Threshold:\t"   + parameters.get("threshold")   + "\n" +
                         "Theshold value:\t"  + parameters.get("value")       + "\n" +
                         "\n";

        List<Measure> measures = reader.readMeasuresFromLogXml(parameter);
        String output2 = "foundX\tfoundY\tmarkedX\tmarkedY\n";
        for (Measure measure: measures) {
            int foundX  = measure.getCurrent().getPoint().x;
            int foundY  = measure.getCurrent().getPoint().y;
            int markedX = measure.getMarked().getPoint().x;
            int markedY = measure.getMarked().getPoint().y;;
            output2 = output2 + foundX +"\t" + foundY + "\t" + markedX + "\t" + markedY + "\n";
        }


        File file = new File(currentPath+"/"+name+".txt");
        FilePrinter printer = new FilePrinter();
        printer.printToFile(file,output1 + output2);

        return file;
    }

}
