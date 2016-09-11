package com.ugent.networkplanningtool.data.enums;

import android.content.Context;

import com.ugent.networkplanningtool.LocalisationActivity;
import com.ugent.networkplanningtool.PlannerActivity;
import com.ugent.networkplanningtool.R;

import java.util.HashMap;
import java.util.Map;

/**
 * The frequency band of an access point
 */
public enum FrequencyBand {
	FREQBAND_2100("2100"),
	FREQBAND_2400("2400"),
	FREQBAND_2600("2600");
	
	private String text;
	
	private static Map<String, FrequencyBand> textToFreqBandMapping;
	
	private FrequencyBand(String type){
		this.text = loadResources(type);
	}
	
	private static void initMapping() {
		textToFreqBandMapping = new HashMap<String, FrequencyBand>();
        for (FrequencyBand s : values()) {
        	textToFreqBandMapping.put(s.getText(), s);
        }
    }
	
	public static FrequencyBand getFrequencyBandByText(String text){
		if(textToFreqBandMapping == null){
			initMapping();
		}
		FrequencyBand fb = textToFreqBandMapping.get(text);
		return fb==null?FREQBAND_2400:fb;
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
			case "2100":
				value = m.getResources().getString(R.string.frequencyBand2100);
				break;
			case "2400":
				value = m.getResources().getString(R.string.frequencyBand2400);
				break;
			case "2600":
				value = m.getResources().getString(R.string.frequencyBand2600);
				break;
		}
		return value;
	}
	
	public String getText(){
		return text;
	}

    @Override
    public String toString() {
        return text;
    }
}
