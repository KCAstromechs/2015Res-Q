package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

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

    //Servos
    Servo mjolnir;
    Servo grabber;
    Servo leftZipline;
    Servo rightZipline;
    Servo leftLock;
    Servo rightLock;

    //sensors
    GyroSensor gyro;
    ColorSensor color;


    //camera


    public RobotBase(HardwareMap hardwareMap) {
        //motor init
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.REVERSE);
        motorWinch = hardwareMap.dcMotor.get("winch");
        motorDrawerSlide = hardwareMap.dcMotor.get("drawerSlide");

        //Servo init
        mjolnir=hardwareMap.servo.get("box");
        grabber=hardwareMap.servo.get("grabber");
        leftZipline=hardwareMap.servo.get("leftZipline");
        rightZipline=hardwareMap.servo.get("rightZipline");
        leftLock=hardwareMap.servo.get("leftLock");
        rightLock=hardwareMap.servo.get("rightLock");

        //sensor init
        gyro=hardwareMap.gyroSensor.get("gyro");
        color = hardwareMap.colorSensor.get("color");
        //leftColorSensor = hardwareMap.colorSensor.get("leftColorSensor");

        //camera


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

    public void beaconLightRed(){
        //use camera
        //

    }

}
