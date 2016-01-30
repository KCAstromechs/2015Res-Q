/* Copyright (c) 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


/**
 * Base Drive code
 * Blue side of field
 * far left corner
 *
 * added camera for beacon light
 */

public class BluePos1Camera extends LinearOpMode {

    AstroRobotBaseInterface robotBase;

    //Drive Constants
    private static final double klongDrive = 106.0;
    private static final double kClearWall = 5.5;
    private static final double kSlowApproach = 23.0;


    @Override
    public void runOpMode() throws InterruptedException {

        robotBase = new RobotBaseUMKC(hardwareMap, this);
        robotBase.initializeServos();
        robotBase.calibrateGyro();
        robotBase.cameraSetup();
        telemetry.addData("Ready to run:", "Gyro is calibrated and camera is set up. You are ready to run. " +
                "Make sure that the robot is centered on the tile furthest to the left on the blue side.");

        waitForStart();

        robotBase.driveStraight(kClearWall, 1, 0, 1.0f); //clears wall
        robotBase.turn(40, 1.0f); //turns 45 degrees
        robotBase.driveStraight(klongDrive, 1, 50, 1.0f); // long drive down the field
        robotBase.turn(80, 1.0f); // turns towards safety beacon
        sleep(500);
        robotBase.snapPic();
        while(!robotBase.get_cameraProcessDone()){
            sleep(10);
        }
        robotBase.setPushDown();
        if (robotBase.get_yBlueAvg() > robotBase.get_yRedAvg()) {
            System.out.println("Blue Side: " + "Left");
            System.out.println("Red Side: " + "Right");
            System.out.println("==>   Blue | Red      avgBlueY:" + robotBase.get_yBlueAvg() + " avgRedY:" + robotBase.get_yRedAvg());
            robotBase.driveStraight(kSlowApproach, 0.5, 84, 1.0f); //approaches safety beacon
            robotBase.setDriveReverse();
            sleep(1000);
            robotBase.driveStraight(kSlowApproach - 2.5, 0.5, 84, 1.0f); //backs away from beacon
            robotBase.setDriveForward();
            robotBase.setPushUp();
            robotBase.turn(90, 1);
            robotBase.driveStraight(kSlowApproach, 0.5, 90, 1.0f); //approaches safety beacon
        } else {
            System.out.println("Blue Side: " + "Right");
            System.out.println("Red Side: " + "Left");
            System.out.println("==>   Red | Blue      avgBlueY:" + robotBase.get_yBlueAvg() + " avgRedY:" + robotBase.get_yRedAvg());
            robotBase.driveStraight(kSlowApproach - 2.5, 0.5, 93, 1.0f); //approaches safety beacon
        }

        robotBase.hammerTime();
    }
}