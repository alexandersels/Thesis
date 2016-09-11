package com.ugent.networkplanningtool.data.enums;

import android.content.Context;
import android.graphics.Color;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The connection type of a ConnectionPoint (data or power)
 */
public enum ConnectionPointType {
    DATA("data", Color.rgb(114, 15, 24)),
    POWER("power", Color.rgb(116, 172, 36));

    private String text;
    private int color;

    private static Map<String, ConnectionPointType> textToConnectionPointTypeMapping;

    private ConnectionPointType(String type, int color) {
        this.text = loadResources(type);
        this.color = color;
    }

    private static void initMapping() {
        textToConnectionPointTypeMapping = new HashMap<String, ConnectionPointType>();
        for (ConnectionPointType s : values()) {
            textToConnectionPointTypeMapping.put(s.getText(), s);
        }
    }

    public static ConnectionPointType getConnectionPointTypeByText(String text) {
        if (textToConnectionPointTypeMapping == null) {
            initMapping();
        }
        return textToConnectionPointTypeMapping.get(text);
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
            case "data":
                value = m.getResources().getString(R.string.dataConnectionPointText);
                break;
            case "power":
                value = m.getResources().getString(R.string.powerConnectionPointText);
                break;
        }
        return value;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }
}
