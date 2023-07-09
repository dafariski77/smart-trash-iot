package net.smarttrash;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ekn.gruzer.gaugelibrary.ArcGauge;
import com.ekn.gruzer.gaugelibrary.Range;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    DatabaseReference mydb;
    ArcGauge CapacityGraphic;
    Range Range_1, Range_2, Range_3;
    int Capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView status = findViewById(R.id.status);
        CapacityGraphic = findViewById(R.id.kapasitas);

        Range_1 = new Range();
        Range_2 = new Range();
        Range_3 = new Range();

        Range_1.setFrom(0);
        Range_1.setTo(10);

        Range_2.setFrom(10);
        Range_2.setTo(20);

        Range_3.setFrom(20);
        Range_3.setTo(30);

        Range_1.setColor(Color.GREEN);
        Range_2.setColor(Color.YELLOW);
        Range_3.setColor(Color.RED);

        CapacityGraphic.setMinValue(0);
        CapacityGraphic.setMaxValue(30);
        CapacityGraphic.setValue(0);

        CapacityGraphic.addRange(Range_1);
        CapacityGraphic.addRange(Range_2);
        CapacityGraphic.addRange(Range_3);

        mydb = FirebaseDatabase.getInstance().getReference();
        mydb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dataStatus;
                String stringStatus;

                dataStatus = dataSnapshot.child("status").getValue().toString();
                CapacityGraphic.setValue(Integer.parseInt(dataStatus));

                if(Integer.parseInt(dataStatus) <= 10) {
                    stringStatus = "Kosong";
                } else if (Integer.parseInt(dataStatus) <= 20) {
                    stringStatus = "Hampir Penuh";
                } else if (Integer.parseInt(dataStatus) <= 30) {
                    stringStatus = "Penuh";
                } else {
                    stringStatus = "Invalid";
                }

                status.setText(stringStatus);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}