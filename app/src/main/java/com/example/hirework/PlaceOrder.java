package com.example.hirework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlaceOrder extends AppCompatActivity {
    Button btn;
    ImageView img;
    TextView price,commission,total,deliveryTime,grandTotal,desc,f;

    String c_username;
    SharedPreferences sp;
    String FILE_NAME;

    GigModel gig;
    OrderModel order;
    Calendar calendar;
    Date currentDate;// variable for current date
    Date submissionDate;// variable for submission date
    SimpleDateFormat dateFormat;
    String orderPlacedDate;// to store the order placed date
    String dueDate;// to store the due date

    String total_price;
Button uploadBtn;
    Uri pdfUri;
    TextView fileName;
    private static final int PICK_PDF_FILE = 1;
    public static final String PLACE_ORDER_URL="https://hirework.tech/addOrder.php";
    public static final String NOTIFY_FREELANCER_URL="https://hirework.tech/OrderNotification.php";

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference("/uploads/-NSqeRCS-JFx7txb-CO8/url");

    StorageReference storageReference;
    DatabaseReference databaseReference;

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        f=findViewById(R.id.f);
        btn=findViewById(R.id.sendOrder);
        uploadBtn=findViewById(R.id.sample);
        fileName=findViewById(R.id.fileNam);
        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");



        gig=new GigModel(this);
        order=new OrderModel(this);
        calendar = Calendar.getInstance();
        currentDate=new Date();
        submissionDate=new Date();

        sp = getSharedPreferences("proj", Context.MODE_PRIVATE);
        c_username=sp.getString("username","");

        gig.GigTitle=getIntent().getStringExtra(gig.INTENT_GIG_ID);
        gig.fUsername=getIntent().getStringExtra(gig.INTENT_GIG_FREELANCER_USERNAME);

        gig.GigId=getIntent().getIntExtra(gig.INTENT_GIG_ID,0);
        gig.price=getIntent().getIntExtra(gig.INTENT_GIG_PRICE,0);
        gig.CompletionDays=getIntent().getIntExtra(gig.INTENT_GIG_COMPLETION_DAYS,0);
        gig.Image1=getIntent().getStringExtra(gig.INTENT_GIG_IMAGE);


        btn=findViewById(R.id.sendOrder);
        price=findViewById(R.id.orderPrice);
        commission=findViewById(R.id.orderCommission);
        total=findViewById(R.id.orderTotal);
        deliveryTime=findViewById(R.id.orderDeliveryDate);
        grandTotal=findViewById(R.id.orderGrandTotal);
        desc=findViewById(R.id.orderDescription);
        img=findViewById(R.id.gigImg);

        price.setText("₹ "+String.valueOf(gig.price));
        commission.setText("₹ "+String.valueOf(50));
        total.setText("₹ "+String.valueOf(gig.price+50));
        deliveryTime.setText(String.valueOf(gig.CompletionDays)+" Days");
        grandTotal.setText("₹ "+String.valueOf(gig.price+50));
        //grandTotal.setText(String.valueOf(gig.price+50));

        Glide.with(PlaceOrder.this).load(gig.Image1).into(img);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total_price=String.valueOf(gig.price+50);
                placeOrder();
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPdfFile();
                uploadBtn.setVisibility(View.GONE);
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Log.d("TAG", "Value from database: " + value);

                if(value==null){
                    Toast.makeText(PlaceOrder.this, "empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    f.setText(value);

                    url=f.getText().toString();
                    //url="https://firebasestorage.googleapis.com/v0/b/linkup-a9e74.appspot.com/o/uploads%2Ftest.pdf?alt=media&token=d23ffda8-a500-4a14-bda3-0273f09575e7";
                    Toast.makeText(PlaceOrder.this, "File loaded successfully !!", Toast.LENGTH_SHORT).show();
                    //new viewPDF.RetrievePdfStream().execute(url);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

                Toast.makeText(PlaceOrder.this, "Failed to load", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void placeOrder(){
        String order_desc=desc.getText().toString().trim();
        String gigId=String.valueOf(gig.GigId);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        orderPlacedDate= dateFormat.format(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, gig.CompletionDays);
        submissionDate=calendar.getTime();
        dueDate=dateFormat.format(submissionDate);

        StringRequest request = new StringRequest(Request.Method.POST, PLACE_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")){
                    Toast.makeText(PlaceOrder.this, response, Toast.LENGTH_SHORT).show();
                    notifyFreelancer(gig.fUsername,c_username,order_desc);
                    uploadFile(pdfUri);
                    //goNext();
                }
                else{
                    Toast.makeText(PlaceOrder.this,response,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Toast.makeText(PlaceOrder.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("oDescription", order_desc);
                params.put("cUsername", c_username);
                params.put("fUsername", gig.fUsername);
                params.put("gigId", gigId);
                params.put("orderPrice", total_price);
                params.put("oPlacedOn",orderPlacedDate);
                params.put("dueDate",dueDate);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(PlaceOrder.this);
        requestQueue.add(request);
    }

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
            //pdfView.fromStream(inputStream).load();
        }

    }

    public void notifyFreelancer(String fusername,String cUsername,String desc){
        StringRequest request = new StringRequest(Request.Method.POST, NOTIFY_FREELANCER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")){
                }
                else{
                    Toast.makeText(PlaceOrder.this,response,Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                Toast.makeText(PlaceOrder.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("fUsername", gig.fUsername);
                params.put("cUsername", c_username);
                params.put("oDescription", desc);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(PlaceOrder.this);
        requestQueue.add(request);
    }

    private void goNext(){
        Intent intent=new Intent(getApplicationContext(),PlaceOrder2.class);
        intent.putExtra("purpose","placeOrder");
        intent.putExtra("order_price",total_price);
        startActivity(intent);
    }

    private void pickPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("application/pdf");
        intent.setType("*/*");
        startActivityForResult(intent, PICK_PDF_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK && data != null && data.getData()!=null) {
            pdfUri = data.getData();
            if (pdfUri != null) {
                fileName.setVisibility(View.VISIBLE);
                FILE_NAME=getFileName(pdfUri);
                fileName.setText(String.valueOf(FILE_NAME));
                //submitBtn.setVisibility(View.VISIBLE);


                //uploadFile(data.getData());
            }
        }
    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void uploadFile(Uri data){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading....");
        progressDialog.show();


        //StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        StorageReference reference=storageReference.child("samples/"+FILE_NAME);
        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isComplete());
                        Uri url=uriTask.getResult();

                        FileClass pdfClass=new FileClass(FILE_NAME.toString(),url.toString(),order.orderId);

//                        FileClass pdfClass=new FileClass(pdf_name.getText().toString(),url.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(pdfClass);

                        Toast.makeText(PlaceOrder.this, "File  Uploaded !!", Toast.LENGTH_SHORT).show();
//                        updateUserOnWorkSubmitted("Submitted");
//                        submitBtn.setVisibility(View.GONE);
//                        uploadWork.setVisibility(View.GONE);

                        progressDialog.dismiss();
                        //progressBar.setVisibility(View.GONE);

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {

                double progress=(100.0* snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading:"+(int)progress+"%");


            }
        });
    }

    private void getFile(){

    }
}