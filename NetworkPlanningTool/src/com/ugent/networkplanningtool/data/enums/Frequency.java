package com.ugent.networkplanningtool.data.enums;

import android.content.Context;
import android.util.SparseArray;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The frequency of an access point
 */
public enum Frequency {
    FREQ_1("1", 2412),
    FREQ_2("2", 2417),
    FREQ_3("3", 2422),
    FREQ_4("4", 2427),
    FREQ_5("5", 2432),
    FREQ_6("6", 2437),
    FREQ_7("7", 2442),
    FREQ_8("8", 2447),
    FREQ_9("9", 2452),
    FREQ_10("10", 2457),
    FREQ_11("11", 2462);

    private String text;
    private int number;

    private static Map<String, Frequency> textToFreqMapping;
    private static SparseArray<Frequency> numberToFreqMapping;

    private Frequency(String type, int number) {
        this.text = loadResources(type);
        this.number = number;
    }

    private static void initMapping() {
        textToFreqMapping = new HashMap<String, Frequency>();
        for (Frequency s : values()) {
            textToFreqMapping.put(s.getText(), s);
        }
    }

    public static Frequency getFrequencyByText(String text) {
        if (textToFreqMapping == null) {
            initMapping();
        }
        return textToFreqMapping.get(text);
    }

    private static void initMapping2() {
        numberToFreqMapping = new SparseArray<Frequency>();
        for (Frequency s : values()) {
            numberToFreqMapping.put(s.getNumber(), s);
        }
    }

    public static Frequency getFreqByNumber(int number) {
        if (numberToFreqMapping == null) {
            initMapping2();
        }
        return numberToFreqMapping.get(number);
    }

    public String getText() {
        return text;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return text+" ("+number+")";
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
            case "1":
                value = m.getResources().getString(R.string.frequency1);
                break;
            case "2":
                value = m.getResources().getString(R.string.frequency2);
                break;
            case "3":
                value = m.getResources().getString(R.string.frequency3);
                break;
            case "4":
                value = m.getResources().getString(R.string.frequency4);
                break;
            case "5":
                value = m.getResources().getString(R.string.frequency5);
                break;
            case "6":
                value = m.getResources().getString(R.string.frequency6);
                break;
            case "7":
                value = m.getResources().getString(R.string.frequency7);
                break;
            case "8":
                value = m.getResources().getString(R.string.frequency8);
                break;
            case "9":
                value = m.getResources().getString(R.string.frequency9);
                break;
            case "10":
                value = m.getResources().getString(R.string.frequency10);
                break;
            case "11":
                value = m.getResources().getString(R.string.frequency11);
                break;
        }
        return value;
    }
}
