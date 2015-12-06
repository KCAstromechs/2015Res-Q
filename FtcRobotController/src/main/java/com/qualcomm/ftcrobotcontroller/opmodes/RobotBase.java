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
    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor motorBackRight;
    DcMotor motorBackLeft;

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
        //motor intit
        motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        motorBackRight = hardwareMap.dcMotor.get("backRight");
        motorBackLeft = hardwareMap.dcMotor.get("backLeft");

        //Servo init
        mjolnir=hardwareMap.servo.get("box");
        grabber=hardwareMap.servo.get("grabber");
        leftZipline=hardwareMap.servo.get("leftZipline");
        rightZipline=hardwareMap.servo.get("rightZipline");
        leftLock=hardwareMap.servo.get("leftLock");
        rightLock=hardwareMap.servo.get("rightLock");

    }

    public void setGrabberUp() {
        grabber.setPosition(0.6);
    }

    public void setGrabberDown() {
        grabber.setPosition(0.1);
    }

    public void setLeftZiplineUp() {
        leftZipline.setPosition(0.5);
    }

    public void setLeftZiplineDown() {
        leftZipline.setPosition(1.0);
    }

    public void setRightZiplineUp() {
        rightZipline.setPosition(0.5);
    }

    public void setRightZiplineDown() {
        rightZipline.setPosition(0.0);
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

    }

    public void setRightLockOpen(){

    }

    public void setLeftLockClosed(){

    }

    public void setRightLockClosed(){

    }
}
