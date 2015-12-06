package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Kevin on 11/19/2015.
 */

public class servoTest extends OpMode {
    Servo mjolnir;
    Servo grabber;
    @Override
    public void init() {
        mjolnir=hardwareMap.servo.get("box");
        grabber=hardwareMap.servo.get("grabber");
    }


    @Override
    public void loop() {
        if(gamepad1.a){
            mjolnir.setPosition(0.45);
        }
        if(gamepad1.b){
            mjolnir.setPosition(1.0);
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
