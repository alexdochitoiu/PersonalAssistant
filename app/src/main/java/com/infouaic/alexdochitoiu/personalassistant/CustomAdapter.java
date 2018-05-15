package com.infouaic.alexdochitoiu.personalassistant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alex Dochitoiu on 5/13/2018.
 */

public class CustomAdapter extends BaseAdapter {

    private ArrayList<Command> commands;

    CustomAdapter(ArrayList<Command> commands) {
        this.commands = commands;
    }

    public int getCount() {  return commands.size();  }

    public Object getItem(int arg0) {  return null;  }

    public long getItemId(int position) { return position; }

    @SuppressLint("ViewHolder")
    public View getView(final int position, View convertView, final ViewGroup parent) {
        TextView titleTv;
        ImageView iconIv;
        Switch switchBtn;
        final View row;

        LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;

        row = inflater.inflate(R.layout.row, parent, false);
        iconIv = row.findViewById(R.id.imgIcon);
        titleTv = row.findViewById(R.id.txtTitle);
        switchBtn = row.findViewById(R.id.switchBtn);

        if(getCount() > 0) {
            titleTv.setText(commands.get(position).getDescription());
            iconIv.setImageResource(commands.get(position).getImage());
            switchBtn.setChecked(commands.get(position).isActive());

            switchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked) {
                        Toast.makeText(parent.getContext(), "Command activated",
                                Toast.LENGTH_LONG).show();
                        commands.get(position).setActive(true);
                        CommandsListActivity.saveCommands(parent.getContext());
                        CommandsListActivity.adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        Toast.makeText(parent.getContext(), "Command deactivated",
                                Toast.LENGTH_LONG).show();
                        commands.get(position).setActive(false);
                        CommandsListActivity.saveCommands(parent.getContext());
                        CommandsListActivity.adapter.notifyDataSetChanged();
                    }
                }
            });

            row.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(parent.getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(parent.getContext());
                    }
                    builder.setTitle("Delete command")
                            .setMessage("Are you sure you want to remove this command?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Utils.commands.remove(position);
                                    CommandsListActivity.saveCommands(parent.getContext());
                                    CommandsListActivity.adapter.notifyDataSetChanged();
                                    Toast.makeText(parent.getContext(), "Command deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return true;
                }
            });
        }
        return row;
    }
}