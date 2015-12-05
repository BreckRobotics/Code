/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Robo;

/**
 *
 * @author paul
 */

public class RoboAutopilot extends RoboTask {
    
    // sub-tasks for autonomous mode
    class Track extends RoboTask{
        double ignoreCrossUntil = 1;

        double accelTime = 0.5;
        double speed1 = 0;
        double time1 = 0;
        double decelTime = 0.2;
        double speed2 = 0;
        double time2 = 0;
        
        // manipulator command
        double mTime = 0;   // when to activater the manipulator command
        double elHeight = -1;   // elevator target height
        double armAngle = -1;   // arm atrget angle
        int peg = -1;           // target peg

        boolean trackLeft = true;   // go left if line not visible at all
        boolean trackLastSeenSide = false;  // track toward the last seen side
        boolean flipTrackOnIntercept = false;
        public boolean yDetected = false;
        private boolean lineIsLeft = false;
        private boolean lineIsRight = false;

        private double ignoreLineTime = 0;

        Track(){
        }

        public void start(){
            super.start();
            //robot.drive.enableAutoRotation();
            //robot.drive.resetGyro();
        }

        public void handle(){
            if(!active) return;
            super.handle();

            // speed control
            if(taskTime < accelTime && taskTime < time1){
                // accelerate
                robot.drive.ySpeed = speed1 * (taskTime / accelTime);
            }
            else if(taskTime < time1){
                robot.drive.ySpeed = speed1;
            }
            else if(taskTime < (time1 + time2)){
                robot.drive.ySpeed = speed2;
            }
            else {
                robot.drive.ySpeed = 0;
                robot.drive.xSpeed = 0;
            }

            // cross detection
            if(taskTime > ignoreCrossUntil && robot.lineTracker.cross){
                robot.drive.go(0, 0, 0);
                stop();
                return;
            }

            // note: driving backwards steering is reversed
            double turn = 0;
            double slide = 0;
            if(robot.drive.ySpeed > 0){
                turn = 0.05;
                slide = 0.3;
            }
            else {
                turn = -0.05;
                slide = 0.3;
            }

            // analyze line sensor data
            if(robot.lineTracker.center){
                // 0 1 0 = straight
                lineIsLeft = false;
                lineIsRight = false;
            }
            else if(robot.lineTracker.left && !robot.lineTracker.right){
                // 1 0 0 = too far right
                lineIsLeft = true;
                lineIsRight = false;
                // adjust autotrack (after 2 seconds!)
                if(taskTime >= 2 && trackLastSeenSide) trackLeft = true;
            }
            else if(!robot.lineTracker.left && robot.lineTracker.right) {
                // 0 0 1 = too far left
                lineIsLeft = false;
                lineIsRight = true;
                if(taskTime >= 2 && trackLastSeenSide) trackLeft = false;
            }
            else if(!robot.lineTracker.left && !robot.lineTracker.right) {
                // 0 0 0 - line not visible - track in preferred direction
                lineIsLeft = trackLeft;
                lineIsRight = !trackLeft;
            }
 
            // actual driving
            if(taskTime > ignoreLineTime){
                if(robot.lineTracker.lineVisible){
                    // flip tracking side if requested
                    if(flipTrackOnIntercept){
                        trackLeft = !trackLeft;
                        flipTrackOnIntercept = false;
                    }
                    if(lineIsLeft){
                        // turn/slide left
                        robot.drive.rotSpeed = -turn;
                        robot.drive.xSpeed = -slide;
                    }
                    else if(lineIsRight){
                        // turn/slide right
                        robot.drive.rotSpeed = turn;
                        robot.drive.xSpeed = slide;
                    }
                    else{
                        robot.drive.rotSpeed = 0;
                        robot.drive.xSpeed = 0;
                    }
                }
                else {
                    // line not visible - just slide sideways pretty aggressively
                    if(lineIsLeft) robot.drive.xSpeed = -0.4;
                    else if(lineIsRight) robot.drive.xSpeed = 0.4;
                    else robot.drive.xSpeed = 0;
                }
            }
            else{
               robot.drive.xSpeed = 0;
            }

            // manipulator commands
            if(taskTime >= mTime){
                if(peg >= 0) robot.manipulator.goToPeg(peg);
                if(elHeight >= 0) robot.manipulator.moveElevatorTo(elHeight);
                if(armAngle >= 0) robot.manipulator.moveArmTo(armAngle);
                mTime = 999;
            }

            if(taskTime >= 15) {
                robot.drive.go(0, 0, 0);
                stop();
            }
        }
    }

