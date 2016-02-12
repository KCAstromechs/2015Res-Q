package com.qualcomm.ftcrobotcontroller.opmodes.archive;

import com.qualcomm.ftcrobotcontroller.opmodes.AstroRobotBaseInterface;
import com.qualcomm.ftcrobotcontroller.opmodes.RobotBaseUMKC;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Kevin on 12/6/2015.
 */
public class ServoTest extends OpMode {

    AstroRobotBaseInterface robotBase;

    @Override
    public void init() {

        robotBase = new RobotBaseUMKC(hardwareMap);

    }

    @Override
    public void loop() {

        if (gamepad1.a) {
            robotBase.setRightHookPosition(0.5);
            robotBase.setRightHookPosition(0.5);
        }
        if (gamepad1.b) {
            robotBase.setMjolnirUp();
        }

        if(gamepad1.x){

        }
    }
}
