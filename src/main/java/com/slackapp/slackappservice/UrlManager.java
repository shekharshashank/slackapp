package com.slackapp.slackappservice;

// import org.json.simple.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.buf.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/** @author shashshe */
@Service
public class UrlManager {
  static Map<String, List<String>> sharedUrlMap;
  private static final String hook =
      "https://hooks.slack.com/services/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";
  private static final String appMention = "U01AF3PB8PR";

  public List<String> urlList(String channelName) {
    return sharedUrlMap.get(channelName);
  }

  public void processAndAddUrl(JSONObject message) throws JsonProcessingException {
    Object ev = message.get("event");
    LinkedHashMap eventJsonObject = (LinkedHashMap) ev;
    String eventType = (String) eventJsonObject.get("type");
    String text = (String) eventJsonObject.get("text");
    if (eventType.equalsIgnoreCase("app_mention")) {
      if (text.contains(appMention)) {
        sendUrlsSlackMessage(
            sharedUrlMap.getOrDefault((String) eventJsonObject.get("channel"), new ArrayList<>()));
        return;
      }
    }
    if (sharedUrlMap == null) {
      sharedUrlMap = new HashMap<>();
    }
    List<String> urls = extractUrlsFromJson(message);
        sharedUrlMap.getOrDefault((String) eventJsonObject.get("channel"), new ArrayList<>());
    urls.add("test-url");
    sharedUrlMap.put((String) eventJsonObject.get("channel"), urls);
    System.out.println(urls);
  }

  public void sendUrlsSlackMessage(List<String> urls) throws JsonProcessingException {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    ObjectMapper mapper = new ObjectMapper();
    headers.add("Content-Type", "application/json");

    Map<String, String> urlMap = new HashMap<>();
    String urlsStr = StringUtils.join(urls, '\n');
    urlMap.put("text", urlsStr);

    HttpEntity requestEntity = new HttpEntity(mapper.writeValueAsString(urlMap), headers);
    restTemplate.exchange(hook, HttpMethod.POST, requestEntity, String.class);
  }

  public List<String> extractUrlsFromJson(JSONObject message){
    JSONArray blocks =(JSONArray) ((JSONObject)message.get("event")).get("blocks");
    JSONArray elements= new JSONArray();
    for(int i=0; i< blocks.size(); i++){
      if(((JSONObject)blocks.get(i)).getOrDefault("type","").equals("rich_text")){
        elements = (JSONArray)((JSONObject)blocks.get(i)).get("elements");
      }
    }

    JSONArray elementsInner= new JSONArray();
    for(int j=0; j< elements.size(); j++){
      if(((JSONObject)elements.get(j)).getOrDefault("type","").equals("rich_text_section")){
        elementsInner = (JSONArray)((JSONObject)elements.get(j)).getOrDefault("elements", new JSONArray());
      }
    }
    List<String> urls = new ArrayList<>();
    for(int k=0; k< elementsInner.size(); k++){
      JSONObject elmInnerNode  =  (JSONObject)elementsInner.get(k);
      if(elmInnerNode.getOrDefault("type","").equals("link")){
        urls.add((String) elmInnerNode.getOrDefault("url",""));
      }
    }

    elements = (JSONArray)((JSONObject)elements.get(0)).get("elements");
    System.out.println(elements.get(0));
    System.out.println(urls);

    return urls;

  }
}
