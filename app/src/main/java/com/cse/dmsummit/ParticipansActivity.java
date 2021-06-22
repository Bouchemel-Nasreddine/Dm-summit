package com.cse.dmsummit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParticipansActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<MainActivity.User> list;
    private ParticipansAdapter adapter;
    private FirebaseAuth fAuth;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private MaterialButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participans);

        back = findViewById(R.id.participans_toolbar_back);

        list = new ArrayList<>();

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        list = retriveParticipansList();


        listView = findViewById(R.id.packedr_list_view);
        adapter = new ParticipansAdapter(getApplicationContext(), R.layout.participans, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("mode", "view" );
                intent.putExtra("first", list.get(position).getFirstName() );
                intent.putExtra("last", list.get(position).getLastName() );
                intent.putExtra("email", list.get(position).getEmail() );
                intent.putExtra("id", list.get(position).getID() );
                intent.putExtra("reserved", list.get(position).getReservation() );
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private ArrayList<MainActivity.User> retriveParticipansList() {
        ArrayList<MainActivity.User> list = new ArrayList<>();

        db.collection("users").whereEqualTo("type", "participant")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (documentSnapshot != null && !documentSnapshot.getDocuments().isEmpty()) {

                            List<DocumentSnapshot> documents = documentSnapshot.getDocuments();
                            for (DocumentSnapshot value : documents) {

                                String fname = value.getString("FirstName");
                                String lname = value.getString("LastName");
                                String email = value.getString("email");
                                String reservation = value.getString("reserved");
                                String id = value.getId();

                                list.add(new MainActivity.User(id, fname, lname, email, reservation, null, null));
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });


        return list;
    }

}
