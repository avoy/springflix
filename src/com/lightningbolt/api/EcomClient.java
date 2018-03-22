package com.lightningbolt.api;

import java.security.SignatureException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
//import org.apache.log4j.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.lightningbolt.api.HTTPRequest;
import com.lightningbolt.api.HttpStatusCodeException;

public class EcomClient {
    protected final Log logger = LogFactory.getLog(getClass());
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	protected OoyalaAPI api;
	protected String pcode = null;
	protected String apiKey = null;
	protected String secretKey = null;
	protected String backjaxUser = null;
	protected String backjaxPW = null;

	public EcomClient() {
	}
/*
	public void createEcomAccount(String pAccountToken, String pEmail) {
		
		String requestParams = "{\"account\":{";
			requestParams += "\"email\": \""+ pEmail + "\",";
			requestParams += "\"name\": \""+ "Jack Bauer" + "\",";
			requestParams += "\"shipping_address\": {";
			requestParams += "\"country\": \"US\"" ;
			requestParams += "}}}" ;
		try {
			System.out.println("requestParams - "  + requestParams);
			String response = makeRequest("https://player-staging.ooyala.com/commerce/accounts/new?account_token="+pAccountToken, requestParams, "POST", true);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.createEcomAccount() - Exception - ");
			e.printStackTrace();
		}
	}
	*/
	
	@SuppressWarnings("unchecked")
	public void createEcomAccount(String pAccountToken, String pEmail, String pName, String pCountry) {
		JSONObject requestObject = new JSONObject();
		JSONObject userObject = new JSONObject();
		JSONObject addressObject = new JSONObject();
		
		addressObject.put("country", pCountry);
		userObject.put("shipping_address", addressObject);
		userObject.put("name", pName);
		userObject.put("email", pEmail);
		requestObject.put("account", userObject);
		
		String requestParams = requestObject.toJSONString();
				
		try {
			System.out.println("requestParams - "  + requestParams);
			String response = makeRequest("https://player-staging.ooyala.com/commerce/accounts/new?account_token="+pAccountToken, requestObject.toJSONString(), "POST", true);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.createEcomAccount() - Exception - ");
			e.printStackTrace();
		}
	}
	
	public String purchases(String pAccountToken) {
		String response = null;
		try {
			response = makeGetRequest("https://player-staging.ooyala.com/commerce/v2/accounts/purchases?account_token="+pAccountToken);
			//String response = makeGetRequest("https://player-staging.ooyala.com/commerce/accounts/purchases?account_token="+pAccountToken);
		}
		catch(Exception e) {
			logger.error("Adamas.purchases() - Exception - ");
			e.printStackTrace();
		}
		return response;
	}
	
	public void products(String pAccountToken) {
		try {
			String response = makeGetRequest("https://player-staging.ooyala.com/commerce/v1/accounts/products?account_token="+pAccountToken);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.products() - Exception - ");
			e.printStackTrace();
		}
	}
	
	public void paymentMethods(String pAccountToken) {
		
		try {
			String response = makeGetRequest("https://player-staging.ooyala.com/commerce/accounts/payment-methods?account_token="+pAccountToken);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.paymentMethods() - Exception - ");
			e.printStackTrace();
		}
	}
	/*
	public Object createAccountPaymentMethod(String pAccountToken) {
		String response = null;
		String requestParams = "{"
				+ "\"payment_method\":" + "{"
				+ "\"type\": \"CreditCard\""
				+ "},";
		requestParams += "\"callback_urls\":" + "{"
				+ "\"success\": \"http://url.com/success\","
				+ "\"failure\": \"http://url.com/failure\""
				+ "},";
		requestParams += "\"billing_address\":" + "{"
				+ "\"country\": \"US\","
				+ "\"district\": \"CA\","
				+ "\"city\": \"Santa Clara\""
				+ "}"
				+ "}";

		try {
			System.out.println("requestParams - "  + requestParams);
			response = makeRequest("https://player-staging.ooyala.com/commerce/accounts/payment-methods?account_token="+pAccountToken, requestParams, "POST", true);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.createAccountPaymentMethod() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}
	
	*/
	//ec.createAccountPaymentMethod(accountToken(), "CreditCard", "http://url.com/suceess", "http://url.com/failure", "US", "CA", "Santa Clara") {
	
