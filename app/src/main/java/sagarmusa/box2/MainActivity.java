/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package sagarmusa.box2;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;
import android.widget.MediaController;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import android.widget.VideoView;
import android.os.Environment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sagarmusa.box2.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/*
 * MainActivity class that loads MainFragment
 */
public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    int downloadedSize = 0;
    int totalSize = 0;
    static final String URL_PRODUCTS = "http://localhost/Api.php";
    List<Product> productList;
    String box_id = "1";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*int PERMISSION_ALL = 1;
        //Add necessary permissions
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }*/
        //productList = new ArrayList<>();
        //loadProducts();
        //to play video
        //final Product product = productList.get(productList.indexOf(1));
        //final String file = product.getD1();
        //String file = "abc.mp4";
        /*new Thread(new Runnable() {
            public void run() {
                String dwnload_file_path = "http://172.16.48.144/abc.mp4";
                String name = "abc.mp4";
                downloadFile(dwnload_file_path,name);
            }
        }).start();*/

        VideoView myVideoView = (VideoView) findViewById(R.id.video1);
        myVideoView.setVideoPath("http://172.16.48.144/abc.mp4");
        myVideoView.setMediaController(new MediaController(this));
        myVideoView.setMediaController(null);//to disable video controller
        myVideoView.start();
        //to repeat the video in a loop
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);

            }
        });

        /*
        ImageView iview= (ImageView) findViewById(R.id.ig1);
        iview.setImageBitmap(BitmapFactory.decodeFile("http://192.168.43.133/android/a.png"));
        */

        //to display image from server using webview
        /*new Thread(new Runnable() {
            public void run() {
                String dwnload_file_path = "http://172.16.48.144/image.png";
                String name = "image.png";
                downloadFile(dwnload_file_path,name);
            }
        }).start();*/


        final WebView myWebView1 = findViewById(R.id.web1);
        myWebView1.getSettings().setLoadWithOverviewMode(true);
        myWebView1.getSettings().setUseWideViewPort(true);
        myWebView1.setWebViewClient(new WebViewClient());
        myWebView1.setInitialScale(1);
        myWebView1.loadUrl("http://172.16.48.144/image.png");

        /*ImageView jpgView = (ImageView) findViewById(R.id.web1);
        Bitmap bitmapImage = BitmapFactory.decodeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/image.png");
        jpgView.setImageBitmap(bitmapImage);*/


        //to open a URL
        final WebView myWebView2 =  (WebView) findViewById(R.id.web2);
        myWebView2.setWebViewClient(new WebViewClient());
        myWebView2.loadUrl("http://172.16.48.144/Check/"+box_id+"1.html");


        //To display a text
        final WebView myWebView3 = (WebView) findViewById(R.id.web3);
        myWebView3.setWebViewClient(new WebViewClient());
        myWebView3.loadUrl("http://172.16.48.144/Check/"+box_id+"2.html");

        Timer repeatTask = new Timer();
        repeatTask.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myWebView1.loadUrl("http://172.16.48.144/image.png");
                        myWebView2.loadUrl("http://172.16.48.144/Check"+box_id+"1.html");
                        myWebView3.loadUrl("http://172.16.48.144/Check"+box_id+"2.html");
                    }
                });
            }
        }, 0, 20000);

    }

    public static boolean hasPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    void downloadFile(String dwnload_file_path,String name){
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            //connect
            urlConnection.connect();

            //set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            //create a new file, to save the downloaded file
            File file = new File(SDCardRoot,name);

            FileOutputStream fileOutput = new FileOutputStream(file);

            //Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            //this is the total size of the file which we are downloading
            totalSize = urlConnection.getContentLength();

            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
            }
            //close the output stream when complete //
            fileOutput.close();
            runOnUiThread(new Runnable() {
                public void run() {
                    // pb.dismiss(); // if you want close it..
                }
            });

        } catch (final MalformedURLException e) {
            showError("Error : MalformedURLException " + e);
            e.printStackTrace();
        } catch (final IOException e) {
            showError("Error : IOException " + e);
            e.printStackTrace();
        }
        catch (final Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
    }

    void showError(final String err){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG).show();
            }
        });
    }

    void loadProducts(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                                //adding the product to product list
                                productList.add(new Product(
                                        product.getString("id"),
                                        product.getString("name"),
                                        product.getString("d1"),
                                        product.getString("d2"),
                                        product.getString("d3")
                                ));
                                Log.d("string " , product.getString("d1"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
    }
}