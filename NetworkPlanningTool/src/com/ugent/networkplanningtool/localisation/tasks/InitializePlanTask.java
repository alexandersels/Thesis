package com.ugent.networkplanningtool.localisation.tasks;

import com.ugent.networkplanningtool.io.AbstractASyncTask;

import be.ugent.intec.wica.viterbi.Lokalisatie;

/**
 * Created by Alexander on 20/11/2015.
 * ASync task to initialize the floorplan.
 */
public class InitializePlanTask extends AbstractASyncTask<String, Boolean> {
    @Override
    protected Boolean performTaskInBackground(String path) throws Exception {
        String verdiepEnGrid = "V3_gridsize50DomP"; //"gridsize50"; //"gridsize100"; //"V2_gridsize50"; //"V2_gridsize100";
        Lokalisatie.importeerPlan(Lokalisatie.gridPoints, Lokalisatie.deurenGPs,
                Lokalisatie.middenDGPs,
                Lokalisatie.APs,
                path + "/basis/planGPS_" + verdiepEnGrid + ".txt",
                path + "/basis/planDGPS_" + verdiepEnGrid + ".txt",
                path + "/basis/planMDGPS_" + verdiepEnGrid + ".txt",
                path + "/basis/APs_" + verdiepEnGrid + ".txt");
        Lokalisatie.importeer3Lists(Lokalisatie.pathlossRef, path + "/basis/PLsSensor_" + verdiepEnGrid + ".txt", null);
        return true;
    }
}
