package com.ugent.networkplanningtool.data.enums;

import android.content.Context;
import android.graphics.Color;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The material of a Wall
 */
public enum Material {
    BRICK("brick", Color.rgb(221, 0, 0)),
    LAYERED_DRYWALL("layered_drywall", Color.rgb(176, 129, 64)),
    CONCRETE("concrete", Color.rgb(136, 136, 136)),
    WOOD("wood", Color.rgb(252, 206, 140)),
    GLASS("glass", Color.rgb(149, 165, 236)),
    METAL("metal", Color.rgb(102, 102, 153)),
    ZERO_DB("zero_db", Color.rgb(221, 221, 221));

    private static Material[] doorMaterials = new Material[]{GLASS, WOOD};
    private static Material[] wallMaterials = new Material[]{BRICK, LAYERED_DRYWALL, CONCRETE, WOOD, GLASS, METAL, ZERO_DB};
    private static Material[] windowMaterials = new Material[]{GLASS};

    private String text;
    private int color;

    private static Map<String, Material> textToMaterialMapping;

    private Material(String type, int color) {
        this.text = loadResources(type);
        this.color = color;
    }

    private static void initMapping() {
        textToMaterialMapping = new HashMap<String, Material>();
        for (Material s : values()) {
            textToMaterialMapping.put(s.getText(), s);
        }
    }

    public static Material getMaterialByText(String text) {
        if (textToMaterialMapping == null) {
            initMapping();
        }
        return textToMaterialMapping.get(text);
    }

    public int getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public static Material[] getMaterialsForWallType(WallType wt) {
        switch (wt) {
            case WALL:
                return wallMaterials;
            case DOOR:
                return doorMaterials;
            case WINDOW:
                return windowMaterials;
            default:
                return new Material[0];
        }
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
            case "brick":
                value = m.getResources().getString(R.string.brickText);
                break;
            case "layered_drywall":
                value = m.getResources().getString(R.string.layeredDrywallText);
                break;
            case "concrete":
                value = m.getResources().getString(R.string.concreteText);
                break;
            case "wood":
                value = m.getResources().getString(R.string.woodText);
                break;
            case "glass":
                value = m.getResources().getString(R.string.glassText);
                break;
            case "metal":
                value = m.getResources().getString(R.string.metalText);
                break;
            case "zero_db":
                value = m.getResources().getString(R.string.zeroDbText);
                break;
        }
        return value;
    }
}
