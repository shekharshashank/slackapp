package com.slackapp.slackappservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

  @Autowired UrlManager urlManager;

  @Test
  void contextLoads() {}

  @Test
  void testSlackHook() throws JsonProcessingException {
    urlManager.sendUrlsSlackMessage(Arrays.asList("test1.com", "test2.com"));
  }

  @Test
  void queryUrlsTest() throws IOException, ParseException {
    JSONParser parser = new JSONParser();
    Reader reader = new FileReader("/Users/shashshe/workspace/slackapp/src/test/resources/testEventWithUrl.json");
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    urlManager.processAndAddUrl(jsonObject);
//    urlManager.sendUrlsSlackMessage(Arrays.asList("test1.com", "test2.com"));
  }

  @Test
  void testGetUrls() throws IOException, ParseException {
    JSONParser parser = new JSONParser();
    Reader reader = new FileReader("/Users/shashshe/workspace/slackapp/src/test/resources/testEventWithUrl.json");
    JSONObject jsonObject = (JSONObject) parser.parse(reader);
    JSONArray blocks =(JSONArray) ((JSONObject)jsonObject.get("event")).get("blocks");
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
  }
}