	@SuppressWarnings("unchecked")
	public Object createAccountPaymentMethod(String pAccountToken, String pPaymentType, String pSuccessUrl, String pFailureUrl, String pCountry, String pDistrict, String pCity) {
		String response = null;
		JSONObject requestObject = new JSONObject();
		JSONObject paymentMethodObject = new JSONObject();
		JSONObject callbackURLObject = new JSONObject();
		JSONObject billingAddressObject = new JSONObject();
		
		paymentMethodObject.put("type", pPaymentType);
		requestObject.put("payment_method", paymentMethodObject);
		callbackURLObject.put("success", pSuccessUrl);
		callbackURLObject.put("failure", pFailureUrl);
		requestObject.put("callback_urls", callbackURLObject);
		billingAddressObject.put("country", pCountry);
		billingAddressObject.put("district", pDistrict);
		billingAddressObject.put("city", pCity);
		requestObject.put("billing_address", billingAddressObject);
		
		try {
			System.out.println("requestParams - "  + requestObject.toJSONString());
			response = makeRequest("https://player-staging.ooyala.com/commerce/accounts/payment-methods?account_token="+pAccountToken, requestObject.toJSONString(), "POST", true);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.createAccountPaymentMethod() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}
	/*
	public Object authorizeAccountPaymentMethod(String pAccountToken, String pAuthId) {
		String response = null;
		String requestParams = "{"
				+ "\"payment_method\":" + "{"
				+ "\"type\": \"CreditCard\""
				+ "}"
				+ "}";

		try {
			System.out.println("requestParams - "  + requestParams);
			response = makeRequest("https://player-staging.ooyala.com/commerce/accounts/payment-methods/" + pAuthId + "?account_token="+pAccountToken, requestParams, "POST", true);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.authorizeAccountPaymentMethod() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}
	*/
	//ec.authorizeAccountPaymentMethod(acountToken(), String pAuthId, "CreditCard");	
	@SuppressWarnings("unchecked")
	public Object authorizeAccountPaymentMethod(String pAccountToken, String pAuthId, String pPaymentMethodType) {
		String response = null;
		JSONObject requestObject = new JSONObject();
		JSONObject paymentMethodObject = new JSONObject();
		paymentMethodObject.put("type", pPaymentMethodType);
		requestObject.put("payment_method", paymentMethodObject);
		
		try {
			System.out.println("requestParams - "  + requestObject.toJSONString());
			response = makeRequest("https://player-staging.ooyala.com/commerce/accounts/payment-methods/" + pAuthId + "?account_token="+pAccountToken, requestObject.toJSONString(), "POST", true);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.authorizeAccountPaymentMethod() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}
	
	public Object postCCInfoToVindicia() {
		String response = null;
		String requestParams = generateCreditCardRequest();

		try {
			System.out.println("requestParams - "  + requestParams);
			response = makeRequest("https://secure.prodtest.sj.vindicia.com/vws", requestParams, "POST", false);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.postCCInfoToVindicia() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}
	
	public String generateCreditCardRequest() {
		   String hoaCCString = 
			"vin_WebSession_version=3.9" + 
			"&vin_WebSession_method=Account_updatePaymentMethod" + 
			"&vin_PaymentMethod_type=CreditCard" +
			"&vin_WebSession_VID=" + "28bc6b31381a9f3acb83ac9f56b69c9cd1cd3a13" +
			"&vin_PaymentMethod_accountHolderName=" + "Jack+Bauer" +
			"&vin_PaymentMethod_creditCard_account=" + "4444444444444448" + 
			"&vin_PaymentMethod_creditCard_expirationDate_Month=" + "10" + 
			"&vin_PaymentMethod_creditCard_expirationDate_Year=" + "2017" + 
			"&vin_PaymentMethod_nameValues_cvn=" + "123" + 
			"&vin_PaymentMethod_billingAddress_addr1=" + "123+Main+Street" + 
			"&vin_PaymentMethod_billingAddress_addr2=" + "Apartment+47" +
			"&vin_PaymentMethod_billingAddress_country=" + "US" + 
			"&vin_PaymentMethod_billingAddress_district=" + "CA" + 
			"&vin_PaymentMethod_billingAddress_city=" + "Los+Gatos" +
			"&vin_PaymentMethod_billingAddress_postalCode=" + "95033" + 
			"&vin_PaymentMethod_billingAddress_phone=" + "4088917877" + 
			"&vin_PaymentMethod_billingAddress_name=" + "Jacks+House" ;
		  // "&vin_Account_name=" + "" + 
		 //  "&vin_Account_emailAddress=" + "kevinavoy+jackbauer@gmail.com";
		   
		   return hoaCCString;
		
	}
	public Object subscribe(String pAccountToken) {
		String response = null;
		String requestParams = "{"
				+ "\"dryrun\": false,"
				+ "\"target_account_id\": \"\","
				+ "\"ignore_credits\": false,"
				+ "\"subscription\":" + "{"
					+ "\"id\": \"aa_aaaaaaa_aa\", "
					+ "\"payment_method\":" + "{"
						+ "\"id\": \"CreditCard_56e39417_5a281705\""
					+ "},"
					+ "\"items\":" + "[{"
						+ "\"product\":" + "{"
							+ "\"id\": \"MIDAS_SVOD_BASIC\""
						+ "}"
					+ "}]"
				+ "}"
			+ "}";

		try {
			System.out.println("requestParams - "  + requestParams);
			response = makeRequest("https://player-staging.ooyala.com/commerce/subscriptions" + "?account_token="+pAccountToken, requestParams, "POST", true);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.subcribe() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}
	public Object subscribeOffer(String pAccountToken, String pOfferId, String pProductId) {
		String response = null;
		int dayForSubscription = 180;
			
		ZonedDateTime now = ZonedDateTime.now();        
        ZonedDateTime endDate = now.plusDays(dayForSubscription);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");  //”2015-03-02T15:04:05-07:00”,

		String requestParams = "{"
				+ "\"dryrun\": false,"
				+ "\"target_account_id\": \"\","
				+ "\"ignore_credits\": false,"
				+ "\"subscription\":" + "{"
					+ "\"id\": \"aa_aaaaaaa_aa\", "
					+ "\"payment_method\":" + "{"
						+ "\"id\": \"CreditCard_56e39417_5a281705\""
					+ "},"
					+ "\"items\":" + "[{"
					
							+ "\"content\":" + "{"
							+ "\"hash\": \"" + generateSignature(pOfferId, pProductId, now.format(formatter), endDate.format(formatter), "") + "\","
							+ "\"offers\": ["
							+ "{"
								+ "\"content_id\": \"" + pOfferId +"\","
								+ "\"end_time\": \"" + endDate.format(formatter) +  "\","
								+ "\"external_product_id\": \"" + pProductId + "\","
								+ "\"start_time\": \"" + now.format(formatter) + "\""
							+ "} ]"
						+ "},"
													
						+ "\"product\":" + "{"
							+ "\"id\": \"MIDAS_SVOD_BASIC\""
						+ "}"
					+ "}]"
				+ "}"
			+ "}";


		try {
			System.out.println("requestParams - "  + requestParams);
			response = makeRequest("https://player-staging.ooyala.com/commerce/subscriptions" + "?account_token="+pAccountToken, requestParams, "POST", true);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.subcribe() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}
	
	@SuppressWarnings("unchecked")
	public Object onetimePurchase(String pAccountToken, String pPaymentMethod, String pOfferId, String pProductId) {
		String response = null;
		int hoursToFinish = 48;
		
		ZonedDateTime now = ZonedDateTime.now();        
        ZonedDateTime endDate = now.plusHours(hoursToFinish);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");  //”2015-03-02T15:04:05-07:00”,
		
		JSONObject requestObject = new JSONObject();
		JSONObject purchaseObject = new JSONObject();
		JSONObject paymentMethodObject = new JSONObject();
		LinkedList<JSONObject> items = new LinkedList<JSONObject>();
		LinkedList<JSONObject> offers = new LinkedList<JSONObject>();
		JSONObject contentObject = new JSONObject();
		JSONObject offerObject = new JSONObject();
		JSONObject rentalWindowObject = new JSONObject();
		JSONObject productObject = new JSONObject();
		JSONObject itemsObject = new JSONObject();

		
        requestObject.put("dryrun", false);
        requestObject.put("target_account_id", "");
        requestObject.put("ignore_credits", false);
        paymentMethodObject.put("id", pPaymentMethod);
        purchaseObject.put("payment_method", paymentMethodObject);
        
        
        offerObject.put("content_id", pOfferId);
        offerObject.put("end_time", endDate.format(formatter));
        offerObject.put("external_product_id", pProductId);
        offerObject.put("start_time", now.format(formatter));
        rentalWindowObject.put("hours_to_finish", hoursToFinish);
        offerObject.put("rental_window", rentalWindowObject);
        offers.add(offerObject);
        contentObject.put("offers", offers);
        contentObject.put("hash", generateSignature(pOfferId, pProductId, now.format(formatter), endDate.format(formatter),"" + hoursToFinish));
        productObject.put("id", pProductId);
        itemsObject.put("content", contentObject);
        itemsObject.put("product", productObject);
        items.add(itemsObject);
        purchaseObject.put("items", items);
        
        requestObject.put("purchase", purchaseObject);

		try {
			System.out.println("requestObject - "  + requestObject.toJSONString());

			response = makeRequest("https://player-staging.ooyala.com/commerce/onetime" + "?account_token="+pAccountToken, requestObject.toJSONString(), "POST", true);
			System.out.println("ONETIME response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.onetimePurchase() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}
	
	/*
	public Object onetimePurchase(String pAccountToken, String pOfferId, String pProductId) {
		String response = null;
		int hoursToFinish = 48;
		
		ZonedDateTime now = ZonedDateTime.now();        
        ZonedDateTime endDate = now.plusHours(hoursToFinish);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");  //”2015-03-02T15:04:05-07:00”,
        
		String requestParams = "{"
				+ "\"dryrun\": false,"
				+ "\"target_account_id\": \"\","
				+ "\"ignore_credits\": false,"
				+ "\"purchase\":" + "{"
					+ "\"payment_method\":" + "{"
							+ "\"id\": \"CreditCard_56e39417_5a281705\""   // need to pass this in
					+ "},"
					+ "\"items\":" + "[{"
					
						+ "\"content\":" + "{"
							+ "\"hash\": \"" + generateSignature(pOfferId, pProductId, now.format(formatter), endDate.format(formatter), "" + hoursToFinish) + "\","
							+ "\"offers\": ["
							+ "{"
								+ "\"content_id\": \"" + pOfferId +"\","
								+ "\"end_time\": \"" + endDate.format(formatter) +  "\","
								+ "\"external_product_id\": \"" + pProductId + "\","
								+ "\"rental_window\": {"
									+ "\"hours_to_finish\": " + hoursToFinish
								+ "},"
								+ "\"start_time\": \"" + now.format(formatter) + "\""
							+ "} ]"
						+ "},"
						
						+ "\"product\":" + "{"
							+ "\"id\": \"" + pProductId + "\""
						+ "}"
				+ "}]"
			+ "}"
		+ "}";


		try {
			System.out.println("requestParams - "  + requestParams);
			response = makeRequest("https://player-staging.ooyala.com/commerce/onetime" + "?account_token="+pAccountToken, requestParams, "POST", true);
			System.out.println("ONETIME response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.onetimePurchase() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}
*/
	public String generateSignature(String pContentId, String pExternalProductId, String pStartTime, String pEndTime, String pHoursToFinish) {
		String returnVal = null;
		String message = pContentId + pExternalProductId + pStartTime + pEndTime + pHoursToFinish;
        String key = "SPSiGhETS4lFSpsi_uQLBAcL3ZryXEDzPafb4zUf";  // what secret key is this Freya's - it does not match mine
        try {
        	returnVal =  sign(message, key);
        }
        catch(Exception e) {
	        System.err.println("Adamas.sign() - exception - " + e);
        }
        return returnVal;
	}

	public String sign(String data, String key) throws java.security.SignatureException {
		String result;
		try {

			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] rawHmac = mac.doFinal(data.getBytes());

			// base64-encode the hmac
			result = new String(Base64.encodeBase64(rawHmac), "UTF-8");

		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
		return result;
	}
	
	public Object entitlements(String pAccountToken) {
		String response = null;
		try {
			response = makeRequest("https://player-staging.ooyala.com/commerce/entitlements" + "?account_token="+pAccountToken, null, "GET", true);
		}
		catch(Exception e) {
			logger.error("Adamas.reset() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}
	
	public String userId(String pAccountToken) {
		JSONObject entitlements = (JSONObject)entitlements(pAccountToken);
		JSONObject status = (JSONObject)entitlements.get("status");
		return (String)status.get("user_id");
	}

	
	public Object reset(String pAccountToken) {
		String response = null;
		String requestParams = "{"
				+ "\"users\":" + "{"
					+ "\"id\": \"55e9ad09f0664f97b3583ee1ec83fb6b\" "
				+ "}"
			+ "}";

		try {
			System.out.println("requestParams - "  + requestParams);
			response = makeRequest("https://player-staging.ooyala.com/commerce/reset" + "?account_token="+pAccountToken, requestParams, "POST", true);
			System.out.println("response - "  + response);
		}
		catch(Exception e) {
			logger.error("Adamas.reset() - Exception - ");
			e.printStackTrace();
		}
		return parseJSONResponse(response);
	}

	public Object playerTokenValues(String pAccountToken, String pEmbedCode) throws HttpStatusCodeException  {
		JSONObject values = null;
		
		String response = makeGetRequest("https://player-staging.ooyala.com/commerce/tokens?account_token="+pAccountToken + "&embed_codes="+ pEmbedCode);
		System.out.println("response - "  + response);
		values = (JSONObject)parseJSONResponse(response);
			
		return (Object)values;
	}
	public void syncEntitlementsFromStagingToProd(String pAccountToken) {
		String userId = userId(pAccountToken);

		JSONObject response = getEntitlementsForUser(userId, false); // stage
		setEntitlementsForUser(userId,convertEntitlementToRequest(response),true); // Prod
	}
	
	public JSONObject getEntitlementsForUser(String pCustomerId, boolean pPROD) {
		JSONObject response = null;
		if(api().cookie == null) {
			setBackjaxTokenCookie(backjaxUser, backjaxPW);  
		}
		api().debug = true;
		if(pPROD == true) {
			api().setBaseURL("https://rl.ooyala.com/v2/");  
		}
		else {			
			api().setBaseURL("https://rl-staging.ooyala.com/v2/");  //api().setBaseURL("https://api.ooyala.com/v2/");  
		}
		
		 try {
			 System.out.println("");
			 response = (JSONObject) api().getRequest("entitlements/providers/" + pcode + "/accounts/" +  pCustomerId + "/content");
			 //print(response);
			 
			 //returnVal = (String)response.get("backjax_token");
			 logger.info("response - " + response);
			 logger.info("content - " + response.get("content"));

		 } catch (Exception e) {
			e.printStackTrace();
		 }	
		 return response;
	}
	public JSONObject setEntitlementsForUser(String pCustomerId, JSONObject pEntitlementsJSON, boolean pPROD) {
		JSONObject response = null;		
		
		if(api().cookie == null) {
			setBackjaxTokenCookie(backjaxUser, backjaxPW);
		}
		api().debug = true;
		if(pPROD == true) {
			api().setBaseURL("https://rl.ooyala.com/v2/");  
		}
		else {			
			api().setBaseURL("https://rl-staging.ooyala.com/v2/");  //api().setBaseURL("https://api.ooyala.com/v2/");  
		}
		
		try {
			System.out.println("");
			//response = (JSONObject) api().postRequest("entitlements/providers/" + pcode + "/accounts/" +  pCustomerId + "/content",parameters );
			response = (JSONObject) api().postRequest("entitlements/providers/" + pcode + "/accounts/" +  pCustomerId + "/content",pEntitlementsJSON.toJSONString() );
			logger.info("response - " + response);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return response;
	}

	@SuppressWarnings("unchecked")
	public JSONObject convertEntitlementToRequest(JSONObject pEntitlementResponse) {
		JSONObject responseContent = (JSONObject)pEntitlementResponse.get("content");
		JSONObject offers = new JSONObject();
		LinkedList<JSONObject> offersList = new LinkedList<JSONObject> ();
		
		// Doesn;t handle Assets or Labels yet
		LinkedList<JSONObject> offersResponse = (LinkedList<JSONObject>)responseContent.get("offers");
		ListIterator<JSONObject> listIterator = offersResponse.listIterator();
		while (listIterator.hasNext()) {
			JSONObject anOffer = listIterator.next();
			JSONObject newOffer = new JSONObject();
			for (Object key: anOffer.keySet()) {
				if (!key.equals("updated_at")) {
					Object value = (Object)anOffer.get(key);
					if(value != null) {
						newOffer.put(key, value);
					}
				}
			}
			offersList.add(newOffer);
		}
		offers.put("offers", offersList);
		return offers;
	}

	public void setBackjaxTokenCookie(String pEmail, String pPassword) {
		try {
			// set the backjax token cookie
			api().cookie = "backjax_token=" + api().encodeURI(getBackJaxToken(pEmail, pPassword));
		}
		catch(Exception e) {
			System.err.println("Error: " + e);
		}
	}
	
	public String getBackJaxToken(String pEmail, String pPassword) {
		String returnVal = null;
		 HashMap<String, Object> parameters = new HashMap<String, Object>();
		 parameters.put("email", pEmail);
		 parameters.put("password", pPassword);

		 try {
			 @SuppressWarnings("unchecked")
			 HashMap<String,Object> response = (HashMap<String, Object>) api().postRequest("exchange_login_for_cms_token", parameters);
			 returnVal = (String)response.get("backjax_token");

		 } catch (Exception e) {
			e.printStackTrace();
		 }	
		 return returnVal;
	}


	/* -------------------------------------------------------- */

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
		return makeRequest(pURL, null, "GET",false);
	}

	public String makeRequest(String pURL, String postParams, String pMethod, boolean pJSONRequest) throws HttpStatusCodeException {
		HTTPRequest req = new HTTPRequest();
		String response  = req.makeRequest(pURL, postParams, pMethod, pJSONRequest);
		return response;
	}
	public OoyalaAPI api() {
		if (api == null) {
			api = new OoyalaAPI(pcode, secretKey);  // need API key and secret key
		}
		return api;
	}
	public String pcode() {return pcode;}
	public void setPCode(String pValue) { pcode = pValue;}
	public void setSecretKey(String pValue) { secretKey = pValue;}
	public void setApiKey(String pValue) { apiKey = pValue;}
	public String backjaxUser() { return backjaxUser;}
	public void setBackjaxUser(String pVal) {backjaxUser = pVal;}
	public String backjaxPW() { return backjaxPW;}
	public void setBackjaxPW(String pVal) {backjaxPW = pVal;}

}
