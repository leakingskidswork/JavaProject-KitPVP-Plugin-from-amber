package me.javaproject.kitpvp.team;

import com.mongodb.BasicDBList;
import com.mongodb.Block;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.Setter;
import me.javaproject.kitpvp.KitPvP;
import me.javaproject.kitpvp.profile.Profile;
import me.javaproject.kitpvp.team.type.PlayerTeam;
import me.javaproject.kitpvp.util.PlayerUtility;
import me.javaproject.kitpvp.util.SimpleOfflinePlayer;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;

@Getter
public class Team {

    public static Set<Team> teams = new HashSet<>();

    @Setter
    private UUID uuid;

    @Setter
    private String name;

    public Team(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;

        if (uuid == null) {
            this.uuid = UUID.randomUUID();
        }

        teams.add(this);
    }

    public static Team getByName(String name) {
        for (Team team : getTeams()) {
            if (team.getName().replace(" ", "").equalsIgnoreCase(name.replace(" ", ""))) {
                return team;
            }
        }

        return null;
    }

    public static Team getByUuid(UUID uuid) {
        for (Team team : getTeams()) {
            if (team.getUuid().equals(uuid)) {
                return team;
            }
        }

        return null;
    }

    public static void load() {

        MongoCollection teamCollection = KitPvP.getInstance().getMongo().getDatabase().getCollection("teams");

        teamCollection.find().forEach((Block) obj -> {

            Document dbo = (Document) obj;

            UUID uuid = UUID.fromString(dbo.getString("uuid"));

            UUID leader = UUID.fromString(dbo.getString("leader"));

            String name = dbo.getString("name");

            int balance = dbo.getInteger("balance");

            Set<UUID> members = new HashSet<>();

            Map<UUID, UUID> invitedPlayers = new HashMap<>();

            Document invitedPlayerMap = (Document) dbo.get("invitedPlayers");

            for (String key : invitedPlayerMap.keySet()) {
                UUID invitedPlayer = UUID.fromString(key);
                UUID invitedBy = (UUID) invitedPlayerMap.get(key);
                invitedPlayers.put(invitedPlayer, invitedBy);
            }

            List<String> membersList = (List<String>) dbo.get("members");

            for (String member : membersList) {
                if (member.length() == uuid.toString().length()) {
                    members.add(UUID.fromString(member));
                }
            }

            final PlayerTeam playerTeam = new PlayerTeam(name, leader, uuid);
            playerTeam.setMembers(members);
            playerTeam.setBalance(balance);
            playerTeam.setInvitedPlayers(invitedPlayers);

        });

        for (Player player : PlayerUtility.getOnlinePlayers()) {

            Profile profile = Profile.getByUuid(player.getUniqueId());

            PlayerTeam playerTeam = PlayerTeam.getByPlayerName(player.getName());

            if (profile.getTeam() == null && playerTeam != null) {
                profile.setTeam(playerTeam);
            }

        }
    }

    public static void save() {
        if (!(getTeams().isEmpty())) {

            MongoCollection teamCollection = KitPvP.getInstance().getMongo().getDatabase().getCollection("teams");

            for (Team team : getTeams()) {
                if (team instanceof PlayerTeam) {
                    PlayerTeam playerTeam = (PlayerTeam) team;

                    Document dbo = new Document();
                    dbo.put("uuid", playerTeam.getUuid().toString());
                    dbo.put("leader", playerTeam.getLeader().toString());
                    dbo.put("name", playerTeam.getName());
                    dbo.put("balance", playerTeam.getBalance());

                    List<String> members = new ArrayList<>();
                    Document invitedPlayers = new Document();

                    members.addAll(PlayerUtility.getConvertedUuidSet(playerTeam.getMembers()));

                    for (UUID invitedPlayer : playerTeam.getInvitedPlayers().keySet()) {
                        invitedPlayers.put(invitedPlayer.toString(), playerTeam.getInvitedPlayers().get(invitedPlayer));
                    }


                    dbo.put("members", members);
                    dbo.put("invitedPlayers", invitedPlayers);

                    teamCollection.replaceOne(eq("uuid", playerTeam.getUuid().toString()), dbo, new UpdateOptions().upsert(true));
                }
            }
        }
    }

    public static Set<Team> getAllByString(String string) {
        Set<Team> toReturn = new HashSet<>();

        for (Team team : teams) {
            if (!(toReturn.contains(team))) {
                if (team.getName().replace(" ", "").equalsIgnoreCase(string)) {
                    toReturn.add(team);
                }

                if (team instanceof PlayerTeam) {
                    PlayerTeam playerTeam = (PlayerTeam) team;

                    for (UUID uuid : playerTeam.getAllPlayerUuids()) {
                        SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(uuid);

                        if (offlinePlayer != null && offlinePlayer.getName().equalsIgnoreCase(string)) {
                            toReturn.add(team);
                        }
                    }
                }

            }
        }

        return toReturn;
    }

    public static Set<Team> getTeams() {
        return teams;
    }

}
