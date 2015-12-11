package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
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

    public void setRightPower(double rightPower){
        motorFrontRight.setPower(rightPower);
        motorBackRight.setPower(rightPower);
    }

    public void setLeftPower(double leftPower){
        motorFrontLeft.setPower(leftPower);
        motorBackLeft.setPower(leftPower);
    }

    private boolean calcTurnDirection (int target, int heading, int variation)throws InterruptedException {
        //Finds weather to turn clockwise or anticlockwise
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
                    return false;
                } else {
                    return true;
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
        int variation = Math.abs(turnHeading-gyro.getHeading());
        if(calcTurnDirection(turnHeading, gyro.getHeading(), variation)){
            leftPower=1;
            rightPower=-1;
            motorFrontRight.setPower(rightPower);
            motorFrontLeft.setPower(leftPower);
            motorBackRight.setPower(rightPower);
            motorBackLeft.setPower(leftPower);
            while(gyro.getHeading()<turnHeading){
                java.lang.Thread.sleep(50);
            }
            motorFrontRight.setPower(0);
            motorFrontLeft.setPower(0);
            motorBackRight.setPower(0);
            motorBackLeft.setPower(0);
        }
        else {
            leftPower=-1;
            rightPower=1;
            motorFrontRight.setPower(rightPower);
            motorFrontLeft.setPower(leftPower);
            motorBackRight.setPower(rightPower);
            motorBackLeft.setPower(leftPower);
            while(gyro.getHeading()>turnHeading){
                java.lang.Thread.sleep(50);
            }
            motorFrontRight.setPower(0);
            motorFrontLeft.setPower(0);
            motorBackRight.setPower(0);
            motorBackLeft.setPower(0);
        }
    }

    public void driveStraight(int dist, double power, int heading)throws InterruptedException {
        //gyro stabilization - PID
        float proportionalConst = 0.05f;
        int degErr;
        float correction;
        int lastDegErr;
        double localLeftPower=power;
        double localRightPower=power;

        degErr=0;

        while (motorBackRight.getCurrentPosition() < dist) {
            //System.out.println("Encoder= " +motorBackRight.getCurrentPosition());
            lastDegErr = degErr;
            degErr = gyro.getHeading() - heading;
            if (degErr != lastDegErr) {
                correction = degErr * proportionalConst;
                localLeftPower = Range.clip(power - correction, -1.0f, 1.0f);
                localRightPower = Range.clip(power + correction, -1.0f, 1.0f);

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


            }
            java.lang.Thread.sleep(50);
        }
    }
}
