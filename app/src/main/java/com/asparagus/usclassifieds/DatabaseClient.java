package com.asparagus.usclassifieds;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import com.mongodb.client.model.Filters;

import java.util.LinkedList;
import java.util.Locale;

import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.configuration.CodecRegistries;

public final class DatabaseClient {

    private String username = "app";
    private String password = "app";
    private String hostname = "cluster-oomas.mongodb.net";
    private Integer port = 27017;
    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<User> users;
    private MongoCollection<Listing> listings;

    public DatabaseClient() {

        /* Create a URI to specify the Mongo database location */
        ConnectionString uri = new ConnectionString(
                String.format(Locale.US,
                        "mongodb+srv://%s:%s@%s:%d",
                        this.username,
                        this.password,
                        this.hostname,
                        this.port
                )
        );

        /* Configure codec registry to include codecs that can handle the
        translation to/from BSON for POJOs */

        CodecRegistry registry = MongoClientSettings.getDefaultCodecRegistry();

        CodecRegistry provider = CodecRegistries.fromProviders(
            PojoCodecProvider.builder().automatic(true).build()
        );

        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
            registry, provider);

        /* Configure the MongoDB client settings, specifying to use
        the POJO Codec registry, and the URI generated above */

        MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .applyConnectionString(uri)
            .build();

        this.client = MongoClients.create(settings);
        this.database = this.client.getDatabase("app");

        this.users = database.getCollection("users", User.class);
        this.listings = database.getCollection("listings", Listing.class);

    }

    void addUser(User user) {
        /* Add the user to the collection of users */
        users.insert(user);
    }

    void removeUser(User user) {
        /* Remove the user from the collection of users */
        users.findOneAndDelete(Filters.eq("email", user.email));
    }

    void addFriend(User requester, User receiver) {
        listings.updateOne(
            Filters.eq("email", requester.email);
        );

        // Updates.addToSet("")
        // Add each user's respective USC ID # to each
        // user's set of friends
    }

    void removeFriend(User rival, User nemesis) {
        // Remove the respective USC ID # from each
        // user's set of friends
    }

    void addListing(Listing listing) {
        /* Add the listing to the collection of listings */
        listings.insert(listing);
    }


    LinkedList<Listing> listingsBy(User user) {
        /* Given a user, return the listings that
        correspond to his email */
    }

    void removeListing(Listing listing) {
        users.findOneAndDelete(Filters.eq('description', listing.description));
        /* Remove the user from the collection of users */
    }

    LinkedList<Listing> queryListing(Location location, Double radius) {

        /* The angular distance of a place north or south of the earth's equator,
        or of a celestial object north or south of the celestial equator */
        Double latitude = location.getLatitude();

        /* The angular distance of a place east or west of the meridian at
        Greenwich, England, or west of the meridian of a celestial object */
        Double longitude = location.getLongitude();

        /* The maximum distance away in meters */
        Double maximumDistance = radius;

        /* Them minimum distance away in meters */
        Double minimumDistance = 0.0;

        /* The reference point to originate the query from */
        Point referencePoint = new Point(new Position(latitude, longitude));

        /* The field name within the collection to filter for */
        String fieldName = "location";

        LinkedList<Listing> result = listings.find(
            Filters.near(
                fieldName,
                referencePoint,
                maximumDistance,
                minimumDistance
            )
        ).limit(100).toArray();
    }

}
