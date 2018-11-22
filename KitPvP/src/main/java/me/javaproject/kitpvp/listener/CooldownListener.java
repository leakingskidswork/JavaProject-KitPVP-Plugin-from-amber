package me.javaproject.kitpvp.listener;

import me.javaproject.kitpvp.util.Cooldown;
import me.joeleoli.nucleus.util.CC;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.text.DecimalFormat;

public class CooldownListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        final Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

            final Material material = event.getMaterial();

            final Player player = event.getPlayer();

            if (material == Material.ENDER_PEARL && material != null && player.getGameMode() != GameMode.CREATIVE) {

                if (!Cooldown.isOnCooldown("ENDER_PEARL", player)) {
                    Cooldown.addCooldown("ENDER_PEARL", player, 16);
                } else if (Cooldown.isOnCooldown("ENDER_PEARL", player)) {
                    player.sendMessage(CC.RED + "You cannot use this for another " + CC.BOLD + String.valueOf(new DecimalFormat("0.0").format(Cooldown.getCooldownForPlayerLong("ENDER_PEARL", player) / 1000.0) + "s") + CC.RED + ".");
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {

        final Player player = event.getPlayer();

        final Material material = event.getItem().getType();

        if (material == Material.GOLDEN_APPLE && material != null) {
            if (!Cooldown.isOnCooldown("APPLE", player)) {
                Cooldown.addCooldown("APPLE", player, 2);
            } else if (Cooldown.isOnCooldown("APPLE", player)) {
                player.sendMessage(CC.RED + "You cannot use this for another " + CC.BOLD + String.valueOf(new DecimalFormat("0.0").format(Cooldown.getCooldownForPlayerLong("APPLE", player) / 1000.0) + "s") + CC.RED + ".");
                event.setCancelled(true);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {

            final Player player = (Player) event.getEntity();

            final Player damager = (Player) event.getDamager();

            if (!Cooldown.isOnCooldown("SPAWN_TAG", player)) {
                player.sendMessage(CC.YELLOW + "You are now spawn-tagged for " + CC.RED + 30 + CC.YELLOW + " seconds.");
                Cooldown.addCooldown("SPAWN_TAG", player, 30);
            } else {
                Cooldown.addCooldown("SPAWN_TAG", player, 30);
            }

            if (!Cooldown.isOnCooldown("SPAWN_TAG", damager)) {
                damager.sendMessage(CC.YELLOW + "You are now spawn-tagged for " + CC.RED + 30 + CC.YELLOW + " seconds.");
                Cooldown.addCooldown("SPAWN_TAG", damager, 30);
            } else {
                Cooldown.addCooldown("SPAWN_TAG", damager, 30);
            }

        }
    }
}