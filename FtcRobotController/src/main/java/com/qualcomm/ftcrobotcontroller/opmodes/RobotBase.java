package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.os.Environment;
import java.io.File;
import java.io.FileOutputStream;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.util.Log;

/**
 * Created by Kevin on 12/6/2015.
 */
public class RobotBase {
    //motors
    public DcMotor motorFrontRight;
    public DcMotor motorFrontLeft;
    public DcMotor motorBackRight;
    public DcMotor motorBackLeft;
    public DcMotor motorWinch;
    public DcMotor motorDrawerSlide;
    public DcMotor motorRight;
    public DcMotor motorLeft;

    //Servos
    Servo mjolnir;
    Servo grabber;
    Servo leftZipline;
    Servo rightZipline;
    Servo leftLock;
    Servo rightLock;
    Servo leftHook;
    Servo rightHook;

    //sensors
    GyroSensor gyro;

    //camera
    Camera camera;
    PictureCallback picDone;
    int CameraID = -1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;



    public RobotBase(HardwareMap hardwareMap) {
        //motor init
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorRight = hardwareMap.dcMotor.get("right");
        motorLeft = hardwareMap.dcMotor.get("left");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorWinch = hardwareMap.dcMotor.get("winch");
        motorDrawerSlide = hardwareMap.dcMotor.get("drawerSlide");

        //Servo init
        mjolnir=hardwareMap.servo.get("box");
        grabber=hardwareMap.servo.get("grabber");
        leftZipline=hardwareMap.servo.get("leftZipline");
        rightZipline=hardwareMap.servo.get("rightZipline");
        leftLock=hardwareMap.servo.get("leftLock");
        rightLock=hardwareMap.servo.get("rightLock");
        leftHook=hardwareMap.servo.get("leftHook");
        rightHook=hardwareMap.servo.get("rightHook");

        //sensor init
        gyro=hardwareMap.gyroSensor.get("gyro");

    }

    public void snapPic(){
        camera.startPreview();
        camera.takePicture(null, null, picDone);
    }

    public void cameraSetup() throws InterruptedException {
        //finds frontal camera
        //mounted vertically
        int numOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numOfCameras; i++){
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i,info);
            if(info.facing == CameraInfo.CAMERA_FACING_FRONT){
                CameraID = i;

                try {
                    SurfaceTexture texture = new SurfaceTexture(0);
                    camera = Camera.open(CameraID);
                    camera.setPreviewTexture(texture);
                    picDone = getPicCallback();
                    System.out.println("Found Camera");
                    Thread.sleep(2000);
                }
                catch (Exception e){
                   System.out.println("cameraSetup Failed");
                }
            }
        }
    }

    private static File getOutputMediaFile(int type, String folder_name) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), folder_name);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("DEBUG", "Unable to create directory!");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
            System.out.println("TYPE: Image");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
            System.out.println("TYPE: Video");
        } else {
            return null;
        }
