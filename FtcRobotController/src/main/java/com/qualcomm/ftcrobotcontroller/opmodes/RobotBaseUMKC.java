package com.qualcomm.ftcrobotcontroller.opmodes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Environment;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kevin on 12/6/2015.
 */
public class RobotBaseUMKC implements AstroRobotBaseInterface {

    double inchesToEncoder = 86.3;
    //motors
    public DcMotor motorFrontRight;
    public DcMotor motorFrontLeft;
    public DcMotor motorBackRight;
    public DcMotor motorBackLeft;
    public DcMotor motorWinch;
    public DcMotor motorDrawerSlide;
    public DcMotor motorRight;
    public DcMotor motorLeft;
    public DcMotor encoderMotor;

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

    HardwareMap hardwareMap;

    LinearOpMode callingOpMode;


    //camera
    Camera camera;
    PictureCallback picDone;
    int CameraID = -1;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;


    public RobotBaseUMKC(HardwareMap hardwareMap, LinearOpMode _callingOpMode) {
        this.initializeVariables(hardwareMap);
        callingOpMode=_callingOpMode;
    }


    public RobotBaseUMKC(HardwareMap hardwareMap) {
        this.initializeVariables(hardwareMap);
    }

    public void initializeVariables (HardwareMap _hardwareMap){

        boolean rightZiplineDown = false;
        boolean leftZiplineDown = false;


                //motor init
        hardwareMap = _hardwareMap;
        setDriveForward();
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        motorWinch = hardwareMap.dcMotor.get("Winch");
        motorDrawerSlide = hardwareMap.dcMotor.get("DrawerSlide");

        //Servo init
        leftHook=hardwareMap.servo.get("leftHook");
        rightHook=hardwareMap.servo.get("rightHook");
        leftZipline=hardwareMap.servo.get("leftZipline");
        rightZipline=hardwareMap.servo.get("rightZipline");
        mjolnir=hardwareMap.servo.get("box");

        //sensor init
        gyro=hardwareMap.gyroSensor.get("gyro");
    }

    @Override
    public void setDriveReverse(){
        motorRight = hardwareMap.dcMotor.get("left");
        motorLeft = hardwareMap.dcMotor.get("right");
        encoderMotor = motorLeft;
    }

    @Override
    public void setDriveForward(){
        motorRight = hardwareMap.dcMotor.get("right");
        motorLeft = hardwareMap.dcMotor.get("left");
        encoderMotor = motorLeft;
    }

    public void snapPic(){
        camera.startPreview();
        camera.takePicture(null, null, picDone);
        System.out.println("Picture Taken");
    }

