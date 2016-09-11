package com.ugent.networkplanningtool.data.enums;

import android.content.Context;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The type of an activity
 */
public enum ActivityType {
    HD_VIDEO("hd_video"),
    ACTION_GAMES("action_games"),
    YOUTUBE("youtube"),
    SURFING("surfing"),
    NO_COVERAGE("no_coverage");

    private String text;
    private int textSize = 0;

    private static Map<String, ActivityType> textToActivityTypeMapping;

    private ActivityType(String type) {
        this.text = loadResources(type);
    }

    private static void initMapping() {
        textToActivityTypeMapping = new HashMap<String, ActivityType>();
        for (ActivityType s : values()) {
            textToActivityTypeMapping.put(s.getText(), s);
        }
    }

    public static ActivityType getActivityTypeByText(String text) {
        if (textToActivityTypeMapping == null) {
            initMapping();
        }
        return textToActivityTypeMapping.get(text);
    }

    public String getText() {
        return text;
    }

    /**
     * @return the textSize
     */
    public int getTextSize() {
        return textSize;
    }

    /**
     * @param textSize the textSize to set
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
    public String toString() {
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
            case "hd_video":
                value = m.getResources().getString(R.string.hdVideotext);
                break;
            case "action_games":
                value = m.getResources().getString(R.string.actionGamesText);
                break;
            case "youtube":
                value = m.getResources().getString(R.string.youtubeText);
                break;
            case "surfing":
                value = m.getResources().getString(R.string.surfingText);
                break;
            case "no_coverage":
                value = m.getResources().getString(R.string.noCoverageText);
                break;
        }
        return value;
    }
}
