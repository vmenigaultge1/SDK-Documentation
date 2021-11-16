package com.example.tutorial_buddy_test;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import com.bfr.buddy.usb.shared.IUsbCommadRsp;
import com.bfr.buddy.utils.events.EventItem;
import com.bfr.buddysdk.BuddyActivity;
import com.bfr.buddysdk.BuddySDK;
import com.example.testapplication.R;


public class MainActivity extends BuddyActivity {
    TextView mText1;//defining a text parameter so we show the text we want
    Button mButtonEnable ;//definning buttons Enable ( will be used to enable wheels motors )
    Button mButtonAdvance;//definning buttons Advance ( will be used to make buddy advance )
    Button mButtonMoveWheelStraight;//button to start moving straight non stop
    Button mButtonEnableRotateAuto;
    Button mButtonStop;
    Button mButtonRotation180;
    String TAG = "Message" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         Log.i(TAG, "wheels create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //link with user interface
        mText1 = findViewById(R.id.textView1);
        //linking the id of the buttons of Layout to buttons in the code
        mButtonRotation180 = findViewById(R.id.button_enable_Rotation180);//180° rotation
        mButtonEnable=findViewById(R.id.button_enable_wheels);//activate the wheels motor
        mButtonAdvance = findViewById(R.id.button_advance);//advance of 50 cm 0.5m/s
        mButtonMoveWheelStraight  = findViewById(R.id.button_MoveWheelStraight);//button to start tests
        mButtonStop = findViewById(R.id.button_Stop);// stop button
        mButtonEnableRotateAuto = findViewById(R.id.button_MoveRotate);//rotation non stop
        mText1.setText(" "); //setting no text                                                         //Log.i(TAG, "wheels 1");



        mButtonStop.setOnClickListener(view -> StopMotors());//function called to stop motors and moving the robbot
       // mButtonAdvance.setOnClickListener(v -> AdvanceFunct());//to advance with speed and distance defined

        mButtonRotation180.setOnClickListener(view -> rotate180());//to rotate at a given angle


        mButtonEnable.setOnClickListener(view -> EnableWheels());//function called to enable the wheels of the robbot
        mButtonEnableRotateAuto.setOnClickListener(v -> RotateNonStop());//rotate undefinitly

    }

    private void MoveWheelsStraight() {//ongoing work
        BuddySDK.USB.moveWheelStraight(0.5F, new IUsbCommadRsp.Stub() {//if speed > 0 goes forward if speed<0 goes backward
            @Override
            public void onSuccess(String s) throws RemoteException {//in case of success we want an answer
                Log.i(TAG, "Straight Working : "+s);// we show on the screen the success
                mText1.setText("Straight working");// we show on the screen the success
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.i(TAG, "movingStraight Failed : "+s);// we show the failure + the responsible for failure
                mText1.setText("Straight Failed");//// we show on the screen the failure
            }
        });
    }

    private void rotate180() {//function in charge of rotate buddy of a defined angle and speed
        //speed is in deg/seconds
        float iSpeed = 60F;//definition of the speed (>0 to go forward , <0 to go backward)
        float iDegree = 180F;//degree of rotation

        //function to rotate Buddy with a certain angle
        BuddySDK.USB.rotateBuddy(iSpeed, iDegree, new IUsbCommadRsp.Stub() {

            @Override
            public void onSuccess(String s) throws RemoteException {//in case of success we want an answer
                Log.i(TAG, "Rotaion 180 Working");
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.i(TAG, "Rotaion 180 notWorking : "+s);//will show the error in the logcat
                mText1.setText("Fail to rotate : 180");
            }
        });

    }

    private void StopMotors() {//created function to call the stop function of the motors
        //function to stop motors
        BuddySDK.USB.emergencyStopMotors(new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {//in case of success we want an answer
                mText1.setText("Motor Stopped");// we show on the screen the success
            }

            @Override
            public void onFailed(String s) throws RemoteException {//in case of failure we want to have the information
                // we show on the screen the
                mText1.setText("Fail to Stop Motors");
                Log.i(TAG, "StopMotors notWorking : "+s);
            }
        });
    }


    private void EnableWheels(){//created a function wich calls enable the wheels of the robbot

        int turnOnRightWeel = 1;//setting the right weel motor on On of "int" type (On=1) (Off=0)
        int turnOnLeftWeel = 1;//setting the Left weel motor on On of "int" type (On=1) (Off=0)

        BuddySDK.USB.enableWheels(turnOnLeftWeel, turnOnRightWeel, new IUsbCommadRsp.Stub() {   //function which enable the wheels

            @Override
            public void onSuccess(String s) throws RemoteException {
                //in Case of sucess of enabeling the wheels we decide to show some text at screen
                mText1.setText("wheels are enabled ");
                Log.i(TAG, "wheels are enabled");
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                //In case of failure we want to be inform of the reason of the failure
               Log.i(TAG, "Wheels enable failed :" + s);
            }
        });
    // Listener for button to make the robot go forward or backward
           mButtonAdvance.setOnClickListener(view -> AdvanceFunct());
    //in case of click on the button, call of the function AdvanceFunct()
        mButtonMoveWheelStraight.setOnClickListener(view -> MoveWheelsStraight());//to move straight non stop
        BuddySDK.USB.getMotorBoardStatus(new IUsbCommadRsp.Stub() {//motor status
            @Override
            public void onSuccess(String s) throws RemoteException {
                Log.i(TAG, "onSuccess: "+s);
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.i(TAG, "onFailed: "+s);
            }
        });
}

    private void RotateNonStop()  {//ongoing work
//rotate the robot
        BuddySDK.USB.WheelRotate(-0.5F, new IUsbCommadRsp.Stub() {//if speed < 0 turn clockwise if speed >0 turn counterclockwise

            @Override
            public void onSuccess(String s) throws RemoteException {//in success case show this :
                Log.i(TAG, "Rotate onSuccess: ");
                mText1.setText("rotate working");
            }

            @Override
            public void onFailed(String s) throws RemoteException {//in failure case show this :
               Log.i(TAG, "Rotate onFailed: " + s);
               mText1.setText("Rotate failure"); }
        });

    }


    //function called for clicking on the Advance button
    private void AdvanceFunct() {//function working
        float speed = 0.5F;//definition of the speed (>0 to go forward , <0 to go backward)
        float distance = 1F;//definition of the distance to pracour(ALWAYS>0)

        //call of the function to make buddy go forward or backwars
            BuddySDK.USB.moveBuddy(speed, distance, new IUsbCommadRsp.Stub() {
                @Override
                public void onSuccess(String s) throws RemoteException {
                    Log.i(TAG, "AdvanceFunct: sucess");//in case of success show in the logcat window 'sucess'

                }

                @Override
                public void onFailed(String s) throws RemoteException {//in case of failure to achieve this function
                    Log.i(TAG, "AdvanceFunct: fail : " + s);//show in the logcat window a message
                    mText1.setText("Fail to advance");//show on the screen of the robot 'Fail to advance'
                }
            });
    }
    @Override
    public void onSDKReady() {}
    @Override
    public void onEvent(EventItem eventItem) {}
}