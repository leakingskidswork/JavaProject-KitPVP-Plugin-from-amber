package me.javaproject.kitpvp.listener;

import com.customhcf.base.BasePlugin;
import com.customhcf.base.kit.Kit;
import me.javaproject.kitpvp.gui.GUI;
import me.javaproject.kitpvp.profile.Profile;
import me.joeleoli.nucleus.util.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIListener implements Listener {

    @EventHandler
    public void onSettingsClick(InventoryClickEvent event) {

        final Player player = (Player) event.getWhoClicked();

        final Profile profile = Profile.getByUuid(player.getUniqueId());

        if (event.getInventory() != null && event.getInventory().getTitle().equalsIgnoreCase(GUI.settingsGUI(player).getTitle())) {
            event.setCancelled(true);

            if (event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                return;
            }

            if (event.getCurrentItem().getType() == Material.ITEM_FRAME) {
                profile.setScoreboard(!profile.isScoreboard());
                player.closeInventory();
                GUI.openSettings(player);
            } else if (event.getCurrentItem().getType() == Material.GOLD_INGOT) {
                profile.setAutoDeposit(!profile.isAutoDeposit());
                player.closeInventory();
                GUI.openSettings(player);
            }

        } else if (event.getInventory() != null && event.getInventory().getTitle().equalsIgnoreCase(GUI.kitGUI(player).getTitle())) {
            event.setCancelled(true);

            if (event.getCurrentItem().getType() == Material.STAINED_GLASS_PANE) {
                return;
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("VIP")) {

                final Kit kit = BasePlugin.getPlugin().getKitManager().getKit("VIP");

                if (kit == null) {
                    player.sendMessage(CC.RED + "Kit not found.");
                    return;
                }

                player.getInventory().setArmorContents(null);
                player.getInventory().clear();
                player.closeInventory();

                kit.applyTo(player, true, false);
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("MVP")) {

                final Kit kit = BasePlugin.getPlugin().getKitManager().getKit("MVP");

                if (kit == null) {
                    player.sendMessage(CC.RED + "Kit not found.");
                    return;
                }

                player.getInventory().setArmorContents(null);
                player.getInventory().clear();
                player.closeInventory();

                kit.applyTo(player, true, false);
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("PRO")) {

                final Kit kit = BasePlugin.getPlugin().getKitManager().getKit("PRO");

                if (kit == null) {
                    player.sendMessage(CC.RED + "Kit not found.");
                    return;
                }

                player.getInventory().setArmorContents(null);
                player.getInventory().clear();
                player.closeInventory();

                kit.applyTo(player, true, false);
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Elite")) {

                final Kit kit = BasePlugin.getPlugin().getKitManager().getKit("Elite");

                if (kit == null) {
                    player.sendMessage(CC.RED + "Kit not found.");
                    return;
                }

                player.getInventory().setArmorContents(null);
                player.getInventory().clear();
                player.closeInventory();

                kit.applyTo(player, true, false);
            }

            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Amber")) {

                final Kit kit = BasePlugin.getPlugin().getKitManager().getKit("Amber");

                if (kit == null) {
                    player.sendMessage(CC.RED + "Kit not found.");
                    return;
                }

                player.getInventory().setArmorContents(null);
                player.getInventory().clear();
                player.closeInventory();

                kit.applyTo(player, true, false);
            }
        }
    }
}
