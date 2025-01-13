package com.example.loyaltyfirst;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity4 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Spinner spinner = findViewById(R.id.spinner);
        TextView textView7 = findViewById(R.id.textView7); // Transaction Date
        TextView textView23 = findViewById(R.id.textView23); // Transaction Points
        TableLayout tableLayout = findViewById(R.id.tableLayout2); // Table for product details

        ArrayList<String> transactionIds = new ArrayList<>();
        Intent intent = getIntent();
        String cid = intent.getStringExtra("cid"); // Get customer ID
        Log.d("CustomerID", "Received CID: " + cid); // Debugging log
        RequestQueue queue = Volley.newRequestQueue(this);

        // Fetch transaction references (only tref for spinner)
        String url = "http://10.0.2.2:8080/loyaltyfirst/Transactions.jsp?cid=" + cid;
        Log.d("TransactionURL", "Fetching transactions from: " + url);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("TransactionResponse", response); // Log response for debugging
                String[] transactions = response.trim().split("#");
                for (String transaction : transactions) {
                    String[] fields = transaction.split(","); // Split by comma
                    if (fields.length > 0) {
                        transactionIds.add(fields[0]); // Add tref (Transaction ID) to spinner
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity4.this, android.R.layout.simple_spinner_item, transactionIds);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        }, error -> Log.e("VolleyError", "Error fetching transactions: " + error.getMessage()));
        queue.add(request);

        // Handle spinner selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                if (transactionIds.isEmpty()) return;
                String selectedTref = transactionIds.get(position);
                Log.d("SelectedTref", "Selected Transaction Ref: " + selectedTref);

                String url2 = "http://10.0.2.2:8080/loyaltyfirst/TransactionDetails.jsp?tref=" + selectedTref;
                Log.d("DetailsURL", "Fetching details from: " + url2);

                // Fetch transaction details
                StringRequest detailsRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DetailsResponse", response); // Log response for debugging
                        String[] result = response.trim().split("#");

                        if (result.length > 0) {
                            String[] firstEntry = result[0].split(",");
                            if (firstEntry.length > 1) {
                                try {
                                    String timestamp = firstEntry[0].trim();
                                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                    Date date = inputFormat.parse(timestamp);
                                    String formattedDate = outputFormat.format(date);
                                    textView7.setText(formattedDate); // Set transaction date
                                }catch (Exception e) {
                                    textView7.setText(firstEntry[0]);
                                }textView23.setText(firstEntry[1]); // Set transaction points
                            }

                            // Clear the table before adding rows
                            tableLayout.removeAllViews();

                            // Add header row
                            TableRow headerRow = new TableRow(MainActivity4.this);
                            //headerRow.setGravity(Gravity.CENTER);
                            headerRow.addView(createTextView("Prod. Name", true));
                            headerRow.addView(createTextView("Quantity", true));
                            headerRow.addView(createTextView("Points", true));
                            tableLayout.addView(headerRow);

                            // Add product rows
                            for (String entry : result) {
                                String[] fields = entry.split(",");
                                if (fields.length >= 5) { // Ensure valid row data
                                    TableRow row = new TableRow(MainActivity4.this);
                                    row.addView(createTextView(fields[2], false)); // Product Name
                                    row.addView(createTextView(fields[4], false)); // Quantity
                                    row.addView(createTextView(fields[3], false)); // Points
                                    tableLayout.addView(row);
                                }
                            }
                        }
                    }
                }, error -> Log.e("VolleyError", "Error fetching details: " + error.getMessage()));
                queue.add(detailsRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Helper method to create TextView for table cells
    private TextView createTextView(String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setGravity(Gravity.CENTER);
        if (isHeader) {
            textView.setTextSize(16);
            textView.setTypeface(null, android.graphics.Typeface.BOLD);
        } else {
            textView.setTextSize(14);
        }
        return textView;
    }
}
