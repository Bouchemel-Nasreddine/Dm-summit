package com.cse.dmsummit;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private TextView profileMail, profileID, profileName;
    private MaterialButton info, back, confirmParticipation;
    private FirebaseUser user;
    private ImageView profilePicture;
    StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        profileName = findViewById(R.id.profile_name);
        profileMail = findViewById(R.id.profile_mail);
        profileID = findViewById(R.id.profile_ID);
        info = findViewById(R.id.verify_email);
        profilePicture = findViewById(R.id.profile_picture);
        back = findViewById(R.id.profile_toolbar_back);
        confirmParticipation = findViewById(R.id.profile_confirm_participation);

        user = fAuth.getCurrentUser();

        String mail = user.getEmail();

        user.reload();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        String mode = getIntent().getStringExtra("mode");

        if (mode.equals("view")) {
            profileName.setText(getIntent().getStringExtra("first") + " " + getIntent().getStringExtra("last"));
            profileMail.setText(getIntent().getStringExtra("email"));
            profileID.setText(getIntent().getStringExtra("id"));
            TextView reservation = findViewById(R.id.profile_reservation);
            reservation.setVisibility(View.VISIBLE);
            reservation.setText("participation confirmé: " + getIntent().getStringExtra("reserved"));

            StorageReference pictureRef = storageReference.child(getIntent().getStringExtra("id") + ".jpg");
            pictureRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext())
                            .load(uri)
                            .centerCrop()
                            .into(profilePicture);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", e.getMessage());
                    Toast.makeText(getApplicationContext(), "could not get profile picture", Toast.LENGTH_LONG).show();
                }
            });

            info.setVisibility(View.INVISIBLE);
            confirmParticipation.setVisibility(View.INVISIBLE);
        }
        else {

            retrieveUserInfo(user);

            if (!user.isEmailVerified()) {
                info.setVisibility(View.INVISIBLE);
            }


            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
                    dialog.setTitle("verification d'email");
                    dialog.setMessage("vous n'avez pas verifie votre adresse mail, envoyer un mail de verification?");
                    dialog.setPositiveButton("envoyer", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (user == null) return;
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "email de verification envoyé", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    });
                    dialog.setNegativeButton("annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.create().show();
                }
            });

            /**resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
            EditText oldPassword = new EditText(v.getContext());
            oldPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            oldPassword.setHint("encien mot de passe");
            EditText resetPassword = new EditText(v.getContext());
            resetPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            resetPassword.setHint("nouveau mot de passe");
            LinearLayout layout = new LinearLayout(v.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(oldPassword);
            layout.addView(resetPassword);
            AlertDialog.Builder dialog = new AlertDialog.Builder(v.getContext());
            dialog.setView(layout);
            dialog.setTitle("changez votre mot de passe");
            dialog.setPositiveButton("changer", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
            fAuth.signInWithEmailAndPassword(mail, oldPassword.getText().toString());
            user.updatePassword(resetPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()) {
            Toast.makeText(getApplicationContext(), "le mot de passe a été changé", Toast.LENGTH_LONG).show();
            } else {
            Log.d("TAG", "TAG " + task.getException().getMessage());
            Toast.makeText(getApplicationContext(), "erreur dans le changement de mot de passe", Toast.LENGTH_LONG).show();
            }
            }
            });
            }
            });
            dialog.setNegativeButton("annuler", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {

            }
            });
            dialog.create().show();

            }
            });**/

            profilePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, 1000);
                }
            });

            confirmParticipation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user.isEmailVerified()) {
                        DocumentReference documentReference = db.collection("users").document(user.getUid());
                        documentReference.update("reserved", "YES");
                        confirmParticipation.setVisibility(View.GONE);
                    }
                }
            });

        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                profilePicture.setImageURI(imageUri);

                uploadImageToFireBase(imageUri);
            }
        }
    }

    private void uploadImageToFireBase(Uri imageUri) {
        StorageReference fileRef = storageReference.child(user.getUid() + ".jpg");
        fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "profile picture uploaded", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("TAG", task.getException().getMessage());
                    Toast.makeText(getApplicationContext(), "error in uploading image", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void retrieveUserInfo(FirebaseUser user) {
        if (user == null) return;

        DocumentReference documentReference = db.collection("users").document(user.getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value == null) return;

                profileName.setText(value.getString("FirstName") + " " + value.getString("LastName"));
                profileMail.setText(value.getString("email"));
                if (value.contains("reserved") && value.getString("reserved").equals("NO")) {
                    confirmParticipation.setVisibility(View.VISIBLE);
                }
                profileID.setText("ID: " + user.getUid());
                StorageReference pictureRef = storageReference.child(user.getUid() + ".jpg");
                pictureRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext())
                                .load(uri)
                                .centerCrop()
                                .into(profilePicture);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", e.getMessage());
                        Toast.makeText(getApplicationContext(), "could not get profile picture", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

}
