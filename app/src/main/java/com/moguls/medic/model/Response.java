package com.moguls.medic.model;
import com.moguls.medic.etc.BaseKeys;

import java.util.List;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


public class Response {

    public int statusCode = 400;
    public JSONObject content;
    private String raw;
    private Map<String, List<String>> headerContent;

    public String getRaw() {
        return raw;
    }

    public Response(int statusCode, String content) {
        this.statusCode = statusCode;
        this.raw = content;
        try {
            this.content = new JSONObject(new JSONTokener(content));
        } catch (JSONException e) {
            try {
                JSONArray jAr = new JSONArray(new JSONTokener(content));
                this.content = new JSONObject();
                this.content.put(BaseKeys.RESULTS, jAr);
            } catch (Exception ex) {
                this.content = new JSONObject();
            }
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setHeaderContent(Map<String, List<String>> headerContent) {
        this.headerContent = headerContent;
    }

}
