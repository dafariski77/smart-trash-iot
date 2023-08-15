package net.smarttrash;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseReference mydb;
    ArcGauge CapacityGraphic;
    Range Range_1, Range_2, Range_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView status = findViewById(R.id.status);
        CapacityGraphic = findViewById(R.id.kapasitas);
        Button btnHidup = findViewById(R.id.btnON);
        btnHidup.setOnClickListener(this);
        Button btnMati = findViewById(R.id.btnOFF);
        btnMati.setOnClickListener(this);

        Range_1 = new Range();
        Range_2 = new Range();
        Range_3 = new Range();

        Range_1.setFrom(0);
        Range_1.setTo(45);

        Range_2.setFrom(45);
        Range_2.setTo(78);

        Range_3.setFrom(78);
        Range_3.setTo(100);

        Range_1.setColor(Color.GREEN);
        Range_2.setColor(Color.YELLOW);
        Range_3.setColor(Color.RED);

        CapacityGraphic.setMinValue(0);
        CapacityGraphic.setMaxValue(100);
        CapacityGraphic.setValue(0);

        CapacityGraphic.addRange(Range_1);
        CapacityGraphic.addRange(Range_2);
        CapacityGraphic.addRange(Range_3);

        mydb = FirebaseDatabase.getInstance().getReference();
        mydb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dataStatus;
                String statusText;
                int data;

                dataStatus = dataSnapshot.child("sensor/jarak").getValue().toString();
                data = 100 - (Integer.parseInt(dataStatus) * 100 / 15);

                if (data < 0) {
                    data = 0;
                }

                CapacityGraphic.setValue(data);

                if(data <= 45 ) {
                    statusText = "Kosong";
                } else if (data <= 78) {
                    statusText = "Hampir Penuh";
                } else {
                    statusText = "Penuh";
                }

                status.setText(statusText);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnON) {
            mydb.child("servo").setValue(1);
            Toast.makeText(MainActivity.this, "Tempat sampah terbuka!", Toast.LENGTH_SHORT).show();
        } else if(v.getId() == R.id.btnOFF) {
            mydb.child("servo").setValue(0);
            Toast.makeText(MainActivity.this, "Tempat sampah tertutup!", Toast.LENGTH_SHORT).show();
        }
    }
}