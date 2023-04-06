package com.hightech.highttechtest;



import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    public static final String mUrl = "https://open.er-api.com/v6/latest/USD";
    EditText etCurrency1,etCurrency2;
    Spinner spinner1,spinner2;
    String selectCurrency1,selectCurrency2;
    String strAmt1,strAmt2;
    DecimalFormat precision = new DecimalFormat("#.##");
    HashMap<String,Double> map = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ViewDataBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        activityMainBinding.setVariable(BR.viewModel,new ViewModel());
//        activityMainBinding.executePendingBindings();
        etCurrency1 = findViewById(R.id.editText1);
        etCurrency2 = findViewById(R.id.edittext2);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinnar2);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new mHttpURLConnection(mUrl));
    }

    class mHttpURLConnection implements Runnable{
        String strUrl;
        mHttpURLConnection(String url){
            this.strUrl = url;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(strUrl);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                connection.disconnect();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateData(response.toString());
                    }
                });



            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

    public void updateData(String response){
        JsonParser parser = new JsonParser();
        JsonObject jArry = (JsonObject) parser.parse(response).getAsJsonObject().get("rates");
        map = new Gson().fromJson(jArry.toString(), HashMap.class);
        ArrayList<String> countriesName = new ArrayList<>(map.keySet());
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item,countriesName);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter1);



        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCurrency1 =parent.getItemAtPosition(position).toString();
                strAmt1 = etCurrency1.getText().toString().trim();
                if (map.size() > 0) {
                    convertCurrency(map, "");
                    convertCurrency2(map, "");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCurrency2 =parent.getItemAtPosition(position).toString();
                strAmt2 = etCurrency2.getText().toString().trim();
                if (map.size() > 0) {
                    convertCurrency(map, "");
                    convertCurrency2(map, "");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etCurrency1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                convertCurrency(map,s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCurrency2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {

                    convertCurrency2(map,s);
//                    if (etCurrency2.requestFocus()) {
//                        double currencyValue = (double) map.get(selectCurrency1);
//                        double number = Double.parseDouble(s.toString());
//                        double result = currencyValue * number;
//                        String str = String.valueOf(doubleToStringNoDecimal(result));
//                        etCurrency1.setText(str);
//                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###.##");
        return formatter.format(d);
    }

    public void convertCurrency(HashMap<String,Double> map,CharSequence s){
        if (etCurrency1.isFocused()) {
            String firstAmountEnter = etCurrency1.getText().toString().trim();
            try {
                if (s == null || s.toString().isEmpty()) s = "0";
                double fstCurValueDollar = map.get(selectCurrency2);
                double secCurValueDollar = map.get(selectCurrency1);
                double number = Double.parseDouble(firstAmountEnter.toString());
                double result = fstCurValueDollar * number;
                double result2 = result / secCurValueDollar;

                String str = String.valueOf(doubleToStringNoDecimal(result2));
                etCurrency2.setText(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }





    }
    public void convertCurrency2(HashMap<String,Double> map,CharSequence s){
        if (etCurrency2.isFocused()) {
            String firstAmountEnter = etCurrency2.getText().toString().trim();
            try {
                if (s == null || s.toString().isEmpty()) s = "0";
                double fstCurValueDollar = map.get(selectCurrency1);
                double secCurValueDollar = map.get(selectCurrency2);
                double number = Double.parseDouble(firstAmountEnter.toString());
                double result = fstCurValueDollar * number;
                double result2 = result / secCurValueDollar;

                String str = String.valueOf(doubleToStringNoDecimal(result2));
                etCurrency1.setText(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }





    }




}