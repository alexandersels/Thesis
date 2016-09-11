package com.ugent.networkplanningtool.data.enums;

import android.content.Context;
import android.graphics.Color;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Represent networks
 */
public enum Network {
    NETWORK_A("a", Color.rgb(115, 128, 190)),
    NETWORK_B("b", Color.rgb(0, 128, 190)),
    NETWORK_C("c", Color.rgb(115, 0, 190)),
    NETWORK_D("d", Color.rgb(115, 128, 0)),
    NETWORK_E("e", Color.rgb(255, 128, 190)),
    NETWORK_F("f", Color.rgb(115, 255, 190)),
    NETWORK_G("g", Color.rgb(115, 128, 255)),
    NETWORK_H("h", Color.rgb(255, 128, 0));

    private String text;
    private int color;

    private static Map<String, Network> textToNetworkMapping;

    private Network(String type, int color) {
        this.text = loadResources(type);
        this.color = color;
    }

    private static void initMapping() {
        textToNetworkMapping = new HashMap<String, Network>();
        for (Network s : values()) {
            textToNetworkMapping.put(s.getText(), s);
        }
    }

    public static Network getNetworkByText(String text) {
        if (textToNetworkMapping == null) {
            initMapping();
        }
        return textToNetworkMapping.get(text);
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString(){
        return text;
    }

    private String loadResources(String type) {
        String resource = "";
        if (PlannerActivity.getInstance() != null) {
            resource = fetchValue(PlannerActivity.getInstance(),type);
        }
        else if (LocalisationActivity.getInstance() != null) {
            resource = fetchValue(LocalisationActivity.getInstance(),type);
        }
        return resource;
    }

    private String fetchValue(Context m, String source) {
        String value = "";
        switch(source) {
            case "a":
                value = m.getResources().getString(R.string.networkAText);
                break;
            case "b":
                value = m.getResources().getString(R.string.networkBText);
                break;
            case "c":
                value = m.getResources().getString(R.string.networkCText);
                break;
            case "d":
                value = m.getResources().getString(R.string.networkDText);
                break;
            case "e":
                value = m.getResources().getString(R.string.networkEText);
                break;
            case "f":
                value = m.getResources().getString(R.string.networkFText);
                break;
            case "g":
                value = m.getResources().getString(R.string.networkGText);
                break;
            case "h":
                value = m.getResources().getString(R.string.networkHText);
                break;
        }
        return value;
    }
}
