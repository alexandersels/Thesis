package com.ugent.networkplanningtool.data.enums;

import android.content.Context;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The model of the radio device
 */
public enum RadioModel {
    DLINK("dlink"),
    CUSTOM("custom"),
    CC2420("cc2420"),
    JN516x("jn516x"),
    HUAWEI("huawei");

    private String text;

    private static Map<String, RadioModel> textToRadioModelMapping;

    private RadioModel(String type) {
        this.text = loadResources(type);
    }

    private static void initMapping() {
        textToRadioModelMapping = new HashMap<String, RadioModel>();
        for (RadioModel s : values()) {
            textToRadioModelMapping.put(s.getText().toLowerCase(), s);
        }
    }

    public static RadioModel getRadioModelByText(String text) {
        if (textToRadioModelMapping == null) {
            initMapping();
        }
        return textToRadioModelMapping.get(text.toLowerCase());
    }

    public String getText() {
        return text;
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
            case "dlink":
                value = m.getResources().getString(R.string.dlinkText);
                break;
            case "custom":
                value = m.getResources().getString(R.string.customText);
                break;
            case "cc2420":
                value = m.getResources().getString(R.string.cc2420Text);
                break;
            case "jn516x":
                value = m.getResources().getString(R.string.jn516xtext);
                break;
            case "huawei":
                value = m.getResources().getString(R.string.huaweiText);
                break;
        }
        return value;
    }
}
