package com.example.voiceassistant.logics;

public interface OnTranslationCompleteListener {
    void onTranslationComplete(String translatedText);
    void onTranslationError(String errorMessage);
}
