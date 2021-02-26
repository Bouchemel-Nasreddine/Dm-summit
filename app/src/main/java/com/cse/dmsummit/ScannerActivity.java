package com.cse.dmsummit;

import android.Manifest;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.rpc.Code;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Arrays;

public class ScannerActivity extends AppCompatActivity {
    private final String [] agendaItem = {"welcome speech + annonce de l’ideathon", "conférence 1", "workshop", "pause déjeuner",
            "fishbowl 1", "fishbowl 2", "conférence 2", "panel (table ronde)", "pause déjeuner", "salon",
            "conférence 3", "travailler sur l’ideathon", "pause déjeuner", "continuer l’ideathon",
            "présentations des projets de l’ideathon", "pause café", "Q/A",
            "annonce du gagnant de l’ideathon + closing ceremony"};

    private CodeScannerView codeScannerView;
    private CodeScanner codeScanner;
    String resultData = "";
    private FirebaseAuth fAuth;
    private FirebaseFirestore db;
    private FirebaseUser user;
    String events = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();

        codeScannerView = findViewById(R.id.scannerView);
        codeScanner = new CodeScanner(this, codeScannerView);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultData = result.getText();
                        if (Arrays.asList(agendaItem).contains(resultData)) {
                            Toast.makeText(getApplicationContext(), "vous etez dans " + resultData, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "ce code n'est recconu ", Toast.LENGTH_LONG).show();

                        }
                        finish();
                    }
                });
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        reqestCameraPermission();
    }

    private void reqestCameraPermission() {
        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getApplicationContext(), "la cam est requis pour scanner les cpdes QR", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}
