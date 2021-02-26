package com.cse.dmsummit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class AgendaAdapter extends ArrayAdapter<AgendaActivity.AgendaItem> {

    private ArrayList<AgendaActivity.AgendaItem> agendaList;
    private LayoutInflater inf;

    public AgendaAdapter(@NonNull Context context, int resource, ArrayList<AgendaActivity.AgendaItem> list) {
        super(context, resource);
        this.agendaList = list;
        this.inf = LayoutInflater.from(context);
    }

    @Nullable
    @Override
    public AgendaActivity.AgendaItem getItem(int position) {
        return agendaList.get(position);
    }

    @Override
    public int getCount() {
        return agendaList.size();
    }

    @Override
    public long getItemId(int position) {
        return agendaList.get(position).getId();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inf.inflate(R.layout.agenda_item_layout, null);
        TextView title = convertView.findViewById(R.id.agenda_item_title);
        TextView day = convertView.findViewById(R.id.agenda_item_day);
        TextView time = convertView.findViewById(R.id.agenda_item_time);

        AgendaActivity.AgendaItem currentItem = agendaList.get(position);

        title.setText(currentItem.getTitle());
        day.setText(currentItem.getDay());
        time.setText(currentItem.getTime());

        return convertView;
    }
}
