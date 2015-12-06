package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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
        mjolnir.setPosition(1.0);
    }

    public void setMjolnirUp(){
        mjolnir.setPosition(0.45);
    }

    public void setMjolnirAllClear(){
        mjolnir.setPosition(0.0);
    }

    public void setLeftLockOpen(){
        leftLock.setPosition(0.01);
    }

    public void setRightLockOpen(){
        rightLock.setPosition(0.7);
    }

    public void setLeftLockClosed(){
        leftLock.setPosition(0.7);
    }

    public void setRightLockClosed(){
        rightLock.setPosition(0.01);
    }
    public void setRightPower(double rightPower){
        motorFrontRight.setPower(rightPower);
        motorBackRight.setPower(rightPower);
    }
    public void setLeftPower(double leftPower){
        motorFrontLeft.setPower(leftPower);
        motorBackLeft.setPower(leftPower);
    }
}
