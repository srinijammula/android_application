package com.example.loyaltyfirst;

import static android.R.color.darker_gray;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Date;

public class MainActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        RequestQueue queue = Volley.newRequestQueue(this);
        Spinner prizeSpinner = findViewById(R.id.spinner);
        TextView prizeDesc = findViewById(R.id.textView13);
        TextView pointsNeeded = findViewById(R.id.textView16);
        TableLayout tableLayout = findViewById(R.id.tablelayout);
        String cid = getIntent().getStringExtra("cid");

        String urlPrizeIds = "http://10.0.2.2:8080/loyaltyfirst/PrizeIds.jsp?cid=" + cid;
        StringRequest prizeRequest = new StringRequest(Request.Method.GET, urlPrizeIds, response -> {
            String[] prizes = response.trim().split("#");
            prizeSpinner.setAdapter(new ArrayAdapter<>(MainActivity5.this, android.R.layout.simple_spinner_dropdown_item, prizes));
        }, null);
        queue.add(prizeRequest);

        prizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPrizeId = (String) parent.getItemAtPosition(position);
                String urlRedemption = "http://10.0.2.2:8080/loyaltyfirst/RedemptionDetails.jsp?prizeid=" + selectedPrizeId + "&cid=" + cid;
                StringRequest redemptionRequest = new StringRequest(Request.Method.GET, urlRedemption, response -> {
                    String[] rows = response.trim().split("#");
                    String[] firstRow = rows[0].split(",");
                    prizeDesc.setText(firstRow[0].trim());
                    pointsNeeded.setText(firstRow[1].trim());
                    populateTable(rows, tableLayout);
                }, null);
                queue.add(redemptionRequest);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void populateTable(String[] rows, TableLayout tableLayout) {
        tableLayout.removeAllViews();

        // Add header row
        TableRow headerRow = new TableRow(this);

        TextView headerDate = new TextView(this);
        headerDate.setText("Redemption Date");
        headerDate.setPadding(8, 8, 8, 8);
        headerRow.addView(headerDate);

        TextView headerCenter = new TextView(this);
        headerCenter.setText("Exchange Center");
        headerCenter.setPadding(8, 8, 8, 8);
        headerRow.addView(headerCenter);

        tableLayout.addView(headerRow);

        // Add a line after the header row
        View headerLine = new View(this);
        headerLine.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                2 // Thickness of the line
        ));
        headerLine.setBackgroundColor(getResources().getColor(darker_gray));
        tableLayout.addView(headerLine);

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMM-yy");
        View line1 = new View(this);
        line1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
        line1.setBackgroundColor(getResources().getColor(darker_gray));
        tableLayout.addView(line1);
        for (int i = 0; i < rows.length; i++) {
            String[] columns = rows[i].split(",");
            if (columns.length >= 4) {
                TableRow tableRow = new TableRow(this);

                TextView dateView = new TextView(this);
                try {
                    Date date = inputFormat.parse(columns[2].trim());
                    dateView.setText(outputFormat.format(date));
                } catch (Exception e) {
                    dateView.setText(columns[2].trim());
                }
                dateView.setPadding(8, 8, 8, 8);
                tableRow.addView(dateView);

                TextView centerView = new TextView(this);
                centerView.setText(columns[3].trim());
                centerView.setPadding(8, 8, 8, 8);
                tableRow.addView(centerView);

                tableLayout.addView(tableRow);
                View line = new View(this);
                line.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 3));
                line.setBackgroundColor(getResources().getColor(darker_gray));
                tableLayout.addView(line);
            }
        }
    }
}
