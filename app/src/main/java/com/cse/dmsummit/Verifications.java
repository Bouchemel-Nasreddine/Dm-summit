package com.cse.dmsummit;

import android.text.TextUtils;
import android.widget.EditText;

import java.util.Arrays;

public class Verifications {

    final static private String [] orgMails = {"bouchemelnasreddine@gmail.com", "ja_gueddoud@esi.dz", "ji_brahimi@esi.dz"};

    public static boolean verifyEmpty(EditText editText) {
        String text = editText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            editText.setError("ce champ est requis");
            return false;
        }
        return true;
    }

    public static boolean verifyOrgEmail(EditText email, String type) {
        String mail = email.getText().toString().trim();
        if (type.equals("org") && !Arrays.asList(orgMails).contains(mail)) {
            email.setError("ce n'est pas une adresse email d'organisateur ");
            return false;
        }
        return true;
    }

    public static boolean verifyParEmail(EditText email, String type) {
        String mail = email.getText().toString().trim();
        if (type.equals("par") && Arrays.asList(orgMails).contains(mail)) {
            email.setError("c'est une adresse email d'organisateur ");
            return false;
        }
        return true;
    }

    public static boolean verifyEmail(EditText email) {
        String mail = email.getText().toString().trim();
        if (!mail.contains("@") || !mail.contains(".")) {
            email.setError("l'adresse email n'est pas correct");
            return false;
        }
        return true;
    }

    public static boolean verifyParticipationEmail(EditText email) {
        String mail = email.getText().toString().trim();
        if (Arrays.asList(orgMails).contains(mail)) {
            email.setError("cet email est un email d'organisateur!");
            return false;
        };
        return true;
    }

    public static boolean verifyPassword(EditText password) {
        String pass = password.getText().toString();
        if (pass.length() < 6) {
            password.setError("le mot de passe doit contenir au moins 6 carectère");
            return false;
        }
        return true;


    }

    public static boolean verifyPasswordConfirmation(EditText password, EditText password2) {
        String pass = password.getText().toString();
        String pass2 = password2.getText().toString();

        if (!pass.equals(pass2)) {
            password2.setError("les deux mots de passe ne sont pas identique");
            return false;
        }
        return true;

    }

}



