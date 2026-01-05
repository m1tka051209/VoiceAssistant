package com.example.voiceassistant;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.voiceassistant.logics.*;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;

import com.example.voiceassistant.databinding.ActivityTranslatorBinding;

import java.util.ArrayList;

public class TranslatorActivity extends AppCompatActivity {


    private VoiceInputManager voiceManager;
    private TranslationManager translationManager;
    private String recognizedText;
    private TextToSpeechManager ttsManager;


    private String sourceLang;
    private String targetLang;

    private ActivityTranslatorBinding binding;

    private final ActivityResultLauncher<Intent> speechLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    ArrayList<String> text = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (text != null && !text.isEmpty()) {
                        String recognizedText = text.get(0);
                        translateAndShow(recognizedText);
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTranslatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ttsManager = new TextToSpeechManager(this);

        Intent intent = getIntent();
        this.sourceLang = intent.getStringExtra("SOURCE_LANG");
        this.targetLang = intent.getStringExtra("TARGET_LANG");
        String inputType = intent.getStringExtra("INPUT_TYPE");

        if (this.sourceLang == null || this.targetLang == null || inputType == null) {
            finish();
            return;
        }

        binding.btnBack.setOnClickListener(v -> {
            Intent intent1 = new Intent(TranslatorActivity.this, MainActivity.class);
            startActivity(intent1);
        });

        voiceManager = new VoiceInputManager(this, speechLauncher);
        translationManager = new TranslationManager();

        setupUI(binding, sourceLang, targetLang, inputType);
    }

    private void translateAndShow(String textToTranslate) {

        translationManager.translate(textToTranslate, sourceLang, targetLang,
                new OnTranslationCompleteListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTranslationComplete(String translatedText) {
                        binding.resultText.setText("Перевод: \n" + translatedText);
                        binding.btnSpeakResult.setVisibility(View.VISIBLE);
                        binding.btnCopy.setVisibility(View.VISIBLE);
                        binding.btnSpeakResult.setOnClickListener(v -> {
                            Toast.makeText(TranslatorActivity.this, "Сейчас начнётся речь, подождите", Toast.LENGTH_SHORT).show();
                            ttsManager.speak(translatedText, targetLang);
                        });

                        binding.btnCopy.setOnClickListener(v -> {
                            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clipData = ClipData.newPlainText("label", translatedText);
                            clipboardManager.setPrimaryClip(clipData);
                            Toast.makeText(TranslatorActivity.this, "Перевод скопирован успешно!", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTranslationError(String errorMessage) {
                        binding.resultText.setText("Ошибка: " + errorMessage);
                    }
                }
        );

    }

    @SuppressLint("SetTextI18n")
    private void setupUI(ActivityTranslatorBinding binding, String sourceLang, String targetLang, String inputType) {
        String srcLang = sourceLang.equals("en") ? "с английского" : "с русского";
        String tgtLang = targetLang.equals("en") ? "на английский" : "на русский";

        binding.title.setText("Перевод: " + srcLang + " —> " + tgtLang);

        binding.blockText.setVisibility(View.GONE);
        binding.blockVoice.setVisibility(View.GONE);
        binding.blockPhoto.setVisibility(View.GONE);


        if ("text".equals(inputType)) {
            binding.blockText.setVisibility(View.VISIBLE);
            binding.btnTranslateText.setOnClickListener(v -> {
                String text = binding.inputText.getText().toString();
                if (!text.trim().isEmpty()) {
                    binding.btnTranslateText.setVisibility(View.VISIBLE);
                    translateAndShow(text);
                } else {
                    Toast.makeText(TranslatorActivity.this, "Введите ваш текст", Toast.LENGTH_SHORT).show();
                    binding.inputText.setText("");
                }
            });
        } else if ("voice".equals(inputType)) {
            binding.blockVoice.setVisibility(View.VISIBLE);
            binding.btnSpeak.setOnClickListener(v -> voiceManager.startListening(sourceLang));
        } else if ("photo".equals(inputType)) {
            binding.blockPhoto.setVisibility(View.VISIBLE);
        }

        binding.resultText.setText("");
    }

    public TextToSpeechManager getTtsManager() {
        return ttsManager;
    }

    public void setTtsManager(TextToSpeechManager ttsManager) {
        this.ttsManager = ttsManager;
    }
}