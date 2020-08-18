package com.example.first;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
@SuppressLint("HandlerLeak")
public class UI_updata_handler extends AppCompatActivity {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String str = msg.getData().getString("result");
                    text.setText(str);

                    break;
            }
        }
    };
    private Button button;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_i_updata_handler);

        button = findViewById(R.id.button_handler);
        text = findViewById(R.id.textView_handler);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        String result = null;
                        final Request request = new Request.Builder()
                                .url("https://httpbin.org/ip")
                                .build();
                        final Call call = okHttpClient.newCall(request);
                        Response response = null;
                        try {
                            response = call.execute();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            assert response != null;
                            assert response.body() != null;
                            result = response.body().string();
                            System.out.println("onResponse: " + result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Message message = Message.obtain();
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("result",result);
                        message.setData(bundle);
                        handler.sendMessage(message);

                    }
                });
                thread.start();
            }
        });
    }

}
