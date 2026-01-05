package com.example.voiceassistant.logics;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;
import java.util.Set;

public class TextToSpeechManager implements TextToSpeech.OnInitListener {
    private TextToSpeech tts;
    private Context context;
    private boolean isInitialized = false;
    private String currentLanguage = "en";

    public TextToSpeechManager(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("Context not may be null");
        }
        this.context = context;
        initTTS();
    }

    private void initTTS() {
        if (tts == null) {
            tts = new TextToSpeech(context, this);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true;

            int result = tts.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Язык не поддерживается");
                Toast.makeText(context,
                        "Английский язык не поддерживается для озвучки",
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.d("TTS", "TTS успешно инициализирован");
            }
        } else {
            Log.e("TTS", "Ошибка инициализации TTS");
            Toast.makeText(context,
                    "Ошибка инициализации озвучки",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void speak(String text, String language) {
        if (!isInitialized) return;

        // Настройка перед каждой речью
        tts.setSpeechRate(0.85f);
        tts.setPitch(1.1f);

        // Установить язык
        if (language.equals("ru")) {
            tts.setLanguage(new Locale("ru", "RU"));
        } else {
            tts.setLanguage(Locale.US);
        }

        // Пауза перед речью
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Говорим
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    public void setBetterVoice(String language) {
        if (tts == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        Set<Voice> voices = tts.getVoices();
        Voice bestVoice = null;

        for (Voice voice : voices) {
            if (voice.getLocale().getLanguage().startsWith(language)) {
                if (voice.getName().toLowerCase().contains("premium") ||
                        voice.getName().toLowerCase().contains("neutral") ||
                        voice.getName().toLowerCase().contains("female")) {
                    bestVoice = voice;
                    break;
                }
                if (bestVoice == null) {
                    bestVoice = voice;
                }
            }
        }

        if (bestVoice != null) {
            tts.setVoice(bestVoice);
            Log.d("TTS", "Выбран голос: " + bestVoice.getName());
        }
    }

    private void setLanguage(String language) {
        if (language.equals(currentLanguage)) {
            return;
        }

        Locale locale;
        if (language.equals("ru")) {
            locale = new Locale("ru", "RU");
        } else {
            locale = Locale.US;
        }

        int result = tts.setLanguage(locale);
        if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED) {

            Log.w("TTS", "Язык " + language + " не поддерживается");

            if (!language.equals("en")) {
                setLanguage("en");
            }
        } else {
            currentLanguage = language;
            Log.d("TTS", "Установлен язык: " + language);
        }
    }

    public boolean isSpeaking() {
        return tts != null && tts.isSpeaking();
    }

    public void stop() {
        if (tts != null && tts.isSpeaking()) {
            tts.stop();
        }
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
            isInitialized = false;
        }
    }

    public boolean isLanguageAvailable(String language) {
        if (tts == null) return false;

        Locale locale;
        if (language.equals("ru")) {
            locale = new Locale("ru", "RU");
        } else {
            locale = Locale.US;
        }

        return tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE;
    }
}