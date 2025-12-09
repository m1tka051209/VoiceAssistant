package com.example.voiceassistant;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.voiceassistant.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    protected static final int RESULT_SPEECH = 1;
    private ImageButton btnSpeak;
    private TextView recSpeech;
    private Button btnCopy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSpeak = findViewById(R.id.question);
        recSpeech = findViewById(R.id.text);
        Log.e("ERROR", "Приложение запускается");
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        "en-US");
                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                } catch (ActivityNotFoundException a) {
                    Toast.makeText(getApplicationContext(),
                            "Текст не распознан",
                            Toast.LENGTH_SHORT).show();
                }

            }
        };

        btnSpeak.setOnClickListener(listener);

        // Копирование в буфер обмена

        btnCopy = findViewById(R.id.btn_copy);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textToCopy = recSpeech.getText().toString();
                String hint = "(после нажатия произнесите вопрос)";
                if (textToCopy.equals(hint) || textToCopy.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Нет текста для копирования", Toast.LENGTH_SHORT).show();
                } else {
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Распознанный текст", textToCopy);
                    clipboardManager.setPrimaryClip(clipData);

                    Toast.makeText(MainActivity.this, "Текст скопирован успешно", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    recSpeech.setText(text.get(0));
                }
                break;
            }
        }
    }
}