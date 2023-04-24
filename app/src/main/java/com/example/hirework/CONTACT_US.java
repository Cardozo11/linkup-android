package com.example.hirework;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CONTACT_US extends AppCompatActivity {
//    sucess

    private RecyclerView chatsRV;
    private ImageButton sendMsgIB;
    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";

    private RequestQueue mRequestQueue;

    private ArrayList<Message> messageModalArrayList;
    private MessageRVAdapter messageRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        chatsRV = findViewById(R.id.idRVChats);
        sendMsgIB = findViewById(R.id.idIBSend);
        userMsgEdt = findViewById(R.id.idEdtMessage);


        mRequestQueue = Volley.newRequestQueue(CONTACT_US.this);
        mRequestQueue.getCache().clear();


        messageModalArrayList = new ArrayList<>();


        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userMsgEdt.getText().toString().isEmpty()) {
                    // if the edit text is empty display a toast message.
                    Toast.makeText(CONTACT_US.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                    return;
                }


                sendMessage(userMsgEdt.getText().toString());


                userMsgEdt.setText("");
            }
        });

        messageRVAdapter = new MessageRVAdapter(messageModalArrayList, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CONTACT_US.this, RecyclerView.VERTICAL, false);

        chatsRV.setLayoutManager(linearLayoutManager);

        chatsRV.setAdapter(messageRVAdapter);
    }

    private void sendMessage(String userMsg) {

        messageModalArrayList.add(new Message(userMsg, USER_KEY));
        messageRVAdapter.notifyDataSetChanged();

        String url = "http://api.brainshop.ai/get?bid=172026&key=WWuGHDxACYLAMZDe&uid=[uid]&msg=[msg]" + userMsg;

        RequestQueue queue = Volley.newRequestQueue(CONTACT_US.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    String botResponse = response.getString("cnt");
                    messageModalArrayList.add(new Message(botResponse, BOT_KEY));

                    messageRVAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();

                    messageModalArrayList.add(new Message("No response", BOT_KEY));
                    messageRVAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error handling.
                messageModalArrayList.add(new Message("Sorry no response found", BOT_KEY));
                Toast.makeText(CONTACT_US.this, "No response from the bot..", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
}