package com.infouaic.alexdochitoiu.personalassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import static com.infouaic.alexdochitoiu.personalassistant.Utils.MAX_COMMANDS;
import static com.infouaic.alexdochitoiu.personalassistant.Utils.commands;

public class AddCommandActivity extends AppCompatActivity {

    public static CustomAdapter2 adapter2;

    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_command);

        lv = findViewById(R.id.add_command_lv);
        adapter2 = new CustomAdapter2(getCommandList());

        lv.setAdapter(adapter2);
    }

    public ArrayList<Command> getCommandList() {
        ArrayList<Command> list = new ArrayList<>(MAX_COMMANDS);
        list.add(new Command("Call [contact]", R.drawable.ic_call, true,
                "Call [contact]\ncontact = contacts from your phone\nThe command should be spoken with " +
                        "a contact you want to call.\nFor example: Call Baby."));
        list.add(new Command("Check email", R.drawable.ic_mail, true,
                "Check email\nThis command opens the mail inbox."));
        list.add(new Command("Show me my messages", R.drawable.ic_messages, true,
                "Show me my messages\nWith this command you can check your messages."));
        list.add(new Command("Open [application]", R.drawable.ic_app, true,
                "Open [application]\napplication = an application from your phone.\n" +
                        "Available applications: Facebook, Google Chrome, YouTube, Messenger, " +
                        "WhatsUp, Calendar, Gallery, Calculator and Music Player." +
                        "\nFor example: Open Music Player"));
        list.add(new Command("Where is [place]", R.drawable.ic_place, true,
                "Where is [place]\nUsing this command you can find a place via Google Maps.\n" +
                        "For example: Where is Faculty of Computer Science"));
        list.add(new Command("Set alarm at [time]", R.drawable.ic_alarm, true,
                "Set alarm at [time]\nWith this command you can set an alarm.\n" +
                        "For example: Set alarm at 8:15."));
        return list;
    }
}
