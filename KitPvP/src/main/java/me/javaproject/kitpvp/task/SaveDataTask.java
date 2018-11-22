package me.javaproject.kitpvp.task;

import me.javaproject.kitpvp.profile.Profile;
import me.javaproject.kitpvp.team.Team;

public class SaveDataTask implements Runnable {

    @Override
    public void run() {
        for (Profile profile : Profile.getCached().values()) {
            profile.save();
        }

        for (Team team : Team.getTeams()) {
            team.save();
        }
    }

}
