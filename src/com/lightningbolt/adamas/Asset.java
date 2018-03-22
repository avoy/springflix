package com.lightningbolt.adamas;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;

import org.json.simple.JSONObject;

import com.lightningbolt.adamas.Image;
import com.lightningbolt.adamas.Media;
import com.lightningbolt.adamas.Offer;
import com.lightningbolt.adamas.Rendition;

public class Asset extends Media {
	private static final long serialVersionUID = 1L;
	private boolean isFullDetails = false;
	private LinkedList<Image> images;
	private LinkedList<Offer> offers;
	private LinkedList<Rendition> renditions;

	//public Asset(HashMap<String, Object> pValues) {
    public Asset(JSONObject pValues) {
		setValues(pValues);
	}
	
	public String type() {
		return (String) values.get("type");
	}

	public String displayName() {
		return (String) values.get("display_name");
	}

	public String description() {
		return (String) metadata().get("description");
	}
	
	public String year() {
		String returnVal = null;
		Object year = metadata().get("year");
		if(year != null) {
			returnVal = (String)year.toString();
		}
		return returnVal;
	}
	
	public String rating() {
		return (String) metadata().get("rating");
	}
	
	public String id() {
		String returnVal = null;
		try {
			// handle longs
			Object val = (Object) values.get("id");

			if (val.getClass().toString().equals("java.lang.Long")) {
				returnVal = val.toString(); // can I just do this for all
											// objects (Long, String, other?)
			} else { // java.lang.String
				returnVal = (String) val;
			}
		} catch (Exception e) {
			logger.error(e.getStackTrace());
		}
		return returnVal;
	}

	public String fullHref() {
		return "https://player.ooyala.com/opm/v1" + (String) values.get("href")
				+ "?details=all&pcode=FhN2MyOlEAUn2NZ0OM94L7XNcL0f";
	}
	
	@SuppressWarnings("unchecked")
	public String primaryImageId() {
		String returnVal = null;
		
		HashMap<String, Object> md =  metadata();
		if(md != null) {
			HashMap<String, Object> imageId = (HashMap<String,Object>)md.get("ooyala-primary-image");
			if(imageId != null) {
				returnVal  = (String)imageId.get("id");
			}
		}
		return returnVal;
	}
	
	public Image primaryImage() {
		Image returnVal = null;
		String id = primaryImageId();
		ListIterator<Image> listIterator = images().listIterator();
		while (listIterator.hasNext()) {
			Image anImage = (Image) listIterator.next();
			if (anImage.id().equals(id)) {
				anImage.setIsPrimaryImage(true);
				returnVal = anImage;
				break;
			}
		}
		// fallback to first image
		if(returnVal == null) {
			returnVal = firstImage();
		}
		return returnVal;
	}
	
	// currently only return the first image
	public Image nonMovieTitleImage() {
		Image returnVal = null;
		ListIterator<Image> listIterator = images().listIterator();
		while (listIterator.hasNext()) {
			Image anImage = (Image) listIterator.next();
			if (!anImage.isMovieScreen()) {
				returnVal = anImage;
				break;
			}
		}
		return returnVal;
	}

	public boolean isOffers() {
		return ((offers() == null) || (offers().isEmpty())) ? false : true;
	}

	public LinkedList<Offer> offers() {
		if (offers == null) {
			offers = new LinkedList<Offer>();
			@SuppressWarnings("unchecked")
			//LinkedList<HashMap<String, Object>> off = (LinkedList<HashMap<String, Object>>) values.get("offers");
			LinkedList<JSONObject> off = (LinkedList<JSONObject>) values.get("offers");
			if (off != null) {
				//ListIterator<HashMap<String, Object>> listIterator = off.listIterator();
				ListIterator<JSONObject> listIterator = off.listIterator();
				while (listIterator.hasNext()) {
					offers.add(new Offer(listIterator.next()));
				}
			}
		}
		return offers;
	}
	
	//"MIDAS_SVOD_BASIC"
	public boolean isSubcription() {
		/*
		boolean returnVal = false;
		ListIterator<Offer> listIterator = offers().listIterator();
		while (listIterator.hasNext()) {
			Offer anOffer = (Offer)listIterator.next();
			if(anOffer.productId().equals("MIDAS_SVOD_BASIC")) {
				returnVal = true;
				break;
			}
		}
		//return returnVal;
		 */
		 
		return true;
	}

	public LinkedList<Image> images() {
		if (images == null) {
			images = new LinkedList<Image>();
			@SuppressWarnings("unchecked")
			//LinkedList<HashMap<String, Object>> imgs = (LinkedList<HashMap<String, Object>>) values.get("images");
			LinkedList<JSONObject> imgs = (LinkedList<JSONObject>) values.get("images");
			if (imgs != null) {
				//ListIterator<HashMap<String, Object>> listIterator = imgs.listIterator();
				ListIterator<JSONObject> listIterator = imgs.listIterator();
				while (listIterator.hasNext()) {
					images.add(new Image(listIterator.next()));
				}
			}
		}
		return images;
	}

	// currently only return the first image
	public Image firstImage() {
		Image returnVal = null;
		if ((images() != null) && (!images().isEmpty())) {
			returnVal = (Image) images().getFirst();
		}
		return returnVal;
	}

	public boolean isImages() {
		return ((images() == null) || (images().isEmpty())) ? false : true;
	}
	
	// currently only return the first rendition
	public Rendition rendition() {
		Rendition aRendition = null;
		if(!renditions().isEmpty()) {
			aRendition = renditions().getFirst();
		}
		return aRendition;
	}
	
	public LinkedList<Rendition> renditions() {
		if (renditions == null) {
			renditions = new LinkedList<Rendition>();
			@SuppressWarnings("unchecked")
			LinkedList<HashMap<String, Object>> rends = (LinkedList<HashMap<String, Object>>) values.get("renditions");
			if (rends != null) {
				ListIterator<HashMap<String, Object>> listIterator = rends.listIterator();
				while (listIterator.hasNext()) {
					HashMap<String, Object> renditionMap = listIterator.next();
					//renditions.add(session().renditionForId((String)renditionMap.get("id")));
					renditions.add(renditionForId((String)renditionMap.get("id")));
				}
			}
		}
		return renditions;
	}
	
	public Rendition renditionForId(String pId) {
		Rendition returnValue = null;
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("details", "all");
		
		JSONObject aRendition = (JSONObject) at().assetsForId(pId, parameters);
		String errorMessage = (String) aRendition.get("error");
		
		if (errorMessage == null) {
			returnValue = new Rendition(aRendition);
		} else { // if there is a 404 error - we don't want to keep trying
			returnValue = new Rendition(aRendition);
			returnValue.setError(true);
			returnValue.setErrorMap(jsonErrorToMap(errorMessage));
			logger.error("Error in Session.renditionForId() - "
					+ errorMessage);
		
		}
		return returnValue;
	}

	public boolean hasRenditions() { return (rendition() == null) ? false : true; }
	public boolean isFullDetails() { return isFullDetails; }
	public void setIsFullDetails(boolean pFlag) { isFullDetails = pFlag; }
	public void setFullDetails(JSONObject pValues) {
		values = pValues;
		setIsFullDetails(true);
	}
}
