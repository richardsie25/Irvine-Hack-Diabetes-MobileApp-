package com.example.myapplication2;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class BolusCalculator extends Activity {

    private EditText editTextCarbRatio, editTextGlucoseReading;
    private double totalCarbsConsumed;
    private double totalBolus;
    private CheckBox checkBoxCondition1, checkBoxCondition2, checkBoxCondition3, checkBoxCondition4;
    private TextView textViewResult;
    private Spinner insulinTimingSpinner;
    private ArrayList<LogData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bolus_calculator);

        textViewResult = findViewById(R.id.textViewResult);
        Intent intent = getIntent();
        totalCarbsConsumed = intent.getDoubleExtra("TotalCarbs", 0);
        Log.d("bolus-calc-44", totalCarbsConsumed+"");
        editTextCarbRatio = findViewById(R.id.editTextCarbRatio);
        editTextGlucoseReading = findViewById(R.id.editTextGlucoseReading);
        checkBoxCondition1 = findViewById(R.id.checkBoxCondition1);
        checkBoxCondition2 = findViewById(R.id.checkBoxCondition2);
        checkBoxCondition3 = findViewById(R.id.checkBoxCondition3);
        checkBoxCondition4 = findViewById(R.id.checkBoxCondition4);

        insulinTimingSpinner = findViewById(R.id.insulinTimingSpinner);
        setupSpinner();
        CheckBox[] checkBoxes = {checkBoxCondition1, checkBoxCondition2, checkBoxCondition3, checkBoxCondition4};
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> calculateBolus());
        }


        Button buttonReturn = findViewById(R.id.buttonReturn);
        buttonReturn.setOnClickListener(v -> finish());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                calculateBolus();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        };

        editTextCarbRatio.addTextChangedListener(textWatcher);
        editTextGlucoseReading.addTextChangedListener(textWatcher);
        dataList = getIntent().getParcelableArrayListExtra("dataList");
        if (dataList == null) {
            Log.d("datalist-single", "empty");
        } else {
            Log.d("datalist-single", "contains something");
            Log.d("datalist-single-so", dataList.toString());
        }
        Button buttonHome = findViewById(R.id.calculatorButton);
        buttonHome.setOnClickListener(v -> {
            Intent intent2 = new Intent(BolusCalculator.this, logsActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent2.putParcelableArrayListExtra("dataList", dataList);

            startActivity(intent2);
//            finish();
        });

        Button buttonDone = findViewById(R.id.buttonDone);
        buttonDone.setOnClickListener(v -> {
            if (areFieldsFilled() && insulinTimingSpinner.getSelectedItemPosition() != 0) {
                Intent sendDataIntent = new Intent(BolusCalculator.this, logsActivity.class);
                Log.d("datalist-bolus-calculator", totalCarbsConsumed +"");
                sendDataIntent.putExtra("CarbsConsumed", totalCarbsConsumed);
                sendDataIntent.putExtra("GlucoseReading", Double.parseDouble(editTextGlucoseReading.getText().toString()));
                sendDataIntent.putExtra("InsulinTiming", insulinTimingSpinner.getSelectedItem().toString());
                sendDataIntent.putExtra("InsulinTaken", totalBolus);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String currentDateTime = dateFormat.format(new Date());
                sendDataIntent.putExtra("CurrentDateTime", currentDateTime);
                sendDataIntent.putParcelableArrayListExtra("dataList", dataList);
                startActivity(sendDataIntent);



            } else {
                Toast.makeText(BolusCalculator.this, "Error: Please fill in all information correctly", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupSpinner() {

        String[] insulinTimings = {"Select Insulin Timing", "Before Breakfast", "After Lunch", "After Dinner"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, insulinTimings) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(position == 0 ? Color.GRAY : Color.BLACK);
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        insulinTimingSpinner.setAdapter(adapter);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void calculateBolus() {
        try {
            double carbRatio = Double.parseDouble(editTextCarbRatio.getText().toString());
            double glucoseReading = Double.parseDouble(editTextGlucoseReading.getText().toString());

            if (carbRatio == 0) {
                textViewResult.setText("Carbohydrate ratio cannot be zero.");
                return;
            }

            double carbBolus = totalCarbsConsumed / carbRatio;
            double glucoseCorrectionFactor = 50;
            double glucoseCorrectionBolus = (glucoseReading - 120) / glucoseCorrectionFactor;

            if (shouldOmitGlucoseCorrection(glucoseReading)) {
                glucoseCorrectionBolus = 0;
            }

            totalBolus = carbBolus + glucoseCorrectionBolus;
            totalBolus = roundBolus(totalBolus);
            textViewResult.setText(String.format("Total Insulin Bolus: %.1f units", totalBolus));
        } catch (NumberFormatException e) {
            textViewResult.setText("Invalid input");
        }
    }

    private boolean areFieldsFilled() {
        String carbRatio = editTextCarbRatio.getText().toString();
        String glucoseReading = editTextGlucoseReading.getText().toString();
        return !carbRatio.isEmpty() && !glucoseReading.isEmpty() && !carbRatio.equals("0");
    }

    private boolean shouldOmitGlucoseCorrection(double glucoseReading) {
        return glucoseReading < 120 || checkBoxCondition1.isChecked() || checkBoxCondition2.isChecked()
                || checkBoxCondition3.isChecked() || checkBoxCondition4.isChecked();
    }

    private double roundBolus(double bolus) {
        double fractionalPart = bolus - (int) bolus;
        return fractionalPart < 0.4 ? Math.floor(bolus) : fractionalPart <= 0.7 ? Math.floor(bolus) + 0.5 : Math.ceil(bolus);
    }
}
