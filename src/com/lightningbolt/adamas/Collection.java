package com.lightningbolt.adamas;

import java.util.LinkedList;
import java.util.ListIterator;

import org.json.simple.JSONObject;

import com.lightningbolt.adamas.Asset;
import com.lightningbolt.adamas.Media;


public class Collection extends Media {
	private static final long serialVersionUID = 1L;
	private LinkedList<Asset> childrenWithImages;
	
	//public Collection(HashMap<String, Object> pValues) {
	public Collection(JSONObject pValues) {
		setValues(pValues);
	}	
	
	public LinkedList<Asset> childrenWithImages() {
		if(childrenWithImages == null) {
			childrenWithImages = new LinkedList<Asset>();
			
			if(children() != null) {
				ListIterator<Asset> listIterator = children().listIterator();
				while (listIterator.hasNext()) {
					Asset currAsset = (Asset)listIterator.next();
					if((currAsset != null) && (currAsset.isImages())) {
						childrenWithImages.add(currAsset);
					}
				}
			}
		}
		return childrenWithImages;
	}
	
	public LinkedList<Asset> threeChildren() {
		LinkedList<Asset> children = new LinkedList<Asset>();

		int numItems = childrenWithImages().size();
		if(numItems > 3) {
			numItems = 3;
		}
		for(int i=0;i<numItems;i++) {
			Asset currAsset = (Asset)childrenWithImages().get(i);
			if(currAsset.isImages()) {
				children.add(currAsset);
			}
		}
		return children;
	}

}
