package springapp.web;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.LinkedList;

import com.lightningbolt.adamas.Asset;
import com.lightningbolt.adamas.Collection;
import com.lightningbolt.adamas.Rendition;

import springapp.core.Application;

@Controller
public class MainController {
    protected final Log logger = LogFactory.getLog(getClass());
    public Collection mastHeadCollection;
    public Collection firstRailCollection;
    public Collection secondRailCollection;
    
    @RequestMapping(value={"/Main.htm"}, method = RequestMethod.GET)
    public ModelAndView displayMainPage()  {        
    	Application app = Application.getInstance();

		mastHeadCollection = app.getCollection("Science Fiction: Days of Fear and Wonder"); 
		firstRailCollection = app.getCollection("New Movies"); 
		secondRailCollection = app.getCollection("TV Featured Collection"); 
        
		LinkedList<Collection> railList = new LinkedList<Collection>();
		railList.add(mastHeadCollection);
		railList.add(firstRailCollection);
		railList.add(secondRailCollection);
		
		HashMap<String, Object> myModel = new HashMap<String, Object>();
        myModel.put("masthead", mastHeadCollection);
        myModel.put("railList", railList);
        
		System.out.println("MainController.displayMainPage()");
        
        //return new ModelAndView("main");
       return new ModelAndView("main", "model", myModel);
    }

    @RequestMapping(value="/details.htm", method = RequestMethod.GET)
    public ModelAndView viewDetails(@RequestParam String assetId,@RequestParam(required=false) boolean isV3)  {
    	String embedCode = null;
    	String embedToken = null;
    	
    	Application app = Application.getInstance();
    	System.out.println("isV3 - " + isV3);
    	
    	Asset anAsset = app.assetForId(assetId);
    	Rendition rendition = anAsset.rendition();
    	if(rendition != null) {
        	embedCode = rendition.embed_code();
        	embedToken = (String)app.playerToken(embedCode);
    	}
    	
		HashMap<String, Object> myModel = new HashMap<String, Object>();

        myModel.put("embedToken", embedToken);
        myModel.put("asset", anAsset);
        myModel.put("isV3", isV3);
        return new ModelAndView("detailspage", "model", myModel);
    }    

}