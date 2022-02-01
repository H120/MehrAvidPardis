package com.example.myapplication;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class SaveJson {
    String text = "";
    ContextWrapper contextWrapper;
    File root;
    File file;
    String form_1_url = "form_1.json",form_2_url = "form_2.json",form_3_url = "form_3.json";
    String formselected = form_2_url;

    public void Save(ArrayList<UserList> userLists, String name , String description , String shouldScan, Context context){

        text = "{\n" +
                "    \"name\": \""+name+"\",\n" +
                "    \"description\": \""+description+"\",    \n" +
                "    \"shouldScan\": \""+shouldScan+"\",\n" +
                "    \"row\": [";

        for (int i = 0; i < userLists.size(); i++) {

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("orderShow", userLists.get(i).getorderShow());
                jsonObject.put("value", userLists.get(i).getvalue());
                jsonObject.put("title", userLists.get(i).gettitle());
                jsonObject.put("placeholder", userLists.get(i).getplaceholder());
                jsonObject.put("ModelShow", userLists.get(i).getModelShow());
            } catch (JSONException e) {
                e.printStackTrace();

            }

            text = text + jsonObject.toString()+",\n";

        }

        text=text+"    ]\n" +
                "}";


        contextWrapper = new ContextWrapper(context);
        root = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        file = new File(root, formselected);

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file+".txt");
            fileOutputStream.write(text.getBytes());
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}