package com.lightningbolt.adamas;

import org.json.simple.JSONObject;

import com.lightningbolt.adamas.Media;

public class Offer extends Media {
	private static final long serialVersionUID = 1L;

	public Offer(JSONObject pValues) {
		setValues(pValues);
	}

	// Accessors
	public String productId() { return (String) values.get("product-id");}

}
