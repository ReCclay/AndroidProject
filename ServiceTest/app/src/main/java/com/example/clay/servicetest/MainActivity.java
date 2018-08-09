package com.example.clay.servicetest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button startService;
    private Button stopService;

    private Button bindService;
    private Button unbindService;

    private MyService.MyBinder myBinder;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            myBinder = (MyService.MyBinder) service;//向下转型，实现在Activity中根据具体的场景来调用MyBinder中的任何public方法
            myBinder.startDownload();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService = (Button) findViewById(R.id.start_service);
        stopService = (Button) findViewById(R.id.stop_service);
        bindService = (Button) findViewById(R.id.bind_service);
        unbindService = (Button) findViewById(R.id.unbind_service);
        startService.setOnClickListener(startServiceListener);
        stopService.setOnClickListener(stopServiceListener);
        bindService.setOnClickListener(bindServiceListener);
        unbindService.setOnClickListener(unbindServiceListener);

        Log.e("MyService", "MainActivity thread id is " + Thread.currentThread().getId());//打印线程id

    }


    private View.OnClickListener startServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent startIntent = new Intent(MainActivity.this, MyService.class);
            startService(startIntent);//onCreate()和onStartCommand()方法都会执行。
        }
    };

    private View.OnClickListener stopServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "click stop");
            Intent stopIntent = new Intent(MainActivity.this, MyService.class);
            stopService(stopIntent);
        }
    };

    private View.OnClickListener bindServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent bindIntent = new Intent(MainActivity.this, MyService.class);
            bindService(bindIntent, connection, BIND_AUTO_CREATE);//onCreate()方法得到执行，但onStartCommand()方法不会执行。
        }
    };

    private View.OnClickListener unbindServiceListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.e(TAG, "click unbind" );
            unbindService(connection);
        }
    };

}
