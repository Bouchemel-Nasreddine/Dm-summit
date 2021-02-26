package com.cse.dmsummit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class AgendaActivity extends AppCompatActivity {

    private MaterialButton back;
    private ListView agendaListView;
    private ArrayList<AgendaItem> agendaItemsList;
    private AgendaAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        agendaItemsList = new ArrayList<>();
        agendaItemsList.add(new AgendaItem("welcome speech + annonce de l’ideathon", "day1", "9:30-10:00", ""));
        agendaItemsList.add(new AgendaItem("conférence 1", "day1", "10:00-11:00", "définition du MD, ses avantages, et quelques exemples"));
        agendaItemsList.add(new AgendaItem("workshop", "day1", "11:00-12:30", "comment perfectionner le contenu de leurs sites ( photos, vidéos de leurs produits …. )"));
        agendaItemsList.add(new AgendaItem("pause déjeuner", "day1", "12:30-13:30", ""));
        agendaItemsList.add(new AgendaItem("fishbowl 1", "day1", "13:30-14:30", " échange sur les meilleures pratiques du MD"));
        agendaItemsList.add(new AgendaItem("fishbowl 2", "day1", "14:30-15:30", " comment intégrer le MD dans son business , et la maîtrise de la cohérence de l’image des entreprises"));
        agendaItemsList.add(new AgendaItem("conférence 2", "day2", "9:30-11:00", "les meilleures tactiques pour doper sa prospection en ligne"));
        agendaItemsList.add(new AgendaItem("panel (table ronde)", "day2", "11:00-12:00", " leurs avis sur l’avenir du MD dans le monde et particulièrement en algérie “ / “ comment le marketing digital a évolué ces dernières années et son effect"));
        agendaItemsList.add(new AgendaItem("pause déjeuner", "day2", "12:00-13:00", ""));
        agendaItemsList.add(new AgendaItem("salon", "day2", "13:00-18:00", ""));
        agendaItemsList.add(new AgendaItem("conférence 3", "day3", "9:30-11:00", "le MD en profondeur et ses stratégies les plus utilisées dernièrement"));
        agendaItemsList.add(new AgendaItem("travailler sur l’ideathon", "day3", "11:00-12:30", ""));
        agendaItemsList.add(new AgendaItem("pause déjeuner", "day3", "12:30-13:30", ""));
        agendaItemsList.add(new AgendaItem("continuer l’ideathon", "day3", "13:30-16:00", ""));
        agendaItemsList.add(new AgendaItem("présentations des projets de l’ideathon", "day3", "16:00-17:00", ""));
        agendaItemsList.add(new AgendaItem("pause café", "day3", "17:00-17:30", ""));
        agendaItemsList.add(new AgendaItem("Q/A", "day3", "17:30-18:15", ""));
        agendaItemsList.add(new AgendaItem("annonce du gagnant de l’ideathon + closing ceremony", "day3", "18:15-19:00", ""));


        back = findViewById(R.id.agenda_toolbar_back);
        agendaListView = findViewById(R.id.agenda_list_view);

        adapter = new AgendaAdapter(this, R.layout.agenda_item_layout, agendaItemsList);

        agendaListView.setAdapter(adapter);

        agendaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AgendaItem item = agendaItemsList.get(position);
                LinearLayout info_dialog = findViewById(R.id.event_info_dialog);
                info_dialog.setVisibility(View.VISIBLE);
                TextView tv = info_dialog.findViewById(R.id.event_info_dialog_title);
                tv.setText(item.getTitle());
                tv = info_dialog.findViewById(R.id.event_info_dialog_description);
                tv.setText(item.getDescription());
                tv = info_dialog.findViewById(R.id.event_info_dialog_day);
                tv.setText(item.getDay());
                tv = info_dialog.findViewById(R.id.event_info_dialog_time);
                tv.setText(item.getTime());
                MaterialButton close = findViewById(R.id.event_info_dialog_close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        info_dialog.setVisibility(View.GONE);
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public static class AgendaItem {
        private String title, day, time, description;
        private static int counter = 0;
        private int id;

        public AgendaItem(String title, String day, String time, String description) {
            this.title = title;
            this.day = day;
            this.time = time;
            this.description = description;
            this.id = counter++;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getDay() {
            return day;
        }

        public String getTime() {
            return time;
        }

        public String getDescription() {
            return description;
        }
    }

}