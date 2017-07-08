package ye.wifipower;



import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.net.wifi.ScanResult;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    private WifiInfo wifiInfo = null;
    private WifiManager wifiManager = null;
    private Handler handler;
    private TextView tv1;
    private String otherwifi;
    private List<ScanResult> results;
    private int j;


    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        j=0;
        //获得wifimanager
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        //使用定时器，每隔2S获得一次信号强度值
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                wifiInfo = wifiManager.getConnectionInfo();

                if (wifiInfo.getBSSID() != null) {
                    //wifi名称
                    String ssid = wifiInfo.getSSID();
                    //wifi信号强度
                    int signalLevel = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), 6);
                    //wifi速度
                    int speed = wifiInfo.getLinkSpeed();
                    //wifi速度单位
                    String units = WifiInfo.LINK_SPEED_UNITS;

                    tv1=(TextView)findViewById(R.id.mText1);


                    results = wifiManager.getScanResults();
                    otherwifi = "\n\n";
                   // sharedPre = getSharedPreferences("test",Activity.MODE_PRIVATE);
                    //SharedPreferences.Editor editor = sharedPre.edit();

                    for (int i = 0; i < results.size(); i++) {

                        otherwifi += results.get(i).SSID  + "  ("+(double)
                                results.get(i).frequency/1000+"GHz）:   " + results.get(i).level +"dBm"+ "\n";


                    }


                    String text =  "We are connecting to " + ssid + " at " + String.valueOf(speed) +
                            " " + String.valueOf(units) + "  Strength : " + signalLevel ;
                    otherwifi += "\n  "+j+"\n  ";
                    otherwifi += text;

                    Message msg = new Message();
                    handler.sendMessage(msg);

                }


            }

        }, 100, 1000);

        //使用Handle实现UI线程与Timer线程之间的信息传递，每1秒告诉UI线程获得WIFIinfo
        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                j++;
                tv1.setText(otherwifi);

            }


        };

    }

}