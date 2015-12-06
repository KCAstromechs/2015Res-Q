package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Kevin on 12/6/2015.
 */
public class RobotBase {

    Servo mjolnir;
    Servo grabber;
    Servo leftZipline;
    Servo rightZipline;
    Servo leftLock;
    Servo rightLock;

    public RobotBase(HardwareMap hardwareMap) {

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
}
