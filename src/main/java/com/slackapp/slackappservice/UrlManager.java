package com.slackapp.slackappservice;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shashshe
 */
@Service
public class UrlManager {
Map<String,List<String>> sharedUrlMap = new HashMap<>();

    public List<String> urlList(String channelName){
            return sharedUrlMap.get(channelName);
    }

    public void processAndAddUrl(String channelName, String message){
        List<String> urls = sharedUrlMap.get(channelName);
        urls.add("");
    }
}
