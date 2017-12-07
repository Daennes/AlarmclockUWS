package b00239148.alarmclock;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class WeatherActivity extends AppCompatActivity {
    public ConstraintLayout constraintLayout;
    public static int image;

    class WeatherInfo extends AsyncTask <String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            try{

                URL url =new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream is = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(is);

                int data =reader.read();
                String apiDetails = "";
                char current;

                while(data !=-1){

                    current = (char) data;
                    apiDetails += current;
                    data = reader.read();

                }
                return apiDetails;

            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Log.d("TestTag", "onCreate: weatherActivit");
        constraintLayout=(ConstraintLayout) findViewById(R.id.content_weather_id);

        WeatherInfo weatherInfo = new WeatherInfo();

        TextView tempViewC = (TextView) findViewById(R.id.TempViewC);
        //TextView tempViewF = (TextView) findViewById(R.id.TempViewF);
        TextView condView = (TextView) findViewById(R.id.TextViewCo);
        ImageView i = (ImageView)findViewById(R.id.image);
        TextView windView = (TextView) findViewById(R.id.wind);
        TextView prsView = (TextView) findViewById(R.id.pressure);
        TextView humidView = (TextView) findViewById(R.id.humidity);
        TextView feelsView = (TextView) findViewById(R.id.feels);
        TextView visView = (TextView) findViewById(R.id.visibility);
        TextView nameView = (TextView) findViewById(R.id.name);
        TextView regionView = (TextView) findViewById(R.id.region);
        TextView countryView = (TextView) findViewById(R.id.country);
        TextView latView = (TextView) findViewById(R.id.latitude);
        TextView lonView = (TextView) findViewById(R.id.longitude);


        try {
            //tempViewC.setText("");
            //tempViewF.setText("");

            String weatherApiDetails = weatherInfo.execute("http://api.apixu.com/v1/current.json?q=Glasgow&key=6c57ce30e8d54375ae1102019172610").get();
            Log.i("Weather Api Info", weatherApiDetails);

            JSONObject jsonObject = new JSONObject(weatherApiDetails);

            JSONObject location = jsonObject.getJSONObject("location");
            String name = location.getString("name");
            String region = location.getString("region");
            String country = location.getString("country");
            String lat = location.getString("lat");
            String lon = location.getString("lon");

            nameView.setText(name);
            regionView.setText(region);
            countryView.setText(country);
            latView.setText("Lat " + lat + " )");
            lonView.setText("(Lon " + lon);

            JSONObject current = jsonObject.getJSONObject("current");
            String temp_c = current.getString("temp_c");
            String temp_f = current.getString("temp_f");
            String wind_mph = current.getString("wind_mph");
            String pressure_mb = current.getString("pressure_mb");
            String humidity = current.getString("humidity");
            String feelslike_c = current.getString("feelslike_c");
            String vis_km = current.getString("vis_km");

            tempViewC.setText("Temp " + temp_c + " C (" + temp_f + " F)");
            //tempViewF.setText("Temp " + temp_f + " F");
            windView.setText("Wind mph  " + wind_mph);
            prsView.setText("Wind Pressure  " + pressure_mb);
            humidView.setText("Humidity  " + humidity);
            feelsView.setText("Feels like " + feelslike_c + "C ");
            visView.setText("Visibility " + vis_km + " Km");

            String icon = current.getJSONObject("condition").getString("icon");
            Picasso.with(getBaseContext())
                    .load("http:" + icon)
                    .into(i);



            String condition = current.getJSONObject("condition").getString("text");
            condView.setText("(" + condition + ")");

            String code = current.getJSONObject("condition").getString("code");
            if(code.equals("1000"))
                constraintLayout.setBackgroundResource(R.drawable.sunny);

            else if(code.equals("1063") || code.equals("1066") || code.equals("1069")||
                    code.equals("1072") || code.equals("1087")|| code.equals("1117") ||
                    code.equals("1050")|| code.equals("1053") || code.equals("1068")||
                    code.equals("1171") || code.equals("1080")|| code.equals("1183") ||
                    code.equals("1186")|| code.equals("1189") || code.equals("1192")||
                    code.equals("1195") || code.equals("1198")|| code.equals("1201") ||
                    code.equals("1240")|| code.equals("1243") || code.equals("1246"))

                constraintLayout.setBackgroundResource(R.drawable.rain);

            if(code.equals("1003")|| code.equals("1006") || code.equals("1009")||
                    code.equals("1030")|| code.equals("1135") || code.equals("1147"))

                constraintLayout.setBackgroundResource(R.drawable.cloudy);

            else if(code.equals("1114") || code.equals("1204") || code.equals("1207")||
                    code.equals("1210") || code.equals("1213")|| code.equals("1216") ||
                    code.equals("1219")|| code.equals("1222") || code.equals("1225")||
                    code.equals("1237") || code.equals("1249")|| code.equals("1252") ||
                    code.equals("1255")|| code.equals("1258") || code.equals("1261")||
                    code.equals("1264") || code.equals("1279")|| code.equals("1282"))

                constraintLayout.setBackgroundResource(R.drawable.snow);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