    public void cameraSetup() throws InterruptedException {
        //finds frontal camera
        //mounted screen up, using back camera with mirror
        int numOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numOfCameras; i++){
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i,info);
            if(info.facing == CameraInfo.CAMERA_FACING_BACK){
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
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }
//        Log.d(TAG,mediaStorageDir.getPath() + File.separator +
        //              "IMG_"+ timeStamp + ".jpg");
        return mediaFile;
    }

    public PictureCallback getPicCallback(){
        PictureCallback picture = new PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                System.out.println("got Data");
                Bitmap picture = BitmapFactory.decodeByteArray(data, 0,data.length);

                System.out.println("width: " + picture.getWidth());
                System.out.println("Hight: " + picture.getHeight());

                /*
                int clr = picture.getPixel(60,80);
                System.out.println("Red: " +Color.red(clr));
                System.out.println("Blue: " + Color.blue(clr));
                System.out.println("Green: " + Color.green(clr));
                */

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
                    System.out.println("Pixel (x,y): "+ "(90,"+ y +")");  //location of pixel
                    System.out.print(Color.red(currentPixel));
                    System.out.print(",");
                    System.out.print(Color.green(currentPixel) );
                    System.out.print(",");
                    System.out.println(Color.blue(currentPixel));
                    if(Color.red(currentPixel) < Color.blue(currentPixel)) {
                        totalBlue++;
                        System.out.println("Current pixel is Blue");
                    }
                    else {
                        totalRed++; //THIS FOR LOOP IS TOTALLY UNTESTED, CHEERS
                        System.out.println("Current pixel is Red");
                    }
                }

                if (totalBlue > totalRed){
                    System.out.println("Blue");
                }
                else {
                    System.out.println("Red");
                }
            }
        };
        System.out.println("PictureCallback is done");
        return picture;

    }

    public void calibrateGyro()throws InterruptedException{
        gyro.calibrate();

        Thread.sleep(100);
        while (gyro.isCalibrating()) {
            Thread.sleep(50);
        }
        // check calibrate gyro
        System.out.println("gyro (PreCalibration" + gyro.getHeading());
    }

    public void setGrabberUp() {

   }

    public void setGrabberMiddle() {

    }

    public void setGrabberDown() {

    }

    public void setLeftZiplineUp() {
        leftZipline.setPosition(1.0);
    }

    public void setLeftZiplineDown() {
        leftZipline.setPosition(0.4);
    }

    public void setRightZiplineUp() {
        rightZipline.setPosition(0.4);
    }

    public void setRightZiplineDown() {
        rightZipline.setPosition(1.0);
    }

    public void setMjolnirDown(){
        mjolnir.setPosition(1);
    }

    public void setMjolnirUp(){
        mjolnir.setPosition(0);
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

    @Override
    public void setRightHookUp(){
        setRightHookPosition(0);
    }

    @Override
    public void setLeftHookUp(){
        setLeftHookPosition(1);
    }

    @Override
    public void setRightHookDown(){
        setRightHookPosition(0.9);
    }

    @Override
    public void setLeftHookDown(){
        setLeftHookPosition(0.1);
    }

    public void initializeServos() {
        setLeftZiplineUp();
        setRightZiplineUp();
        setMjolnirDown();
        setRightHookUp();
        setLeftHookUp();

    }

    public void setRightPower(double rightPower) {
        motorRight.setPower(rightPower);
    }

    public void setLeftPower(double leftPower){
        motorLeft.setPower(leftPower);
    }

    @Override
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

        System.out.println("turn... c="+clockwise+" cc="+cclockwise);



        if(cclockwise > clockwise){
            leftPower=power;
            rightPower=-power;
            motorRight.setPower(rightPower);
            motorLeft.setPower(leftPower);
            while(Math.abs(gyro.getHeading() - turnHeading) > 2){
                callingOpMode.waitForNextHardwareCycle();
            }
            motorRight.setPower(0);
            motorLeft.setPower(0);
        }
        else {
            leftPower=-power;
            rightPower=power;
            motorRight.setPower(rightPower);
            motorLeft.setPower(leftPower);
            while(Math.abs(gyro.getHeading() - turnHeading) > 2){
                //java.lang.Thread.sleep(25);
                callingOpMode.waitForNextHardwareCycle();
            }
            motorRight.setPower(0);
            motorLeft.setPower(0);
        }
        Thread.sleep(100);
    }


    public void driveStraight(double inches, double power, int heading, float direction) throws InterruptedException {
        this.driveStraightEncoder((int) (inches * inchesToEncoder), power, heading, direction);
    }

    @Override
    public void driveStraightEncoder(int dist, double power, int heading, float direction)throws InterruptedException {
        //gyro stabilization - PID
        float proportionalConst = 0.05f;
        int degErr;
        float correction;
        int lastDegErr;
        double localLeftPower = power;
        double localRightPower=power;

        //reset encoders
        encoderMotor.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        callingOpMode.waitOneFullHardwareCycle();
        //callingOpMode.waitOneFullHardwareCycle();
        callingOpMode.sleep(250);

        encoderMotor.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        while(encoderMotor.getCurrentPosition()>20) {
            callingOpMode.waitOneFullHardwareCycle();
            System.out.println("Resetting Encoder= " + encoderMotor.getCurrentPosition());
        }

        //set motor Power
        motorRight.setPower(power);
        motorLeft.setPower(power);

        callingOpMode.waitOneFullHardwareCycle();

        degErr=0;


        System.out.println("Start Encoder= " + encoderMotor.getCurrentPosition());
        while (encoderMotor.getCurrentPosition() < dist) {
            //System.out.println("Encoder= " +encoderMotor.getCurrentPosition());
            lastDegErr = degErr;
            degErr = gyro.getHeading() - heading;
            if (degErr > 180){
                degErr -= 360;
            }
            if (degErr != lastDegErr) {
                correction = degErr * proportionalConst;
                localLeftPower = Range.clip((power - correction)*direction, -1.0f, 1.0f);
                localRightPower = Range.clip((power + correction)*direction, -1.0f, 1.0f);

                motorRight.setPower(localRightPower);
                motorLeft.setPower(localLeftPower);

                callingOpMode.waitForNextHardwareCycle();

            }

        }
        System.out.println("End Encoder= " +encoderMotor.getCurrentPosition());
        motorRight.setPower(0);
        motorLeft.setPower(0);
        Thread.sleep(100);
    }

    @Override
    public void setLeftHookPosition(double position){
        leftHook.setPosition(position);
    }

    @Override
    public void setRightHookPosition(double position){
        rightHook.setPosition(position);
    }

    @Override
    public void updateWinchAndDrawerSlide(float winch, float DrawerSlide) {
        motorWinch.setPower(winch);
        motorDrawerSlide.setPower(DrawerSlide);
    }
}
