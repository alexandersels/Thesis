package com.ugent.networkplanningtool.localisation.tasks;

import com.ugent.networkplanningtool.io.AbstractASyncTask;
import com.ugent.networkplanningtool.localisation.Measure;
import com.ugent.networkplanningtool.localisation.io.XmlReader;

import java.io.File;
import java.util.List;

/**
 * Created by Alexander on 20/11/2015.
 * ASync task to load the created measures.
 */
public class LoadLogTask extends AbstractASyncTask<File, List<Measure>> {
    @Override
    protected List<Measure> performTaskInBackground(File parameter) throws Exception {
        XmlReader reader = new XmlReader();
        return reader.readMeasuresFromLogXml(parameter);
    }
}
