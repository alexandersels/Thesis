package com.ugent.networkplanningtool.localisation.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Alexander on 19/11/2015.
 * Class used to print the convert measures to a file.
 */
public class FilePrinter {

    public FilePrinter() {

    }

    /**
     * Print text to a given file.
     * @param file the given destination.
     * @param text the given text?
     */
    public void printToFile(File file, String text) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(text);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            System.out.println("Something went wrong in printing: " + e);
        }
    }
}
