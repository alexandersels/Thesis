package com.ugent.networkplanningtool.data.enums;

import android.content.Context;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The type of radio device
 */
public enum RadioType {
    WIFI("wifi", new FrequencyBand[]{FrequencyBand.FREQBAND_2400}),
    SENSOR("sensor", new FrequencyBand[]{FrequencyBand.FREQBAND_2400}),
    LTE_FEMTOCELL("lte", new FrequencyBand[]{FrequencyBand.FREQBAND_2600}),
    UMTS_FEMTOCELL("umts", new FrequencyBand[]{FrequencyBand.FREQBAND_2100});

    private String text;
    private FrequencyBand[] fbs;

    private static Map<String, RadioType> textToRadioTypeMapping;

    private RadioType(String type, FrequencyBand[] fbs) {
        this.text = loadResources(type);
        this.fbs = fbs;
    }
	
	private static void initMapping() {
		textToRadioTypeMapping = new HashMap<String, RadioType>();
        for (RadioType s : values()) {
        	textToRadioTypeMapping.put(s.getText().toLowerCase(), s);
        }
    }
	
	public static RadioType getRadioTypeByText(String text){
		if(textToRadioTypeMapping == null){
			initMapping();
		}
		return textToRadioTypeMapping.get(text.toLowerCase());
	}
	
	public String getText(){
		return text;
	}

    public FrequencyBand[] getFrequencyBands() {
        return fbs;
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
            case "wifi":
                value = m.getResources().getString(R.string.wifiText);
                break;
            case "sensor":
                value = m.getResources().getString(R.string.sensorText);
                break;
            case "lte":
                value = m.getResources().getString(R.string.lteFemtocellText);
                break;
            case "umts":
                value = m.getResources().getString(R.string.umtsFemtocellText);
                break;
        }
        return value;
    }
}
