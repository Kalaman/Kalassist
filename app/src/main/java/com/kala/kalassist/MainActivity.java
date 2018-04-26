package com.kala.kalassist;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kala.kalassist.Commands.CommunityCommand;
import com.kala.kalassist.Commands.WeatherCommand;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class MainActivity extends AppCompatActivity implements RecognitionListener,SpeechCommand.CommandListener{

    ImageButton btnMicrophone;
    SpeechRecognizer speechRecognizer;
    SpeechCommandControlller speechCommandControlller;
    TextSpeaker textSpeaker;
    RecyclerView recyclerViewSpeechDialog;
    DialogRecyclerAdapter dialogRecyclerAdapter;
    AVLoadingIndicatorView speakingProgressView;
    AVLoadingIndicatorView commandProgressView;
    FrameLayout frameLayoutMicHolder;
    FrameLayout frameLayoutSpeechPartial;
    RelativeLayout relativeLayoutMain;
    TextView textViewPartialResult;
    boolean listeningForSpeech = false;
    Intent recognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Wenn die Zugriffsrechte schon erteilt sind, wird ein "true" zurückgegeben
        if (requestPermissions()) {
            setupObjects();
            setupViews();
            setupListeners();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Wenn ein benötigtes Zugriffsrecht nicht gegeben ist, soll die App beendet werden
        for (int permissionGranted : grantResults)
            if (permissionGranted == PackageManager.PERMISSION_DENIED){
                finish();
                return;
            }

        //Wenn alles gut läuft wird die App geladen
        setupObjects();
        setupViews();
        setupListeners();
    }

    public void setupObjects () {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechCommandControlller = new SpeechCommandControlller(this);

        ArrayList<SpeechDialog> speechDialogs = new ArrayList<>();
        dialogRecyclerAdapter = new DialogRecyclerAdapter(speechDialogs);
    }

    public void setupViews () {
        btnMicrophone = (ImageButton)findViewById(R.id.imageButton_microphone);
        speakingProgressView = (AVLoadingIndicatorView)findViewById(R.id.speakingProgressView);
        commandProgressView = (AVLoadingIndicatorView)findViewById(R.id.commandProgressView);
        textViewPartialResult = (TextView)findViewById(R.id.textView_partial_result);
        frameLayoutMicHolder = (FrameLayout)findViewById(R.id.frameLayoutSpeechMicHolder);
        relativeLayoutMain = (RelativeLayout)findViewById(R.id.relativeLayout_main);
        relativeLayoutMain.setDrawingCacheEnabled(true);
        frameLayoutSpeechPartial = (FrameLayout)findViewById(R.id.frameLayoutSpeechPartial);
        frameLayoutMicHolder.setDrawingCacheEnabled(true);

        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayoutMain.getBackground();
        animationDrawable.setEnterFadeDuration(12000);
        animationDrawable.setExitFadeDuration(12000);
        animationDrawable.start();

        recyclerViewSpeechDialog = (RecyclerView)findViewById(R.id.recyclerView_speechdialog);
        recyclerViewSpeechDialog.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSpeechDialog.setAdapter(dialogRecyclerAdapter);
        recyclerViewSpeechDialog.setItemAnimator(new LandingAnimator());
        ItemClickSupport.addTo(recyclerViewSpeechDialog).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                SpeechCommand selectedCommand = dialogRecyclerAdapter.getArrayListSpeechDialog().get(position).getSpeechCommand();

                if (selectedCommand instanceof CommunityCommand) {
                    CommunityDialog dialog = (CommunityDialog)dialogRecyclerAdapter.getArrayListSpeechDialog().get(position);
                    Utilities.showReportAlertDialog(dialog,MainActivity.this,dialogRecyclerAdapter,position);
                }
            }
        });
    }

    public void setupListeners () {
        speechRecognizer.setRecognitionListener(this);
        speechCommandControlller.setCommandListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "de");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 2);

        btnMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listeningForSpeech) {
                    speechRecognizer.startListening(recognizerIntent);
                    if (textSpeaker.textToSpeech.isSpeaking())
                        textSpeaker.textToSpeech.stop();
                }
                else
                    speechRecognizer.stopListening();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        textSpeaker = new TextSpeaker(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        textSpeaker.getTextToSpeech().shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.destroy();
        }
    }

    public Bitmap getPartialBarBitmap () {
        return null;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        listeningForSpeech = true;
        updateSpeakUI(SpeechCommandControlller.statusListening);
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    public void updateSpeakUI (int status) {

        switch (status) {
            case SpeechCommandControlller.statusStandby:
                btnMicrophone.setVisibility(View.VISIBLE);
                frameLayoutSpeechPartial.setVisibility(View.INVISIBLE);
                speakingProgressView.setVisibility(View.INVISIBLE);
                commandProgressView.setVisibility(View.INVISIBLE);
                break;
            case SpeechCommandControlller.statusListening:
                speakingProgressView.setVisibility(View.VISIBLE);
                frameLayoutSpeechPartial.setVisibility(View.VISIBLE);
                btnMicrophone.setVisibility(View.INVISIBLE);
                commandProgressView.setVisibility(View.INVISIBLE);
                textViewPartialResult.setText("Höre zu ...");
                break;
            case SpeechCommandControlller.statusProcessing:
                speakingProgressView.setVisibility(View.INVISIBLE);
                frameLayoutSpeechPartial.setVisibility(View.INVISIBLE);
                btnMicrophone.setVisibility(View.INVISIBLE);
                commandProgressView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onEndOfSpeech() {
        updateSpeakUI(SpeechCommandControlller.statusProcessing);
    }

    @Override
    public void onError(int error) {
        Log.d("SpeechRecognizer","Error Code: " + error);
        listeningForSpeech = false;
        switch (error)
        {
            case SpeechRecognizer.ERROR_CLIENT:
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                updateSpeakUI(SpeechCommandControlller.statusStandby);
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                break;
        }
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> resultsStringArrayList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String question = resultsStringArrayList.get(0);
        speechCommandControlller.processCommand(question);

        speakingProgressView.setVisibility(View.INVISIBLE);
        commandProgressView.setVisibility(View.VISIBLE);

        listeningForSpeech = false;
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        ArrayList<String> resultsStringArrayList = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (!resultsStringArrayList.get(0).equals(""))
            textViewPartialResult.setText(resultsStringArrayList.get(0).substring(0,1).toUpperCase() + resultsStringArrayList.get(0).substring(1));
    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    /**
     * Hier werden nach den notwendigen Rechten gefragt.
     *
     * Zum Beispiel braucht die Klasse 'SpeechRecognizer' die
     * Rechte zum aufnehmen von Audio.
     * @return
     */
    public boolean requestPermissions() {
        //Überprüfe ob Rechte vorhanden sind
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO
                        ,Manifest.permission.INTERNET
                        ,Manifest.permission.READ_CONTACTS
                        ,Manifest.permission.CALL_PHONE}, 1);

                return false;
        }
        return true;
    }

    @Override
    public void onCommandProcessed(SpeechDialog dialog) {
        Log.d("CommandProcessed",dialog.getAnswer());

        dialogRecyclerAdapter.addSpeechDialog(dialog);
        textSpeaker.speakText(dialog.getAnswer());
        recyclerViewSpeechDialog.smoothScrollToPosition(0);
        updateSpeakUI(SpeechCommandControlller.statusStandby);
    }
}
