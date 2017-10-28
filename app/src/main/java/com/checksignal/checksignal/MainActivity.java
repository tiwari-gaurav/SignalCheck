package com.checksignal.checksignal;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button _getGsmSignalStrengthButton;
    TelephonyManager _telephonyManager;
    ImageView _gmsStrengthImageView;
    TextView _gmsStrengthTextView, _operatorName, _networkSpeed;
    MyPhoneStateListener MyListener;


    public static final int NETWORK_TYPE_EHRPD = 14; // Level 11
    public static final int NETWORK_TYPE_EVDO_B = 12; // Level 9
    public static final int NETWORK_TYPE_HSPAP = 15; // Level 13
    public static final int NETWORK_TYPE_IDEN = 11; // Level 8
    public static final int NETWORK_TYPE_LTE = 13; // Level 11

    private Handler mHandler = new Handler();
    private long mStartRX = 0;
    private long mStartTX = 0;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    int signalStrength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();

        // _networkSpeed.setText( signalStrength);


        // Get a reference to the TelephonyManager and instantiate the GsmSignalStrengthListener.
        // These will be used by the Button's OnClick event handler.

        /*MyListener = new MyPhoneStateListener();
        _telephonyManager.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);*/

        // Toast.makeText(getApplicationContext(),checkConnectivity(),Toast.LENGTH_LONG);

       // Log.d("connectivity", checkConnectivity());




    }

    private void initializeViews() {


        _gmsStrengthTextView = (TextView) findViewById(R.id.operator_name);
        _operatorName = (TextView) findViewById(R.id.op_name);
        _networkSpeed = (TextView) findViewById(R.id.network_speed);
        // _gmsStrengthImageView = (ImageView) findViewById(R.id.imageView1);
        _getGsmSignalStrengthButton = (Button) findViewById(R.id.myButton);
    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {
            TextView RX = (TextView) findViewById(R.id.RX);
            TextView TX = (TextView) findViewById(R.id.TX);
            long rxBytes = TrafficStats.getTotalRxBytes() - mStartRX;
            double receiveKb = rxBytes / 1024;
            RX.setText("receive Bytes:" + Double.toString(receiveKb));
            long txBytes = TrafficStats.getTotalTxBytes() - mStartTX;
            double transmitKb = txBytes / 1024;
            TX.setText("Transmit Bytes: " + Double.toString(transmitKb));


            mHandler.postDelayed(mRunnable, 1000);

        }
    };


    private void requestPermission() {
        String permission = android.Manifest.permission.ACCESS_COARSE_LOCATION;
        if (ActivityCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {permission}, 1);

        }
    }

    private String checkConnectivity() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        String networkType = "";


        if (isConnected) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                Toast.makeText(getApplicationContext(), "your mobile network is connected", Toast.LENGTH_LONG).show();
                switch (activeNetwork.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        networkType = "NETWORK TYPE 1xRTT"; // ~ 50-100 kbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        networkType = "NETWORK TYPE CDMA (3G) Speed: 2 Mbps";// ~ 14-64 kbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:

                        networkType = "NETWORK TYPE EDGE (2.75G) Speed: 100-120 Kbps"; // ~
                        // 50-100
                        // kbps

                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        networkType = "NETWORK TYPE EVDO_0"; // ~ 400-1000 kbps

                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        networkType = "NETWORK TYPE EVDO_A"; // ~ 600-1400 kbps

                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        networkType = "NETWORK TYPE GPRS (2.5G) Speed: 40-50 Kbps"; // ~ 100
                        // kbps

                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        networkType = "NETWORK TYPE HSDPA (4G) Speed: 2-14 Mbps"; // ~ 2-14
                        // Mbps

                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        networkType = "NETWORK TYPE HSPA (4G) Speed: 0.7-1.7 Mbps"; // ~
                        // 700-1700
                        // kbps

                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        networkType = "NETWORK TYPE HSUPA (3G) Speed: 1-23 Mbps"; // ~ 1-23
                        // Mbps

                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        networkType = "NETWORK TYPE UMTS (3G) Speed: 0.4-7 Mbps"; // ~ 400-7000
                        // kbps
                        // NOT AVAILABLE YET IN API LEVEL 7

                        break;
                    case MainActivity.NETWORK_TYPE_EHRPD:
                        networkType = "NETWORK TYPE EHRPD"; // ~ 1-2 Mbps

                        break;
                    case MainActivity.NETWORK_TYPE_EVDO_B:
                        networkType = "NETWORK_TYPE_EVDO_B"; // ~ 5 Mbps

                        break;
                    case MainActivity.NETWORK_TYPE_HSPAP:
                        networkType = "NETWORK TYPE HSPA+ (4G) Speed: 10-20 Mbps"; // ~ 10-20
                        // Mbps

                        break;
                    case MainActivity.NETWORK_TYPE_IDEN:
                        networkType = "NETWORK TYPE IDEN"; // ~25 kbps

                        break;
                    case MainActivity.NETWORK_TYPE_LTE:
                        networkType = "NETWORK TYPE LTE (4G) Speed: 10+ Mbps"; // ~ 10+ Mbps

                        break;
                    // Unknown
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        networkType = "NETWORK TYPE UNKNOWN";

                        break;
                    default:
                        networkType = "";
                        break;
                }
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                networkType = "You are connected to WIFI";

            }
        } else {
            networkType = "You are not connected to any network";
        }

        return networkType;
    }

    /*@Override
    protected void onPause() {
        super.onPause();

        _telephonyManager.listen(MyListener, PhoneStateListener.LISTEN_NONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _telephonyManager.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
    }*/

    public class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);

            /*Toast.makeText(getApplicationContext(), "Go to Firstdroid!!! GSM Cinr = "
                    + String.valueOf(signalStrength.getGsmSignalStrength()), Toast.LENGTH_SHORT).show();*/

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            Toast.makeText(this, "Location Permission granted", Toast.LENGTH_SHORT).show();
            initializeViews();
            _telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            _operatorName.setText(_telephonyManager.getNetworkOperatorName());
            checkSignalStrength();
            mStartRX = TrafficStats.getTotalRxBytes();
            mStartTX = TrafficStats.getTotalTxBytes();

            if (mStartRX == TrafficStats.UNSUPPORTED || mStartTX == TrafficStats.UNSUPPORTED) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Uh Oh!");
                alert.setMessage("Your device does not support traffic stat monitoring.");
                alert.show();
            } else {
                mHandler.postDelayed(mRunnable, 1000);
            }
        } else {
            Toast.makeText(this, "Location Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkSignalStrength() {


        //GET SIGNAL STRENGTH OF MOBILE NETWORK

        for (final CellInfo info : _telephonyManager.getAllCellInfo()) {
            if (info instanceof CellInfoGsm) {
                final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                signalStrength = gsm.getDbm();
                // do what you need
            } else if (info instanceof CellInfoCdma) {
                final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                signalStrength = cdma.getDbm();
                // do what you need
            } else if (info instanceof CellInfoLte) {
                final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                signalStrength = lte.getDbm();
                // do what you need
            } else {
                Toast.makeText(this, "Unknown type of cell signal", Toast.LENGTH_SHORT).show();
            }
        }
        Toast.makeText(getApplicationContext(), String.valueOf(signalStrength), Toast.LENGTH_LONG).show();
        _gmsStrengthTextView.setText("Signal Strength: "+String.valueOf(signalStrength));

    }
}


