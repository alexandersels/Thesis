package com.ugent.networkplanningtool.data.enums.parameters;

import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Types of receivers
 */
public enum Receiver {
    REF_RECEIVER_2400(PlannerActivity.getInstance().getResources().getString(R.string.receiver2400Text), new String[]{PlannerActivity.getInstance().getResources().getString(R.string.wifiText)}),
    CC2420(PlannerActivity.getInstance().getResources().getString(R.string.cc2420Text), new String[]{PlannerActivity.getInstance().getResources().getString(R.string.sensorText)}),
    JN516x(PlannerActivity.getInstance().getResources().getString(R.string.jn516xtext), new String[]{PlannerActivity.getInstance().getResources().getString(R.string.sensorText)}),
    N80211(PlannerActivity.getInstance().getResources().getString(R.string.rec802_11n), new String[]{PlannerActivity.getInstance().getResources().getString(R.string.wifiText)}),
    PHONE3G(PlannerActivity.getInstance().getResources().getString(R.string.phone3G), new String[]{PlannerActivity.getInstance().getResources().getString(R.string.wifiText), PlannerActivity.getInstance().getResources().getString(R.string.g3Text)}),
    PHONE4G(PlannerActivity.getInstance().getResources().getString(R.string.phone4G), new String[]{PlannerActivity.getInstance().getResources().getString(R.string.wifiText), PlannerActivity.getInstance().getResources().getString(R.string.g4Text)});

    private String text;
    private final String[] types;

    private static Map<String, Receiver> textToReceiverMapping;

    private Receiver(String text, String[] types) {
        this.text = text;
        this.types = types;
    }

    private static void initMapping() {
        textToReceiverMapping = new HashMap<String, Receiver>();
        for (Receiver s : values()) {
            textToReceiverMapping.put(s.getText(), s);
        }
    }

    public static Receiver getReceiverByText(String text) {
        if (textToReceiverMapping == null) {
            initMapping();
        }
        return textToReceiverMapping.get(text);
    }

    public String getText() {
        return text;
    }

    public String[] getTypes() {
        return types;
    }

    @Override
    public String toString() {
        return text;
    }
}
