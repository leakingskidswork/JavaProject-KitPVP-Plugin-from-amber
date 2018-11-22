package me.javaproject.kitpvp.team.type;

import lombok.Getter;
import lombok.Setter;
import me.javaproject.kitpvp.team.Team;
import me.javaproject.kitpvp.util.SimpleOfflinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
@Setter
public class PlayerTeam extends Team {

    private UUID leader;

    private Set<UUID> members;

    private Map<UUID, UUID> invitedPlayers;

    private int balance;

    public PlayerTeam(String name, UUID uuid, UUID leader) {
        super(name, uuid);

        this.leader = leader;

        this.members = new HashSet<>();

        this.invitedPlayers = new HashMap<>();

        this.balance = 0;
    }

    public void sendMessage(String message) {

        this.members.forEach(member -> Bukkit.getPlayer(member).sendMessage(message));

    }


    public List<UUID> getAllPlayerUuids() {
        List<UUID> toReturn = new ArrayList<>();

        toReturn.add(leader);
        toReturn.addAll(members);

        return toReturn;
    }


    public Set<Player> getOnlinePlayers() {

        final Set<Player> toReturn = new HashSet<>();

        for (UUID uuid : getAllPlayerUuids()) {

            final Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                toReturn.add(player);
            }
        }

        return toReturn;
    }

    public Set<Player> getAllMembers() {

        Set<Player> toReturn = new HashSet<>();

        for (UUID uuid : getAllPlayerUuids()) {

            if(this.leader == uuid) {
                continue;
            }

            Player player = Bukkit.getPlayer(uuid);

            if (player != null) {
                toReturn.add(player);
            }

        }

        return toReturn;
    }

    public static PlayerTeam getByPlayerName(String name) {
        for (Team team : getTeams()) {
            if (team instanceof PlayerTeam) {

                PlayerTeam playerFaction = (PlayerTeam) team;

                for (UUID uuid : playerFaction.getAllPlayerUuids()) {
                    SimpleOfflinePlayer offlinePlayer = SimpleOfflinePlayer.getByUuid(uuid);

                    if (offlinePlayer != null) {
                        if (offlinePlayer.getName().equalsIgnoreCase(name)) {
                            return playerFaction;
                        }
                    }
                }
            }
        }

        return null;
    }

    public static Set<PlayerTeam> getPlayerTeams() {

        Set<PlayerTeam> toReturn = new HashSet<>();

        for (Team team : getTeams()) {

            if (team instanceof PlayerTeam) {
                toReturn.add((PlayerTeam)team);
            }
        }

        return toReturn;
    }
}
