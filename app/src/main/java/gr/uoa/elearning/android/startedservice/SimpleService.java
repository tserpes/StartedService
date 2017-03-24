package gr.uoa.elearning.android.startedservice;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tserpes on 24/03/17.
 */

public class SimpleService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(),"Service started!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),"Service stopped!",Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final long multiplier = intent.getLongExtra("multiplier",0);

        Thread t = new Thread(new Runnable() {  //we put the "heavy" task in a new thread to avoid blocking the UI thread
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                try {
                    Thread.sleep(multiplier*1000);  //this is how we simulate a "heavy" task. 1000 millisecs for each multiplier unit
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long elapsedTime = System.currentTimeMillis()-startTime;
                long aggregatedTime = readLongFromFile();
                writeLongToFile(aggregatedTime+elapsedTime);
            }
        });
        t.start();

        return Service.START_REDELIVER_INTENT;  //resurrect the service if it is killed with the same intent
    }

    //two convenience methods to read to and write from file
    private void writeLongToFile(long value){
        try {
            FileOutputStream fos = openFileOutput("delay.dat", Context.MODE_PRIVATE);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeLong(value);
            dos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not access file. Service stopped!", Toast.LENGTH_SHORT).show();
            stopSelf();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could not write to file. Service stopped!", Toast.LENGTH_SHORT).show();
            stopSelf();
        }
    }
    private long readLongFromFile(){
        long time = 0;
        try {
            FileInputStream fis = openFileInput("delay.dat");
            DataInputStream dis = new DataInputStream(fis);
            time = dis.readLong();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            //in case there's no value to read from file just push zero to it just to make sure there's something stored
            writeLongToFile(0);
        }
        return time;
    }
}
