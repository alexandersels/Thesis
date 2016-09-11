package com.ugent.networkplanningtool.data.enums;

import android.content.Context;
import android.util.SparseArray;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The thickness of a Wall
 */
public enum Thickness {
    THICK("thick", 30),
    THIN("thin", 10);

    private String text;
    private int number;
    private static Map<String, Thickness> textToThicknessMapping;
    private static SparseArray<Thickness> numberToThicknessMapping;

    private Thickness(String type, int number) {
        this.text = loadResources(type);
        this.number = number;
    }

    private static void initMapping1() {
        textToThicknessMapping = new HashMap<String, Thickness>();
        for (Thickness s : values()) {
            textToThicknessMapping.put(s.getText(), s);
        }
    }

    public static Thickness getThicknessByText(String text) {
        if (textToThicknessMapping == null) {
            initMapping1();
        }
        return textToThicknessMapping.get(text);
    }

    private static void initMapping2() {
        numberToThicknessMapping = new SparseArray<Thickness>();
        for (Thickness s : values()) {
            numberToThicknessMapping.put(s.getNumber(), s);
        }
    }

    public static Thickness getThicknessByNumber(int number) {
        if (numberToThicknessMapping == null) {
            initMapping2();
        }
        return numberToThicknessMapping.get(number);
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
            case "thick":
                value = m.getResources().getString(R.string.thickText);
                break;
            case "thin":
                value = m.getResources().getString(R.string.thinText);
                break;
        }
        return value;
    }

    public String getText() {
        return text;
    }

    public int getNumber() {
        return number;
    }
}
