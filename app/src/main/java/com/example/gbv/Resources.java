package com.example.gbv;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Resources extends AppCompatActivity {
    ImageView back;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resources);

        FirebaseApp.initializeApp(this);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        back = findViewById(R.id.Back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Resources.this, Dashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler);
        FirebaseStorage.getInstance().getReference().child("videos").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(ListResult listResult) {
                ArrayList<Video> arrayList = new ArrayList<>();
                VideoAdapter adapter = new VideoAdapter(Resources.this, arrayList);
                adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(Video video) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getUrl()));
                        intent.setDataAndType(Uri.parse(video.getUrl()), "video/*");
                        startActivity(intent);

                    }

                });
                recyclerView.setAdapter(adapter);
                listResult.getItems().forEach(new Consumer<StorageReference>() {
                    @Override
                    public void accept(StorageReference storageReference) {
                        Video video = new Video();
                        video.setTitle(storageReference.getName());
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String url = "https://" + task.getResult().getEncodedAuthority() + task.getResult().getEncodedPath() + "?alt=media&token=" +
                                        task.getResult().getQueryParameters("token").get(0);
                                video.setUrl(url);
                                arrayList.add(video);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Resources.this, "Failed to retrieve videos", Toast.LENGTH_SHORT).show();
            }
        });

    }
}