package edu.turtlekit3.warbot.teams.noobteam;

import edu.turtlekit3.warbot.agents.agents.WarEngineer;
import edu.turtlekit3.warbot.brains.braincontrollers.WarEngineerAbstractBrainController;

public class WarEngineerBrainController extends WarEngineerAbstractBrainController {
	
	public WarEngineerBrainController() {
		super();
	}

	@Override
	public String action() {
		// Develop behaviour here
		
		return WarEngineer.ACTION_CREATE;
		
		/*
		if (getBrain().isBlocked())
			getBrain().setRandomHeading();
		return WarEngineer.ACTION_MOVE;*/
	}
}
