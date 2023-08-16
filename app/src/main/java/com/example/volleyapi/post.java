package com.example.volleyapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class post extends AppCompatActivity {

    // creating variables for our edittext,
    // button, textview and progressbar.
    private EditText taskEdt;
    private Button postDataBtn, showBtnData;
    private TextView responseTV;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // initializing our views
        taskEdt = findViewById(R.id.idEdtTask);
        postDataBtn = findViewById(R.id.idBtnPost);
        responseTV = findViewById(R.id.idTVResponse);
        loadingPB = findViewById(R.id.idLoadingPB);
        showBtnData = findViewById(R.id.idBtnShow);


        // show data page
        showBtnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent show = new Intent(post.this,MainActivity.class);
                startActivity(show);
            }
        });

        postDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (taskEdt.getText().toString().isEmpty()){
                    Toast.makeText(post.this, "Please enter both the values", Toast.LENGTH_SHORT).show();
                } else {
                    postDataUsingVolley(taskEdt.getText().toString());
                }
            }
        });
    }

    private void postDataUsingVolley(String task) {
        String url = "https://daily-task-server.glitch.me/task";
        loadingPB.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(post.this);

        // post method
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loadingPB.setVisibility(View.GONE);
                taskEdt.setText("");

                Toast.makeText(post.this, "Data added the api", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    for (int i = 0; i<jsonObject.length();i++){
                        String task = jsonObject.getString("text");
                        responseTV.setText("Task Name : " + task);
                        Log.d("Text", "onResponse: "+ task);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(post.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                // below line we are creating a map for
                // storing our values in key and value pair.
                Map<String, String> params = new HashMap<String, String>();

                // on below line we are passing our key
                // and value pair to our parameters.
                params.put("text", task);

                // at last we are
                // returning our params.
                return params;
            }
        };
        // below line is to make
        // a json object request.
        queue.add(request);
    }
}