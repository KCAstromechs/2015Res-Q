package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Kevin on 1/31/2016.
 */
public class AutonomousInterface  extends LinearOpMode {

    AstroRobotBaseInterface robotBase;

    boolean redSide = true;
    boolean blueSide = false;
    boolean isCameraOn = true;
    boolean isWaitOn = false;
    boolean isPos1 = true;
    boolean isPos2 = false;
    boolean aButtonPast = false;
    boolean bButtonPast = false;
    boolean xButtonPast = false;
    boolean yButtonPast = false;

    @Override
    public void runOpMode() throws InterruptedException {

        robotBase = new RobotBaseUMKC(hardwareMap,this);
        robotBase.initializeServos();

        while(!gamepad1.start){
            if(!aButtonPast){
                if(gamepad1.a){
                    if(redSide){
                        redSide = false;
                    } else {
                        redSide = true;
                    }
                    if(blueSide){
                        blueSide = false;
                    } else {
                        blueSide = true;
                    }
                    aButtonPast = true;
                }
            } else{
                if(!gamepad1.a){
                    aButtonPast = false;
                }
            }

            if(!bButtonPast){
                if(gamepad1.b){
                    if(isPos1){
                        isPos1 = false;
                    } else {
                        isPos1 = true;
                    }
                    if(isPos2){
                        isPos2 = false;
                    } else {
                        isPos2 = true;
                    }
                    bButtonPast = true;
                }
            } else {
                if(!gamepad1.b){
                    bButtonPast = false;
                }
            }

            if(!xButtonPast){
                if(gamepad1.x){
                    if(isCameraOn){
                        isCameraOn = false;
                    } else {
                        isCameraOn = true;
                    }
                    xButtonPast = true;
                }
            } else {
                if(!gamepad1.x){
                    xButtonPast = false;
                }
            }

            if(!yButtonPast){
                if(gamepad1.y){
                    if(isWaitOn){
                        isWaitOn = false;
                    } else {
                        isWaitOn = true;
                    }
                    yButtonPast = true;
                }
            } else {
                if(!gamepad1.y){
                    yButtonPast = false;
                }
            }

            if(redSide){
                if(isPos1){
                    telemetry.addData("Position:", "Red 1");
                }
                if(isPos2){
                    telemetry.addData("Position:", "Red 2");
                }
            }
            if(blueSide){
                if(isPos1){
                    telemetry.addData("Position:", "Blue 1");
                }
                if(isPos2){
                    telemetry.addData("Position:", "Blue 2");
                }
            }

            telemetry.addData("Camera:", isCameraOn);
            telemetry.addData("Wait:", isWaitOn);

            waitForNextHardwareCycle();
        }

        telemetry.clearData();
        
        if(redSide){
            if(isPos1){
                if(isCameraOn){
                    if(isWaitOn){
                        telemetry.addData("Selection Loop Exited. Program Queued", "Red Position 1 with Camera and Wait");
                    } else {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Red Position 1 with Camera");
                    }
                } else {
                    if(isWaitOn){
                        telemetry.addData("Selection Loop Exited. Program Queued", "Red Position 1 with Wait");
                    } else {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Red Position 1");
                    }
                }
            } else if (isPos2) {
                if(isCameraOn){
                    if(isWaitOn){
                        telemetry.addData("Selection Loop Exited. Program Queued", "Red Position 2 with Camera and Wait");
                    } else {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Red Position 2 with Camera");
                    }
                } else {
                    if(isWaitOn){
                        telemetry.addData("Selection Loop Exited. Program Queued", "Red Position 2 with Wait");
                    } else {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Red Position 2");
                    }
                }
            }

            } else if (blueSide) {
            if (isPos1) {
                if (isCameraOn) {
                    if (isWaitOn) {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Blue Position 1 with Camera and Wait");
                    } else {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Blue Position 1 with Camera");
                    }
                } else {
                    if (isWaitOn) {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Blue Position 1 with Wait");
                    } else {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Blue Position 1");
                    }
                }
            } else if (isPos2) {
                if (isCameraOn) {
                    if (isWaitOn) {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Blue Position 2 with Camera and Wait");
                    } else {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Blue Position 2 with Camera");
                    }
                } else {
                    if (isWaitOn) {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Blue Position 2 with Wait");
                    } else {
                        telemetry.addData("Selection Loop Exited. Program Queued", "Blue Position 2");
                    }
                }
            }
        }
        waitForStart();


    }
}
