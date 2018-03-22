package com.lightningbolt.adamas;

import org.json.simple.JSONObject;

import com.lightningbolt.adamas.Media;

public class Image extends Media {
	private static final long serialVersionUID = 1L;
	protected boolean isPrimaryImage;

	public Image(JSONObject pValues) {
		setValues(pValues);
	}

	public boolean isMovieScreen() {
		return (name().equals("MovieScreen.jpeg")) ? true : false;
	}

	// Accessors
	public String id() { return (String) values.get("id");}
	public String name() { return (String) values.get("name");}
	public String displayName() { return (String) values.get("display_name");}
	public String description() { return (String) values.get("description");}
	public String type() { return (String) values.get("type");}
	public String href() { return (String) values.get("href");}
	public String url() { return (String) values.get("url");}
	public boolean isPrimaryImage() { return isPrimaryImage;}
	public void setIsPrimaryImage(boolean pVal) { isPrimaryImage = pVal;}

}
