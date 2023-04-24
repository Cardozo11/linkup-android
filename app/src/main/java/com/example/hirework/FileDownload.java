package com.example.hirework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FileDownload extends AppCompatActivity {

    OrderModel order;

    Button view;
    DatabaseReference database;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_download);

        order=new OrderModel(this);

        order.orderId=getIntent().getIntExtra("order_id",0);

        view = findViewById(R.id.view);


        database = FirebaseDatabase.getInstance().getReference().child("pdf");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                message = dataSnapshot.getValue(String.class);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(FileDownload.this, "Error Loading Pdf", Toast.LENGTH_SHORT).show();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CharSequence options[] = new CharSequence[]{
                        "Download",
                        "View",
                        "Cancel"
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Choose One");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // we will be downloading the pdf
                        if (which == 0) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(message));
                            startActivity(intent);
                        }
                        // We will view the pdf
                        if (which == 1) {
                            Intent intent = new Intent(v.getContext(), viewPDF.class);
                            intent.putExtra("url", message);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });


    }
}