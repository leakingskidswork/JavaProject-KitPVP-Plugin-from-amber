package me.javaproject.kitpvp.command;

import me.javaproject.kitpvp.gui.GUI;
import me.javaproject.kitpvp.profile.Profile;
import me.joeleoli.nucleus.command.Command;
import me.joeleoli.nucleus.command.param.Parameter;
import me.joeleoli.nucleus.player.PlayerInfo;
import me.joeleoli.nucleus.util.CC;
import org.bukkit.entity.Player;

public class GeneralCommands {

    @Command(names = "stats")
    public static void stats(Player player) {

        final Profile profile = Profile.getByUuid(player.getUniqueId());

        player.sendMessage(CC.GOLD + "Kills: " + CC.WHITE + profile.getKills());
        player.sendMessage(CC.GOLD + "Deaths: " + CC.WHITE + profile.getDeaths());
        player.sendMessage(CC.GOLD + "Killstreak: " + CC.WHITE + profile.getKillStreak());
        player.sendMessage(CC.GOLD + "KDR: " + CC.WHITE + profile.getKdr());
        player.sendMessage(CC.GOLD + "Balance: " + CC.WHITE + profile.getBalance());
        player.sendMessage(CC.GOLD + "Level: " + CC.WHITE + profile.getLevel());

    }

    @Command(names = "stats")
    public static void statsPlayer(Player player, @Parameter(name = "target")PlayerInfo playerInfo) {

        final Profile profile = Profile.getByUuid(playerInfo.getUuid());

        if (profile == null) {
            player.sendMessage(CC.RED + "Player not found.");
            return;
        }

        player.sendMessage(playerInfo.getDisplayName() + CC.GOLD + "'s Stats:");
        player.sendMessage(CC.GOLD + "Kills: " + CC.WHITE + profile.getKills());
        player.sendMessage(CC.GOLD + "Deaths: " + CC.WHITE + profile.getDeaths());
        player.sendMessage(CC.GOLD + "Killstreak: " + CC.WHITE + profile.getKillStreak());
        player.sendMessage(CC.GOLD + "KDR: " + CC.WHITE + profile.getKdr());
        player.sendMessage(CC.GOLD + "Balance: " + CC.WHITE + profile.getBalance());
        player.sendMessage(CC.GOLD + "Level: " + CC.WHITE + profile.getLevel());

    }

    @Command(names = "settings")
    public static void settings(Player player) {
        GUI.openSettings(player);
    }

    @Command(names = "kit")
    public static void kit(Player player) {
        GUI.openKits(player);
    }
}
