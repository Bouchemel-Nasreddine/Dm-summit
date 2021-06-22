package com.cse.dmsummit;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ParticipansAdapter extends ArrayAdapter<MainActivity.User> {
    private ArrayList<MainActivity.User> list;
    private LayoutInflater inf;
    public ParticipansAdapter(@NonNull Context context, int resource, ArrayList<MainActivity.User> list) {
        super(context, resource);
        this.inf = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public MainActivity.User getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getPosition(@Nullable MainActivity.User item) {
        return list.indexOf(item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = inf.inflate(R.layout.participans, null);

        TextView name = convertView.findViewById(R.id.participans_name);
        ImageView picture = convertView.findViewById(R.id.participans_picture);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference pictureRef = storageReference.child(list.get(position).getID() + ".jpg");
        View finalConvertView = convertView;
        pictureRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(finalConvertView.getContext())
                        .load(uri)
                        .centerCrop()
                        .into(picture);
            }
        });


        name.setText(list.get(position).getFirstName() + " " + list.get(position).getLastName());

        return convertView;
    }
}