//        Log.d(TAG,mediaStorageDir.getPath() + File.separator +
        //              "IMG_"+ timeStamp + ".jpg");
        return mediaFile;
    }

    private PictureCallback getPicCallback(){
        PictureCallback picture = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                System.out.println("got Data");
                Bitmap picture = BitmapFactory.decodeByteArray(data, 0,data.length);

                System.out.println("width: " + picture.getWidth());
                System.out.println("Hight: " + picture.getHeight());

                int clr = picture.getPixel(60,80);
                System.out.println("Red: " +Color.red(clr));
                System.out.println("Blue: " + Color.blue(clr));
                System.out.println("Green: " + Color.green(clr));

                int totalRed = 0;
                int totalBlue = 0;
                int currentPixel = 0;


                File picturefile = getOutputMediaFile(MEDIA_TYPE_IMAGE, "capture");

                try {
                    //write the file
                    FileOutputStream fos = new FileOutputStream(picturefile);
                    fos.write(data);
                    fos.close();
                } catch (Exception e)
                {
                    System.out.println("Camera: " + "failed to save pic, exception"+e.getMessage());
                }

                for(int y = 0; y < picture.getHeight() / 2; y++ ) {
                    currentPixel = picture.getPixel(90,y);
                    System.out.println("RED: " + Color.red(currentPixel));
                    System.out.println("GREEN: " + Color.green(currentPixel));
                    System.out.println("BLUE: " + Color.blue(currentPixel));

                    if(Color.red(currentPixel) < Color.blue(currentPixel)) {
                        totalBlue++;
                    }
                    else {
                        totalRed++; //THIS FOR LOOP IS TOTALLY UNTESTED, CHEERS
                    }
                }
                System.out.println("Total RED: " + totalRed);
                System.out.println("Total BLUE: " + totalBlue);

                if (totalBlue > totalRed){
                    System.out.println("Blue");
                }
                else {
                    System.out.println("Red");
                }


            }
        };
        return picture;



    }

    public void calibrateGyro()throws InterruptedException{
        gyro.calibrate();

        java.lang.Thread.sleep(100);
        while (gyro.isCalibrating()) {
            java.lang.Thread.sleep(50);
        }
        // check calibrate gyro
        System.out.println("gyro (PreCalibration" + gyro.getHeading());
    }

    public void setGrabberUp() {
        grabber.setPosition(0.7);
    }

    public void setGrabberMiddle() {
        grabber.setPosition(0.4);
    }

    public void setGrabberDown() {
        grabber.setPosition(0.1);
    }

    public void setLeftZiplineUp() {
        leftZipline.setPosition(1.0);
    }

    public void setLeftZiplineDown() {
        leftZipline.setPosition(0.5);
    }

    public void setRightZiplineUp() {
        rightZipline.setPosition(0.4);
    }

    public void setRightZiplineDown() {
        rightZipline.setPosition(1.0);
    }

    public void setMjolnirDown(){
        mjolnir.setPosition(0.9);
    }

    public void setMjolnirUp(){
        mjolnir.setPosition(0.1);
    }

    public void hammerTime() throws InterruptedException {
        setMjolnirUp();
        System.out.println("Mjolnir Down");
        Thread.sleep(1500);
        System.out.println("Mjolnir sleep done");
        mjolnir.setPosition(0.9);
        System.out.println("Mjolnir Up");
        Thread.sleep(1000);
        System.out.println("Mjolnir last sleep done");
    }

    public void setLeftLockOpen(){
        leftLock.setPosition(0.2);
    }

    public void setRightLockOpen(){
        rightLock.setPosition(0.98);
    }

    public void setLeftLockClosed(){
        leftLock.setPosition(1.0);
    }

    public void setRightLockClosed(){
        rightLock.setPosition(0.18);
    }

    public void initializeServos() {
        setGrabberDown();
        setLeftZiplineUp();
        setRightZiplineUp();
        setMjolnirDown();
        setRightLockOpen();
        setLeftLockOpen();
    }

    public void setRightPower(double rightPower) {
        motorFrontRight.setPower(rightPower);
        motorBackRight.setPower(rightPower);
    }

    public void setLeftPower(double leftPower){
        motorFrontLeft.setPower(leftPower);
        motorBackLeft.setPower(leftPower);
    }

    private boolean calcTurnDirection (int target, int heading, int variation)throws InterruptedException {
        //Finds weather to turn clockwise or anticlockwise
        //true is clockwise; false is counter clockwise
        if(target>180){
            if(heading>180){
                if(heading>target){
                    return false;
                } else {
                    return true;
                }
            } else {
                if (variation>180){
                    return false;
                } else {
                    return true;
                }
            }
        } else {
            if (heading>180){
                if(variation>180){
                    return true;
                } else {
                    return false;
                }
            } else {
                if (heading>target){
                    return false;
                } else {
                    return true;
                }
            }
        }

    }

    public void turn(int turnHeading, double power)throws InterruptedException{
        double rightPower;
        double leftPower;
        double cclockwise = gyro.getHeading() - turnHeading;
        double clockwise = turnHeading - gyro.getHeading();

        if (clockwise >= 360){
            clockwise -= 360;
        }
        if (clockwise < 0){
            clockwise += 360;
        }
        if (cclockwise >= 360){
            cclockwise -= 360;
        }
        if (cclockwise < 0){
            cclockwise += 360;
        }

        if(cclockwise > clockwise){
            leftPower=power;
            rightPower=-power;
            motorFrontRight.setPower(rightPower);
            motorFrontLeft.setPower(leftPower);
            motorBackRight.setPower(rightPower);
            motorBackLeft.setPower(leftPower);
            while(Math.abs(gyro.getHeading() - turnHeading) > 2){
                java.lang.Thread.sleep(25);
            }
            motorFrontRight.setPower(0);
            motorFrontLeft.setPower(0);
            motorBackRight.setPower(0);
            motorBackLeft.setPower(0);
        }
        else {
            leftPower=-power;
            rightPower=power;
            motorFrontRight.setPower(rightPower);
            motorFrontLeft.setPower(leftPower);
            motorBackRight.setPower(rightPower);
            motorBackLeft.setPower(leftPower);
            while(Math.abs(gyro.getHeading() - turnHeading) > 2){
                java.lang.Thread.sleep(25);
            }
            motorFrontRight.setPower(0);
            motorFrontLeft.setPower(0);
            motorBackRight.setPower(0);
            motorBackLeft.setPower(0);
        }
        Thread.sleep(100);
    }

    public void driveStraight(int dist, double power, int heading, float direction)throws InterruptedException {
        //gyro stabilization - PID
        float proportionalConst = 0.05f;
        int degErr;
        float correction;
        int lastDegErr;
        double localLeftPower=power;
        double localRightPower=power;

        //reset encoders
        motorFrontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        Thread.sleep(250);

        motorFrontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorFrontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorBackRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        motorBackLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        //set motor Power
        motorFrontRight.setPower(power);
        motorFrontLeft.setPower(power);
        motorBackRight.setPower(power);
        motorBackLeft.setPower(power);

        degErr=0;


        while (motorBackRight.getCurrentPosition() < dist) {
            //System.out.println("Encoder= " +motorBackRight.getCurrentPosition());
            lastDegErr = degErr;
            degErr = gyro.getHeading() - heading;
            if (degErr != lastDegErr) {
                correction = degErr * proportionalConst;
                localLeftPower = Range.clip((power - correction)*direction, -1.0f, 1.0f);
                localRightPower = Range.clip((power + correction)*direction, -1.0f, 1.0f);

                motorFrontRight.setPower(localRightPower);
                motorFrontLeft.setPower(localLeftPower);
                motorBackRight.setPower(localRightPower);
                motorBackLeft.setPower(localLeftPower);

                /*
                telemetry.addData("motorFrontRight", motorFrontRight.getCurrentPosition());
                telemetry.addData("motorFrontLeft", motorFrontLeft.getCurrentPosition());
                telemetry.addData("motorBackRight", motorBackRight.getCurrentPosition());
                telemetry.addData("motorBackLeft", motorBackLeft.getCurrentPosition());
                */
                Thread.sleep(50);

            }

        }
        motorBackRight.setPower(0);
        motorBackLeft.setPower(0);
        motorFrontLeft.setPower(0);
        motorFrontRight.setPower(0);
        Thread.sleep(100);
    }
}
