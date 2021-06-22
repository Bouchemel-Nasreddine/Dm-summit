package com.cse.dmsummit;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class TachesActivity extends AppCompatActivity {
    private ArrayList<Tache> list;
    private ListView listView;
    private TachesAdapter adapter;
    private MaterialButton back, add, add_task;
    private LinearLayout layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taches);

        back = findViewById(R.id.taches_toolbar_back);
        add = findViewById(R.id.taches_add);
        add_task = findViewById(R.id.add_task_button);

        if (true ) {
            list = new ArrayList<>();
            list.add(new Tache("imprimer les badges", false));
            list.add(new Tache("organiser la salle de conference", false));
            list.add(new Tache("guider les participans vers les salles", false));
            list.add(new Tache("distribuer les badges", false));
            list.add(new Tache("preparer la table du jury", false));
            list.add(new Tache("preparer la céremonie final", false));
        }
        listView = findViewById(R.id.taches_list_view);
        adapter = new TachesAdapter(this, R.layout.tache, list);
        listView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout = findViewById(R.id.add_tache_view);
                layout.setVisibility(View.VISIBLE);
            }
        });

        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText addedTaskName = findViewById(R.id.added_task_name);
                list.add(new Tache(addedTaskName.getText().toString(), false));
                adapter.notifyDataSetChanged();
                layout.setVisibility(View.GONE);
            }
        });

    }

    public class Tache{
        private String name;
        private boolean status;

        public Tache(String name, boolean status) {
            this.name = name;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public boolean isStatus() {
            return status;
        }

        public void changeStatus() {
            this.status = !this.status;
        }
    }

}
