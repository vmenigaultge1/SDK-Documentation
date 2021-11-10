package com.example.tutorial_buddy_test;


import static android.service.controls.ControlsProviderService.TAG;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import com.bfr.buddy.utils.events.EventItem;
import com.bfr.buddy.usb.shared.IUsbCommadRsp;
import com.bfr.buddysdk.BuddyActivity;
import com.bfr.buddysdk.BuddySDK;
import com.example.testapplication.R;
import com.bfr.buddy.ui.shared.FacialEvent;
import com.bfr.buddy.network.shared.InternetState;
import com.bfr.buddy.network.shared.NetworkStatus;
import com.bfr.buddy.utils.events.EventState;
import com.bfr.buddy.utils.events.EventType;
import androidx.appcompat.app.AppCompatActivity;
import android.media.audiofx.AudioEffect;
import android.os.IBinder;



public class MainActivity extends BuddyActivity {
    TextView mText1;//defining a text parameter so we show the text we want
    Button mButtonEnable ;//definning buttons Enable ( will be used to enable wheels motors )
    Button mButtonAdvance;//definning buttons Advance ( will be used to make buddy advance )

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Log.i(TAG, "wheels create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //link with user interface
        mText1 = findViewById(R.id.textView1);
        //linking the id of the buttons of Layout to buttons in the code
        mButtonEnable=findViewById(R.id.button_enable_wheels);
        mButtonAdvance = findViewById(R.id.button_advance);
        mText1.setText(" "); //setting no text                                                                                                                     //Log.i(TAG, "wheels 1");


        // Listener for button to enable the wheels
       mButtonEnable.setOnClickListener(view -> {                                                                                                                 // Log.i(TAG, "wheels 2");
           int turnOnRightWeel = 1;//setting the right weel motor on On of "int" type (On=1) (Off=0)
           int turnOnLeftWeel = 1;//setting the Left weel motor on On of "int" type (On=1) (Off=0)

           BuddySDK.USB.enableWheels(turnOnLeftWeel, turnOnRightWeel, new IUsbCommadRsp.Stub() {

               @Override
               public void onSuccess(String s) throws RemoteException {
                //in Case of sucess of enabeling the wheels we decide to show some text at screen
                   mText1.setText("wheels are on ");
                   Log.i(TAG, "wheels are on");
               }

               @Override
               public void onFailed(String s) throws RemoteException {
                   //In case of failure we want to be inform of the reason of the failure
                   Log.i(TAG, "Wheels enable failed because :" + s);
               }
           });
       });

           // Listener for button to make the robot go forward or backward
           mButtonAdvance.setOnClickListener(view -> AdvanceFunct());
           //in case of click on the button, call of the function AdvanceFunct()

    }
    //function called for clicking on the Advance button
    private void AdvanceFunct() {
        //Here we decide to go forward of 0.5meters with a 0.5m/s speed
        float speed = 0.5F;//definition of the speed (>0 to go forward , <0 to go backward)
        float distance = 0.5F;//definition of the distance to pracour(ALWAYS>0)

        //call of the function to make buddy go forward or backwars
            BuddySDK.USB.moveBuddy(speed, distance, new IUsbCommadRsp.Stub() {
                @Override
                public void onSuccess(String s) throws RemoteException {
                    Log.i(TAG, "AdvanceFunct: sucess");//in case of success show in the logcat window 'sucess'

                }

                @Override
                public void onFailed(String s) throws RemoteException {//in case of failure to achieve this function
                    Log.i(TAG, "AdvanceFunct: fail");//show in the logcat window a message
                    mText1.setText("Fail to advance");//show on the screen of the robot 'Fail to advance'
                }
            });
    }
    @Override
    public void onSDKReady() {}
    @Override
    public void onEvent(EventItem eventItem) {}
}


