/* Copyright (c) 2014 Qualcomm Technologies Inc

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

package com.qualcomm.ftcrobotcontroller.opmodes.archive;

import com.qualcomm.ftcrobotcontroller.opmodes.archive.RobotBaseSmithville;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.Range;

public class tankDriveSpinnerLifterThing extends OpMode {

	RobotBaseSmithville robotBaseSmithville;

	public tankDriveSpinnerLifterThing() {

	}


	@Override
	public void init() {

		robotBaseSmithville = new RobotBaseSmithville(hardwareMap);
		robotBaseSmithville.initializeServos();

	}
	@Override
	public void loop() {

		/*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // tank drive
        // note that if y equal -1 then joystick is pushed all of the way forward.
        float left = -gamepad1.left_stick_y;
        float right = -gamepad1.right_stick_y;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);

		// write the values to the motors
		robotBaseSmithville.motorFrontRight.setPower(right);
		robotBaseSmithville.motorFrontLeft.setPower(left);
		robotBaseSmithville.motorBackRight.setPower(right);
		robotBaseSmithville.motorBackLeft.setPower(left);

		// update the position of the arm.

		float right2 = gamepad2.right_stick_y;
		float left2 = gamepad2.left_stick_y;

		if (Math.abs(left2)<0.1){
			left2=0.0f;
		}
		if (Math.abs(right2)<0.1){
			right2=0;
		}
		if (gamepad2.right_bumper)
		{
			robotBaseSmithville.setGrabberUp();
		}
		if (gamepad2.right_trigger>0.75)
		{
			robotBaseSmithville.setGrabberMiddle();
		}

		if (gamepad2.left_trigger>0.75)
		{
			robotBaseSmithville.setGrabberDown();
		}
		telemetry.addData("leftT", gamepad2.left_trigger);

		if (gamepad2.left_bumper){
			right2 /= 10.0;
		}
		telemetry.addData("right2", right2);

		if(gamepad1.a){
			robotBaseSmithville.setLeftZiplineDown();
		}

		if(gamepad1.b){
			robotBaseSmithville.setRightZiplineDown();
		}

		if(gamepad1.x){
			robotBaseSmithville.setLeftZiplineUp();
		}

		if(gamepad1.y){
			robotBaseSmithville.setRightZiplineUp();
		}

		if(gamepad2.a){
			robotBaseSmithville.setMjolnirUp();
		}

		if(gamepad2.b){
			robotBaseSmithville.setMjolnirDown();
		}

		if(gamepad2.x){
			robotBaseSmithville.setLeftLockClosed();
			robotBaseSmithville.setRightLockClosed();
		}

		if(gamepad2.y){
			robotBaseSmithville.setLeftLockOpen();
			robotBaseSmithville.setRightLockOpen();
		}

		robotBaseSmithville.motorWinch.setPower(right2);
		robotBaseSmithville.motorDrawerSlide.setPower(left2);

	}

	@Override
	public void stop() {

	}
	
	double scaleInput(double dVal)  {
		double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
				0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

		// get the corresponding index for the scaleInput array.
		int index = (int) (dVal * 16.0);

		// index should be positive.
		if (index < 0) {
			index = -index;
		}

		// index cannot exceed size of array minus 1.
		if (index > 16) {
			index = 16;
		}

		// get value from the array.
		double dScale = 0.0;
		if (dVal < 0) {
			dScale = -scaleArray[index];
		} else {
			dScale = scaleArray[index];
		}

		// return scaled value.
		return dScale;
	}

}
