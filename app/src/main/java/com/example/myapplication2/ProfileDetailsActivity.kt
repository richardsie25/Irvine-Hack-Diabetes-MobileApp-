package com.example.myapplication2
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication2.databinding.ProfileDetailsBinding

class ProfileDetailsActivity : AppCompatActivity(){
    private lateinit var binding: ProfileDetailsBinding;
    private lateinit var completeButton: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ProfileDetailsBinding.inflate(layoutInflater);
        setContentView(binding.root);
        completeButton = binding.CompleteButton
        completeButton.setOnClickListener({view -> changeToLogsPage()})
    }

    fun changeToLogsPage() {
        val intent = Intent(this,logsActivity::class.java );
        Toast.makeText(applicationContext, "You've entered your information!", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();
    }
}