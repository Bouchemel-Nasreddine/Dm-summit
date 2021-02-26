package com.cse.dmsummit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText userFirstName, userLastName, userMail, userPasswd, user2Passwd;
    private MaterialButton register, back;
    private FirebaseAuth fAuth;
    private TextView loginRedirecting;
    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userFirstName = findViewById(R.id.first_name);
        userLastName = findViewById(R.id.last_name);
        userMail = findViewById(R.id.user_mail);
        userPasswd = findViewById(R.id.user_passwd);
        user2Passwd = findViewById(R.id.user_2passwd);
        loginRedirecting = findViewById(R.id.loginRedirecting);
        back = findViewById(R.id.register_toolbar_back);
        register = findViewById(R.id.register);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = userFirstName.getText().toString();
                String lastName = userLastName.getText().toString();
                String email = userMail.getText().toString();
                if (Verifications.verifyEmpty(userFirstName) && Verifications.verifyEmpty(userLastName)
                        && Verifications.verifyEmpty(userMail) && Verifications.verifyEmpty(userPasswd)
                        && Verifications.verifyEmpty(user2Passwd) && Verifications.verifyEmail(userMail)
                        && Verifications.verifyParticipationEmail(userMail)
                        && Verifications.verifyPassword(userPasswd)
                        && Verifications.verifyPasswordConfirmation(userPasswd, user2Passwd)) {

                    fAuth.createUserWithEmailAndPassword(userMail.getText().toString().trim(), userPasswd.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "compte crée", Toast.LENGTH_LONG).show();
                                        userID = fAuth.getCurrentUser().getUid();
                                        DocumentReference documentReference = db.collection("users").document(userID);
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("FirstName", firstName);
                                        user.put("LastName", lastName);
                                        user.put("email", email);
                                        user.put("type", "participant");
                                        user.put("reserved", "NO");
                                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "user successfully created for "+ userID);
                                            }
                                        });
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loginRedirecting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

    }

}
