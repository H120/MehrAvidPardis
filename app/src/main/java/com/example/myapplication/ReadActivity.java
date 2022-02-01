package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ReadActivity extends AppCompatActivity {

    TextView tv_one_text,tv_two_text,tv_three_text,tv_four_text,tv_five_text,tv_statuschange,tv_nfcid;
    EditText et_name, et_family,et_address;
    RadioButton rb_male,rb_female;
    CheckBox cb_one, cb_two, cb_three;

    Button bt_changedata;
    String site_url = "http://forms.avidp.com/";
    String form_1_url = "form_1.json",form_2_url = "form_2.json",form_3_url = "form_3.json";
    String formselected = form_1_url;
    String checkedtext="";
    String nfcid= "";
    public ProgressDialog mProgressDialog;
    ContextWrapper contextWrapper;
    File root;
    File file;
    ArrayList<UserList> userdata =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        init();
        downloadfile();
        setonclicklistener();

    }

    private void init(){
        Bundle extras = getIntent().getExtras();
        nfcid = extras.getString("nfcid");
        Toast.makeText(getApplicationContext(),nfcid.toString(),Toast.LENGTH_SHORT).show();
        tv_one_text = (TextView) findViewById(R.id.tv_one_text);
        tv_two_text = (TextView) findViewById(R.id.tv_two_text);
        tv_three_text = (TextView) findViewById(R.id.tv_three_text);
        tv_four_text = (TextView) findViewById(R.id.tv_four_text);
        tv_five_text = (TextView) findViewById(R.id.tv_five_text);
        tv_statuschange = (TextView) findViewById(R.id.tv_statuschange);
        tv_nfcid = (TextView) findViewById(R.id.tv_nfcid);

        et_name = (EditText) findViewById(R.id.et_name);
        et_family = (EditText) findViewById(R.id.et_family);
        et_address = (EditText) findViewById(R.id.et_address);

        rb_male=(RadioButton) findViewById(R.id.rb_male);
        rb_female=(RadioButton) findViewById(R.id.rb_female);
        cb_one =(CheckBox) findViewById(R.id.cb_one);
        cb_two =(CheckBox) findViewById(R.id.cb_two);
        cb_three =(CheckBox) findViewById(R.id.cb_three);

        bt_changedata = (Button) findViewById(R.id.bt_changedata);

        tv_nfcid.setText("شماره کارت انتخابی : "+nfcid);
        contextWrapper = new ContextWrapper(getApplicationContext());
        root = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        file = new File(root, formselected);

        // instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(this); // MainActivity = activity name
        mProgressDialog.setMessage("A message");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);


    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            wl.acquire();

            try {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(sUrl[0]);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    // expect HTTP 200 OK, so we don't mistakenly save error report
                    // instead of the file
                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
                        return "Server returned HTTP " + connection.getResponseCode()
                                + " " + connection.getResponseMessage();

                    // this will be useful to display download percentage
                    // might be -1: server did not report the length
                    int fileLength = connection.getContentLength();

                    // download the file
                    input = connection.getInputStream();


                    output = new FileOutputStream(file.getPath());

                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        // allow canceling with back button
                        if (isCancelled())
                            return null;
                        total += count;
                        // publishing the progress....
                        if (fileLength > 0) // only if total length is known
                            publishProgress((int) (total * 100 / fileLength));
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    return e.toString();
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }

                    if (connection != null)
                        connection.disconnect();
                }
            } finally {
                wl.release();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            if (result != null) {
                Toast.makeText(context, "دانلود با مشکل مواجه شد : " + result, Toast.LENGTH_LONG).show();
                Log.e("TAG", "onPostExecute: " + result);
            } else
                Toast.makeText(context, "فایل دانلود و در حافظه ذخیره شد", Toast.LENGTH_LONG).show();

//            textView.setText(readfile());


            String str = "{\n" +
                    "    \"name\": \"عنوان فرم یک\",\n" +
                    "    \"description\": \"توضیحات فرم یک\",    \n" +
                    "    \"shouldScan\": \"both\",\n" +
                    "    \"row\": [\n" +
                    "        {\n" +
                    "            \"orderShow\": \"1\",            \n" +
                    "            \"value\": \"NULL\",          \n" +
                    "            \"title\": \"نام\",\n" +
                    "            \"placeholder\": \"\",                        \n" +
                    "            \"ModelShow\": \"Text\"            \n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"orderShow\": \"3\",\n" +
                    "            \"value\": \"زن=1|مرد=0\",\n" +
                    "            \"title\": \"جنسیت\",\n" +
                    "            \"placeholder\": \"\",\n" +
                    "            \"ModelShow\": \"Radio\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"orderShow\": \"2\",\n" +
                    "            \"value\": \"NULL\",\n" +
                    "            \"title\": \"نام خانوادگی\",      \n" +
                    "            \"placeholder\": \"\",\n" +
                    "            \"ModelShow\": \"Text\"           \n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"orderShow\": \"5\",            \n" +
                    "            \"value\": \"NULL\",            \n" +
                    "            \"title\": \"آدرس\",            \n" +
                    "            \"placeholder\": \"آدرس محل سکونت\",            \n" +
                    "            \"ModelShow\": \"Textarea\"            \n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"orderShow\": \"4\",            \n" +
                    "            \"value\": \"عنوان سه=3|عنوان دو=2|عنوان یک=1\",            \n" +
                    "            \"title\": \"گزینه های مورد نظر خود را انتخاب نمایید:\",            \n" +
                    "            \"placeholder\": \"\",            \n" +
                    "            \"ModelShow\": \"Checkbox\"            \n" +
                    "        },\n" +
                    "\n" +
                    "    ]\n" +
                    "}";

            ParseJson parseJson = new ParseJson();
            userdata = parseJson.read(readfile());

            if (userdata.size()>3) {
                showdata();
            }

        }

    }

    private String readfile() {

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();
    }

    private void showdata(){

        tv_one_text.setText(userdata.get(0).gettitle());
        tv_two_text.setText(userdata.get(1).gettitle());
        tv_three_text.setText(userdata.get(2).gettitle());
        tv_four_text.setText(userdata.get(3).gettitle());
        tv_five_text.setText(userdata.get(4).gettitle());
        switch (userdata.get(0).getshouldScan()){
            case "both":{
                tv_statuschange.setText("شما میتوانید به صورت دستی فرم را پر کنید");
                tv_statuschange.setTextColor(Color.GREEN);
                break;
            } case "scan":{
                tv_statuschange.setText("شما فقط با کارت میتوانید اطلاعات را وارد کنید");
                tv_statuschange.setTextColor(Color.RED);

                break;
            }
        }

        int positionone =0;
        int positiontwo =0;

        for (int i = -1; (i = userdata.get(3).getvalue().indexOf("|", i + 1)) != -1; i++) {
            System.out.println(i);
            if (positionone==0)positionone=i;
            else positiontwo=i;

        }
        cb_one.setText(userdata.get(3).getvalue().substring(0,userdata.get(3).getvalue().indexOf("|")));
        cb_two.setText(userdata.get(3).getvalue().substring(positionone+1,positiontwo));
        cb_three.setText(userdata.get(3).getvalue().substring(positiontwo+1));

    }

    private void savedata(){

        ArrayList<UserList> userdata_forsave =new ArrayList<>();
        userdata_forsave=userdata;

        userdata_forsave.get(0).setvalue(et_name.getText().toString());
        userdata_forsave.get(1).setvalue(et_family.getText().toString());
        if (rb_male.isChecked()) {
            userdata_forsave.get(2).setvalue("مرد");
        }else if (rb_female.isChecked()){
            userdata_forsave.get(2).setvalue("زن");
        }


        if (cb_one.isChecked()) checkedtext=checkedtext+cb_one.getText().toString();
        if (cb_two.isChecked())checkedtext=checkedtext+cb_two.getText().toString();
        if (cb_three.isChecked())checkedtext=checkedtext+cb_three.getText().toString();

        userdata_forsave.get(3).setvalue(checkedtext);
        if (checkedtext=="") userdata_forsave.get(3).setvalue("موردی انتخاب نشده است !");
        checkedtext="";


        userdata_forsave.get(4).setvalue(et_address.getText().toString());

        SaveJson saveJson=new SaveJson();
        saveJson.Save(userdata_forsave,userdata.get(0).getname(),userdata.get(0).getdescription(),userdata.get(0).getshouldScan(),this);

    }

    private void downloadfile(){
        // execute this when the downloader must be fired
        final DownloadTask downloadTask = new DownloadTask(this); // MainActivity = activity name
        downloadTask.execute(site_url + formselected); // the url to the file you want to download
    }

    private void setonclicklistener(){

        bt_changedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                savedata();

            }
        });
    }

}