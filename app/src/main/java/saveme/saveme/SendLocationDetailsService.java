package saveme.saveme;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import util.ServiceHandler;

public class SendLocationDetailsService extends Service {
    public SendLocationDetailsService() {
    }

    LocationManager locationManager;
    public static String sendLocationDetailsReturnedJSON;
    public static final String sendLocationDetailsURL = "";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate(){

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                new SendLocation().execute(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    public static class SendLocation extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {

            List<NameValuePair> paramsList = new ArrayList<>();

            paramsList.add(new BasicNameValuePair("latitude",params[0]));
            paramsList.add(new BasicNameValuePair("longitude",params[1]));

            ServiceHandler jsonParser = new ServiceHandler();
            sendLocationDetailsReturnedJSON = jsonParser.makeServiceCall(sendLocationDetailsURL,ServiceHandler.POST,paramsList);

            if(sendLocationDetailsReturnedJSON!=null){

                try{
                    Log.i("Send Location Details", "Successful");
                }catch (Exception e){
                    Log.i("Send Location Details","Failure");
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
