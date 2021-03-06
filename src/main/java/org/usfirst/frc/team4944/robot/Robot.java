package org.usfirst.frc.team4944.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team4944.robot.custom.XboxController;
import org.usfirst.frc.team4944.robot.subsystems.ArmSubsystem;
import org.usfirst.frc.team4944.robot.subsystems.DriveSystem;
import org.usfirst.frc.team4944.robot.subsystems.HoodSubsystem;
import org.usfirst.frc.team4944.robot.subsystems.HopperSubsystem;
import org.usfirst.frc.team4944.robot.subsystems.IntakeSubsystem;
import org.usfirst.frc.team4944.robot.subsystems.ShooterSubsystem;
import org.usfirst.frc.team4944.robot.subsystems.TurretSubsystem;
import org.usfirst.frc.team4944.robot.subsystems.WenchSubsystem;

public class Robot extends TimedRobot {

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
	HoodSubsystem hood;
	ArmSubsystem arms;
	WenchSubsystem wench;

	// SmartDashboard Values

	double turretEncoder;
	double shooterPower;

	@Override
	public void robotInit() {
		// CONTROLLERS INIT
		this.driver = new XboxController(0);
		this.operator = new XboxController(1);

		// SUBSYSTEMS INIT
		this.arms = new ArmSubsystem();
		this.turret = new TurretSubsystem();
		this.shooter = new ShooterSubsystem();
		this.driveSystem = new DriveSystem();
		this.hopper = new HopperSubsystem();
		this.intake = new IntakeSubsystem();
		this.hood = new HoodSubsystem();
		this.oi = new OI();

		// SmartDashboard
		SmartDashboard.putNumber("Shooter Power", this.shooterPower);
		this.SmartDashboardDisplay();

		// Double
		this.shooterPower = 0.5;
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

		final double Y = -driver.getLeftStickY();
		final double X = driver.getRightStickX();
		this.driveSystem.setPower(X + Y, X - Y);

		// Update Values
		this.updateValues();
	}

	@Override
	public void testPeriodic() {
	}

	public void updateValues() {

		// SETS SHOOTER TO DESIRED SPEED

		this.shooterPower = SmartDashboard.getNumber("Shooter Power", 0);
		if (this.driver.getLeftTriggerDown()) {
			this.shooter.setManualShooterPower(this.shooterPower);
		} else {
			this.shooter.setManualShooterPower(0);
		}

		// RIGHT MENU LOCK ON TURRET/HOOD

		if (this.driver.getRightMenu()) {
			this.turret.followLimelightNoEncoder();
			this.hood.updateValues();
			this.hood.setAngleByLM();
			this.hood.driveHoodPID();
		} else {
			this.turret.setTurretMotorPower(0);
			this.hood.setHoodMotorPower(0);
		}

		// SMARTDASHBOARD

		// Displays all Smartdashboard Values

		this.SmartDashboardDisplay();
	}

	public void SmartDashboardDisplay() {

		// Turret

		// SmartDashboard.putNumber("Turret SetPoint", this.turret.getTurretSetPoint());
		// SmartDashboard.putNumber("Turret Encoder",
		// this.turret.getTurretEncoderValue());
		// SmartDashboard.putNumber("Turret Power", this.turret.getTurretPower());

		// Hood

		SmartDashboard.putNumber("Hood Encoder", this.hood.getHoodEncoderValue());
		SmartDashboard.putNumber("Hood SetPoint", this.hood.getHoodSetPoint());
		SmartDashboard.putNumber("Hood Power", this.hood.getHoodMotorPower());
		SmartDashboard.putNumber("Set Hood Angle", this.hood.getRequiredAngle());

		// Calculated Values

		// SmartDashboard.putNumber("Vx", this.hood.getVx());
		// SmartDashboard.putNumber("Vy", this.hood.getVy());

		// Limelight

		// SmartDashboard.putNumber("Limelight Y Offset", turret.lm.getYOffset());
		// SmartDashboard.putNumber("Limelight X Offset", turret.lm.getXOffset());
		SmartDashboard.putNumber("Distance From Target", turret.lm.getDistInFeet());
		SmartDashboard.putBoolean("Limelight Connection:", turret.lm.getLimeLightConnected());
	}
}