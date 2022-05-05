package com.example.geofenceapp;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiv";
    private double person = 1;
    private double time = 1;
    private double loc = 0.2;
    private double newLat, newLong;
    final private double obfuscationfactor = 0.0003;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if(geofencingEvent.hasError()){
            Log.d(TAG, "onReceive: Error receiving geofence");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for(Geofence geofence:geofenceList){
            Log.d(TAG, "onReceive TOSTRING: " + geofence.toString());
            Log.d(TAG, "onReceive: REQUESTID" + geofence.getRequestId());
        }

        Location location = geofencingEvent.getTriggeringLocation();
        int transitionType = geofencingEvent.getGeofenceTransition();

        double finalprivacyvalue = person * time * loc;
        double rand = finalprivacyvalue * obfuscationfactor;

        if(Math.random() * 1 < 0.5)
            newLat = location.getLatitude() + Math.random() * (rand - 0) + 0;
        else
            newLat = location.getLatitude() - Math.random() * (rand - 0) + 0;

        if(Math.random() * 1 < 0.5)
            newLong = location.getLongitude() + Math.random() * (rand - 0) + 0;
        else
            newLong = location.getLongitude() - Math.random() * (rand - 0) + 0;

        System.out.println("Old Coordinates: " + location.getLatitude() + "" + location.getLongitude());
        System.out.println("New Coordinates: " + newLat + "" + newLong);

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                break;

            case Geofence.GEOFENCE_TRANSITION_DWELL:
                notificationHelper.sendHighPriorityNotification("Privacy Coordinate", newLat + ", " + newLong, MapsActivity.class);
                break;

            case Geofence.GEOFENCE_TRANSITION_EXIT:
                break;
        }
    }
}