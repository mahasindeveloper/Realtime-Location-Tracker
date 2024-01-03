package com.mahasin.inforootdelivery;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.*;

import org.greenrobot.eventbus.EventBus;

public class LocationService extends Service {

    private static final String CHANNEL_ID = "12345";
    private static final int NOTIFICATION_ID = 12345;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private NotificationManager notificationManager;
    private Location location;

    @Override
    public void onCreate() {
        super.onCreate();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onNewLocation(locationResult);
            }
        };

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, "locations", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @SuppressWarnings("MissingPermission")
    public void createLocationRequest() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback, null
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
        stopForeground(true);
        stopSelf();
    }

    private void onNewLocation(LocationResult locationResult) {
        location = locationResult.getLastLocation();
        EventBus.getDefault().post(new LocationEvent(
                location != null ? String.valueOf(location.getLatitude()) : null,
                location != null ? String.valueOf(location.getLongitude()) : null
        ));
        startForeground(NOTIFICATION_ID, getNotification());
    }

    public Notification getNotification() {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Updates")
                .setContentText("Latitude--> " + (location != null ? location.getLatitude() : "N/A")
                        + "\nLongitude --> " + (location != null ? location.getLongitude() : "N/A"))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification.setChannelId(CHANNEL_ID);
        }
        return notification.build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        createLocationRequest();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeLocationUpdates();
    }

}
