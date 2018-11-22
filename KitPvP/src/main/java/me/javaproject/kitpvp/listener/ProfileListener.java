package me.javaproject.kitpvp.listener;

import me.javaproject.kitpvp.profile.Profile;
import me.joeleoli.nucleus.Nucleus;
import me.joeleoli.nucleus.nametag.NameTagHandler;
import me.joeleoli.nucleus.player.PlayerData;
import me.joeleoli.nucleus.util.CC;
import me.joeleoli.nucleus.util.Style;
import me.joeleoli.nucleus.uuid.UUIDCache;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ProfileListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {

        try {
            final Profile profile = Profile.getByUuid(event.getUniqueId());

            profile.setName(event.getName());

            if (!profile.isLoaded()) {
                profile.load();
            }

            if (!profile.isLoaded()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(Style.API_FAILED);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(Style.API_FAILED);
            return;
        }

        if (Nucleus.getInstance().getNucleusJedis().isActive()) {
            UUIDCache.update(event.getName(), event.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        final Player player = event.getPlayer();

        final PlayerData playerData = PlayerData.getByUuid(player.getUniqueId());

        player.setPlayerListName(playerData.getActiveRank().getColor() + player.getName());

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        final Player player = event.getEntity();

        final Profile profile = Profile.getByUuid(player.getUniqueId());

        event.setDeathMessage(null);


        if (profile.getKillStreak() > 0) {
            profile.setKillStreak(0);
        }

        profile.setDeaths(profile.getDeaths() + 1);

        if (event.getEntity().getKiller() != null) {

            final Player killer = event.getEntity().getKiller();

            final Profile killerProfile = Profile.getByUuid(killer.getUniqueId());

            killer.sendMessage(CC.GOLD + "You have earned " + CC.WHITE + "$5" +CC.GOLD + " for killing " + player.getName() + CC.GOLD + "!");

            killerProfile.setBalance(profile.getBalance() + 5);
            killerProfile.setKills(killerProfile.getKills() + 1);
            killerProfile.setKillStreak(killerProfile.getKillStreak() + 1);
        }
    }
}
