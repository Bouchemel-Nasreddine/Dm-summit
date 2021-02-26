package com.cse.dmsummit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText userMail, userPassword;
    private FirebaseAuth fAuth;
    MaterialButton login, back;
    private TextView registerRedirecting, passwordForgotten, loginTypeRedirection;
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        back = findViewById(R.id.login_toolbar_back);
        userMail = findViewById(R.id.user_mail);
        userPassword = findViewById(R.id.user_password);
        login = findViewById(R.id.login);
        registerRedirecting = findViewById(R.id.registerRedirecting);
        passwordForgotten = findViewById(R.id.password_forgotten);
        loginTypeRedirection = findViewById(R.id.organizer_participant_redirection);
        image = findViewById(R.id.person_login);

        fAuth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Verifications.verifyEmpty(userMail) && Verifications.verifyEmpty(userPassword)
                        && Verifications.verifyEmail(userMail)) {
                        fAuth.signInWithEmailAndPassword(userMail.getText().toString().trim(), userPassword.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "connexion", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
            }
        });

        registerRedirecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });

        passwordForgotten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText resetMail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("reset Password");
                passwordResetDialog.setMessage("Enter your email to receive a reset link");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("envoyer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "reset link has been sent to your email, check your inbox", Toast.LENGTH_LONG).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Error! " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                passwordResetDialog.create().show();
            }
        });

        loginTypeRedirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginTypeRedirection.getText().toString().equals(getResources().getString(R.string.organizer_redirect))) {
                    loginTypeRedirection.setText(R.string.participant_redirect);
                    image.setBackgroundResource(R.drawable.organistaeur);
                } else {
                    loginTypeRedirection.setText(R.string.organizer_redirect);
                    image.setBackgroundResource(R.drawable.person);
                }
            }
        });

    }
}
