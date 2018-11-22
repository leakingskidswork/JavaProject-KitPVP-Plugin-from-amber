package me.javaproject.kitpvp;


import lombok.Getter;
import me.javaproject.kitpvp.mongo.Mongo;
import me.javaproject.kitpvp.parameter.PlayerTeamParameterType;
import me.javaproject.kitpvp.scoreboard.ScoreboardAdapter;
import me.javaproject.kitpvp.task.SaveDataTask;
import me.javaproject.kitpvp.util.Cooldown;
import me.javaproject.kitpvp.util.SimpleOfflinePlayer;
import me.joeleoli.nucleus.Nucleus;
import me.joeleoli.nucleus.board.BoardManager;
import me.joeleoli.nucleus.command.CommandHandler;
import me.joeleoli.nucleus.config.FileConfig;
import me.joeleoli.nucleus.listener.ListenerHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class KitPvP extends JavaPlugin {

    @Getter private static KitPvP instance;

    @Getter private Mongo mongo;

    @Getter private FileConfig mainConfigFile;

    @Getter private FileConfig langConfigFile;

    @Getter private FileConfig scoreboardConfigFile;

    @Override
    public void onEnable() {
        instance = this;

        Nucleus.getInstance().setBoardManager(new BoardManager(this, new ScoreboardAdapter()));
        this.mainConfigFile = new FileConfig(this, "config.yml");
        this.langConfigFile = new FileConfig(this, "lang.yml");
        this.scoreboardConfigFile = new FileConfig(this, "scoreboard.yml");
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, new SaveDataTask(), 20L * 60 * 5, 20L * 60 * 5);

        cooldowns();

        this.mongo = new Mongo();

        SimpleOfflinePlayer.load(this);

        CommandHandler.registerParameterType(PlayerTeamParameterType.class, new PlayerTeamParameterType());
        ListenerHandler.loadListenersFromPackage(this, "me.javaproject.kitpvp.listener");
        CommandHandler.loadCommandsFromPackage(this, "me.javaproject.kitpvp.command");
    }

    @Override
    public void onDisable() {

        try {
            SimpleOfflinePlayer.save(this);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void cooldowns() {
        Cooldown.createCooldown("SPAWN_TAG", 30);
        System.out.println("Registered SPAWN_TAG");
        Cooldown.createCooldown("ENDER_PEARL", 16);
        System.out.println("Registered ENDER_PEARL");
        Cooldown.createCooldown("APPLE", 2);
        System.out.println("Registered APPLE");
        Cooldown.createCooldown("GAPPLE", 60);
        System.out.println("Registered GAPPLE");
    }

}
