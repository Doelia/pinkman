package edu.turtlekit3.warbot.teams.doe.environnement;

import java.util.ArrayList;

public class TeamManager {
	private ArrayList<Team> teams;
	
	public TeamManager() {
		teams = new ArrayList<Team>();
	}
	
	public Team getTeamOf(int id) throws NoTeamFoundException {
		for (Team team : teams) {
			if(team.contains(id)) {
				return team;
			}
		}
		throw new NoTeamFoundException();
	}
	
	public void affectTeamTo(int brainId) {
		for (Team team : teams) {
			if(team.getSize() < team.getMaxSize()) {
				team.addMember(brainId);
				return;
			}
		}
		createTeam(brainId);
	}
	
	public Team createTeam(int brainId) {
		Team team = new Team();
		team.addMember(brainId);
		teams.add(team);
		return team;
	}
}
