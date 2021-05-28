package com.slackapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.slackapp.slackappservice.UrlManager;
import org.json.simple.JSONObject;
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
    public JSONObject uplevelOrgEncryption(
            @RequestBody JSONObject request) throws JsonProcessingException {
        System.out.println("request = "+request);
        String requestType= (String)request.get("type");
        JSONObject obj = new JSONObject();
        if(requestType!=null && requestType.equalsIgnoreCase("url_verification")){
            obj.put("challenge", request.get("challenge"));

        }else{
            urlManager.processAndAddUrl(request);
        }
        System.out.println("response = "+obj);
        return obj;


    }

//    public

}
