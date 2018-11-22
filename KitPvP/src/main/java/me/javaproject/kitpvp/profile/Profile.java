package me.javaproject.kitpvp.profile;

import lombok.Getter;
import lombok.Setter;
import me.javaproject.kitpvp.KitPvP;
import me.javaproject.kitpvp.listener.ProfileListener;
import me.javaproject.kitpvp.team.type.PlayerTeam;
import me.joeleoli.nucleus.config.FileConfig;
import me.joeleoli.nucleus.player.PlayerInfo;
import me.joeleoli.nucleus.uuid.UUIDCache;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class Profile extends PlayerInfo {

    @Getter
    private static Map<UUID, Profile> cached = new HashMap<>();

    /* General */
    @Setter
    private int kills;
    @Setter
    private int deaths;
    @Setter
    private int killStreak;
    @Setter
    private int kdr;
    @Setter
    private int balance;
    @Setter
    private int level;
    @Setter
    private boolean scoreboard;
    @Setter
    private boolean autoDeposit;
    @Setter
    private PlayerTeam team;

    private boolean loaded;

    private long loadedAt = System.currentTimeMillis();

    public Profile(UUID uuid) {
        super(uuid, null);

        for (PlayerTeam playerTeam : PlayerTeam.getPlayerTeams()) {
            if (playerTeam.getAllPlayerUuids().contains(uuid)) {
                this.team = playerTeam;
            }
        }

        Profile.getCached().put(uuid, this);
    }

    /**
     * Retrieves a cached instance of Profile or creates and returns a new instance.
     *
     * @param uuid the player uuid
     * @return the player's Profile instance
     */
    public static Profile getByUuid(UUID uuid) {
        final Profile toReturn = cached.get(uuid);
        return toReturn == null ? new Profile(uuid) : toReturn;
    }

    /**
     * This method should only be called asynchronously as it could fetch results from Redis.
     *
     * @param name the name
     * @return A Profile instance if results were fetched
     */
    public static Profile getByName(String name) {
        if (Bukkit.isPrimaryThread()) {
            throw new RuntimeException("Cannot use Profile#getByName on primary thread!");
        }

        final Player target = Bukkit.getPlayer(name);
        final Profile profile;

        if (target == null) {
            UUID uuid = UUIDCache.getUuid(name);

            if (uuid != null) {
                profile = Profile.getByUuid(uuid);
            } else {
                return null;
            }
        } else {
            profile = Profile.getByUuid(target.getUniqueId());
        }

        return profile;
    }

    public void load() {
        if (Bukkit.isPrimaryThread()) {
            throw new RuntimeException("Attempting to query on main thread!");
        }

        Document document = KitPvP.getInstance().getMongo().getProfile(this.getUuid());

        if (document == null) {
            this.loaded = true;
            this.kills = 0;
            this.deaths = 0;
            this.killStreak = 0;
            this.kdr = 0;
            this.balance = 0;
            this.level = 0;
            this.scoreboard = true;
            this.autoDeposit = true;
            this.save();

            return;
        }

        if (this.getName() == null) {
            this.setName(document.getString("name"));
        }

        if (document.containsKey("kills")) {

            final int kills = document.getInteger("kills");

            this.kills = kills;
        }

        if (document.containsKey("deaths")) {

            final int deaths = document.getInteger("deaths");

            this.deaths = deaths;
        }

        if (document.containsKey("killStreak")) {

            final int killStreak = document.getInteger("killStreak");

            this.killStreak = killStreak;

        }

        if (document.containsKey("kdr")) {

            final int kdr = document.getInteger("kdr");

            this.kdr = kdr;

        }

        if (document.containsKey("balance")) {

            final int balance = document.getInteger("balance");

            this.balance = balance;

        }

        if (document.containsKey("level")) {

            final int level = document.getInteger("level");

            this.level = level;

        }

        if (document.containsKey("scoreboard")) {

            final boolean scoreboard = document.getBoolean("scoreboard");

            this.scoreboard = scoreboard;
        }

        if (document.containsKey("autoDeposit")) {

            final boolean autoDeposit = document.getBoolean("autoDeposit");

            this.autoDeposit = autoDeposit;
        }

        this.loaded = true;
    }

    /**
     * Saves the player's profile.
     */
    public void save() {
        Document document = KitPvP.getInstance().getMongo().getProfile(this.getUuid());


        if (document == null) {
            document = new Document();
        }

        document.put("uuid", this.getUuid().toString());
        document.put("name", this.getName());
        document.put("kills", this.getKills());
        document.put("deaths", this.getDeaths());
        document.put("killStreak", this.getKillStreak());
        document.put("kdr", this.getKdr());
        document.put("balance", this.getBalance());
        document.put("level", this.getLevel());
        document.put("scoreboard", this.isScoreboard());
        document.put("autoDeposit", this.isAutoDeposit());

        Document teamDocument = new Document();

        if (this.team != null) {
            teamDocument.put("uuid", this.team.getUuid().toString());
            teamDocument.put("name", this.team.getName());
        }

        if (!teamDocument.isEmpty()) {
            document.put("team", team);
        }

        KitPvP.getInstance().getMongo().replaceProfile(this, document);
    }
}
