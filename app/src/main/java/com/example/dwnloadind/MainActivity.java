package com.example.dwnloadind;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    Button button;
    ImageView imageView;
    TextView textView, starDescription;
    int buttonClickedCount = 0 ;
    String url = "https://svenskainfluencers.nu/";

    public void downloadImg(View view){
        dowloadImage download = new dowloadImage();
        downloadText description = new downloadText();
        String result = null;
        // link of picture
        String[] picutreLink = {"https://svenskainfluencers.nu/wp-content/uploads/Sk%C3%A4rmavbild-2020-06-28-kl.-22.33.08.png",
                "https://svenskainfluencers.nu/wp-content/uploads/Sk%C3%A4rmavbild-2020-06-28-kl.-22.31.16.png",
                "https://svenskainfluencers.nu/wp-content/uploads/Sk%C3%A4rmavbild-2020-06-28-kl.-22.30.30.png",
                "https://svenskainfluencers.nu/wp-content/uploads/Sk%C3%A4rmavbild-2020-06-28-kl.-22.29.25.png",
                "https://svenskainfluencers.nu/wp-content/uploads/Sk%C3%A4rmavbild-2020-06-28-kl.-22.28.17.png",
                "https://svenskainfluencers.nu/wp-content/uploads/Sk%C3%A4rmavbild-2020-06-28-kl.-22.27.21.png",
                "https://svenskainfluencers.nu/wp-content/uploads/Sk%C3%A4rmavbild-2020-06-28-kl.-22.26.42.png",
                "https://svenskainfluencers.nu/wp-content/uploads/Sk%C3%A4rmavbild-2020-06-28-kl.-22.24.06.png",
                "https://svenskainfluencers.nu/wp-content/uploads/Sk%C3%A4rmavbild-2020-06-28-kl.-22.21.13.png",
                "https://svenskainfluencers.nu/wp-content/uploads/Sk%C3%A4rmavbild-2020-06-28-kl.-22.19.55-1.png"};
        String[] star = {"MARGAUX DIETZ", "ISABELLA LÖWENGRIP ", "BENJAMIN INGROSSO ", "LINN AHLBORG ", "OSCAR ENESTAD ", "OSCAR ZIA ", "MAURI HERMUNDSSON ", "FELICIA BERGSTRÖM "
        ,"ALICE STENLÖF ", "NICOLE FALCIANI "};
        Bitmap myImage;
        Pattern pattern;
        Matcher matcher;

        try {
                if(buttonClickedCount <= 9){
                    myImage = download.execute(picutreLink[buttonClickedCount]).get();
                    imageView.setImageBitmap(myImage);
                    textView.setText(star[buttonClickedCount]);

                    result = description.execute("https://svenskainfluencers.nu/").get();
                    String[] splitResult = result.split("</div><!-- .entry-content -->");
                    pattern = Pattern.compile(star[buttonClickedCount] +  "<p>(.*?)</p>");
                    matcher = pattern.matcher(splitResult[0]);
                    String descript = "";
                    while(matcher.find()){
                        descript +=(matcher.group(1));
                    }
                    starDescription.setText(descript);

                    Log.i("Success", "Success");
                    buttonClickedCount++;
                }else{
                    buttonClickedCount = 0;
                }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        starDescription = findViewById(R.id.starDescription);

    }
    public class dowloadImage extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);

                return  myBitmap;

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

    }
    public class downloadText extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;

            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
            }

                return result;

            }catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

}