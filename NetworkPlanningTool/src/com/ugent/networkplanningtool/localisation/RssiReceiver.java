package com.ugent.networkplanningtool.localisation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.model.LocalisationModel;

import java.util.List;

/**
 * Created by Alexander on 9-11-2015.
 * The RSSIReceiver will be used to receive RSSI-values from the WifiManager.
 */
public class RssiReceiver extends BroadcastReceiver {

    private WifiManager mgr;
    private LocalisationModel model;
    private int counter = 1;

    public RssiReceiver(WifiManager mgr) {
        this.mgr = mgr;
        this.model = LocalisationModel.INSTANCE;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        List<ScanResult> results = mgr.getScanResults();
        model.fillRssiMap(results);
        if (model.isLocalisationMode()) {
            if (counter < model.getMaxScans()) {
                counter++;
                mgr.startScan();
            } else {
                LocalisationActivity.getInstance().unregisterReceiver(this);
                model.calculateLocation();
                counter = 1;
            }
        } else if (model.isFingerPrintMode()) {
            LocalisationActivity.getInstance().unregisterReceiver(this);
            model.doFingerprint();
        }
    }
}
