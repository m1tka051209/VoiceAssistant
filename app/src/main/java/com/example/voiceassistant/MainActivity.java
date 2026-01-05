package com.example.voiceassistant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voiceassistant.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textEngRu.setOnClickListener(v -> intentManager("en", "ru", "text"));
        binding.textRuEng.setOnClickListener(v -> intentManager("ru", "en", "text"));
        binding.voiceEngRu.setOnClickListener(v -> intentManager("en", "ru", "voice"));
        binding.voiceRuEng.setOnClickListener(v -> intentManager("ru", "en", "voice"));
//        binding.photoEngRu.setOnClickListener(v -> intentManager("en", "ru", "photo"));
//        binding.photoRuEng.setOnClickListener(v -> intentManager("ru", "en", "photo"));
    }

    private void intentManager(String source, String target, String type) {
        Intent intent = new Intent(this, TranslatorActivity.class);
        intent.putExtra("SOURCE_LANG", source);
        intent.putExtra("TARGET_LANG", target);
        intent.putExtra("INPUT_TYPE", type);
        startActivity(intent);
    }
}