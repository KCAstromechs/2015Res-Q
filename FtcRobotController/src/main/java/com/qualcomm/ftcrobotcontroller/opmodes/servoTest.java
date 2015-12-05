package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Kevin on 11/19/2015.
 */

public class servoTest extends OpMode {
    Servo scythe;

    @Override
    public void init() {
        scythe=hardwareMap.servo.get("scythe");
    }


    @Override
    public void loop() {
        if(gamepad1.a){
            scythe.setPosition(0.45);
        }
        if(gamepad1.b){
            scythe.setPosition(1.0);
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
