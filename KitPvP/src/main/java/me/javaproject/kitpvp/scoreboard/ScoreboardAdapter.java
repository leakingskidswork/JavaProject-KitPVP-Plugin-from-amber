package me.javaproject.kitpvp.scoreboard;

import me.javaproject.kitpvp.KitPvP;
import me.javaproject.kitpvp.profile.Profile;
import me.javaproject.kitpvp.util.Cooldown;
import me.joeleoli.nucleus.board.Board;
import me.joeleoli.nucleus.board.BoardAdapter;
import me.joeleoli.nucleus.util.CC;
import me.joeleoli.nucleus.util.Style;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter implements BoardAdapter {

    @Override
    public String getTitle(Player player) {
        return Style.translate(KitPvP.getInstance().getScoreboardConfigFile().getConfig().getString("TITLE"));
    }

    @Override
    public List<String> getScoreboard(Player player, Board board) {

        List<String> toReturn = KitPvP.getInstance().getScoreboardConfigFile().getConfig().getStringList("LINES");

        final Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile.isScoreboard()) {

            toReturn.add(Style.BORDER_LINE_SCOREBOARD);

            if (Cooldown.isOnCooldown("SPAWN_TAG", player)) {
                toReturn.add(CC.GOLD + "Spawn Tag: " + CC.WHITE + this.format("SPAWN_TAG", player));
            }

            if (Cooldown.isOnCooldown("ENDER_PEARL", player)) {
                toReturn.add(CC.GOLD + "Enderpearl: " + CC.WHITE + this.format("ENDER_PEARL", player));
            }

            if (Cooldown.isOnCooldown("APPLE", player)) {
                toReturn.add(CC.GOLD + "Apple: " + CC.WHITE + this.format("APPLE", player));
            }

            if (!Cooldown.isOnCooldown("SPAWN_TAG", player)) {
                toReturn.add(CC.GOLD + "Kills: " + CC.WHITE + profile.getKills());
                toReturn.add(CC.GOLD + "Deaths: " + CC.WHITE + profile.getDeaths());
                toReturn.add(CC.GOLD + "Killstreak: " + CC.WHITE + profile.getKillStreak());
                toReturn.add(CC.GOLD + "KDR: " + CC.WHITE + profile.getKdr());
                toReturn.add(CC.GOLD + "Balance: " + CC.WHITE + profile.getBalance());
                toReturn.add(CC.GOLD + "Level: " + CC.WHITE + profile.getLevel());
            }

            toReturn.add(Style.BORDER_LINE_SCOREBOARD);

        }

        return toReturn;

    }

    @Override
    public long getInterval() {
        return 2L;
    }

    @Override
    public void onScoreboardCreate(Player player, Scoreboard scoreboard) {

    }

    @Override
    public void preLoop() {

    }

    public String format(String cooldownName, Player player) {
        return String.valueOf(new DecimalFormat("0.0").format(Cooldown.getCooldownForPlayerLong(cooldownName, player) / 1000.0) + "s");
    }
}
