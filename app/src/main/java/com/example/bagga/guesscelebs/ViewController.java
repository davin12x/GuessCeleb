package com.example.bagga.guesscelebs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewController extends AppCompatActivity {

    ArrayList<String> name = new ArrayList<String>();
    ArrayList<String> imagePath = new ArrayList<String>();
    int randomImage = 0;
    ImageView imageView;
//    Button bb = (Button)findViewById(R.id.button);
//    Button b1 = (Button)findViewById(R.id.button1);
//    Button b2 = (Button)findViewById(R.id.button2);
//    Button b3 = (Button)findViewById(R.id.button3);
    URL url = null;
    HttpURLConnection connection = null;

    public class DownloadImage extends AsyncTask<String,Void,Bitmap>{


        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                url = new URL(urls[0]);
                connection = (HttpURLConnection)url.openConnection();
                InputStream inputStream = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }

            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    public class Service extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();


            try{
                url = new URL(urls[0]);
                connection =(HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data != -1){
                    char current = (char)data;
                    result.append(current);
                    data = reader.read();
                }

                return result.toString();
            }
            catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_controller);
        imageView = (ImageView) findViewById(R.id.celeb);
        Service downloadTask = new Service();
        String result = "";
        try {
            result = downloadTask.execute("http://www.posh24.com/celebrities").get();
            String[] splitResult = result.split("<div class=\"sidebarContainer\">");
            Pattern p = Pattern.compile("<img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResult[0]);
            while (m.find()){
                //ImagePath
                imagePath.add(m.group(1));
            }
            p = Pattern.compile("alt=\"(.*?)\"");
            m = p.matcher(splitResult[0]);
            while (m.find()) {
                //Celeb Names
                name.add(m.group(1));
                Result();
            }
            Random random = new Random();
            randomImage = random.nextInt(imagePath.size());
            DownloadImage imageDownloader = new DownloadImage();
            Bitmap celebImage;
            celebImage = imageDownloader.execute(imagePath.get(randomImage)).get();
            imageView.setImageBitmap(celebImage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    public void Result(){

    }
}
