package com.slackapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.slackapp.slackappservice.UrlManager;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author shashshe
 */
@Controller
public class SlackAppController {
    @Autowired
    UrlManager urlManager;
    @PostMapping(
            value = "/events",
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public JSONObject slackAppPost(
            @RequestBody String request) throws JsonProcessingException, ParseException {
        System.out.println("request = "+request);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(request);

        String requestType= (String)jsonObject.get("type");
        JSONObject obj = new JSONObject();
        if(requestType!=null && requestType.equalsIgnoreCase("url_verification")){
            obj.put("challenge", jsonObject.get("challenge"));

        }else{
            urlManager.processAndAddUrl(jsonObject);
        }
        System.out.println("response = "+obj);
        return obj;


    }

//    public

}
