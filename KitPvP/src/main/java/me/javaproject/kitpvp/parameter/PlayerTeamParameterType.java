package me.javaproject.kitpvp.parameter;

import me.javaproject.kitpvp.team.Team;
import me.javaproject.kitpvp.team.type.PlayerTeam;
import me.joeleoli.nucleus.command.param.ParameterType;
import me.joeleoli.nucleus.rank.Rank;
import me.joeleoli.nucleus.util.CC;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PlayerTeamParameterType implements ParameterType<PlayerTeam> {

    public PlayerTeam transform(CommandSender sender, String source) {
        PlayerTeam team = (PlayerTeam) Team.getByName(source);

        if (!(team instanceof PlayerTeam)) {
            sender.sendMessage(CC.RED + "That is not a player team.");
            return null;
        }

        if (team == null) {
            sender.sendMessage(CC.RED + "A rank with that name could not be found.");
            return null;
        }

        return team;
    }

    public List<String> tabComplete(Player sender, Set<String> flags, String source) {
        List<String> completions = new ArrayList<>();

        Rank.getRanks().forEach(rank -> {
            if (StringUtils.startsWithIgnoreCase(rank.getName(), source)) {
                completions.add(rank.getName());
            }
        });

        return (completions);
    }
}
