package com.cse.dmsummit;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TachesAdapter extends ArrayAdapter<TachesActivity.Tache> {
    private ArrayList<TachesActivity.Tache> list;
    private LayoutInflater inf;

    public TachesAdapter(@NonNull Context context, int resource, ArrayList<TachesActivity.Tache> list) {
        super(context, resource);
        this.inf = LayoutInflater.from(context);
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public TachesActivity.Tache getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getPosition(@Nullable TachesActivity.Tache item) {
        return list.indexOf(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inf.inflate(R.layout.tache, null);

        TextView name = convertView.findViewById(R.id.tache_name);
        ImageView status = convertView.findViewById(R.id.tache_status);

        name.setText(list.get(position).getName());

        if (list.get(position).isStatus()) {
            status.setImageResource(R.drawable.check2);
        } else {
            status.setImageResource(R.drawable.check);
        }

        View finalConvertView = convertView;
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.get(position).isStatus()) {
                    status.setImageResource(R.drawable.check2);
                    list.get(position).changeStatus();
                    finalConvertView.setAlpha((float) 0.5);
                    status.setFocusable(false);
                }
            }

        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (list.get(position).isStatus()) {
                    Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vib.vibrate(200);
                    status.setImageResource(R.drawable.check);
                    list.get(position).changeStatus();
                    finalConvertView.setAlpha((float) 1);
                    status.setFocusable(true);
                }
                return true;
            }
        });

        return convertView;
    }
}
