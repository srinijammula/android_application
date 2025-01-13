package com.example.loyaltyfirst;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity6 extends AppCompatActivity {

    private String[] fullTransactions; // Class-level variable to store full rows

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);

        RequestQueue queue = Volley.newRequestQueue(this);
        Spinner transactionSpinner = findViewById(R.id.spinner);
        TextView txnPoints = findViewById(R.id.textView19);
        TextView familyId = findViewById(R.id.textView20);
        TextView familyPercent = findViewById(R.id.textView21);
        Button addFamilyPointsButton = findViewById(R.id.button7);
        String cid = getIntent().getStringExtra("cid");

        String urlTransactions = "http://10.0.2.2:8080/loyaltyfirst/Transactions.jsp?cid=" + cid;
        queue.add(new StringRequest(Request.Method.GET, urlTransactions, response -> {
            String[] transactions = response.trim().split("#");
            fullTransactions = transactions; // Store full rows for processing

            String[] transactionRefs = new String[transactions.length];
            for (int i = 0; i < transactions.length; i++) {
                transactionRefs[i] = transactions[i].split(",")[0].trim(); // Extract only tref for display
            }

            transactionSpinner.setAdapter(new ArrayAdapter<>(MainActivity6.this, android.R.layout.simple_spinner_dropdown_item, transactionRefs));
        }, error -> {
            Toast.makeText(MainActivity6.this, "Error fetching transactions.", Toast.LENGTH_SHORT).show();
            Log.e("TransactionsError", "Error: " + error.getMessage());
        }));

        transactionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (fullTransactions != null && position < fullTransactions.length) {
                    String selectedRow = fullTransactions[position]; // Retrieve full row
                    String[] txnDetails = selectedRow.split(","); // Split row into details

                    if (txnDetails.length >= 3) {
                        txnPoints.setText(txnDetails[2].trim()); // Set transaction points
                        familyId.setText("");
                        familyPercent.setText("");

                        String tref = txnDetails[0].trim();
                        String urlSupportFamily = "http://10.0.2.2:8080/loyaltyfirst/SupportFamilyIncrease.jsp?cid=" + cid + "&tref=" + tref;
                        queue.add(new StringRequest(Request.Method.GET, urlSupportFamily, response -> {
                            String[] familyDetails = response.trim().split("#");
                            if (familyDetails.length > 0) {
                                String[] familyInfo = familyDetails[0].split(",");
                                if (familyInfo.length >= 2) {
                                    familyId.setText(familyInfo[0].trim());
                                    familyPercent.setText(familyInfo[1].trim() + "%");
                                }
                            }
                        }, null));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addFamilyPointsButton.setOnClickListener(v -> {
            String fid = familyId.getText().toString().trim();
            String points = txnPoints.getText().toString().trim();
            String urlFamilyIncrease = "http://10.0.2.2:8080/loyaltyfirst/FamilyIncrease.jsp?cid=" + cid + "&fid=" + fid + "&npoints=" + points;
            queue.add(new StringRequest(Request.Method.GET, urlFamilyIncrease, response -> {
                Toast.makeText(MainActivity6.this, points + " Points added to the members of Family ID " + fid, Toast.LENGTH_LONG).show();
            }, error -> {
                Toast.makeText(MainActivity6.this, "Error updating family points.", Toast.LENGTH_SHORT).show();
            }));
        });
    }
}