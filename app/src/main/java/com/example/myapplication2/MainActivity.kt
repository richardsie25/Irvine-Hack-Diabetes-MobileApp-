package com.example.myapplication2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.example.myapplication2.databinding.LoginBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: LoginBinding
    private lateinit var username: EditText;
    private lateinit var loginButton: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = binding.username;
        loginButton = binding.loginButton;
        loginButton.setOnClickListener( {view -> login()});

    }
    @SuppressLint("SetTextI18n")
    fun login() : Void? {
        intent = Intent(this, ProfileDetailsActivity::class.java);
        startActivity(intent);
        finish();
        return null;
    }

}