package com.ugent.networkplanningtool.data.enums;

import android.content.Context;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Whether to snap to the grid or to a wall when drawing
 */
public enum SnapTo {
    GRID("grid"),
    WALLS("walls");
    private String text;

    private static Map<String, SnapTo> textToSnapToMapping;

    private SnapTo(String type) {
        this.text = loadResources(type);
    }

    private static void initMapping() {
        textToSnapToMapping = new HashMap<String, SnapTo>();
        for (SnapTo s : values()) {
            textToSnapToMapping.put(s.getText(), s);
        }
    }

    public static SnapTo getSnapToByText(String text) {
        if (textToSnapToMapping == null) {
            initMapping();
        }
        return textToSnapToMapping.get(text);
    }

    public String getText() {
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
            case "grid":
                value = m.getResources().getString(R.string.snapToGridText);
                break;
            case "walls":
                value = m.getResources().getString(R.string.snapToWallsText);
                break;
        }
        return value;
    }
}
