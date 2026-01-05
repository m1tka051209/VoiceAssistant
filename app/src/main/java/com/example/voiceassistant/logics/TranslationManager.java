package com.example.voiceassistant.logics;

import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class TranslationManager {

    public void translate(String text, String sourceLang, String targetLang, OnTranslationCompleteListener listener) {

        TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(sourceLang)
                .setTargetLanguage(targetLang).build();

        Translator translator = Translation.getClient(options);


        translator.downloadModelIfNeeded()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        return translator.translate(text);
                    } else {
                        Exception error = task.getException();
                        assert error != null;
                        return Tasks.forException(error);
                    }
                })

                .addOnSuccessListener(translatedText -> {
                    listener.onTranslationComplete(translatedText);
                    translator.close();
                })

                .addOnFailureListener(e -> {
                    listener.onTranslationError(e.getMessage());
                    translator.close();
                });
    }
}