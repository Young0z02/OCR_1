package com.example.ocr_j;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    TextView tv1;
    Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv1);
        btn1 = findViewById(R.id.btnJson);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AssetManager assetManager = getAssets();

                try {
                    InputStream is = assetManager.open("output.json");
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader reader = new BufferedReader(isr);

                    StringBuilder buffer = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    String jsondata = buffer.toString();

                    JSONObject jsonObject = new JSONObject(jsondata);

                    JSONArray imagesArray = jsonObject.getJSONArray("images");
                    if (imagesArray.length() > 0) {
                        JSONObject firstImage = imagesArray.getJSONObject(0);
                        JSONArray fieldsArray = firstImage.getJSONArray("fields");
                        StringBuilder resultBuilder = new StringBuilder();
                        for (int i = 0; i < fieldsArray.length(); i++) {
                            JSONObject field = fieldsArray.getJSONObject(i);
                            String fieldName = field.getString("name");
                            String fieldValue = field.getString("inferText");
                            resultBuilder.append(fieldName).append(": ").append(fieldValue).append("\n");
                        }
                        String result = resultBuilder.toString();
                        tv1.setText(result);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
