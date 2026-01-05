package com.example.voiceassistant.logics;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;

import com.example.voiceassistant.TranslatorActivity;

import android.speech.RecognizerIntent;
import android.widget.Toast;

public class VoiceInputManager {

    private final ActivityResultLauncher<Intent> speechLauncher;
    private final TranslatorActivity translatorActivity;

    public void check() {

    }

    public VoiceInputManager(TranslatorActivity translatorActivity, ActivityResultLauncher<Intent> speechLauncher) {
        this.speechLauncher = speechLauncher;
        this.translatorActivity = translatorActivity;
    }

    public void startListening(String sourceLang) {
        String languageCode = sourceLang.equals("en") ? "en-US" : "ru-RU";

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);


        try {
            speechLauncher.launch(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(translatorActivity,
                    "Говорите чётче и медленнее для оффлайн-распознавания",
                    Toast.LENGTH_LONG).show();
        }

    }
}