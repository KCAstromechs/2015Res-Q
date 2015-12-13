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
 * Red side of field
 * far right corner
 */

public class RedPos1Wait extends LinearOpMode {

    RobotBase robotBase;

    //Drive Constants
    private static final int kClicksPerRev = 1100;
    private static final int klongDrive = (int) (kClicksPerRev * 9.0);
    private static final int kClearWall = (int) (kClicksPerRev * 0.50);
    private static final int kSlowApproach =  (int) (kClicksPerRev * 1.0f);
    private static final int kReverse = (int)(kClicksPerRev*0.75);
    private static final int kPark = (int)(kClicksPerRev*1.25);


    @Override
    public void runOpMode() throws InterruptedException {

        sleep(1000);
        robotBase = new RobotBase(hardwareMap);
        robotBase.initializeServos();
        robotBase.calibrateGyro();
        telemetry.addData("Ready to run:", "Gyro is calabrated. You are ready to run. " +
                "Make sure that the robot is centered on the tile furthest to the right on the red side.");


        waitForStart();

        robotBase.driveStraight(kClearWall, 1, 0, 1.0f); //clears wall
        robotBase.turn(325, .25); //turns 45 degrees
        robotBase.driveStraight(klongDrive, 1, 310, 1.0f); // long drive down the field
        robotBase.turn(280, .25); // turns towards safety beacon
        robotBase.driveStraight(kSlowApproach, 0.5, 270, 1.0f); //approaches safety beacon
        robotBase.hammerTime();
        //robotBase.driveStraight(kReverse,0.5,90,-1); // backs away
        //robotBase.turn(180,0.5); //turn towards low goal
        //robotBase.driveStraight(kPark,0.75,180,1); //enter low zone
    }
}