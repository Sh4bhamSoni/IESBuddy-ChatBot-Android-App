package com.ssgmail.shubhammsoni.iesbuddy;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import ai.api.android.AIConfiguration;
import ai.api.android.AIService;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private static final int REQ_CODE_SPEECH_INPUT = 100;

    Message_list_Adapter message_list_adapter;
    List<Message_Model> message_models;
    EditText chat_box_etx;
    ImageView chatBox_mic_imv, chatbox_sent_imv;
    private AIService aiService;
    LinearLayout chatBox_layout;
    TextView sendmsgtxt, recievemstxt;
    AVLoadingIndicatorView loading;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        sendmsgtxt = findViewById(R.id.text_send_message);
        recievemstxt = findViewById(R.id.text_received_message);
        chatBox_mic_imv = findViewById(R.id.button_mic_chatbox);
        chatBox_layout = findViewById(R.id.layout_chatbox);

        // Setting up Dialogue Flow configurations

        final AIConfiguration config =
                new AIConfiguration("b7e5c19269b544df82e67877a30cba27", AIConfiguration.SupportedLanguages.English,
                        AIConfiguration.RecognitionEngine.System);

        // initialize the AI service

        aiService = AIService.getService(this, config);


//       Instantiate views
        chat_box_etx = findViewById(R.id.edittext_chatbox);
        chatBox_mic_imv = findViewById(R.id.button_mic_chatbox);
        chatbox_sent_imv = findViewById(R.id.button_sent_chatbox);
        recyclerView = findViewById(R.id.recyclarView_message_list);

        chatbox_sent_imv.setVisibility(View.GONE);




        chatBox_mic_imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              promptSpeechInput(view);
            }
        });







//        Setting recyclar view
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        Preparing list of messsage
        message_models = new ArrayList<Message_Model>();
        message_models.add(new Message_Model("Hi \uD83D\uDE03 how can i help ?", false));


//        Preparing Adapter with list of messages
        message_list_adapter = new Message_list_Adapter(message_models, getApplicationContext());

//        setting Adapter to recyclar view
        recyclerView.setAdapter(message_list_adapter);

//        start with latest item i.e Scroll dow Recyclar view on start of activity
        recyclerView.smoothScrollToPosition(message_list_adapter.getItemCount());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


//      Button toggle between send button and Mic button when user types message
        chat_box_etx.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    chatbox_sent_imv.setVisibility(View.GONE);
                    chatBox_mic_imv.setVisibility(View.VISIBLE);
                } else {
                    chatbox_sent_imv.setVisibility(View.VISIBLE);
                    chatBox_mic_imv.setVisibility(View.GONE);
                }


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
    }


    public void promptSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say Something..");
        try {

            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "orry! Your device doesn\\'t support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String userQuery = result.get(0);
                    Log.d("result", userQuery);
                    RetrieveFeedTask task = new RetrieveFeedTask();
                    task.execute(userQuery);
                    message_models.add(new Message_Model(userQuery, true, ""));

                }
                break;
            }
        }
    }


    public void sendMessage(View view) {
        String userQuery = chat_box_etx.getText().toString();
        Log.d("Result", userQuery);
        message_models.add(new Message_Model(userQuery, true, ""));





        recyclerView.setAdapter(message_list_adapter);
        recyclerView.scrollToPosition(message_list_adapter.getItemCount() - 1);
        chat_box_etx.setText("");
        RetrieveFeedTask task = new RetrieveFeedTask();
        task.execute(userQuery);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.nav_btn)
        {
            startActivity(new Intent(getApplicationContext(),Directions.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public String GetText(String query) throws UnsupportedEncodingException {

        String text = "";

        BufferedReader reader = null;


        // Send data

        try {

            // Defined URL  where to send data
            URL url = new URL("https://api.api.ai/v1/query?v=20150910");


            // Send POST data request
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);

            conn.setDoInput(true);


            conn.setRequestProperty("Authorization", "Bearer " + "b7e5c19269b544df82e67877a30cba27");

            conn.setRequestProperty("Content-Type", "application/json");


            //Create JSONObject here

            JSONObject jsonParam = new JSONObject();

            JSONArray queryArray = new JSONArray();

            queryArray.put(query);

            jsonParam.put("query", queryArray);


            jsonParam.put("lang", "en");

            jsonParam.put("sessionId", "1234567890");


            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            Log.d("karma", "after conversion is " + jsonParam.toString());

            wr.write(jsonParam.toString());

            wr.flush();

            Log.d("karma", "json is " + jsonParam);


            // Get the server response


            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();

            String line = null;


            // Read Server Response

            while ((line = reader.readLine()) != null) {

                // Append server response in string

                sb.append(line + "\n");

            }


            text = sb.toString();


            JSONObject object1 = new JSONObject(text);

            Log.d("Json object 1", object1.toString());

            JSONObject object = object1.getJSONObject("result");

            Log.d("Json object", object.toString());

            JSONObject fulfillment = null;

            String speech = null;

//            if (object.has("fulfillment")) {

            fulfillment = object.getJSONObject("fulfillment");

            Log.d("Json object fulfillemnt", fulfillment.toString());

//                if (fulfillment.has("speech")) {

            speech = fulfillment.optString("speech");

//                }

//            }


            Log.d("karma ", "response is " + text);

            return speech;


        } catch (Exception ex) {

            Log.d("karma", "exception at last " + ex);

        } finally {

            try {


                reader.close();

            } catch (Exception ex) {

            }

        }


        return null;

    }

    class RetrieveFeedTask extends AsyncTask<String, Void, String>

    {

        @Override
        protected String doInBackground(String... voids) {
            String s = null;
            try {

                s = GetText(voids[0]);
                Log.d("Do in b void[0]", voids[0]);


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d("karma", "Exception occurred " + e);
            }

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (isCharcterExsits('#', s)) {

                String full = s;
                String[] parts = full.split("#", 2);
                String message = parts[0];
                String url = parts[1];



                message_models.add(new Message_Model(message, false, url));

            } else {



                message_models.add(new Message_Model(s, false));
            }


            recyclerView.setAdapter(message_list_adapter);
            recyclerView.scrollToPosition(message_list_adapter.getItemCount() - 1);

        }
    }

    public boolean isCharcterExsits(char ch, String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ch)
                count++;
        }

        if (count > 0) return true;
        else return false;
    }


}
