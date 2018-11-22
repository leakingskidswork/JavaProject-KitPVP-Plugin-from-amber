package me.javaproject.kitpvp.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import me.javaproject.kitpvp.KitPvP;
import me.javaproject.kitpvp.profile.Profile;
import me.joeleoli.nucleus.config.ConfigCursor;
import org.bson.Document;

import java.io.Closeable;
import java.util.Collections;
import java.util.UUID;

@Getter
public class Mongo implements Closeable {

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> profiles;
    private MongoCollection<Document> teams;

    public Mongo() {
        ConfigCursor cursor = new ConfigCursor(KitPvP.getInstance().getMainConfigFile(), "mongo");

        if (!cursor.exists("host")
                || !cursor.exists("port")
                || !cursor.exists("database")
                || !cursor.exists("authentication.enabled")
                || !cursor.exists("authentication.username")
                || !cursor.exists("authentication.password")
                || !cursor.exists("authentication.database")) {
            throw new RuntimeException("Missing configuration option");
        }

        if (cursor.getBoolean("authentication.enabled")) {
            final MongoCredential credential = MongoCredential.createCredential(
                    cursor.getString("authentication.username"),
                    cursor.getString("authentication.database"),
                    cursor.getString("authentication.password").toCharArray()
            );

            this.client = new MongoClient(new ServerAddress(cursor.getString("host"), cursor.getInt("port")),
                    Collections.singletonList(credential)
            );
        } else {
            this.client = new MongoClient(new ServerAddress(cursor.getString("host"), cursor.getInt("port")));
        }

        this.database = this.client.getDatabase("kitpvp");
        this.profiles = this.database.getCollection("profiles");
        this.teams = this.database.getCollection("teams");
    }

    public Document getProfile(UUID uuid) {
        return this.profiles.find(Filters.eq("uuid", uuid.toString())).first();
    }

    public void replaceProfile(Profile profile, Document document) {
        this.profiles.replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document,
                new ReplaceOptions().upsert(true)
        );
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.close();
        }
    }
}