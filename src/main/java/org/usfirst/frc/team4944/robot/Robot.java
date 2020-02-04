package org.usfirst.frc.team4944.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team4944.robot.custom.XboxController;
import org.usfirst.frc.team4944.robot.subsystems.DriveSystem;
import org.usfirst.frc.team4944.robot.subsystems.HopperSubsystem;
import org.usfirst.frc.team4944.robot.subsystems.IntakeSubsystem;
import org.usfirst.frc.team4944.robot.subsystems.ShooterSubsystem;
import org.usfirst.frc.team4944.robot.subsystems.TurretSubsystem;


public class Robot extends TimedRobot {
	// SERIAL PORT
	SerialPort serial;
	// CONTROLLERS
	XboxController driver;
	XboxController operator;
	// SUBSYSTEMS
	OI oi;
	DriveSystem driveSystem;
	TurretSubsystem turret;
	ShooterSubsystem shooter;
	IntakeSubsystem intake;
	HopperSubsystem hopper;
	//SmartDashboard Values
	double turretEncoder;

	@Override
	public void robotInit() {
		// CONTROLLERS INIT
		this.driver = new XboxController(0);
		this.operator = new XboxController(1);
		
		// SUBSYSTEMS INIT
		this.oi = new OI();
		this.turret = new TurretSubsystem();
		this.shooter = new ShooterSubsystem();
		this.driveSystem = new DriveSystem();
		this.hopper = new HopperSubsystem();
		this.intake = new IntakeSubsystem();
		
		//SmartDashboard
		this.SmartDashboardDisplay();
	}

	@Override
	public void disabledInit() {
		this.SmartDashboardDisplay();
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run(); // KEEP HERE TO RUN COMMANDS
		this.SmartDashboardDisplay();
	}

	@Override
	public void autonomousInit() {
		this.SmartDashboardDisplay();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run(); // KEEP HERE TO RUN COMMANDS
		this.updateValues();
	}

	@Override
	public void teleopInit() {
		this.SmartDashboardDisplay();
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run(); // KEEP HERE TO RUN COMMANDS
		// Drive Code
		double Y = -driver.getLeftStickY();
		double X = driver.getRightStickX();
		this.driveSystem.setPower(X + Y, X - Y);
/*
		if(this.driver.getAButton()){
			this.shooter.setManualShooterPower(0.7);
			this.hopper.setFeedMotor(0.75);
			this.hopper.setHopperMotor(0.75);
		}else{
			this.shooter.setManualShooterPower(0);
			this.hopper.setFeedMotor(0);
			this.hopper.setHopperMotor(0);
		}
		if(this.driver.getBButton()){
			this.intake.setIntakeMotor(0.75);
		}else{
			this.intake.setIntakeMotor(0);
		}
*/
		// Update Values
		this.updateValues();
	}

	@Override
	public void testPeriodic() {
	}

	public void updateValues(){
		turret.followLimelight(); // Uses the limelight to change the set points on the turret
		//turret.driveTurretPID(); // Drives the turret motors based off of the current set point
		this.SmartDashboardDisplay(); // Displays all Smartdashboard Values
	}

	public void SmartDashboardDisplay(){
		// Turret
		SmartDashboard.putNumber("Turret SetPoint", turret.getTurretSetPoint());
		SmartDashboard.putNumber("Turret Encoder", turret.getTurretEncoderValue());
		SmartDashboard.putNumber("Turret Power", turret.getTurretPower());
		SmartDashboard.putNumber("Hood Angle", turret.getHoodAngle());
		// Limelight
		SmartDashboard.putNumber("Limelight Y Offset", turret.lm.getYOffset());
		SmartDashboard.putNumber("Limelight X Offset", turret.lm.getXOffset());
		SmartDashboard.putNumber("Distance From Target", turret.lm.getDistInFeet());
		SmartDashboard.putBoolean("Limelight Connection:", turret.lm.getLimeLightConnected());
	}
}
