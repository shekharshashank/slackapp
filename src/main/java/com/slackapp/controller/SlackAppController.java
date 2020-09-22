package com.slackapp.controller;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author shashshe
 */
@Controller
public class SlackAppController {
    @PostMapping(
            value = "test",
            produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public JSONObject uplevelOrgEncryption(
            @RequestBody JSONObject request) {
        System.out.println("request = "+request);
        String requestType= (String)request.get("type");
        JSONObject obj = new JSONObject();
        if(requestType!=null && requestType.equalsIgnoreCase("url_verification")){
            obj.put("challenge", request.get("challenge"));

        }
        System.out.println("response = "+obj);
        return obj;


    }

}
