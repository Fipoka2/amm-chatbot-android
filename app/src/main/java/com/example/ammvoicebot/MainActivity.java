package com.example.ammvoicebot;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ammvoicebot.model.Answer;
import com.example.ammvoicebot.model.Question;
import com.example.ammvoicebot.service.NetworkService;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private TextView questionView;
    private TextView answerView;
    private TextToSpeech tts;
    private ImageButton btnSpeak;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private static final String USER_ID = "MOBILE_DEMO_USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this, this);
        questionView = (TextView) findViewById(R.id.questionInput);
        answerView = (TextView) findViewById(R.id.answerOutput);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);

        // hide the action bar
        getSupportActionBar().hide();

        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        changeAnswerVisibility(View.INVISIBLE);

    }

    private void promptSpeechInput() {
        changeAnswerVisibility(View.INVISIBLE);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Question question = new Question(result.get(0).hashCode(), result.get(0));
                    questionView.setText(question.getQuestion());
                    changeAnswerVisibility(View.INVISIBLE);

                    NetworkService.getInstance().getChatbotApi().ask(USER_ID, question).enqueue(new Callback<Answer>() {
                        @Override
                        public void onResponse(@NonNull Call<Answer> call, @NonNull Response<Answer> response) {
                            Answer answer = response.body();
                            if (answer != null && answer.getAnswer() != null) {
                                answerView.setText(answer.getAnswer());
                                changeAnswerVisibility(View.VISIBLE);
                                speakOut(answer.getAnswer());
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Server response error1",
                                        Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onFailure(@NonNull Call<Answer> call, @NonNull Throwable t) {

                            Toast.makeText(getApplicationContext(),
                                    "Server response error2" + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            t.printStackTrace();
                        }
                    });

                }
                break;
            }

        }
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onInit(int status) {

        if (status == TextToSpeech.SUCCESS) {

            if (tts.isLanguageAvailable(new Locale(Locale.getDefault().getLanguage()))
                    == TextToSpeech.LANG_AVAILABLE) {
                tts.setLanguage(new Locale(Locale.getDefault().getLanguage()));
            } else {
                tts.setLanguage(Locale.US);
            }
            tts.setPitch(1.3f);
            tts.setSpeechRate(0.7f);
        } else {
            Log.e("TTS", "Initialization Failed!");
        }
    }


    private void speakOut(String text) {
        String utteranceId = this.hashCode() + "";
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    private void changeAnswerVisibility(int state) {
        answerView.setVisibility(state);
    }
}
