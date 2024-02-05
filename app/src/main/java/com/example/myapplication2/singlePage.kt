package com.example.myapplication2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication2.databinding.SingleDataBinding

class singlePage: AppCompatActivity() {
    private lateinit var binding: SingleDataBinding;
    private lateinit var returnButton: Button;
//    private var dataList = intent.getParcelableArrayListExtra<com.example.myapplication2.LogData>("dataList")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState);
        binding = SingleDataBinding.inflate(layoutInflater);
        setContentView(binding.root);
        returnButton = binding.returnButton;
        val dataList =
            intent.getParcelableArrayListExtra<LogData>("dataList")
        if (dataList == null){
            Log.d("datalist-single", "empty");
        }
        else{
            Log.d("datalist-single", "contains something");
            Log.d("datalist-single-so", dataList.toString());
        }
        returnButton.setOnClickListener({view -> returnToMainPage(dataList)});

        populatePage(intent,);
    }
    fun returnToMainPage(dataList: ArrayList<LogData>? ){

        val intent = Intent(this, logsActivity::class.java);
        Log.d("datalist", dataList.toString());
        intent.putParcelableArrayListExtra("dataList", dataList);

        startActivity(intent);
    }
    // Helper Function
    @SuppressLint("SetTextI18n")
    fun populatePage(intent: Intent){
        // Set the textboxes to variables to modify their text
        val dateText = findViewById(R.id.single_date) as TextView;
        val timeText = findViewById(R.id.single_time) as TextView;
        val mealTypeText = findViewById(R.id.single_mealType) as TextView;
        val glucoseReadingText = findViewById(R.id.single_glucoseReading) as TextView;
        val carbsConsumedText = findViewById(R.id.single_carbsConsumed) as TextView;
        val insulinTakenText = findViewById(R.id.single_insulinTaken) as TextView;
        // Modifying texts
        dateText.text = "DATE: " + intent.getStringExtra("date")
        timeText.text = "TIME: " + intent.getStringExtra("time")
        mealTypeText.text = "MEAL TYPE:  " + intent.getStringExtra("mealType");
        glucoseReadingText.text = "GLUCOSE READING: " + intent.getStringExtra("glucoseReading") + " mg/dL";
        carbsConsumedText.text = "CARBOHYDRATES CONSUMED: " + intent.getStringExtra("carbsConsumed") + " grams";
        insulinTakenText.text = "INSULIN TAKEN: " + intent.getStringExtra("insulinTaken") + " units";
    }
}