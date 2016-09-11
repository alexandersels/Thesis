package com.ugent.networkplanningtool.data.enums.parameters;

import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains the different path loss models.
 */
public enum PathLossModel {

    WHIPP(PlannerActivity.getInstance().getResources().getString(R.string.whippText), "sidp"),
    TGN(PlannerActivity.getInstance().getResources().getString(R.string.tgnText), "tgn"),
    FREE_SPACE(PlannerActivity.getInstance().getResources().getString(R.string.freespaceText), "freespace"),
    MULTIWALL(PlannerActivity.getInstance().getResources().getString(R.string.multiwallText), "multiwall"),
    ONESLOPE(PlannerActivity.getInstance().getResources().getString(R.string.oneslopeText), "oneslope-40-1-2");

    private String name;
    private String value;

    private static Map<String, PathLossModel> nameToPathLossModelMapping;
    private static Map<String, PathLossModel> valueToPathLossModelMapping;

    private PathLossModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    private static void initMapping() {
        nameToPathLossModelMapping = new HashMap<String, PathLossModel>();
        valueToPathLossModelMapping = new HashMap<String, PathLossModel>();
        for (PathLossModel s : values()) {
            nameToPathLossModelMapping.put(s.getName(), s);
            valueToPathLossModelMapping.put(s.getValue(), s);
        }
    }

    public static PathLossModel getPathLossModelByName(String name) {
        if (nameToPathLossModelMapping == null) {
            initMapping();
        }
        return nameToPathLossModelMapping.get(name);
    }

    public static PathLossModel getPathLossModelByValue(String value) {
        if (valueToPathLossModelMapping == null) {
            initMapping();
        }
        return valueToPathLossModelMapping.get(value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
