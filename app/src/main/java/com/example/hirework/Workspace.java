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
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Workspace extends AppCompatActivity {
    TextView orderTitle,orderPrice,orderDesc,orderCustomer;
    TextView orderId,attachment;
    TextView completionDays_txt,fileFormat_txt;

    ImageView uploadWork;
    Button submitBtn;
    Button startWorkBtn;

    TextView fileName;

    String FILE_NAME;
    Uri pdfUri;

    String[] descriptionData = {"Accepted","In Development", "Submitted","Completed"};

    StateProgressBar stateProgressBar;


    String order_description,order_status,order_customer,order_freelancer,order_tracking_status;
    int gig_id,order_price;

    //int order_id;

    private static final int PICK_PDF_FILE = 1;

    ProgressBar progressBar;

    String TrackingStatus;
    StateProgressBar.StateNumber stateNumber;

    private static final  String FREELANCER_UPDATE_TRACKING_URL="https://hirework.tech/WorkStarted.php";

    private static final String UPDATE_ORDER_URL="https://hirework.tech/AcceptDecline.php";



    StorageReference storageReference;
    DatabaseReference databaseReference;

    OrderModel order;


   Button accept,decline;

   String status;

   Freelancer freelancer;
   SharedPreferences sp;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);

        freelancer=new Freelancer(this);
        sp=getSharedPreferences("proj", Context.MODE_PRIVATE);
        freelancer.FreelancerUsername=sp.getString(freelancer.SP_FREELANCER_USERNAME,"");

        accept=findViewById(R.id.accept_button);
        decline=findViewById(R.id.decline_button);
        orderId=findViewById(R.id.orderId2);
        attachment=findViewById(R.id.attach);

        startWorkBtn=findViewById(R.id.startDevelopment);
        completionDays_txt=findViewById(R.id.gCompletionDays);
        fileFormat_txt=findViewById(R.id.gFileFormat);


        progressBar = findViewById(R.id.progress);


        orderTitle=findViewById(R.id.ord_title);
        orderPrice=findViewById(R.id.ord_price);
        orderDesc=findViewById(R.id.des);
        orderCustomer=findViewById(R.id.orderCustomer);

        uploadWork=findViewById(R.id.uploadWork);
        submitBtn=findViewById(R.id.submitWorkBtn);

        stateProgressBar =findViewById(R.id.order_tracking);
        stateProgressBar.setStateDescriptionData(descriptionData);

        fileName=findViewById(R.id.fileNameTxt);

        order=new OrderModel(this);

        order.ActivityPurpose=getIntent().getStringExtra(order.INTENT_PURPOSE);





        order.orderId=getIntent().getIntExtra(order.INTENT_ORDER_ID,0);
        order.gigOrderTitle=getIntent().getStringExtra("order_Title");
        gig_id=getIntent().getIntExtra("order_gig_id",0);
        order_price=getIntent().getIntExtra("order_price",0);

        order_description=getIntent().getStringExtra("order_desc");
        order_status=getIntent().getStringExtra("order_status");
        order_customer=getIntent().getStringExtra("order_customer");
        order_freelancer=getIntent().getStringExtra("order_freelancer");
        order_tracking_status=getIntent().getStringExtra("order_Tracking_status");

        order.completionDays=getIntent().getStringExtra("order_days");
        order.fileForamt=getIntent().getStringExtra("order_file_format");
        order.additionalInfo=getIntent().getStringExtra("order_Additional");



        if(order.ActivityPurpose.equals("Accept/Decline")){
            stateProgressBar.setVisibility(View.GONE);
            accept.setVisibility(View.VISIBLE);
            decline.setVisibility(View.VISIBLE);
        }
        else{
            checkStatus();
        }





        orderTitle.setText(order.gigOrderTitle);
        orderDesc.setText(String.valueOf(order_description));
        orderPrice.setText("₹ "+String.valueOf(order_price));
        orderCustomer.setText(order_customer);
        completionDays_txt.setText(order.completionDays+" Days");
        fileFormat_txt.setText(order.fileForamt);
        orderId.setText("Order ID : "+String.valueOf(order.orderId));
        attachment.setText("-");


        storageReference= FirebaseStorage.getInstance().getReference();
        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");






        startWorkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserOnWorkStarted("In Development");
                UPDATE_ACTIVE_ORDERS();
            }
        });

        uploadWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPdfFile();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pdfUri !=null){
                    uploadFile(pdfUri);
                }
                else{
                    Toast.makeText(Workspace.this, "Select a File", Toast.LENGTH_SHORT).show();
                }

            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAcceptDecline("Accepted");
                Toast.makeText(Workspace.this, "Order Accepted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Dashboard.class));

            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAcceptDecline("Declined");
                Toast.makeText(Workspace.this, "Order Declined", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
            }
        });

        fileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //display(pdfUri);
            }
        });




    }

    private void checkStatus(){
        if (order_tracking_status.equals("Accepted")){
            startWorkBtn.setVisibility(View.VISIBLE);
            stateProgressBar.setVisibility(View.VISIBLE);
        }

        else if (order_tracking_status.equals("In Development")){
            startWorkBtn.setVisibility(View.GONE);
            uploadWork.setVisibility(View.VISIBLE);
            stateProgressBar.setVisibility(View.VISIBLE);
            submitBtn.setVisibility(View.GONE);
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("TWO"));
        }

        else if(order_tracking_status.equals("Submitted")){
            submitBtn.setVisibility(View.GONE);
            startWorkBtn.setVisibility(View.GONE);
            fileName.setVisibility(View.VISIBLE);
            stateProgressBar.setVisibility(View.VISIBLE);
            fileName.setText("Waiting for conformation from the Customer...");
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("THREE"));
        }
    }

    private void updateUserOnWorkStarted(String TrackingStatus){
        String orderId=String.valueOf(order.orderId);
        StringRequest request = new StringRequest(Request.Method.POST,FREELANCER_UPDATE_TRACKING_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Updated Successfully")) {
                    Toast.makeText(Workspace.this, response, Toast.LENGTH_SHORT).show();
                    startWorkBtn.setVisibility(View.GONE);
                    uploadWork.setVisibility(View.VISIBLE);
                    //stateProgressBar.setCurrentStateNumber(2);
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("TWO"));
                    startActivity(new Intent(getApplicationContext(),Dashboard.class));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Workspace.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("oId",orderId);
                params.put("WorkStatus", TrackingStatus);
                params.put("file","Upload File");
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Workspace.this);
        requestQueue.add(request);

    }


    private void pickPdfFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_PDF_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK && data != null && data.getData()!=null) {
            pdfUri = data.getData();
            if (pdfUri != null) {
                uploadWork.setVisibility(View.GONE);
                fileName.setVisibility(View.VISIBLE);
                FILE_NAME=getFileName(pdfUri);
                fileName.setText(String.valueOf(FILE_NAME));
                submitBtn.setVisibility(View.VISIBLE);


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



    private void updateUserOnWorkSubmitted(String TrackingStatus){
        String orderId=String.valueOf(order.orderId);
        StringRequest request = new StringRequest(Request.Method.POST,FREELANCER_UPDATE_TRACKING_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Updated Successfully")) {
                    Toast.makeText(Workspace.this, response, Toast.LENGTH_SHORT).show();
                    stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.valueOf("THREE"));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Workspace.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("oId",orderId);
                params.put("WorkStatus", TrackingStatus);
                params.put("file",FILE_NAME);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Workspace.this);
        requestQueue.add(request);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),Dashboard.class));
    }

    private void uploadFile(Uri data){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Uploading....");
        progressDialog.show();


        //StorageReference reference=storageReference.child("uploads/"+System.currentTimeMillis()+".pdf");
        StorageReference reference=storageReference.child("uploads/"+FILE_NAME);
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

                        Toast.makeText(Workspace.this, "File  Uploaded !!", Toast.LENGTH_SHORT).show();
                        updateUserOnWorkSubmitted("Submitted");
                        submitBtn.setVisibility(View.GONE);
                        uploadWork.setVisibility(View.GONE);

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

    public void updateAcceptDecline(String status){
        String oId=String.valueOf(order.orderId);
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(PlaceOrder2.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error.getMessage()!=null){
                    Toast toast=Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("oId", oId);
                params.put("oPayStatus",status);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Workspace.this);
        requestQueue.add(request);
    }

    private void UPDATE_ACTIVE_ORDERS(){
        String UPDATE_ACTIVE_ORDER_URL="https://hirework.tech/updateActive.php";
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_ACTIVE_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(PlaceOrder2.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error.getMessage()!=null){
                    Toast toast=Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("fUsername", freelancer.FreelancerUsername);
                return params;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Workspace.this);
        requestQueue.add(request);
    }

}