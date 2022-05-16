package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Date;
import java.util.List;

public class JokeModel {
    List<String> categories;
    String created_at;
    URL icon_url;
    String id;
    String updated_at;
    URL url;
    String value;
    JokeModel(JSONObject json)  {
        try {

            JSONArray a = json.getJSONArray("categories");

            for (int i = 0; i<a.length(); i++)
                categories.add(a.get(i).toString());

            this.created_at = json.getString("created_at");
            this.icon_url = new URL(json.getString("icon_url"));
            this.id = json.getString("id");
            this.updated_at = json.getString("updated_at");
            this.url = new URL(json.getString("url"));
            this.value = json.getString("value");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Модель шутки такова: " +
                "категории = " + categories +
                ", создано = " + created_at +
                ", ссылка иконки = " + icon_url +
                ", id = '" + id + '\'' +
                ", обновлено = " + updated_at +
                ", ссылка = " + url +
                ", сама шутка = '" + value + '\''
                ;
    }
}