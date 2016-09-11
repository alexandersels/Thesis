package com.ugent.networkplanningtool.data.enums;

import android.content.Context;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The type of a Wall object
 */
public enum WallType {
    WALL("wall"),
    DOOR("door"),
    WINDOW("window");

    private String text;

    private static Map<String, WallType> textToWallMapping;

    private WallType(String type) {
        this.text = loadResources(type);
    }

    private static void initMapping() {
        textToWallMapping = new HashMap<String, WallType>();
        for (WallType s : values()) {
            textToWallMapping.put(s.getText(), s);
        }
    }

    public static WallType getWallTypeByText(String text) {
        if (textToWallMapping == null) {
            initMapping();
        }
        return textToWallMapping.get(text);
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
            case "wall":
                value = m.getResources().getString(R.string.wallText).toLowerCase();
                break;
            case "door":
                value = m.getResources().getString(R.string.doorText).toLowerCase();
                break;
            case "window":
                value = m.getResources().getString(R.string.windowText).toLowerCase();
                break;
        }
        return value;
    }

    public String getText() {
        return text;
    }
}
