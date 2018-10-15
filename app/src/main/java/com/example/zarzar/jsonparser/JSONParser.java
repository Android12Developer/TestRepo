package com.example.zarzar.jsonparser;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class JSONParser
{
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    static String tag="JSONParser";

    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params)
    {
        // Making HTTP request
        try {
            Log.i(tag, "do http request....");
            Log.i(tag, "locale="+Utility.languagePref);
            Log.i(tag, "url="+url);
            jObj=new JSONObject();
            // check for request method
            if(method == "POST"){
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost postRequest = new HttpPost(url);
                postRequest.setEntity(new UrlEncodedFormEntity(params,"utf-8"));// use to add data from keyvalue Use this command
                //postRequest.addHeader("Content-Type","application/json");
                postRequest.addHeader("ApiKey",Utility.api_key);
                postRequest.addHeader("LanguagePref",Utility.languagePref);
                HttpResponse httpResponse = httpClient.execute(postRequest);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }else if(method == "GET"){
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet getRequest = new HttpGet(url);
                getRequest.addHeader("ApiKey",Utility.api_key);
                getRequest.addHeader("LanguagePref",Utility.languagePref);
                HttpResponse httpResponse = httpClient.execute(getRequest);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            Log.i(tag, "UnsupportedEncodingException***");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.i(tag, "ClientProtocolException***");
            e.printStackTrace();
        } catch (HttpHostConnectException e) {
            Log.i(tag, "HttpHostConnectException***");
            e.printStackTrace();
        }catch (IOException e) {
            Log.i(tag, "IOException***");
            e.printStackTrace();
        }
        catch (IllegalArgumentException e) {
            Log.i(tag, "IllegalArgumentException___");
        }

        if(is!=null){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
                Log.i(tag, "json string="+json);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            // try parse the string to a JSON object
            try
            {
                Log.i(tag, "try to convert json str to json obj");
                jObj = new JSONObject(json);
            } catch (JSONException e)
            {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
        }
        // return JSON Obj
        return jObj;
    }

    public JSONObject postJsonObject(String url, JSONObject data)
    {
        try {
            Log.i(tag, "do http request....");
            Log.i(tag, "locale="+Utility.languagePref);
            jObj=new JSONObject();
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(url);
            Log.i(tag, "data==>"+data);
            postRequest.setEntity(new StringEntity(data.toString(),"utf-8"));
            postRequest.addHeader("Content-Type","application/json");
            postRequest.addHeader("ApiKey",Utility.api_key);
            postRequest.addHeader("LanguagePref",Utility.languagePref);
            HttpResponse httpResponse = httpClient.execute(postRequest);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            Log.i(tag, "UnsupportedEncodingException***");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Log.i(tag, "ClientProtocolException***");
            e.printStackTrace();
        } catch (HttpHostConnectException e) {
            Log.i(tag, "HttpHostConnectException***");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i(tag, "IOException***");
            e.printStackTrace();
        }

        if(is!=null){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
                Log.i(tag, "json string="+json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        // return JSON Obj
        return jObj;
    }
    }
