package algonquin.cst2335.victorsandroidlabs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class MainActivity extends AppCompatActivity {

    /** This string represents the address of the server we will connect to */
    private String stringURL;
    Bitmap image = null;

    String description = null;
    String iconName = null;
    String current = null;
    String min = null;
    String max = null;
    String humidity = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TextView currentTemp = findViewById(R.id.temp);
        TextView maxTemp = findViewById(R.id.maxTemp);
        TextView minTemp = findViewById(R.id.minTemp);
        TextView humidity = findViewById(R.id.humidity);
        TextView description = findViewById(R.id.description);
        ImageView icon = findViewById(R.id.icon);
        EditText cityField = findViewById(R.id.city_et);

        float oldSize = 14;
        // switch not work in new version
        if (item.getItemId() == R.id.hide_views) {
            currentTemp.setVisibility(View.INVISIBLE);
            maxTemp.setVisibility(View.INVISIBLE);
            minTemp.setVisibility(View.INVISIBLE);
            humidity.setVisibility(View.INVISIBLE);
            description.setVisibility(View.INVISIBLE);
            icon.setVisibility(View.INVISIBLE);
            cityField.setText("");
        } else if (item.getItemId() == R.id.id_increase) {
            oldSize++;
            currentTemp.setTextSize(oldSize);
            minTemp.setTextSize(oldSize);
            maxTemp.setTextSize(oldSize);
            humidity.setTextSize(oldSize);
            description.setTextSize(oldSize);
            cityField.setTextSize(oldSize);
        } else if (item.getItemId() == R.id.id_decrease) {
            oldSize = Float.max(oldSize-1, 5);
            currentTemp.setTextSize(oldSize);
            minTemp.setTextSize(oldSize);
            maxTemp.setTextSize(oldSize);
            humidity.setTextSize(oldSize);
            description.setTextSize(oldSize);
            cityField.setTextSize(oldSize);
        } else if (item.getItemId() == 5) {
            String cityName = item.getTitle().toString();
            runForecast(cityName);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.popout_menu);
        navigationView.setNavigationItemSelectedListener((item) -> {
            onOptionsItemSelected(item);
            drawer.closeDrawer(GravityCompat.START);
            return false;
        });

        Button forecastBtn = findViewById(R.id.forecast_btn);
        EditText cityText = findViewById(R.id.city_et);
        forecastBtn.setOnClickListener((click) -> {
            String cityName = cityText.getText().toString();
            myToolbar.getMenu().add( 0, 5, 0, cityName).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            runForecast(cityName);
        });
    }

    private void runForecast(String cityName) {

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Getting forecast")
                .setMessage("We're calling people in " + cityName + " to look outside their windows and tell us what's the weather like over there.")
                .setView( new ProgressBar(MainActivity.this))
                .show();

        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {
            // This runs on another thread
            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName, "UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric&mode=xml";
                URL url = new URL(stringURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( in  , "UTF-8");

                while ( xpp.next() != XmlPullParser.END_DOCUMENT ) {
                    switch ( xpp.getEventType() ) {
                        case XmlPullParser.START_TAG:
                            if (xpp.getName().equals("temperature")) {
                                current = xpp.getAttributeValue(null, "value");
                                min = xpp.getAttributeValue(null, "min");
                                max = xpp.getAttributeValue(null, "max");
                            } else if (xpp.getName().equals("weather")) {
                                description = xpp.getAttributeValue(null, "value");
                                iconName = xpp.getAttributeValue(null, "icon");
                            } else if (xpp.getName().equals("humidity")) {
                                humidity = xpp.getAttributeValue(null, "value");
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                        case XmlPullParser.TEXT:
                            break;
                    }
                }

//                    String text = (new BufferedReader(
//                            new InputStreamReader(in, StandardCharsets.UTF_8)))
//                            .lines()
//                            .collect(Collectors.joining("\n"));
//                    JSONObject theDocument = new JSONObject( text );
//
//                    JSONArray weatherArray = theDocument.getJSONArray("weather");
//                    JSONObject position0 = weatherArray.getJSONObject(0);
//                    String description = position0.getString("description");
//                    String iconName = position0.getString("icon");
//                    JSONObject mainObject = theDocument.getJSONObject("main");
//                    double current = mainObject.getDouble("temp");
//                    double min = mainObject.getDouble("temp_min");
//                    double max = mainObject.getDouble("temp_max");
//                    int humidity = mainObject.getInt("humidity");

                File file = new File(getFilesDir(), iconName + ".png");
                if (file.exists()) {
                    image = BitmapFactory.decodeFile(getFilesDir() + "/" + iconName + ".png");
                } else  {
                    URL imgUrl = new URL( "http://openweathermap.org/img/w/" + iconName + ".png" ); // use HTTP instead of HTTPS
                    HttpURLConnection connection = (HttpURLConnection) imgUrl.openConnection(); // HTTP S Connection to HTTPConnection
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        image.compress(Bitmap.CompressFormat.PNG, 100, openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                    }
                }


                runOnUiThread( (  )  -> {
                    TextView tv = findViewById(R.id.temp);
                    tv.setText("The current temperature is " + current);
                    tv.setVisibility(View.VISIBLE);
                    tv = findViewById(R.id.minTemp);
                    tv.setText("The min temperature is " + min);
                    tv.setVisibility(View.VISIBLE);
                    tv = findViewById(R.id.maxTemp);
                    tv.setText("The max temperature is " + max);
                    tv.setVisibility(View.VISIBLE);
                    tv = findViewById(R.id.humidity);
                    tv.setText("The humidity is " + humidity + "%");
                    tv.setVisibility(View.VISIBLE);
                    tv = findViewById(R.id.description);
                    tv.setText(description);
                    tv.setVisibility(View.VISIBLE);
                    ImageView iv = findViewById(R.id.icon);
                    iv.setImageBitmap(image);
                    iv.setVisibility(View.VISIBLE);

                    dialog.hide();
                });

            } catch (IOException | XmlPullParserException ioe) {
                Log.e("Connection error:", ioe.getMessage());
                Log.e("Connection error:", String.valueOf(ioe.getCause()));
            }
        });
    }
}