package com.example.loyaltyfirst;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity3 extends AppCompatActivity {

    private static final String TAG = "MainActivity3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        // Initialize TextView
        TextView textView6 = findViewById(R.id.textView6);

        // Get CID from Intent
        Intent intent = getIntent();
        String cid = intent.getStringExtra("cid");

        // Check if CID is valid
        if (cid == null || cid.isEmpty()) {
            Log.e(TAG, "CID is null or empty!");
            textView6.setText("Error: No CID provided.");
            return;
        }
        Log.d(TAG, "CID received: " + cid);

        // Create the URL for fetching transactions
        String url4 = "http://10.0.2.2:8080/loyaltyfirst/Transactions.jsp?cid=" + cid;
        Log.d(TAG, "Generated URL: " + url4);

        // Create a Volley RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Make a StringRequest
        StringRequest request = new StringRequest(Request.Method.GET, url4, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG, "Server Response: " + s);

                // Handle empty server response
                if (s == null || s.trim().isEmpty()) {
                    Log.e(TAG, "Empty response received from server.");
                    textView6.setText("No transactions available.");
                    return;
                }

                // Process the response
                String[] transactions = s.trim().split("#"); // Split transactions by #
                StringBuilder formattedResponse = new StringBuilder();

                // Add table headers
                formattedResponse.append(String.format("%-10s %-15s %-10s %s\n", "TXN Ref", "Date", "Points", "Total"));
                formattedResponse.append("--------------------------------------------------------------\n");

                // Process each transaction
                for (String transaction : transactions) {
                    if (transaction.trim().isEmpty()) continue; // Skip empty transactions
                    String[] parts = transaction.split(","); // Split fields by ,

                    if (parts.length >= 4) {
                        String transactionId = parts[0];
                        String datePart = parts[1].split(" ")[0]; // Extract date only
                        String points = parts[2];
                        String total = parts[3];

                        formattedResponse.append(String.format("%-10s %-15s %-10s %s\n", transactionId, datePart, points, total));
                    } else {
                        Log.e(TAG, "Malformed transaction: " + transaction);
                    }
                }

                // Display the formatted transactions in TextView
                textView6.setText(formattedResponse.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Volley Error: " + error.toString());
                textView6.setText("Error fetching transactions: " + error.getMessage());
                Toast.makeText(MainActivity3.this, "Failed to connect to server.", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the queue
        queue.add(request);
    }
}
