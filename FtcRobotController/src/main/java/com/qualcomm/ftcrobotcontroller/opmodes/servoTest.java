package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Kevin on 12/6/2015.
 */
public class ServoTest extends OpMode {

    RobotBase robotBase;

    @Override
    public void init() {

        robotBase = new RobotBase(hardwareMap);

    }

    @Override
    public void loop() {

        if (gamepad1.a) {
            robotBase.setMjolnirDown();
        }
        if (gamepad1.b) {
            robotBase.setMjolnirUp();
        }

        if(gamepad1.x){

        }
    }
}
