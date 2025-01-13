package com.example.loyaltyfirst;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = findViewById(R.id.login);
        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname=username.getText().toString();
                String pass=password.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                String url1 = "http://10.0.2.2:8080/loyaltyfirst/login?user="+uname+"&pass="+pass;
                StringRequest request1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        String result = s.trim();
                        if (!TextUtils.isEmpty(result) && result.equals("No")){
                            Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String cid = result.split(":")[1];
                            Intent intent1 = new Intent(MainActivity.this, MainActivity2.class);
                            intent1.putExtra("cid", cid);
                            startActivity(intent1);
                        }
                    }
                }, null);
                queue.add(request1);

            }

        });
    }
}