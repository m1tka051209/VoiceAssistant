package com.example.voiceassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voiceassistant.activities.image.ImageEngToRu;
import com.example.voiceassistant.activities.image.ImageRuToEng;
import com.example.voiceassistant.activities.text.TextEngToRu;
import com.example.voiceassistant.activities.text.TextRuToEng;
import com.example.voiceassistant.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textEngRu.setOnClickListener(v -> startActivity(new Intent(this, TextEngToRu.class)));
        binding.textRuEng.setOnClickListener(v -> startActivity(new Intent(this, TextRuToEng.class)));
        binding.imgEngRu.setOnClickListener(v -> startActivity(new Intent(this, ImageEngToRu.class)));
        binding.imgRuEng.setOnClickListener(v -> startActivity(new Intent(this, ImageRuToEng.class)));
    }
}