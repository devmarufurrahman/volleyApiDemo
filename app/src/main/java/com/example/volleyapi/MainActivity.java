package com.example.volleyapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> nameList = new ArrayList<>();
    String url = "https://daily-task-server.glitch.me/task/";
    ListView listView;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        loadingPB = findViewById(R.id.idLoading);


        getData();

    }

    private void getData() {
        loadingPB.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                loadingPB.setVisibility(View.GONE);
                try {
                    JSONArray jsonArray = new JSONArray(response);


                    for (int i = 0; i< jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("text");
                        nameList.add(name);
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, nameList);
                    listView.setAdapter(arrayAdapter);

                } catch (JSONException e) {
                    loadingPB.setVisibility(View.GONE);
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingPB.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue  = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
}