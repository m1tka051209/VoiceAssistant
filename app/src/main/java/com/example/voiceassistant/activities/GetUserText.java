package com.example.voiceassistant.activities;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voiceassistant.databinding.ActivityGetUserTextBinding;

import java.util.ArrayList;

public class GetUserText extends AppCompatActivity {

    private ActivityGetUserTextBinding binding;

    // Новый способ запуска активити с результатом
    private final ActivityResultLauncher<Intent> speechLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ArrayList<String> text = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (text != null && !text.isEmpty()) {
                        binding.recSpeech.setText(text.get(0));
                    } else {
                        binding.recSpeech.setText("Не удалось распознать");
                    }
                } else {
                    binding.recSpeech.setText("Не удалось распознать");
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetUserTextBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageButton btnSpeak = binding.btnSpeak;
        Button btnCopy = binding.btnCopy;

        // Распознавание речи
        btnSpeak.setOnClickListener(view -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            // 🔥 Важно: язык сейчас жёстко задан как "en-US"
            // Позже можно получать из Intent: getIntent().getStringExtra("language")
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");

            try {
                speechLauncher.launch(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "На устройстве нет сервиса распознавания речи", Toast.LENGTH_SHORT).show();
            }
        });

        // Копирование
        btnCopy.setOnClickListener(view -> {
            String textToCopy = binding.recSpeech.getText().toString().trim();
            String hint = "(после нажатия произнесите вопрос)";
            if (textToCopy.isEmpty() || textToCopy.equals(hint)) {
                Toast.makeText(this, "Нет текста для копирования", Toast.LENGTH_SHORT).show();
            } else {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Распознанный текст", textToCopy);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Текст скопирован", Toast.LENGTH_SHORT).show();
            }
        });
    }
}