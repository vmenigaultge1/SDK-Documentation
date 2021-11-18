package com.example.tutorial_buddy_test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bfr.buddy.usb.shared.IUsbCommadRsp;
import com.bfr.buddy.utils.events.EventItem;
import com.bfr.buddysdk.BuddyActivity;
import com.bfr.buddysdk.BuddySDK;
import com.example.testapplication.R;


public class MainActivity extends BuddyActivity implements View.OnClickListener {

    //Button for each move
    Button moveForward;
    Button moveNonStopForward;
    Button turn;
    Button nonStopTurn;
    Button stopMotors;

    //Edit Text for the input of speed, distance and angle
    EditText speedMoveForward;
    EditText distanceMoveForward;
    EditText speedMoveNonStopForward;
    EditText speedTurn;
    EditText angleTurn;
    EditText speedNonStopTurn;

    //TextView for the wheels status
    TextView leftWheelStatus;
    TextView rightWheelStatus;
    TextView test;

    //Switch enable wheels
    Switch enableLeftWheel;
    Switch enableRightWheel;

    String TAG = "Move Tuto" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmain);

        //Button linking
        moveForward = (Button) findViewById(R.id.BstartMvF);
        moveNonStopForward = (Button) findViewById(R.id.BstartNonSMv);
        turn = (Button) findViewById(R.id.BstartTurn);
        nonStopTurn = (Button) findViewById(R.id.BstartNonSTurn);
        stopMotors = (Button) findViewById(R.id.BstopMotors);

        moveForward.setOnClickListener(this);
        moveNonStopForward.setOnClickListener(this);
        turn.setOnClickListener(this);
        nonStopTurn.setOnClickListener(this);
        stopMotors.setOnClickListener(this);

        //Edit Text linking
        speedMoveForward = (EditText) findViewById(R.id.EDchooseSpeedMvF);
        distanceMoveForward = (EditText) findViewById(R.id.EDchooseDistMvF);
        speedMoveNonStopForward = (EditText) findViewById(R.id.EDchooseSpeedNonSMv);
        speedTurn = (EditText) findViewById(R.id.EDchooseSpeedTurn);
        angleTurn = (EditText) findViewById(R.id.EDchooseAngleTurn);
        speedNonStopTurn = (EditText) findViewById(R.id.EDchooseSpeedNonSTurn);

        //Switch linking
        enableLeftWheel = (Switch) findViewById(R.id.SWenableLW);
        enableRightWheel = (Switch) findViewById(R.id.SWenableRW);

        //enableLeftWheel.setOnClickListener(this);
        //enableRightWheel.setOnClickListener(this);
        Log.i(TAG,"onCreate finished");

        test = findViewById(R.id.header);
    }

    //Move straight forward non stop
    //Speed : m/s (> 0 goes forward if speed<0 goes backward)
    private void MoveWheelsStraight(float speed) {//ongoing work
        BuddySDK.USB.moveWheelStraight(speed, new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {//in case of success we want an answer
                Log.i(TAG, "Straight Working : "+s);// we show on the screen the success
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"Success", Toast.LENGTH_SHORT).show();// we show on the screen the success
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.i(TAG, "movingStraight Failed : "+s);// we show the failure + the responsible for failure
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"Failed", Toast.LENGTH_SHORT).show();//// we show on the screen the failure
                            }
                        });
                    }
                }).start();
            }
        });
    }

    //function in charge of rotate buddy of a defined angle and speed
    //Speed : in deg/s (>0 to go forward , <0 to go backward)
    //Degree : degree of rotation
    private void Rotate(float speed, float degree) {

        //function to rotate Buddy with a certain angle
        BuddySDK.USB.rotateBuddy(speed, degree, new IUsbCommadRsp.Stub() {

            @Override
            public void onSuccess(String s) throws RemoteException {//in case of success we want an answer
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"Rotation finished", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                Log.i(TAG, "Rotaion 180 notWorking : "+s);//will show the error in the logcat
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });

    }

    private void StopMotors() {//created function to call the stop function of the motors
        //function to stop motors
        BuddySDK.USB.emergencyStopMotors(new IUsbCommadRsp.Stub() {
            @Override
            public void onSuccess(String s) throws RemoteException {//in case of success we want an answer
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"Motors stopped", Toast.LENGTH_SHORT).show();// we show on the screen the success
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onFailed(String s) throws RemoteException {//in case of failure we want to have the information
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
                Log.i(TAG, "StopMotors notWorking : "+s);
            }
        });
    }

    //Enable the wheels of the robot
    //turnOnLeftWheel : setting the Left wheel motor on On of "int" type (On=1) (Off=0)
    //turnOnRightWheel : setting the right wheel motor on On of "int" type (On=1) (Off=0)
    private void EnableWheels(int turnOnLeftWheel, int turnOnRightWheel){
        Log.i(TAG,"left : " + turnOnLeftWheel + " right : " + turnOnRightWheel);
        BuddySDK.USB.enableWheels(turnOnLeftWheel, turnOnRightWheel, new IUsbCommadRsp.Stub() {   //function which enable the wheels

            @Override
            public void onSuccess(String s) throws RemoteException {
                //in Case of sucess of enabeling the wheels we decide to show some text at screen
                Log.i(TAG, "wheels are enabled");
            }

            @Override
            public void onFailed(String s) throws RemoteException {
                //In case of failure we want to be inform of the reason of the failure
               Log.i(TAG, "Wheels enable failed :" + s);
            }
        });
    }
    //Rotate the robot
    //Speed : deg/s (< 0 turn clockwise if speed >0 turn counterclockwise)
    private void RotateNonStop(float speed)  {//ongoing work

        BuddySDK.USB.WheelRotate(speed, new IUsbCommadRsp.Stub() {

            @Override
            public void onSuccess(String s) throws RemoteException {//in success case show this :
                Log.i(TAG, "Rotate onSuccess: ");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"Success", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onFailed(String s) throws RemoteException {//in failure case show this :
               Log.i(TAG, "Rotate onFailed: " + s);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this,"Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
            }
        });

    }


    //Move forward
    //Speed : (>0 to go forward , <0 to go backward)
    //Distance : Travel distance (ALWAYS>0)
    private void Move(float speed, float distance) {

        //call of the function to make buddy go forward or backwards
            BuddySDK.USB.moveBuddy(speed, distance, new IUsbCommadRsp.Stub() {
                @Override
                public void onSuccess(String s) throws RemoteException {
                    Log.i(TAG, "Move: success");//in case of success show in the logcat window 'success'
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"Destination reached", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).start();
                }

                @Override
                public void onFailed(String s) throws RemoteException {//in case of failure to achieve this function
                    Log.i(TAG, "Move: fail : " + s);//show in the logcat window a message
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"Failed", Toast.LENGTH_SHORT).show();//show on the screen of the robot 'Fail to advance'
                                }
                            });
                        }
                    }).start();
                }
            });
    }

    @Override
    protected void onStop() {
        super.onStop();
        EnableWheels(0,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EnableWheels(0,0);
    }


    @Override
    public void onSDKReady() {

        //Text view linking
        //To put on onSDKReady because sometimes onSDKReady finish before onCreate
        //leftWheelStatus = (TextView) findViewById(R.id.TVenableStatusLW);
        //rightWheelStatus = (TextView) findViewById(R.id.TVenableStatusRW);
        //test = findViewById(R.id.header);
        test.setText("Pat");



        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    test.setText(BuddySDK.Actuators.getLeftWheelStatus());
                    //leftWheelStatus.setText(BuddySDK.Actuators.getLeftWheelStatus());
                    //rightWheelStatus.setText(BuddySDK.Actuators.getRightWheelStatus());
                }

            }
        }).start();

        Log.i(TAG,"onSDKReady finished");
    }

    @Override
    public void onEvent(EventItem eventItem) { }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.BstartMvF:
                    //Check input and Move Forward
                    if (!speedMoveForward.getText().toString().equals("") && !distanceMoveForward.getText().toString().equals("") &&
                            isInputValid(speedMoveForward.getText().toString()) && isInputValid(distanceMoveForward.getText().toString())) {
                        Move(Float.parseFloat(speedMoveForward.getText().toString()), Float.parseFloat(distanceMoveForward.getText().toString()));
                    } else {
                        Log.i(TAG, "Speed or distance input error");
                        Toast.makeText(MainActivity.this, "Speed or distance input error", Toast.LENGTH_SHORT).show();
                    }
                break;

            case R.id.BstartNonSMv:
                    //Check input and Move Non Stop
                    if (!speedMoveNonStopForward.getText().toString().equals("") && isInputValid(speedMoveNonStopForward.getText().toString())) {
                        MoveWheelsStraight(Float.parseFloat(speedMoveNonStopForward.getText().toString()));
                    } else {
                        Log.i(TAG, "Speed input error");
                        Toast.makeText(MainActivity.this, "Speed input error", Toast.LENGTH_SHORT).show();
                    }
                break;

            case R.id.BstartTurn:
                    //Check input and Turn
                    if (!speedTurn.getText().toString().equals("") && !angleTurn.getText().toString().equals("") &&
                            isInputValid(speedTurn.getText().toString()) && isInputValid(angleTurn.getText().toString())) {
                        Rotate(Float.parseFloat(speedTurn.getText().toString()), Float.parseFloat(angleTurn.getText().toString()));
                    } else {
                        Log.i(TAG, "Speed or angle input error");
                        Toast.makeText(MainActivity.this, "Speed or angle input error", Toast.LENGTH_SHORT).show();
                    }
                break;

            case R.id.BstartNonSTurn:
                    //Check input and Turn Non Stop
                    if (!speedNonStopTurn.getText().toString().equals("") && isInputValid(speedNonStopTurn.getText().toString())) {
                        RotateNonStop(Float.parseFloat(speedNonStopTurn.getText().toString()));
                    } else {
                        Log.i(TAG, "Speed input error");
                        Toast.makeText(MainActivity.this, "Speed input error", Toast.LENGTH_SHORT).show();
                    }
                break;

            case R.id.BstopMotors:
                //Stop motors
                //StopMotors();
                //Toast.makeText(MainActivity.this, BuddySDK.Actuators.getLeftWheelStatus(), Toast.LENGTH_SHORT).show();
                break;

            /*case R.id.SWenableLW:
                //Enable left wheel
                EnableWheels(((Switch) v).isChecked() ? 1 : 0, enableRightWheel.isChecked() ? 1 : 0);
                //leftWheelStatus.setText("Status : " + BuddySDK.Actuators.getLeftWheelStatus());
                break;

            case R.id.SWenableRW:
                //Enable right wheel
                EnableWheels(enableLeftWheel.isChecked() ? 1 : 0, ((Switch) v).isChecked() ? 1 : 0);
                //rightWheelStatus.setText("Status : " + BuddySDK.Actuators.getRightWheelStatus());
                break;*/
        }
    }

    //Check if the Edit Text is a valid float input
    public boolean isInputValid(String inputText){
        try{
            float val = Float.parseFloat(inputText);
            return true;
        }
        catch (NumberFormatException e){
            return false;
        }
    }


}