    class Move extends RoboTask{
        double maxTime = 0;
        double x, y, r;

        Move(double fwd, double side, double rot, double mt){
            y = fwd;
            x = side;
            r = rot;
            maxTime = mt;
        }

        public void start(){
            super.start();
            robot.drive.go(y, x, r);
        }

        public void handle(){
            if(!active) return;
            super.handle();

            if(taskTime >= maxTime) {
                robot.drive.go(0, 0, 0);
                // System.out.printf("T=%1.2f: autopilot move stopped\n", taskTime);
                stop();
            }
        }
    }


    class ReleaseTube extends RoboTask{
        public void start(){
            super.start();
            robot.manipulator.moveElevatorTo(robot.manipulator.currentHeight - 13);
        }
        public void handle(){
            if(!active) return;
            super.handle();
            if(taskTime > 2 && !robot.manipulator.isMoving()) {
                // System.out.printf("T=%1.2f: autopilot tube released\n", taskTime);
                stop();
            }
        }
    }

        class ArmForward extends RoboTask{
        public void start(){
            super.start();
            robot.manipulator.moveArmTo(10);
        }
        public void handle(){
            if(!active) return;
            super.handle();
            if(taskTime > 1.5 && !robot.manipulator.isMoving()) {
                stop();
            }
        }
    }

    class ArmDown extends RoboTask{
        public void start(){
            super.start();
            robot.manipulator.moveArmTo(0);
            robot.manipulator.moveElevatorTo(0);
        }
        public void handle(){
            if(!active) return;
            super.handle();
            if(taskTime > 1 && !robot.manipulator.isMoving()) {
                stop();
            }
        }
    }


    class Flip extends RoboTask{
        Flip(){
        }
        public void start(){
            super.start();
            robot.drive.go(0, 0, 1);
        }
        public void handle(){
            if(!active) return;
            super.handle();
            // do the flip...
            if(taskTime > 2.0) {
                // System.out.printf("T=%1.2f: autopilot flipped\n", taskTime);
                robot.drive.go(0, 0, 0);
                stop();
            }
        }
    }

    class Turn extends RoboTask{
        double angle = 0;
        double maxTime = 0;
        Turn(double a, double mt){
            angle = a;
            maxTime = mt;
        }
        public void start(){
            super.start();
            robot.drive.enableAutoRotation();
            robot.drive.targetAngle += angle;
        }
        public void stop(){
            super.stop();
            robot.drive.disableAutoRotation();
        }
        public void handle(){
            if(!active) return;
            super.handle();
            double da = robot.drive.targetAngle - robot.drive.readGyro();
            double dabs = Math.abs(da);
            if(dabs < 3 || taskTime > maxTime) {
                stop();
            }
        }
    }

    // the autopilot code

    RoboTask subtask = null;
    int stage = 0;
    double fwdTime = 0;

    // main configuration variables - adjustable via the trimmer class

    public double stage0_time1y = 4.6;
    public double stage0_speed1y = 0.40;
    public double stage0_time2y = 15;
    public double stage0_speed2y = 0.17;

    public double stage2_time1y = 2.0;
    public double stage2_speed1y = -0.3;
    public double stage2_time2y = 3.0;
    public double stage2_speed2y = -0.2;

    public double stage6_time1y = 1.9;
    public double stage6_speed1y = 1.0;
    public double stage6_time2y = 1.0;
    public double stage6_speed2y = 0.2;

    public void start(){
        super.start();
        stage = 0;
        subtask = null;
    }

