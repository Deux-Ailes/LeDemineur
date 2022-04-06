package com.example.jeufun;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class ServiceDeMusique extends Service {
    private MediaPlayer maMusique;

    public ServiceDeMusique() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        maMusique = MediaPlayer.create(this, R.raw.music);  // Setup de la musique, attribution d'un MediaPlayer à la variable locale
        maMusique.setLooping(true);                                // On fait tourner la musique en boucle
        maMusique.start();                                          // On lance la musique
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        maMusique.release(); // Libération du médiaPlayer pour éviter que la musique ne tourne même quand l'activité est fermée
    }
}