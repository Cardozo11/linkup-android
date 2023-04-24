package com.example.hirework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class viewPDF extends AppCompatActivity {

    String url;
    PDFView pdfView;
    ProgressDialog dialog;
    OrderModel order;

    TextView text1;
    Button download;

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference("/uploads/-NQo9FNDmE0wb6Gz7M9f/url");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        pdfView=findViewById(R.id.pdfview);
        text1=findViewById(R.id.text1);
        download=findViewById(R.id.downloadFile);

        order = new OrderModel(this);

        order.orderId = getIntent().getIntExtra("order_id", 0);

        String purpose=getIntent().getStringExtra("purpose");

        if(purpose.equals("view")){
            download.setVisibility(View.GONE);
        }

       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NotNull DataSnapshot snapshot) {
               String value = snapshot.getValue(String.class);

               if(value==null){
                   Toast.makeText(viewPDF.this, "empty", Toast.LENGTH_SHORT).show();
               }
               else{
               Toast.makeText(viewPDF.this, "Failed to load", Toast.LENGTH_SHORT).show();
           }
       }

           @Override
           public void onCancelled(@NonNull @NotNull DatabaseError error) {
           }

           });


    download.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //Toast.makeText(viewPDF.this, "Downloading ....", Toast.LENGTH_SHORT).show();

            //downloadPDFFromFirebase();
            //downloadFile(viewPDF.this,"test",".pdf","downloads");
            downloadFile(viewPDF.this,"test",".pdf", Environment.DIRECTORY_DOWNLOADS);
        }
    });
    //}




        class RetrievePdfStream extends AsyncTask<String, Void, InputStream> {

            @Override
            protected InputStream doInBackground(String... strings) {
                InputStream inputStream = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    if(urlConnection.getResponseCode()==200){
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());

                    }
                }
                catch (IOException e){
                    return null;
                }
                return inputStream;
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {
                pdfView.fromStream(inputStream).load();
            }

        }
    }



    public long downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory) {


        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);
        request.setDestinationInExternalPublicDir(destinationDirectory, fileName + fileExtension);

        return downloadmanager.enqueue(request);
    }


    }


