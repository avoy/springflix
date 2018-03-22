package com.lightningbolt.adamas;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

import org.json.simple.JSONObject;

import com.lightningbolt.api.AdamasClient;
import com.lightningbolt.adamas.Asset;

//import org.apache.log4j.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Media implements java.io.Serializable  {
	private static final long serialVersionUID = 1L;
	protected JSONObject values;
	protected AdamasClient at;
	protected String pcode;
	protected boolean error = false;
	protected HashMap<String, Object> errorMap = null;
	protected String errorMessage = null;
	protected LinkedList<Asset> children;
	protected LinkedList<String> ordering;
    protected final Log logger = LogFactory.getLog(getClass());

	public LinkedList<Asset> children() {
		if (children == null) {
			children = new LinkedList<Asset>();
			HashMap<String, Object> tempHashMap = new HashMap<String, Object>();
			@SuppressWarnings("unchecked")
			LinkedList<HashMap<String, Object>> kids = (LinkedList<HashMap<String, Object>>) values
					.get("children");

			if(kids != null) {
				ListIterator<HashMap<String, Object>> listIterator = kids.listIterator();
				while (listIterator.hasNext()) {
					HashMap<String, Object> child = listIterator.next();
					Asset anAsset = assetForId((String)child.get("id"));
					anAsset.setAt(at);
					if(!anAsset.error()) {
						 if(isOrdering()) {
							tempHashMap.put(anAsset.id(), anAsset);
						}
						else {
							children.add(anAsset);
						}
					}
				}
				// Sort based on the ordering 
				if(isOrdering()) {
					//sort them using the ordering list
					ListIterator<String> orderingIterator = ordering().listIterator();
					while (orderingIterator.hasNext()) {
						Asset tempAsset = (Asset)tempHashMap.remove(orderingIterator.next());// remove from hashmap
						if(tempAsset != null) {
							children.add(tempAsset);
						}
						
					}
					//if  hashmap has any remaining items - add to end of list
					if(!tempHashMap.isEmpty()) {
						Set<String> keys = tempHashMap.keySet(); 
						for(String key: keys) {
							children.add((Asset)tempHashMap.get(key));
						}
					}
					
				}
			}
		}
		return children;
	}
	public boolean isOrdering() {
		return  ((ordering()!=null)&&(!ordering().isEmpty()))?true:false;
	}

	public LinkedList<String> ordering() {
		if(ordering == null) {
			ordering = new LinkedList<String>();
			@SuppressWarnings("unchecked")
			LinkedList<Object> order = (LinkedList<Object>) (metadata().get("ordering"));
			if(order != null) {
				ListIterator<Object> listIterator = order.listIterator();
				while (listIterator.hasNext()) {
					String anOrder = (String)listIterator.next();
					ordering.add(anOrder);
				}
			}

		}
		return ordering;
	}

	public Asset assetForId(String pId) {
		Asset returnValue = null;
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("details", "all");
		
		JSONObject anAsset = (JSONObject) at().assetsForId(pId, parameters);
		String errorMessage = (String) anAsset.get("error");
		
		
		if (errorMessage == null) {
			returnValue = new Asset(anAsset);
			returnValue.setPCode(pcode());
			returnValue.setAt(at);
		} else { // if there is a 404 error - we don't want to keep trying
			returnValue = new Asset(anAsset);
			returnValue.setError(true);
			returnValue.setErrorMap(jsonErrorToMap(errorMessage));
			
			logger.error("Error in Media.assetForId() - " + errorMessage);
		}
		return returnValue;
	}
	
	public HashMap<String, Object> jsonErrorToMap(String pErrorJSON) {
		
		Object errorObject = at.parseJSONResponse(pErrorJSON);
		@SuppressWarnings("unchecked")
		HashMap<String, Object> errMap = (HashMap<String, Object>)errorObject;

		return errMap;
	}

	//Accessors
	public JSONObject values() { return values;}
	public void setValues(JSONObject pValues) { values = pValues;}
	public String id() { return (String) values.get("id"); }
	public String name() { return (String) values.get("name"); }
	public String href() { return (String) values.get("href"); }
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> metadata() { return (HashMap<String, Object>) values.get("metadata"); }
	public void setPCode(String pValue) { pcode = pValue;}
	public String pcode() {return pcode;}
	public boolean error() { return error; }
	public void setError(boolean pVal) { error = pVal;}
	public HashMap<String, Object> errorMap() { return errorMap; }
	public void setErrorMap(HashMap<String, Object> pVal) { errorMap = pVal; 
		if(errorMap != null) {
			setErrorMessage( (String)errorMap.get("message"));
		}
	}
	public String errorMessage() { return errorMessage; }
	public void setErrorMessage(String pVal) { errorMessage = pVal; }
	
	public AdamasClient at() {
		if(at == null) {
			at = new AdamasClient();	
			at.setPCode(pcode());
		}
		return at;
	}
	public void setAt(AdamasClient pAt) {
		at = pAt;
	}
	
	public String toString() {
		return values().toJSONString();
	}
	
}
