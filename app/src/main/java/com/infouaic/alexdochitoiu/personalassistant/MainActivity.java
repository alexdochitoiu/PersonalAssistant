package com.infouaic.alexdochitoiu.personalassistant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import static com.infouaic.alexdochitoiu.personalassistant.CommandsListActivity.loadCommands;
import static com.infouaic.alexdochitoiu.personalassistant.CommandsListActivity.saveCommands;
import static com.infouaic.alexdochitoiu.personalassistant.Utils.WordsToNumber;
import static com.infouaic.alexdochitoiu.personalassistant.Utils.getPhoneNumber;

public class MainActivity extends AppCompatActivity {

    private static final int RESULT_GALLERY = 0;
    ImageView recButton, swipeButton;
    TextView recText;
    SpeechRecognizer mSpeechRecognizer;
    Intent mSpeechRecognizerIntent;
    NavigationView navigation;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        loadCommands(getApplication());
        swipeButton = findViewById(R.id.arrow);
        drawerLayout = findViewById(R.id.drawer_layout);
        recText = findViewById(R.id.recorder_text);
        recButton = findViewById(R.id.recorder);
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) { }
            @Override
            public void onBeginningOfSpeech() { }
            @Override
            public void onRmsChanged(float rmsdB) { }
            @Override
            public void onBufferReceived(byte[] buffer) { }
            @Override
            public void onEndOfSpeech() { }
            @Override
            public void onError(int error) { }

            @SuppressLint("MissingPermission")
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(matches != null) {
                    String command = matches.get(0).toUpperCase();

                    Toast.makeText(MainActivity.this, command,
                            Toast.LENGTH_LONG).show();

                    if(command.startsWith("CALL") && Utils.isCommandActive("CALL")) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        String contact = command.replace("CALL ","");
                        contact = contact.substring(0,1) + contact.substring(1).toLowerCase();
                        String phoneNumber = getPhoneNumber(contact, MainActivity.this);
                        if(!phoneNumber.equals("Unsaved")){
                            callIntent.setData(Uri.parse("tel:"+phoneNumber));
                            startActivity(callIntent);
                        }
                        else Toast.makeText(MainActivity.this, contact+" doesn't exist in your contact list.",
                                Toast.LENGTH_LONG).show();
                    }
                    else if(command.equals("CHECK EMAIL") && Utils.isCommandActive("CHECK EMAIL")){
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                        startActivity(intent);
                    }
                    else if(command.equals("SHOW ME MY MESSAGES") && Utils.isCommandActive("SHOW ME MY MESSAGES")) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.setClassName("com.android.mms", "com.android.mms.ui.ConversationList");
                        startActivity(intent);
                    }
                    else if(command.startsWith("OPEN") && Utils.isCommandActive("OPEN")) {
                        String application = command.replace("OPEN ","");
                        String packageName = "";
                        switch (application) {
                            case "FACEBOOK":
                                packageName = "com.facebook.katana";
                                break;
                            case "YOUTUBE":
                                packageName = "com.google.android.youtube";
                                break;
                            case "GOOGLE CHROME":
                                packageName = "com.android.chrome";
                                break;
                            case "MESSENGER":
                                packageName = "com.facebook.orca";
                                break;
                            case "WHATSAPP":
                                packageName = "com.whatsapp";
                                break;
                            case "CALENDAR":
                                packageName = "com.android.calendar";
                                break;
                            case "CALCULATOR":
                                packageName = "com.android.calculator2";
                                break;
                            case "GALLERY":
                                Intent galleryIntent = new Intent(
                                        Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(galleryIntent , RESULT_GALLERY );
                                return;
                            case "MUSIC PLAYER":
                                Intent intent = new Intent(MediaStore.INTENT_ACTION_MUSIC_PLAYER);
                                startActivity(intent);
                                return;
                        }
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(packageName);
                        if (launchIntent != null) {
                            startActivity(launchIntent);
                        }
                    }
                    else if(command.startsWith("WHERE IS") && Utils.isCommandActive("WHERE IS")) {
                        String address = command.replace("WHERE IS", "").toLowerCase();
                        String map = "http://maps.google.co.in/maps?q=" + address;
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                        startActivity(intent);
                    }
                    else if(command.startsWith("SET ALARM AT") && Utils.isCommandActive("SET ALARM AT")) {
                        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                        String hour = command.replace("SET ALARM AT ", "");
                        hour = hour.substring(0, hour.indexOf(' '));
                        String minutes = command.replace("SET ALARM AT ", "");
                        if(!minutes.contains("AND ")) minutes = "zero";
                        else minutes = minutes.substring(minutes.indexOf("AND ") + 4);
                        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
                        i.putExtra(AlarmClock.EXTRA_MESSAGE, "Personal Assistant Alarm");
                        i.putExtra(AlarmClock.EXTRA_HOUR, WordsToNumber(hour));
                        i.putExtra(AlarmClock.EXTRA_MINUTES, WordsToNumber(minutes));
                        Log.i("TAG",WordsToNumber(hour)+":"+WordsToNumber(minutes));
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Command not available", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onPartialResults(Bundle partialResults) { }
            @Override
            public void onEvent(int eventType, Bundle params) { }
        });
        recButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                recButton.requestLayout();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    recButton.getLayoutParams().height -= 20;
                    recButton.getLayoutParams().width -= 20;
                    recText.setText(R.string.speak_now);
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    recButton.getLayoutParams().height += 20;
                    recButton.getLayoutParams().width += 20;
                    recText.setText(R.string.tap_to_speak);
                    mSpeechRecognizer.stopListening();
                }
                return true;
            }
        });
        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
        navigation = findViewById(R.id.nav_view);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.commands_list:
                        Intent commandsListIntent = new Intent(MainActivity.this, CommandsListActivity.class);
                        startActivity(commandsListIntent);
                        return true;
                    case R.id.new_command:
                        Intent addCommandIntent = new Intent(MainActivity.this, AddCommandActivity.class);
                        startActivity(addCommandIntent);
                        return true;
                    default:
                        return false;
                }
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            boolean closed = true;
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(closed) {
                    findViewById(R.id.rel_layout).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                closed = false;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                closed = true;
                findViewById(R.id.rel_layout).setVisibility(View.VISIBLE);
            }

            @Override
            public void onDrawerStateChanged(int newState) { }
        });
        swipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else
                {
                    drawerLayout.openDrawer(Gravity.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
