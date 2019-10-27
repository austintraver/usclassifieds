import org.bson.Document;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;

import com.mongodb.MongoException;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoServerException;
import com.mongodb.MongoWriteException;
import com.mongodb.MongoQueryException;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

// import com.mongodb.*;
// import com.mongodb.client.*;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;

import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.configuration.CodecRegistries;


public class DatabaseClient {

  public static void main(String[] args) {

    /*
       Create a URI to specify the Mongo database location
    */
    String username = "app";
    String password = "app";
    String hostname = "cluster-oomas.mongodb.net";
    Integer port = 27017;
    ConnectionString uri = new ConnectionString(
      String.format(
        "mongodb+srv://%s:%s@%s:%d",
        username,
        password,
        hostname
      )
    );

    /*
       Configure codec registry to include codecs that can handle the
       translation to/from BSON for POJOs
    */

    CodecRegistry registry = MongoClientSettings.getDefaultCodecRegistry();

    CodecRegistry provider = CodecRegistries.fromProviders(
      PojoCodecProvider.builder().automatic(true).build()
    );

    CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
      registry, provider);

    /*
       Configure the mongo client settings, specifying to use
       the POJO Codec registry, and the URI generated above
    */

    MongoClientSettings settings = MongoClientSettings
      .builder()
      .codecRegistry(pojoCodecRegistry)
      .applyConnectionString(uri)
      .build();

    MongoClient client = MongoClients.create(settings);

    MongoDatabase database = client.getDatabase("app");

    MongoCollection<User> users = database.getCollection(
      "users",
      User.class);

    MongoCollection<Listing> listings = database.getCollection(
      "listings",
      Listing.class);

    /*
       Create a function to specify what should be done
       to the documents returned from a find() call in a
       MongoDB collection
    */

    /*
       The angular distance of a place north or south of the earth's equator,
       or of a celestial object north or south of the celestial equator
    */
    Double latitude = -73.9667;

    /* The angular distance of a place east or west of the meridian at
       Greenwich, England, or west of the meridian of a celestial object
    */
    Double longitude = 40.78;

    /* The maximum distance away in meters */
    Double maximumDistance = 10000.0;

    /* Them minimum distance away in meters */
    Double minimumDistance = 0.0;

    /* The reference point to originate the query from */
    Point referencePoint = new Point(new Position(latitude, longitude));

    /* The field name within the collection to filter for */
    String fieldName = "contact.location";

    LinkedList<Listing> result = listings.find(
      Filters.near(
        fieldName,
        referencePoint,
        maximumDistance,
        minimumDistance
      )
    ).limit(100).toArray();

  }

  void addUser(User user) {
    // Add the user to the collection of users
  }

  void removeUser(User user) {
    // Remove the user from the collection of users
  }

  User queryUser(String query) {
    // TODO get clarification on what is being queried exactly
  }

  void updateUser(String query) {
    // TODO get clarification on what needs to be updated exactly
  }

  void addFriend(User buddy, User pal) {
    // Add each user's respective USC ID # to each
    // user's set of friends
  }

  void removeFriend(User rival, User nemesis) {
    // Remove the respective USC ID # from each
    // user's set of friends
  }

  void addListing(Listing listing) {
    // add the listing to the collection of listings
  }

  LinkedList<Listing> queryListing(String query) {
    // TODO get clarification on what needs to be updated exactly
  }




}