    public void handle(){
        if(taskTime > 30.0) {
            robot.drive.go(0, 0, 0);
            stop();
        }
        if(!active) return;
        super.handle();
        
        robot.drive.setLed(robot.lineTracker.lineVisible);

        if(subtask != null) subtask.handle();

        if(subtask == null || !subtask.active){
            // next task
            switch(stage){
                case 0:
                    // track to put on first ubertube
                    // notye: set fork time! (delays cross sensing till after the fork!
                    subtask = new Track();
                    ((Track)subtask).ignoreCrossUntil = 6;
                    ((Track)subtask).speed1 = stage0_speed1y;
                    ((Track)subtask).time1 = stage0_time1y;
                    ((Track)subtask).speed2 = stage0_speed2y;
                    ((Track)subtask).time2 = stage0_time2y;
                    ((Track)subtask).trackLeft = true;

                    // manipulator actions
                    ((Track)subtask).mTime = 0.5;
                    ((Track)subtask).peg = 6;

                    subtask.start();
                    stage++;
                    break;

                case 1:
                    // arm forward
                    subtask = new ArmForward();
                    subtask.start();
                    stage++;
                    break;


                    
                case 2:
                    // get the forward drive time
                    fwdTime = subtask.taskTime;
                    // move arm forward
                    subtask = new ReleaseTube();
                    subtask.start();
                    stage++;
                    break;


                    
                case 3:
                    // backtrack (no cross detection needed)
                    /*
                    subtask = new Track();
                    ((Track)subtask).speed1 = stage2_speed1y;
                    ((Track)subtask).time1 = stage2_time1y; 
                    ((Track)subtask).speed2 = stage2_speed2y;
                    ((Track)subtask).time2 = stage2_time1y;
                    ((Track)subtask).trackLeft = false;
                    ((Track)subtask).ignoreLineTime = 1;
                    ((Track)subtask).trackLastSeenSide = false;

                    // manipulator actions
                    ((Track)subtask).mTime = 2;
                    ((Track)subtask).elHeight = 0;
*/
                    // simple timer backtrack
                    subtask = new Move(-0.3, 0.0, 0.0, 2.0); // X Y R T
                    subtask.start();
                    stage++;
                    break;

                case 4:
                    // no movement - just elevator down
                    subtask = new ArmDown();
                    subtask.start();
                    stage++;
                    break;
                    
                case 5:
                    stage++;
                    break;
/*
                case 4:
                    // move forward and turn to grab the 2nd ubertube
                    subtask = new Move(0.3, 0, 0.0, 0.3);
                    subtask.start();
                    stage++;
                    break;
                case 5:
                    // turn 45 deg
                    subtask = new Turn(-45);
                    subtask.start();
                    stage++;
                    break;
                case 6:
                    // drive until line intercepted
                    subtask = new Track(stage6_speed1y + stage6_speed2y * 0.5, 30);
                    ((Track)subtask).speed1 = stage6_speed1y;
                    ((Track)subtask).time1 = stage6_time1y;
                    ((Track)subtask).speed2 = stage6_speed2y;
                    ((Track)subtask).time2 = stage6_time2y;
                    ((Track)subtask).trackLeft = true;
                    ((Track)subtask).flipTrackOnIntercept = true;
                    robot.manipulator.goToPeg(6);
                    subtask.start();
                    stage++;
                    stage = 8;
                    break;
                case 7:
                    //subtask = new Track(.7, 7);
                    //robot.drive.go(0.5, 0, 0);
                    //subtask.start();
                    stage++;
                    break;
                case 8:
                    subtask = new ReleaseTube();
                    subtask.start();
                    stage++;
                    break;
                case 9:
                    subtask = new Move(-0.3, 0, 0, 0.5);
                    subtask.start();
                    stage++;
                    stage = 20;
                    break;
                case 10:
                    // backtrack
                    subtask = new Track(0.5, fwdTime);
                    ((Track)subtask).speed1 = -0.6;
                    ((Track)subtask).time1 = 2; // we go slow first - same time as went forward
                    ((Track)subtask).speed2 = -1.0;
                    ((Track)subtask).time2 = 1;   // full speed for 3 sec, adjust for time lost due to acceleration
                    ((Track)subtask).trackLeft = true;
                    robot.manipulator.goToPeg(1);
                    subtask.start();
                    stage++;
                case 11:
                    break;
 */
                default:
                    stop();
            }
        }
    }

    void simulate(){

    }
}
