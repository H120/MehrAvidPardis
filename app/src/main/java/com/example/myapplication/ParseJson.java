package com.example.myapplication;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ParseJson {

    public ArrayList<UserList> read(String jsonStr){
        JSONObject jObj = null;
        ArrayList<UserList> userList= new ArrayList<>();

        try {

            jObj = new JSONObject(jsonStr);

            String jsonArryname = jObj.getString("name");
            String jsonArrydescription = jObj.getString("description");
            String jsonArryshouldScan = jObj.getString("shouldScan");

            JSONArray jsonArry = jObj.getJSONArray("row");

            for (int i = 0; i < jsonArry.length()-1; i++) {
                UserList user=new UserList();

                JSONObject obj = jsonArry.getJSONObject(i);

                user.setname(jsonArryname);
                user.setdescription(jsonArrydescription);
                user.setshouldScan(jsonArryshouldScan);
                user.setorderShow(obj.getString("orderShow"));
                user.setvalue(obj.getString("value"));
                user.settitle(obj.getString("title"));
                user.setplaceholder(obj.getString("placeholder"));
                user.setModelShow(obj.getString("ModelShow"));
                userList.add(user);

            }

            Collections.sort(userList);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("a", "Error1: "+ e.toString() );
        }
        return userList;

    }
}
