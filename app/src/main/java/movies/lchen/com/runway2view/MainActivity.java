package movies.lchen.com.runway2view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RoadView2 roadView2 = findViewById(R.id.road_view);
        SeekBar seekBar;
        seekBar = (SeekBar) findViewById(R.id.rotationx);
        seekBar.setMax(360);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                roadView2.setRotationX((float)progress);
            }
        });

        roadView2.start();

//        roadView2.setRotationX(40);

//        RunwayView2 runwayView2 = findViewById(R.id.run_way);
//        runwayView2.prepare();
    }
}
