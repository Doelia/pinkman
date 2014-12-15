package doe.teams;

import java.util.ArrayList;

import doe.exceptions.NoTeamFoundException;


public class TeamManager {
	
	private ArrayList<Group> teams;
	
	public TeamManager() {
		teams = new ArrayList<Group>();
	}
	
	public Group getTeamOf(int id) throws NoTeamFoundException {
		for (Group team : teams) {
			if(team.contains(id)) {
				return team;
			}
		}
		throw new NoTeamFoundException();
	}
	
	public int size() {
		return teams.size();
	}
	
	public void affectTeamTo(int brainId) {
		for (Group team : teams) {
			if(team.getSize() < team.getMaxSize() && !team.isReady()) {
				team.addMember(brainId);
				return;
			}
		}
		createTeam(brainId);
	}
	
	public Group createTeam(int brainId) {
		Group team = new Group();
		team.addMember(brainId);
		teams.add(team);
		team.setTeamIndex(teams.indexOf(team));
		return team;
	}

	public void remove(Integer id) {
		for (Group team : teams) {
			team.removeMember(id);
		}
	}

	public void setBaseAttacked(boolean b) {
		for (Group group : teams) {
			group.setBaseAttacked(b);
		}
	}

	public int getIndexOfTeam(Group t) {
		return teams.indexOf(t);
	}
	

}
