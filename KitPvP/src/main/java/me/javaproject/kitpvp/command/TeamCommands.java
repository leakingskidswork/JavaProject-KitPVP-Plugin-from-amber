package me.javaproject.kitpvp.command;

import me.javaproject.kitpvp.profile.Profile;
import me.javaproject.kitpvp.team.Team;
import me.javaproject.kitpvp.team.type.PlayerTeam;
import me.joeleoli.nucleus.command.Command;
import me.joeleoli.nucleus.command.param.Parameter;
import me.joeleoli.nucleus.player.PlayerInfo;
import me.joeleoli.nucleus.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Set;

public class TeamCommands {

    @Command(names = {"team create", "t create"})
    public static void createTeam(Player player, @Parameter(name = "teamName") String teamName) {

        final Profile profile = Profile.getByUuid(player.getUniqueId());

        final PlayerTeam selfTeam = PlayerTeam.getByPlayerName(player.getName());

        if (Team.getByName(teamName) != null) {
            player.sendMessage(CC.RED + "A team with that name already exists!");
            return;
        }

        if (teamName.length() > 16) {
            player.sendMessage(CC.RED + "Try a shorter team name!");
            return;
        }

        if (teamName.length() < 3) {
            player.sendMessage(CC.RED + "Try a bigger team name!");
            return;
        }

        if (selfTeam != null) {
            player.sendMessage(CC.RED + "You are already on a team!");
            return;
        }

        final Team playerTeam = new PlayerTeam(teamName, null, player.getUniqueId());

        profile.setTeam((PlayerTeam) playerTeam);

        Bukkit.broadcastMessage(player.getDisplayName() + CC.GREEN + " has created a new team: " + CC.WHITE + playerTeam.getName());

    }

    @Command(names = {"team show", "team who", "team info", "team i", "t show", "t who", "t info", "t i"})
    public static void teamShow(Player player, @Parameter(name = "teamName") String teamName) {

        final Set<Team> playerTeams = Team.getAllByString(teamName);

        if (playerTeams.isEmpty()) {
            player.sendMessage(CC.RED + "Faction not found.");
            return;
        }

        for (Team team : playerTeams) {

            if (team instanceof PlayerTeam) {

                final PlayerTeam playerTeam = (PlayerTeam) team;

                final Player leader = Bukkit.getPlayer(playerTeam.getLeader());

                player.sendMessage(CC.SCOREBAORD_SEPARATOR);
                player.sendMessage(CC.BLUE + playerTeam.getName());
                player.sendMessage(CC.YELLOW + "Leader: " + (leader.isOnline() ? CC.GREEN + leader.getName() : CC.GRAY + leader.getName()));
                player.sendMessage(CC.SCOREBAORD_SEPARATOR);
            }
        }
    }

    @Command(names = {"team invite", "t invite"})
    public static void teamInvite(Player player, @Parameter(name = "target") PlayerInfo playerInfo) {

        final Profile targetProfile = Profile.getByUuid(playerInfo.getUuid());

        if (targetProfile == null) {
            player.sendMessage(CC.RED + "Couldn't find a player with the name " + CC.RESET + playerInfo.getName() + CC.RED + ". Have they joined the network?");
            return;
        }

        if (!targetProfile.toPlayer().isOnline()) {
            player.sendMessage(CC.RED + "Player is offline.");
            return;
        }

        if (!targetProfile.isLoaded()) {
            targetProfile.load();
        }

        final PlayerTeam playerTeam = PlayerTeam.getByPlayerName(player.getName());

        if (playerTeam == null) {
            player.sendMessage(CC.RED + "You are not on a team!");
            return;
        }

        if (playerTeam.getLeader() != player.getUniqueId()) {
            player.sendMessage(CC.RED + "You must be a leader to invite players to the team.");
            return;
        }

        playerTeam.getInvitedPlayers().put(targetProfile.getUuid(), player.getUniqueId());
        playerTeam.sendMessage(player.getDisplayName() + CC.GREEN + " has invited " + targetProfile.getDisplayName() + CC.GREEN + " to the team!");
        playerInfo.toPlayer().sendMessage(CC.GREEN + "You have been invited to join the team " + CC.WHITE + playerTeam.getName() + CC.GREEN + "!");
    }

    @Command(names = {"team join", "t join"})
    public static void joinTeam(Player player, @Parameter(name = "teamName") String teamName) {

        final Profile profile = Profile.getByUuid(player.getUniqueId());

        final PlayerTeam selfTeam = PlayerTeam.getByPlayerName(player.getName());

        final PlayerTeam playerTeam = (PlayerTeam) PlayerTeam.getByName(teamName);

        if (selfTeam != null) {
            player.sendMessage(CC.RED + "You are already on a team!");
            return;
        }

        if (!playerTeam.getInvitedPlayers().containsKey(player.getUniqueId())) {
            player.sendMessage(CC.RED + "This team has not invited you!");
            return;
        }

        if (playerTeam == null) {
            player.sendMessage(CC.RED + "Team not found.");
            return;
        }

        playerTeam.getInvitedPlayers().remove(player.getUniqueId());
        playerTeam.getMembers().add(player.getUniqueId());
        playerTeam.sendMessage(player.getDisplayName() + CC.GREEN + " has joined the team!");
        profile.setTeam(playerTeam);
    }
}




