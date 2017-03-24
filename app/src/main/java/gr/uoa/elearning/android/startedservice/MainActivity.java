package gr.uoa.elearning.android.startedservice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText multiplierEditText = (EditText)findViewById(R.id.multiplier);
        final TextView totalDelayTextView = (TextView)findViewById(R.id.agreggatedView);
        Button injectDelayButton = (Button)findViewById(R.id.injectDelay);
        Button getTimeButton = (Button)findViewById(R.id.getTotalTime);
        Button stopServiceButton = (Button)findViewById(R.id.stopService);

        injectDelayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String multiplierText = multiplierEditText.getText().toString();
                long multiplier = Long.parseLong(multiplierText);
                intent = new Intent(getApplicationContext(),SimpleService.class);
                intent.putExtra("multiplier",multiplier);
                startService(intent);
            }
        });

        getTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileInputStream fis = openFileInput("delay.dat");
                    DataInputStream dis = new DataInputStream(fis);
                    long time = dis.readLong();
                    totalDelayTextView.setText(time+" milliseconds");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        stopServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intent != null){
                    stopService(intent);
                }
            }
        });
    }
}
