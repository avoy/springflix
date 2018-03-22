package com.lightningbolt.api;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;
//import org.apache.log4j.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lightningbolt.api.HTTPRequest;
import com.lightningbolt.api.HttpStatusCodeException;


public class AdamasClient implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	protected String baseURL = "https://player.ooyala.com/opm/v1/"; // default
	protected String pcode = null;
	protected String accountToken = null;
  //protected Logger logger;
    protected final Log logger = LogFactory.getLog(getClass());

	protected final String GIGYA_API_KEY = "3_d7BCwhMQp8DUOMQ_9psTLRR4X69xvedOR4gffFpOzkm7zatJbzZqflW9UrGUPwOZ"; // Ooyala default
	protected final String GIGYA_SECRET_KEY = ""; 
	
	public AdamasClient() {
		//logger = Logger.getLogger("AdamasClient");
	}
	
	// B2C
	public Object assets(String pAcctToken) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("page_size", "100");
		parameters.put("page", "0");
		Object returnVal = null;
		if (pAcctToken != null) {
			parameters.put("account_token", pAcctToken);
		} else {
			parameters.put("pcode", pcode);
		}

		try {
			JSONObject assets = (JSONObject) makeAdamasRequest("assets", parameters);
			returnVal = (Object) assets.get("results");			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVal;
	}

	public Object assets(HashMap<String, String> pParameters) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("page_size", "100");
		parameters.put("page", "0");
		Object returnVal = null;

		// automatically add pcode if acctToken not specified
		String acctToken = pParameters.get("account_token");
		if (acctToken == null) {
			if(accountToken() != null) {
				parameters.put("account_token", accountToken());
			}
			else {
				parameters.put("pcode", pcode);
			}
		}

		// Merge in all User defined parameters
		parameters.putAll(pParameters);

		try {
			HashMap<String, Object> assets = (HashMap<String, Object>) makeAdamasRequest("assets", parameters);
			returnVal = (Object) assets.get("results");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVal;
	}

	public Object assetsForId(String pAssetId, HashMap<String, String> pParameters) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("page_size", "100");
		parameters.put("page", "0");
		Object returnVal = null;

		// automatically add pcode if acctToken not specified
		String acctToken = pParameters.get("account_token");
		if (acctToken == null) {
			if(accountToken() != null) {
				parameters.put("account_token", accountToken());
			}
			else {
				parameters.put("pcode", pcode);
			}
		}

		// Merge in all User defined parameters
		parameters.putAll(pParameters);

		try {
			HashMap<String, Object> assets = (HashMap<String, Object>) makeAdamasRequest("assets/" + pAssetId, parameters);
			@SuppressWarnings("unchecked")
			LinkedList<HashMap<String, Object>> results  = (LinkedList<HashMap<String, Object>>) assets.get("results");

			returnVal = results.getFirst();

		} catch (Exception e) {
			logger.error("Adamas.assetsForId - " + e);
			HashMap<String, String> error = new HashMap<String, String>();
			error.put("error", e.getMessage());
			returnVal = error;
		}
		return returnVal;
	}

	// B2C
	public Object collections(String pAcctToken) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("page_size", "100");
		parameters.put("page", "0");
		parameters.put("details", "all");
		// parameters.put("s", "discover_2_En vivo");
		// parameters.put("q", "name:/.*main/");
		Object returnVal = null;

		if (pAcctToken != null) {
			parameters.put("account_token", pAcctToken);
		} else {
			parameters.put("pcode", pcode);
		}

		returnVal = collections(parameters);
		return returnVal;

	}

	public Object collections(HashMap<String, String> pParameters) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("page_size", "100");
		parameters.put("page", "0");
		parameters.put("details", "all");
		Object returnVal = null;

		// automatically add pcode if acctToken not specified
		String acctToken = pParameters.get("account_token");
		if (acctToken == null) {
			if(accountToken() != null) {
				parameters.put("account_token", accountToken());
			}
			else {
				parameters.put("pcode", pcode);
			}
		}

		// Merge in all User defined parameters
		parameters.putAll(pParameters);

		try {
			HashMap<String, Object> collections = (HashMap<String, Object>) makeAdamasRequest("collections", parameters);
			returnVal = (Object) collections.get("results");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnVal;
	}
	
	public HashMap<String, Object> makeAdamasRequest(String pRequestPath, HashMap<String, String> parameters ) throws HttpStatusCodeException {
		String request = null;
		
		request  = baseURL +pRequestPath + "?" + concatenateParams(parameters, "&");
		String response = makeGetRequest(request);
		@SuppressWarnings("unchecked")
		HashMap<String,Object> returnVal = (HashMap<String,Object>)parseJSONResponse(response);
		return returnVal;
	}

	/**
	 * Concatenates the key-values of parameters using a separator in between
	 * 
	 * @param parameters
	 *            HashMap with the key-value elements to be concatenated
	 * @param separator
	 *            The separator (a char) which is added between hash elements
	 * @return the concatenated string
	 */
	private String concatenateParams(HashMap<String, String> parameters, String separator) {
		Vector<String> keys = new Vector<String>(parameters.keySet());
		Collections.sort(keys);

		String string = "";
		for (Enumeration<String> e = keys.elements(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String value = (String) parameters.get(key);
			if (!string.isEmpty())
				string += separator;
			try {
				string += key + "=" + encodeURI(value);
			}
			catch(java.io.UnsupportedEncodingException exception) {
				logger.error(exception.getStackTrace());
			}
		}
		return string;
	}

	/**
	 * Encodes a String to be URI friendly.
	 * 
	 * @param input
	 *            The String to encode.
	 * @return The encoded String.
	 * @throws java.io.UnsupportedEncodingException
	 *             if the encoding as UTF-8 is not supported.
	 */
	public String encodeURI(String input)
			throws java.io.UnsupportedEncodingException {
		return URLEncoder.encode(input, "UTF-8");
	}

	public JSONObject gigyaLogin(String pLoginID, String pPassword) {
		String response = null;
		System.out.println("============= Gigya Login =================");
		try {
			String gigyaURL = "https://accounts.gigya.com/accounts.login?apiKey="
					+ GIGYA_API_KEY
					+ "&loginID="
					+ URLEncoder.encode(pLoginID, "UTF-8")
					+ "&password="
					+ URLEncoder.encode(pPassword, "UTF-8")
					+ "&secret="
					+ URLEncoder.encode(GIGYA_SECRET_KEY, "UTF-8");
					//+ "&sessionExpiration=7200";  // didn't work
			logger.info("Login to Gigya - " + gigyaURL);

			response = makeGetRequest(gigyaURL);
			logger.info("Gigya accounts.login() response - " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (JSONObject) parseJSONResponse(response);
	}
	
	public String gigyaSetUserInfo(String pUID) {
		String response = null;
		try {
			String gigyaURL = "https://socialize.gigya.com/socialize.setUserInfo?apiKey="+ GIGYA_API_KEY + "&UID="+ URLEncoder.encode(pUID, "UTF-8") + "&secret=" + URLEncoder.encode(GIGYA_SECRET_KEY, "UTF-8") + "&userInfo={firstName:\"Kevin\",lastName:\"Avoy\",gender:\"m\"}";
			//String gigyaURL = "https://socialize.gigya.com/socialize.setUserInfo?apiKey="+ apiKey + "&UID="+ URLEncoder.encode(pUID, "UTF-8") + "&secret=" + secretKey + "&userInfo={firstName:\"Kevin2\",lastName:\"Avoy\",email:\"avoy%40yahoo.com\"}";
			System.out.println("gigyaSetUserInfo request - " + gigyaURL);
			//response = makeRequest(gigyaURL, null);
			response = makeGetRequest(gigyaURL);
			System.out.println("Gigya gigyaSetUserInfo() response - " + response);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public JSONObject gigyaInitRegistration() {
		String response = null;
		try {
			String gigyaURL = "https://accounts.gigya.com/accounts.initRegistration?apiKey="
					+ GIGYA_API_KEY
					+ "&secret="
					+ URLEncoder.encode(GIGYA_SECRET_KEY, "UTF-8");
			logger.info("initRegistration to Gigya - " + gigyaURL);

			response = makeGetRequest(gigyaURL);
			logger.info("Gigya accounts.initRegistration() response - " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (JSONObject) parseJSONResponse(response);
	}

	public JSONObject gigyaRegister(String pEmail, String pPassword) {
		String response = null;
		
		JSONObject regInfo = gigyaInitRegistration();
		String regToken = (String)regInfo.get("regToken");
		
		try {
			String gigyaURL = "https://accounts.gigya.com/accounts.register?apiKey=" + GIGYA_API_KEY
					+ "&email=" + URLEncoder.encode(pEmail, "UTF-8")
					+ "&password=" + URLEncoder.encode(pPassword, "UTF-8")
					+ "&regToken="+ URLEncoder.encode(regToken, "UTF-8")
					+ "&finalizeRegistration="+ true
					+ "&secret=" + URLEncoder.encode(GIGYA_SECRET_KEY, "UTF-8");
			logger.info("Register with Gigya - " + gigyaURL);

			response = makeGetRequest(gigyaURL);
			logger.info("Gigya accounts.register() response - " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (JSONObject) parseJSONResponse(response);
	}
	
	public JSONObject gigyaFinalizeRegistration(String pRegToken) {
		String response = null;
		
		if(pRegToken == null) {
			JSONObject regInfo = gigyaInitRegistration();
			pRegToken = (String)regInfo.get("regToken");
		}
		try {
			String gigyaURL = "https://accounts.us1.gigya.com/accounts.finalizeRegistration?apiKey=" + GIGYA_API_KEY
					+ "&regToken="+ URLEncoder.encode(pRegToken, "UTF-8")
					+ "&secret=" + URLEncoder.encode(GIGYA_SECRET_KEY, "UTF-8");
			logger.info("Finalize Registration with Gigya - " + gigyaURL);

			response = makeGetRequest(gigyaURL);
			logger.info("Gigya accounts.finalizeRegistration() response - " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (JSONObject) parseJSONResponse(response);
	}
	
	public JSONObject gigyaGetSchema() {
		String response = null;
		
		try {
			String gigyaURL = "https://accounts.us1.gigya.com/accounts.getSchema?apiKey=" + GIGYA_API_KEY
					+ "&secret=" + URLEncoder.encode(GIGYA_SECRET_KEY, "UTF-8");
			logger.info("Get Schema - " + gigyaURL);

			response = makeGetRequest(gigyaURL);
			logger.info("Gigya accounts.gigyaGetSchema() response - " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (JSONObject) parseJSONResponse(response);
	}

	public JSONObject getAccountTokenFromCerebus(
			JSONObject pValues) {
		JSONObject returnVal = null;
		try {
			logger.info("\tUID = " + pValues.get("UID"));
			logger.info("\tUIDSignature = "
					+ pValues.get("UIDSignature"));
			logger.info("\tsignatureTimestamp = "
					+ pValues.get("signatureTimestamp"));

			String cerebusRequestPath = "https://player.ooyala.com"
					+ "/authentication/v1/providers/" + pcode + "/gigya";
			String cerebusPostData = "signatureTimestamp="
					+ pValues.get("signatureTimestamp")
					+ "&uid="
					+ URLEncoder.encode((String) pValues.get("UID"), "UTF-8")
					+ "&UIDSignature="
					+ URLEncoder.encode((String) pValues.get("UIDSignature"),
							"UTF-8");

			logger.info("Get account_token from Cerebus = "
					+ cerebusRequestPath);
			logger.info("\tcerebus POST data - " + cerebusPostData);
			String cerebusResponse = makeRequest(cerebusRequestPath,
					cerebusPostData, "POST");
			returnVal = (JSONObject) parseJSONResponse(cerebusResponse);
		} catch (Exception e) {
			logger.error("Adamas.getAccountTokenFromCerebus() - Exception - ");
			e.printStackTrace();
		}
		return returnVal;
	}

	public Object parseJSONResponse(String pResponse) {
		JSONParser parser = new JSONParser();

		ContainerFactory containerFactory = new ContainerFactory() {
			@SuppressWarnings("rawtypes")
			public List creatArrayContainer() {
				return new LinkedList();
			}

			@SuppressWarnings("rawtypes")
			public java.util.Map createObjectContainer() {
				//return new LinkedHashMap();
				//return new HashMap();
				
				return new JSONObject();
			}
		};

		Object json = null;

		try {
			json = parser.parse(pResponse, containerFactory);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return json;
	}

	public String makeGetRequest(String pURL) throws HttpStatusCodeException {
		return makeRequest(pURL, null, "GET");
	}

	public String makeRequest(String pURL, String postParams, String pMethod) throws HttpStatusCodeException {
		return makeRequest(pURL, postParams, pMethod, false);
	}
	
	public String makeRequest(String pURL, String postParams, String pMethod, boolean pJSONRequest) throws HttpStatusCodeException {
		HTTPRequest req = new HTTPRequest();
		String response  = req.makeRequest(pURL, postParams, pMethod, pJSONRequest);
		return response;
	}

	// ---------------------------------------------------------
	public void setPCode(String pValue) { pcode = pValue;}
	public String pcode() {return pcode;}
	public String accountToken() { return accountToken;}
	public void setAccountToken(String pVal) {accountToken = pVal;}

}
