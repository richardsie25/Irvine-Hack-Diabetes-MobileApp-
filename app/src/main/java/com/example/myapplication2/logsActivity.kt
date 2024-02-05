package com.example.myapplication2

//import androidx.appcompat.app.AlertDialog
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication2.databinding.LogsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class logsActivity : AppCompatActivity() {
    private lateinit var binding: LogsBinding;


    private lateinit var addUserData: Button;
    private lateinit var calculatorButton: Button;
    private lateinit var userDataList: ListView;
    private lateinit var arrayAdapter: ArrayAdapter<*>;
    private var userArrayList = ArrayList<LogData>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        // Setting of variables
        binding = LogsBinding.inflate(layoutInflater);
        setContentView(binding.root);
        userDataList = binding.userDataList;
        addUserData = binding.addData;
        calculatorButton = binding.calculatorButton;
        calculatorButton.setOnClickListener({ view -> changeToCarbCalculatorScreen() });
        addUserData.setOnClickListener({ view ->
            showAlertDialog(
                "Enter Requested Information",
                ""
            )
        });

        val dataList =
            intent.getParcelableArrayListExtra<LogData>("dataList")
        if (dataList == null){
            Log.d("datalist-logsAct-create", "empty");
        }
        else{
            Log.d("datalist=logsAct-create", "contains something");
            userArrayList = dataList;
        }
        if (intent.hasExtra("CurrentDateTime") == true) {
            Log.d("datalist-return", "Received some data back")
            Log.d("datalist-return2", intent.getDoubleExtra("CarbsConsumed", 0.0).toString());
            Log.d("datalist-return3", intent.getStringExtra("CurrentDateTime").toString());
            Log.d("datalist-return4", intent.getDoubleExtra("GlucoseReading", 0.0).toString());
            val dateAndTime = intent.getStringExtra("CurrentDateTime")!!.split(" ");

            userArrayList.add(
                LogData(
                    dateAndTime.get(0),
                    dateAndTime.get(1),
                    intent.getStringExtra("InsulinTiming").toString(),
                    intent.getDoubleExtra("GlucoseReading", 0.0).toString(),
                    intent.getDoubleExtra("CarbsConsumed", 0.0).toString(),
                    intent.getDoubleExtra("InsulinTaken", 0.0).toString()
                )
            )
        }
        else{
            Log.d("datalist-return-FAILED", "Did not receive any data");
        }
        // Saving the data for user regardless if user exits app
        userDataList.setOnItemClickListener(
            { parent, view, position, id ->
                showSinglePage(parent, view, position, id)
            });
        updateList();
    }

    fun changeToCarbCalculatorScreen() {
        val intent = Intent(this, CarbCalculator::class.java);
        intent.putParcelableArrayListExtra("dataList", userArrayList)

        startActivity(intent);
    }

    fun showSinglePage(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val intent = Intent(this, singlePage::class.java);
        val singleData = userDataList.getItemAtPosition(position) as LogData;
        // Sending all required fields to the next activity
        // to allow it populate the page
        intent.putExtra("date", singleData.date);
        intent.putExtra("time", singleData.time);
        intent.putExtra("mealType", singleData.mealType);
        intent.putExtra("glucoseReading", singleData.glucoseReading);
        intent.putExtra("carbsConsumed", singleData.carbsConsumed);
        intent.putExtra("insulinTaken", singleData.insulinTaken);
        intent.putParcelableArrayListExtra("dataList", userArrayList)

        startActivity(intent)

    }
    // Delete later
    fun showAlertDialog(title: String?, msg: String?) {
        val alertDialog = AlertDialog.Builder(this)

        // Modify the layout that will appear on the alert
        val lila1 = LinearLayout(this)
        lila1.orientation = LinearLayout.VERTICAL
        val text = EditText(this);
        text.hint = "Enter Glucose Reading"
        val text2 = EditText(this);
        text2.hint = "Enter Time"
        val text3 = EditText(this);
        text3.hint = "Enter Insulin Taken"
        lila1.addView(text)
        lila1.addView(text2)
        lila1.addView(text3)


        // Setting the alertDialog and modify it to appear on mobile
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setView(lila1)

//        alertDialog.create();
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(Date())
        // Setting OK Button
        alertDialog.setPositiveButton("OK") { dialog, which ->
            Toast.makeText(
                applicationContext,
                "Log Created!", Toast.LENGTH_SHORT
            ).show();
            Log.d("logs-text1", text.text.toString());
            Log.d("logs-text2", text2.text.toString());
            Log.d("logs-text3", text3.text.toString());
//            userData.add(text.text.toString().toInt());
            userArrayList.add(
                LogData(
                    currentDate.toString(), text2.text.toString(),
                    "N/A", text.text.toString(), "N/A", text3.text.toString()
                )
            );
            updateList();
        }

        // Showing Alert Message
        alertDialog.show()
    }

    // Updates the userArraylist and populate the ListView
    private fun updateList(): Void? {
        arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1, userArrayList
        );
        userDataList.adapter = arrayAdapter;
        return null;

    }
}

