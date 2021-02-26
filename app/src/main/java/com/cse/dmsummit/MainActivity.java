package com.cse.dmsummit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private MaterialButton register, logout, login, confirmParticipation;
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private DrawerLayout drawer;
    private NavigationView navView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private TextView userName;
    private ImageView userPicture;
    private FirebaseUser user;
    private StorageReference storageReference;
    private User userObject;
    private Image picture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("");

        drawer = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        register = findViewById(R.id.registerPage);
        login = findViewById(R.id.loginPage);

        userName = findViewById(R.id.nav_profile_name);
        userPicture = findViewById(R.id.nav_profile_picture);
        logout = findViewById(R.id.nav_logout);
        confirmParticipation = findViewById(R.id.confirm_participation);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        setSupportActionBar(toolbar);

        navView.bringToFront();
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navView.setNavigationItemSelectedListener(this);

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                setUserUI(userObject);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        if (fAuth.getCurrentUser() != null) {
            user = fAuth.getCurrentUser();
            register.setVisibility(View.INVISIBLE);
            login.setVisibility(View.INVISIBLE);
            retreiveUserInfo(user);
        }
        
        disableNavigationDrawer();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        confirmParticipation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.isEmailVerified()) {
                    DocumentReference documentReference = db.collection("users").document(user.getUid());
                    documentReference.update("reserved", "YES");
                    confirmParticipation.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getApplicationContext(), "votre adresse email n'est verifier", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setUserUI(User userObject) {

        ImageView picture = (findViewById(R.id.nav_profile_picture));
        Picasso.with(getApplicationContext()).load(userObject.getPictureUri()).centerCrop().into(picture);
        TextView userName = findViewById(R.id.nav_profile_name);
        userName.setText(userObject.getFirstName() + " " + userObject.getLastName());
    }

    private void disableNavigationDrawer() {
        if (fAuth.getCurrentUser() == null) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toggle.setDrawerIndicatorEnabled(false);
        }

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void logout(View view) {
        Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
        if (fAuth.getCurrentUser() != null) {
            fAuth.signOut();
            disableNavigationDrawer();
            register.setVisibility(View.VISIBLE);
            login.setVisibility(View.VISIBLE);
            confirmParticipation.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_LONG).show();
        switch (item.getItemId()) {
         case R.id.nav_agenda:
            startActivity(new Intent(MainActivity.this, AgendaActivity.class));
            break;
         case R.id.nav_scanner:
             startActivity(new Intent(MainActivity.this, ScannerActivity.class));
            break;
         }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openProfile(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        finish();
    }

    public void openFacebook(View view) {
        startActivity(facebookIntent(getPackageManager(), "https://www.facebook.com/club.scientifique.esi") );
    }

    public void openInstagram(View view) {
        Uri uri = Uri.parse("http://instagram.com/cse.club/");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/cse.club/")));
        }
    }

    public void openEmail(View view) {Intent intent=new Intent(Intent.ACTION_SEND);
        String[] recipients={"cse@esi.dz"};
        intent.putExtra(Intent.EXTRA_EMAIL, recipients);
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        startActivity(Intent.createChooser(intent, "Send mail"));
    }

    public class User {
        private String ID, firstName, lastName, email, type, reservation, pictureUri;

        public User(String ID, String firstName, String lastName, String email, String type, String reservation, String pictureUri) {

            this.ID = ID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.type = type;
            this.reservation = reservation;
            this.pictureUri = pictureUri;
        }

        public String getID() {
            return ID;
        }

        public String getPictureUri() {
            return pictureUri;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public String getType() {
            return type;
        }

        public String getReservation() {
            return reservation;
        }
    }

    public User retreiveUserInfo(FirebaseUser user) {
        if (user == null) return new User("", "", "", "", "", "", "");

        String[] userInfo = new String[7];

        DocumentReference documentReference = db.collection("users").document(user.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (value == null) return;

                userInfo[0] = user.getUid();
                userInfo[1] = value.getString("FirstName");
                userInfo[2] = value.getString("LastName");
                userInfo[3] = value.getString("email");
                userInfo[4] = value.getString("reserved");
                userInfo[5] = value.getString("type");

                if (userInfo[4].equals("NO") && userInfo[5].equals("participant")) {
                    confirmParticipation.setVisibility(View.VISIBLE);
                }

                StorageReference pictureRef = storageReference.child(user.getUid() + ".jpg");
                pictureRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        userInfo[6] = uri.toString();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", e.getMessage());
                        Toast.makeText(getApplicationContext(), "could not get profile picture", Toast.LENGTH_LONG).show();

                    }
                });

                userObject = new User(userInfo[0], userInfo[1], userInfo[2], userInfo[3], userInfo[4], userInfo[5], userInfo[6]);

            }
        });


        return userObject;
    }


    public Intent facebookIntent(PackageManager pm, String url) {
        Uri uri = Uri.parse(url);
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                // http://stackoverflow.com/a/24547437/1048340
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return new Intent(Intent.ACTION_VIEW, uri);
    }

}