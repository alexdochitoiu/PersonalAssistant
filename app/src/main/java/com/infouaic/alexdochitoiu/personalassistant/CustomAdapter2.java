package com.infouaic.alexdochitoiu.personalassistant;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alex Dochitoiu on 5/13/2018.
 */

public class CustomAdapter2 extends BaseAdapter {

    private ArrayList<Command> commands;

    CustomAdapter2(ArrayList<Command> commands) {
        this.commands = commands;
    }

    public int getCount() {  return commands.size();  }

    public Object getItem(int arg0) {  return null;  }

    public long getItemId(int position) { return position; }

    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TextView titleTv;
        ImageView iconIv;
        ImageView addImgV;
        final View row;

        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;

        row = inflater.inflate(R.layout.row2, parent, false);
        iconIv = row.findViewById(R.id.imgIcon2);
        titleTv = row.findViewById(R.id.txtTitle2);
        addImgV = row.findViewById(R.id.addBtn);

        if(getCount() > 0) {
            titleTv.setText(commands.get(position).getDescription());
            iconIv.setImageResource(commands.get(position).getImage());

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(parent.getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(parent.getContext());
                    }
                    builder.setTitle("Details")
                            .setMessage(commands.get(position).getDetails())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;
                }
            });

            addImgV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isCommandAdded(commands.get(position))) {
                        Toast.makeText(parent.getContext(), "This command is already added", Toast.LENGTH_SHORT).show();
                    }
                    else 
                    {
                        Utils.commands.add(commands.get(position));
                        CommandsListActivity.saveCommands(parent.getContext().getApplicationContext());
                        Toast.makeText(parent.getContext(), "Command successfully added", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        return row;
    }

    public boolean isCommandAdded(Command cmd){
        for (Command command : Utils.commands) {
            if(command.getDescription().equals(cmd.getDescription())) return true;
        }
        return false;
    }
}