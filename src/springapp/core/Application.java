package springapp.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;

//import javax.servlet.ServletContext;
//import javax.servlet.http.HttpSession;

import com.lightningbolt.adamas.Asset;
import com.lightningbolt.adamas.Collection;
import com.lightningbolt.api.AdamasClient;
import com.lightningbolt.api.EcomClient;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class Application {
	protected HashMap<String, Collection> collections;
	protected AdamasClient ac;
	protected EcomClient ec;
    protected final Log logger = LogFactory.getLog("Application");
    private static Application instance = null;
	protected String gigyaUserName = "kevinavoy@gmail.com";
	protected String gigyaPassword =  "";
    protected JSONObject gigyaValues;
    protected JSONObject cerberusValues;
	protected String accountToken;
	protected String appPath;
	protected Properties properties;

    public Application() {
        System.out.println("Application() was called");
        
        URL url = getClass().getResource("/");
        
        String classesPath = url.getFile();
        setAppPath(classesPath.replaceFirst("WEB-INF/classes/", ""));
        System.out.println("appPath- " + appPath);
    }
    
    public static synchronized Application getInstance() {
    	if(instance == null) {
    		instance = new Application();
    	}
    	return instance;
    }

    private void initializeApp() {
    	System.out.println("Application.initializeApp()");

		collections = new HashMap<String, Collection> ();
    	ac = new AdamasClient();
    	
    	System.out.println("pcode - " + pcode());
		ac.setPCode(pcode()); 
		
		ec = new EcomClient();
		ec.setPCode(pcode());
		ec.setApiKey(apiKey());
		ec.setSecretKey(secretKey());
		ec.setBackjaxUser(backjaxUser());
		ec.setBackjaxPW(backjaxPW());

    }
	
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Application.class, args);
        System.out.println("main() was called");

        System.out.println("Let's inspect the beans provided by Spring Boot:");

        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }
    }
	public JSONObject gigyaValues() {
		if(gigyaValues == null) {
			gigyaValues = ac.gigyaLogin(gigyaUserName, gigyaPassword);
		}
		return gigyaValues;
	}

	public JSONObject cerberusValues() {
		if(cerberusValues == null) {
			cerberusValues = ac.getAccountTokenFromCerebus(gigyaValues());
		}
		return cerberusValues;
	}
	public String accountToken() {
		if(accountToken == null) {
			accountToken = (String)cerberusValues().get("account_token");
		}
		return accountToken;
	}
	public Object playerTokenValues(String pEmbedCode, boolean pRecursive) {
		Object returnVal = null;
		try {

			returnVal =  ec.playerTokenValues(accountToken(), pEmbedCode);
		}
		catch(Exception e) {
			logger.error(e);
			accountToken = null;
			cerberusValues = null;
			gigyaValues = null;
			
			// retry with new accountToken  // recursion - 
			if(pRecursive == false) {	// only call it once
				System.out.println("Error: retry recursive");
				returnVal = playerTokenValues(pEmbedCode, true);
			}

		}
		return returnVal;

	}
	public Object playerToken(String pEmbedCode) {
		JSONObject values = (JSONObject)playerTokenValues(pEmbedCode, false);
		
		JSONObject token = (JSONObject)values.get("tokens");
		JSONObject embed = (JSONObject)token.get(pEmbedCode);
		System.out.println("embed - " + embed.toString());
		String embedToken = (String)embed.get("embed_token");
		System.out.println("embedToken - " + embedToken);
		return embedToken;
	}
    
	public Collection getCollection(String pKey) {
		Collection collection = null;
		
		collection = collections().get(pKey);
		if(collection == null) {
			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put("q", "name:\"" + pKey + "\"");

			@SuppressWarnings("unchecked")
			LinkedList<JSONObject> results = (LinkedList<JSONObject>) ac.collections(parameters);
			
			if(results.isEmpty()) {  // No Results
				logger.error("Application.getCollection() - Error: No results returned");
				collection = new Collection(new JSONObject());
			}
			else {
				JSONObject jo = (JSONObject)results.getFirst();
				collection = new Collection((JSONObject)jo);
				
				// cache result
				collections().put(pKey, collection);
			}
			collection.setAt(ac);			
			collection.setPCode(pcode());
		}
		return collection;

	}
	
	public Asset assetForId(String pId) {
		Asset returnValue = null;
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("details", "all");
		
		System.out.println("Application.assetForId - pId - " + pId);
		
		JSONObject anAsset = (JSONObject) ac.assetsForId(pId, parameters);
		String errorMessage = (String) anAsset.get("error");
		
		if (errorMessage == null) {
			returnValue = new Asset(anAsset);
			//returnValue.setSession(this);
			returnValue.setPCode(pcode());
			returnValue.setAt(ac);
		} else { // if there is a 404 error - we don't want to keep trying
			returnValue = new Asset(anAsset);
			returnValue.setError(true);
			returnValue.setErrorMap(returnValue.jsonErrorToMap(errorMessage));

			logger.error("Error in Session.assetForId() - "
					+ errorMessage);
		
		}
		return returnValue;
	}

	protected HashMap<String, Collection> collections() {
		if(collections == null) {
			initializeApp();
		}

		return collections;
	}
	
	public Properties properties() {
		// Need to implement a per session override ;)
		
		//if(properties == null) {
		//	properties = app.defaultProperties();
			if(properties == null) {  // added for TestNG
				try {
					//Properties File - file:/Applications/eclipse/Eclipse.app/Contents/MacOS/Ooyala.properties

					System.out.println("Properties File - " + new File(appPath() + "Ooyala.properties").toURI().toString());
					
					properties = new Properties();
					try {
					    //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(pPropertyLocation);
				    	InputStream inputStream = new FileInputStream(new File(appPath() + "OoyalaFlix.properties"));

					    if(inputStream != null) {
					    	properties.load(inputStream);
					    }
					} 
					catch (Exception ex) {
					    ex.printStackTrace();
					}
									}
				catch(Exception e) {
					System.err.println("Session.properties() - exception - " + e);
				}
			}
	//	}
		return properties;
	}

	
	public String pcode() {
    	//return "NvdWQyOvtk4BzdVwx_ZDUBP2UyZq";

	    return properties().getProperty("pcode");
	}
	public String apiKey() {
	    //return "NvdWQyOvtk4BzdVwx_ZDUBP2UyZq.gTE31";
	    return properties().getProperty("apiKey");

	}	
	public String secretKey() {
	    //return "yB6Z5N6Q3XUyoqP4dCBA2tBzGXbbLQFqJ0u-y0Hv";
	    return properties().getProperty("secretKey");

	}	
	public String backjaxUser() {
	    //return "kevinavoy+freyademo@ooyala.com";
	    return properties().getProperty("backjaxUser");

	}	
	public String backjaxPW() {
	    //return "Test1234";
	    return properties().getProperty("backjaxPW");

	}	
	public void setAppPath(String pPath) {
	    appPath = pPath;
	}	
	public String appPath() {
		return appPath;
	}


}
