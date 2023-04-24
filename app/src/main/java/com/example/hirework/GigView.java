package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GigView extends AppCompatActivity {


    Button btn;
    ImageView gImage;
    ImageView Profile;


    TextView fUsername,gTitle,desc,gPrice,gCompletionDays,gFileFormat;

    RecyclerView reviewRecycler;
    ReviewAdapter adapter;
    LinearLayoutManager HorizontalLayout;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    ArrayList<String> data;

    GigModel gig;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gig_view);

        btn=findViewById(R.id.plcOrder);
        gImage=findViewById(R.id.gImage);
        Profile =findViewById(R.id.fpro);

        gig=new GigModel(this);


        gig.GigId=getIntent().getIntExtra(gig.INTENT_GIG_ID,0);
        gig.fUsername=getIntent().getStringExtra(gig.INTENT_GIG_FREELANCER_USERNAME);
        gig.GigTitle=getIntent().getStringExtra(gig.INTENT_GIG_TITLE);
        gig.GigDescription=getIntent().getStringExtra(gig.INTENT_GIG_DESCRIPTION);
        gig.Image1=getIntent().getStringExtra(gig.INTENT_GIG_IMAGE);
        gig.price=getIntent().getIntExtra(gig.INTENT_GIG_PRICE,0);
        gig.CompletionDays=getIntent().getIntExtra(gig.INTENT_GIG_COMPLETION_DAYS,1);
        gig.GigFileFormat=getIntent().getStringExtra(gig.INTENT_GIG_FILE_FORMAT);
        gig.freelancerProfile=getIntent().getStringExtra(gig.INTENT_GIG_FREELANCER_PROFILE);

        reviewRecycler=findViewById(R.id.reviewRecycler);
        fUsername=findViewById(R.id.fUsername);
        gTitle=findViewById(R.id.gTitle);
        gPrice=findViewById(R.id.gPrice);
        gCompletionDays=findViewById(R.id.gCompletionDays);
        gFileFormat=findViewById(R.id.gFileFormat);

        desc=findViewById(R.id.des);

        fUsername.setText(gig.fUsername);
        gTitle.setText(gig.GigTitle);
        desc.setText(gig.GigDescription);
        gPrice.setText("₹ "+String.valueOf(gig.price));
        gFileFormat.setText(gig.GigFileFormat);

        Glide.with(GigView.this).load(gig.Image1).into(gImage);
        Glide.with(GigView.this).load(gig.freelancerProfile).into(Profile);


        if(gig.freelancerProfile==null){
            Toast.makeText(this, "empty", Toast.LENGTH_SHORT).show();
        }



        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        reviewRecycler.setLayoutManager(RecyclerViewLayoutManager);
        loadReviews();
        adapter = new ReviewAdapter(data);
        HorizontalLayout = new LinearLayoutManager(GigView.this, LinearLayoutManager.HORIZONTAL, false);
        reviewRecycler.setLayoutManager(HorizontalLayout);
        reviewRecycler.setAdapter(adapter);


        //loadImage();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),PlaceOrder.class);
                i.putExtra(gig.INTENT_GIG_ID,gig.GigId);
                i.putExtra(gig.INTENT_GIG_IMAGE,gig.Image1);
                i.putExtra(gig.INTENT_GIG_FREELANCER_USERNAME,gig.fUsername);
                i.putExtra(gig.INTENT_GIG_PRICE,gig.price);
                i.putExtra(gig.INTENT_GIG_TITLE,gig.GigTitle);
                i.putExtra(gig.INTENT_GIG_COMPLETION_DAYS,gig.CompletionDays);
                startActivity(i);

            }
        });

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (desc.getMaxLines() == 3) {
                    desc.setMaxLines(Integer.MAX_VALUE);
                    //collapseButton.setText("Collapse");
                } else {
                    desc.setMaxLines(3);
                    //collapseButton.setText("Read more");
                }
            }
        });
    }

    private void loadReviews(){
        data = new ArrayList<>();
        data.add("vickiwarner");
        data.add("frankie47b");
        data.add("ceelbesk");
    }


//    private void loadImage(){
//        class GetAllImages extends AsyncTask<String,Void,String> {
//            ProgressDialog loading;
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(GigView.this, "Fetching Data...","Please Wait...",true,true);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                imagesJSON = s;
//                extractJSON();
//                showImage();
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//                String uri = params[0];
//                BufferedReader bufferedReader = null;
//                try {
//                    URL url = new URL(uri);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    StringBuilder sb = new StringBuilder();
//
//                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//                    String json;
//                    while((json = bufferedReader.readLine())!= null){
//                        sb.append(json+"\n");
//                    }
//
//                    return sb.toString().trim();
//
//                }catch(Exception e){
//                    return null;
//                }
//            }
//        }
//        GetAllImages gai = new GetAllImages();
//        gai.execute(LOAD_CUSTOMER_PROFILE_PIC_URL);
//    }

//    private void showImage(){
//        try {
//            JSONObject jsonObject = arrayImages.getJSONObject(TRACK);
//            getImage(jsonObject.getString(IMAGE_URL));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    private void extractJSON(){
//        try {
//            JSONObject jsonObject = new JSONObject(imagesJSON);
//            arrayImages = jsonObject.getJSONArray(JSON_ARRAY);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }


//    private void getImage(String urlToImage){
//        class GetImage extends AsyncTask<String,Void, Bitmap>{
//            ProgressDialog loading;
//            @Override
//            protected Bitmap doInBackground(String... params) {
//                URL url = null;
//                Bitmap image = null;
//
//                String urlToImage = params[0];
//                try {
//                    url = new URL(urlToImage);
//                    image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return image;
//            }
//
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(GigView.this,"Downloading Image...","Please wait...",true,true);
//            }
//
//            @Override
//            protected void onPostExecute(Bitmap bitmap) {
//                super.onPostExecute(bitmap);
//                loading.dismiss();
//                gImage.setImageBitmap(bitmap);
//                //profilePic2.setImageBitmap(bitmap);
//            }
//        }
//        GetImage gi = new GetImage();
//        gi.execute(urlToImage);
//    }
}