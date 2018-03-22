package com.lightningbolt.adamas;

import org.json.simple.JSONObject;
import com.lightningbolt.adamas.Media;

public class Rendition extends Media {
	private static final long serialVersionUID = 1L;

	public Rendition(JSONObject pValues) {
		setValues(pValues);
	}
	
	public String embed_code() { return (String) values.get("embed_code"); }
	public String renditionType() { return (String)metadata().get("ooyala-rendition-type"); }
	public String state() { return (String)metadata().get("ooyala-state"); }
	public boolean isPublished() { return (state().equals("Published")?true:false); }
	public String playerId() { return (String)metadata().get("player-id");}
	public String durationSeconds() { return (String)metadata().get("ooyala-duration-seconds"); }

}
