package me.javaproject.kitpvp.gui;

import me.javaproject.kitpvp.profile.Profile;
import me.joeleoli.nucleus.util.CC;
import me.joeleoli.nucleus.util.ItemBuilder;
import org.apache.commons.lang3.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GUI {

    public static Inventory settingsGUI(Player player) {

        Inventory toReturn = Bukkit.createInventory(null, 27, CC.GOLD + "Settings");

        int[] orangeSpots = {
                0,2,4,6,8,
                18,20,22,24,26
        };

        int[] whiteSpots = {
                1,3,5,7,
                9,11,13,15,17,
                19,21,23,25
        };

        final ItemStack orangeGlass = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(1).build();

        final ItemStack whiteGlass = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(0).build();

        for (int i : orangeSpots) {
            toReturn.setItem(i, orangeGlass);
        }

        for (int i : whiteSpots) {
            toReturn.setItem(i, whiteGlass);
        }

        final Profile profile = Profile.getByUuid(player.getUniqueId());

        final ItemStack scoreboard = new ItemBuilder(Material.ITEM_FRAME)
                .name(CC.GOLD + "Scoreboard")
                .lore(Arrays.asList(""
                ,profile.isScoreboard() == true ? ChatColor.BLUE + StringEscapeUtils.unescapeHtml4("&#9658;") + ChatColor.GREEN + " Enabled" : ChatColor.GRAY + " Disabled"
                ,profile.isScoreboard() == false ? ChatColor.BLUE + StringEscapeUtils.unescapeHtml4("&#9658;") + ChatColor.GREEN + " Disabled" : ChatColor.GRAY + " Enabled"
                )).build();

        final ItemStack autoDeposit = new ItemBuilder(Material.GOLD_INGOT)
                .name(CC.GOLD + "Auto Deposit")
                .lore(Arrays.asList(""
                        ,profile.isScoreboard() == true ? ChatColor.BLUE + StringEscapeUtils.unescapeHtml4("&#9658;") + ChatColor.GREEN + " Enabled" : ChatColor.GRAY + " Disabled"
                        ,profile.isScoreboard() == false ? ChatColor.BLUE + StringEscapeUtils.unescapeHtml4("&#9658;") + ChatColor.GREEN + " Disabled" : ChatColor.GRAY + " Enabled"
                )).build();

        toReturn.setItem(10, scoreboard);
        toReturn.setItem(12, autoDeposit);

        return toReturn;
    }

    public static void openSettings(Player player) {
        player.openInventory(settingsGUI(player));
    }

    public static Inventory kitGUI(Player player) {

        final Inventory toReturn = Bukkit.createInventory(null, 27, CC.GOLD + "Kit");

        int[] orangeSpots = {
                0,2,4,6,8,10,16,
                18,20,22,24,26
        };

        int[] whiteSpots = {
                1,3,5,7,9,17,
                19,21,23,25
        };

        final ItemStack orangeGlass = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(1).build();

        final ItemStack whiteGlass = new ItemBuilder(Material.STAINED_GLASS_PANE).durability(0).build();

        for (int i : orangeSpots) {
            toReturn.setItem(i, orangeGlass);
        }

        for (int i : whiteSpots) {
            toReturn.setItem(i, whiteGlass);
        }

        final ItemStack vipKIT = new ItemBuilder(Material.WOOL)
                .name(CC.GREEN + CC.BOLD + "VIP " + CC.WHITE + "Kit")
                .durability(5)
                .build();

        final ItemStack mvpKIT = new ItemBuilder(Material.WOOL)
                .name(CC.BLUE + CC.BOLD + "MVP " + CC.WHITE + "Kit")
                .durability(11)
                .build();

        final ItemStack proKIT = new ItemBuilder(Material.WOOL)
                .name(CC.GOLD + CC.BOLD + "PRO " + CC.WHITE + "Kit")
                .durability(1)
                .build();

        final ItemStack eliteKIT = new ItemBuilder(Material.WOOL)
                .name(CC.DARK_PURPLE + CC.BOLD + "Elite " + CC.WHITE + "Kit")
                .durability(10)
                .build();

        final ItemStack amberKIT = new ItemBuilder(Material.WOOL)
                .name(CC.GOLD + CC.BOLD + "Amber " + CC.WHITE + "Kit")
                .durability(4)
                .build();

        toReturn.setItem(11, vipKIT);
        toReturn.setItem(12, mvpKIT);
        toReturn.setItem(13, proKIT);
        toReturn.setItem(14, eliteKIT);
        toReturn.setItem(15, amberKIT);

        return toReturn;
    }

    public static void openKits(Player player) {
        player.openInventory(kitGUI(player));
    }
}
