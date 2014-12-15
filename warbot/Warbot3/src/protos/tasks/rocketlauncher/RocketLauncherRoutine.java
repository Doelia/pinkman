package protos.tasks.rocketlauncher;

import java.util.List;

import protos.WarRocketLauncherBrainController;
import protos.communication.ProtosCommunication;
import protos.communication.messages.TargetMessageWrapper;
import protos.tasks.ResultTask;
import protos.tasks.Task;
import edu.turtlekit3.warbot.agents.agents.WarRocketLauncher;
import edu.turtlekit3.warbot.agents.percepts.WarPercept;
import edu.turtlekit3.warbot.agents.projectiles.WarRocket;
import edu.turtlekit3.warbot.agents.resources.WarFood;
import edu.turtlekit3.warbot.brains.WarBrainController;
import edu.turtlekit3.warbot.communications.WarMessage;
import edu.turtlekit3.warbot.tools.CoordPolar;

public class RocketLauncherRoutine extends Task
{
	private static Task instance;
	public static Task getInstance() {
		if (instance == null) {
			synchronized (RocketLauncherRoutine.class) {
				if (instance == null) {
					instance = new RocketLauncherRoutine();
				}
			}
		}
		return instance;
	}
	
	@Override
	public ResultTask execute(WarBrainController wa)
	{
		WarRocketLauncherBrainController w = (WarRocketLauncherBrainController) wa;
		
		if(w.getBrain().getHealth() < WarRocketLauncher.MAX_HEALTH*0.5)
		{
			
			if(w.getBrain().isBagEmpty())
				w.getBrain().broadcastMessageToAll(ProtosCommunication.ASK_FOR_FOOD);
			else
				return new ResultTask(this, WarRocketLauncher.ACTION_EAT);
		}
			
		
		
		w.getBrain().setDebugString("Task:AttackBase");
		List<WarMessage> l = w.getBrain().getMessages();
		WarMessage wmMostNearButNotInRange = null;
		double currentDist = -1;
		if(w.getBrain().isBlocked())
			w.getBrain().setRandomHeading();
		if(l!=null & !l.isEmpty())
		{
			for(WarMessage wm :l)
			{
				if(wm.getMessage().equals(ProtosCommunication.INFORM_ENEMY_BASE))
				{
					TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wm);
					//FROM EXPLORE SO
					CoordPolar cp = tmw.compute();
					w.getBrain().setDebugString(""+cp.getAngle());
					w.getBrain().setHeading(cp.getAngle());
				//	w.getBrain().get
					
					if(cp.getDistance() <= WarRocket.RANGE)
					{//IN RANGE
						if(cp.getDistance() > 2*WarRocket.EXPLOSION_RADIUS)
						{ // NO RISK OF EXPLOSION EFFECT EXCEPT SPECIAL CASE
							if(w.getBrain().isReloaded())
							{
								//TODO RAYCAST
								;
								for(WarPercept wp :w.getBrain().getPerceptsAllies())
								{
									double diff = w.getBrain().getHeading()-wp.getAngle();
									
									if(Math.abs(diff) < 5)//isMoreOrLessInShootingCast(w.getBrain(),wp))
									{
										double h = wp.getAngle();
										if(diff<0)
											h+=Math.abs(diff);
										else
											h-=Math.abs(diff);
										
										w.getBrain().setHeading(h);
										return new ResultTask(this,WarRocketLauncher.ACTION_MOVE);
									}
								}
								
								return new ResultTask(this,WarRocketLauncher.ACTION_FIRE);
							}
							else
							{	if(!w.getBrain().isReloading())
									return new ResultTask(this, WarRocketLauncher.ACTION_RELOAD);
								else
								{
									if(!w.getBrain().isBlocked())
										w.getBrain().setHeading(w.getBrain().getHeading()+90);
									else
										w.getBrain().setHeading(w.getBrain().getHeading());
									return new ResultTask(this,WarRocketLauncher.ACTION_MOVE);
								}
							}
						}
						else
						{
							w.getBrain().setHeading(w.getBrain().getHeading()+45);
							return new ResultTask(this,WarRocketLauncher.ACTION_MOVE);
						}
					}
					if(currentDist==-1 || currentDist> cp.getDistance())
					{
					currentDist = cp.getDistance();
					wmMostNearButNotInRange=wm;
					}
				}
			}
			if(wmMostNearButNotInRange!=null)
			{
				TargetMessageWrapper tmw = TargetMessageWrapper.unwrap(wmMostNearButNotInRange);
				CoordPolar cp = tmw.compute();
				w.getBrain().setHeading(cp.getAngle());
				return new ResultTask(this,WarRocketLauncher.ACTION_MOVE);
			}
			else
			{
				w.getBrain().broadcastMessage("HQ", "HQ", ProtosCommunication.ASK_ENEMY_BASE_POSITION);
				
				return new ResultTask(this, WarRocketLauncher.ACTION_MOVE);
			}
		}
		//SINON 
		//ON DEMANDE
		if(w.getBrain().isBlocked())
			w.getBrain().setRandomHeading();
		return new ResultTask(this, WarRocketLauncher.ACTION_MOVE);
	}
	/*
	 * 

	 */
	
}