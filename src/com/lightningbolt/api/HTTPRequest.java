package com.lightningbolt.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lightningbolt.api.HttpStatusCodeException;

public class HTTPRequest {
    protected final Log logger = LogFactory.getLog(getClass());

	public HTTPRequest() {
	}
	
	public String makeRequest(String pURL, String postParams, String pMethod, boolean pJSONRequest) throws HttpStatusCodeException {
		
		URL url;
		HttpURLConnection conn = null;
		BufferedReader rd = null;
		String line;
		String result = "";

		try {
			System.out.println("");
			logger.info("request - " + pURL);
			
			url = new URL(pURL);
			conn = (HttpURLConnection) url.openConnection();
			if ((pMethod != null) && (pMethod.equals("GET"))) {
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

			} else if ((pMethod != null) && (pMethod.equals("POST"))) {
				conn.setRequestMethod("POST");
				if(pJSONRequest == true) {
					conn.setRequestProperty("Content-Type", "application/json");
					conn.setRequestProperty("Accept", "application/json");
				}
				else {
					conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
				}
				if (postParams != null) {
					conn.setRequestProperty("Content-Length",
							"" + Integer.toString(postParams.getBytes().length));
				} else {
					conn.setRequestProperty("Content-Length", "" + 0);
				}
				conn.setRequestProperty("Cache-Control", "no-cache, no-store, must-revalidate");
				conn.setRequestProperty("Content-Language", "en-US");

				if (postParams != null) {
					// Send post request
					conn.setUseCaches(false);
					conn.setDoInput(true);
					conn.setDoOutput(true);
					DataOutputStream wr = new DataOutputStream(
							conn.getOutputStream());
					wr.writeBytes(postParams);
					wr.flush();
					wr.close();
				}
			} else if ((pMethod != null) && (pMethod.equals("DELETE"))) {
				conn.setRequestMethod("DELETE");
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				conn.setRequestProperty("Content-Language", "en-US");
			}

			/*
			 * //get all headers Map<String, List<String>> map =
			 * conn.getHeaderFields(); for (Map.Entry<String, List<String>>
			 * entry : map.entrySet()) { logger.info(entry.getKey() +
			 * " : " + entry.getValue()); }
			 */

			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();

		} catch (IOException e) {			
			logger.error("HTTPRequest.makeRequest() - IOException - " + e);
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream())); // may return null, hence the check further on
			try {
				while ((line = rd.readLine()) != null) {
					result += line;
				}
				rd.close();
				throw new HttpStatusCodeException(result, conn.getResponseCode());

			}catch (IOException exception) {
				logger.error("HTTPRequest.makeRequest()2 - IOException - " + exception);
			} 

			
		} catch (Exception e) {
			logger.error("HTTPRequest.makeRequest() - Exception - " + e );
			//e.printStackTrace();
		}
		
		logger.info("response - " + result);

		return result;
	}
}
