package com.example.loyaltyfirst;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        RequestQueue queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        String cid = intent.getStringExtra("cid");
        String url2 = "http://10.0.2.2:8080/loyaltyfirst/info.jsp?cid="+cid;
        String image_url="http://10.0.2.2:8080/loyaltyfirst/images/"+cid+".jpg";
        TextView name = findViewById(R.id.name);
        TextView points = findViewById(R.id.points);
        ImageView imageView = findViewById(R.id.image);
        Button all_txns = findViewById(R.id.allTxns);
        Button txn_detail = findViewById(R.id.txnDetail);
        Button redemption_detail = findViewById(R.id.redmptnDetail);
        Button add_to_family = findViewById(R.id.addToFamily);
        Button exit= findViewById(R.id.exit);

        StringRequest request2 = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                String[] result = s.trim().split(",");
                name.setText(result[0]);
                points.setText(result[1]);
            }
        }, null);
        queue.add(request2);
        ImageRequest request3=new ImageRequest(image_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }
        },0,0,null,null);
        queue.add(request3);

        // Button leading to Activity 3
        all_txns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_allTxns = new Intent(MainActivity2.this, MainActivity3.class);
                intent_allTxns.putExtra("cid", cid);
                startActivity(intent_allTxns);
            }
        });

        // Button leading to Activity 4
        txn_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_txnDetail = new Intent(MainActivity2.this, MainActivity4.class);
                intent_txnDetail.putExtra("cid", cid);
                startActivity(intent_txnDetail);
            }
        });

        // Button leading to Activity 5
        redemption_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_redmptnDetail = new Intent(MainActivity2.this, MainActivity5.class);
                intent_redmptnDetail.putExtra("cid", cid);
                startActivity(intent_redmptnDetail);
            }
        });

        // Button leading to Activity 6
        add_to_family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_addToFamily = new Intent(MainActivity2.this, MainActivity6.class);
                intent_addToFamily.putExtra("cid", cid);
                startActivity(intent_addToFamily);
            }
        });

        // Button for exit
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });

    }
}