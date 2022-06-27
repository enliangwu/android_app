package leow.project4task2;
/**
 * @author Enliang (Leo) Wu
 * id: enliangw
 */
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.*;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

// class for data processing include reading, saving, analysing
public class InfoStore {
    // mongodb member object
    private MongoDatabase db;

    // linked hash map objects for storing unsorted analysing data
    private final Map<String, Integer> highFrequencyActivityTypes = new LinkedHashMap<>();
    private final Map<String, Integer> doItActivities = new LinkedHashMap<>();
    private final Map<String, Integer> dislikeActivities = new LinkedHashMap<>();

    // Tree map objects for storing sorted analysing data
    private final TreeMap<String, Integer> sortedHighFrequencyActivityTypes =
            new TreeMap<>(new ValueComparator(highFrequencyActivityTypes));
    private final TreeMap<String, Integer> sortedDoItActivities =
            new TreeMap<>(new ValueComparator(doItActivities));
    private final TreeMap<String, Integer> sortedDislikeActivities =
            new TreeMap<>(new ValueComparator(dislikeActivities));

    // constructor method to create connection to mongodb
    public InfoStore() {
        try {
            String connectionString = "mongodb://leow:leow8018@cluster0-shard-00-00.0cvor.mongodb.net:27017," +
                    "cluster0-shard-00-01.0cvor.mongodb.net:27017,cluster0-shard-00-02.0cvor.mongodb.net:27017/test?w=major" +
                    "ity&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1";
            MongoClient client = MongoClients.create(connectionString);
            // PojoCodecProvider and PojoCodecRegistry code from:
            // https://www.mongodb.com/docs/drivers/java/sync/v4.3/quick-start/
            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

            // database name is boring_activity
            db = client.getDatabase("boring_activity").withCodecRegistry(pojoCodecRegistry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // save activity information to database use pojo:BoringActivity
    public void recordActivity(BoringActivity _activity) {
        if (null != db) {
            // get activities collection
            MongoCollection<BoringActivity> collection = db.getCollection("activities", BoringActivity.class);
            // before saving check the existence of the same activity
            BoringActivity existsActivity = collection.find(eq("activityId", _activity.getActivityId())).first();
            if (null == existsActivity) {
                // save the activity to database
                collection.insertOne(_activity);
            }
        }
    }

    // save operation logs to database use pojo:ActivityLog
    public void recordInfo(ActivityLog _info) {
        if (null != db) {
            // get infos collection
            MongoCollection<ActivityLog> collection = db.getCollection("infos", ActivityLog.class);
            // insert log record
            collection.insertOne(_info);
        }
    }

    // get all operation logs
    public List<ActivityLog> getActivityLogs() {
        // put all results in an ArrayList
        List<ActivityLog> logs = new ArrayList<>();

        if (null != db) {
            // get infos collection
            MongoCollection<ActivityLog> collection = db.getCollection("infos", ActivityLog.class);
            // find all records with no filter
            collection.find().into(logs);
        }

        return logs;
    }

    // get activity information by activityId
    public BoringActivity getActivity(String activityId) {
        BoringActivity activity = null;

        if (null != db) {
            // get activities collection
            MongoCollection<BoringActivity> collection = db.getCollection("activities", BoringActivity.class);
            // find an activity filtering by activityId
            activity = collection.find(eq("activityId", activityId)).first();
        }

        return activity;
    }

    // reference: https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
    // custom value comparator for sorting TreeMap objects
    static class ValueComparator implements Comparator<String> {
        Map<String, Integer> base;

        public ValueComparator(Map<String, Integer> map) {
            this.base = map;
        }

        @Override
        public int compare(String key1, String key2) {
            // TreeMap will drop entries in case of equivalence between values
            // so the code there should not be simple return value2 - value1
            // while equivalence happening just compare two keys
            int value = this.base.get(key2) - this.base.get(key1);
            if (value != 0) return value;
            else return key2.compareTo(key1);
        }
    }

    // get analysing data
    public void getAnalysis() {
        List<ActivityLog> logs = this.getActivityLogs();

        for (ActivityLog log : logs) {
            // filtering only client request actions
            if (log.getActionName().equals("Client Request")) {
                // get activity information from database by activityId in log
                BoringActivity activity = this.getActivity(log.getActivityId());
                // GET /activity is find activity operation
                if (null != log.getClientRequestInfo() && log.getClientRequestInfo().equals("GET /activity")) {
                    if (null != activity) {
                        // look up the hash map to find if the key is already existing
                        // give the key a default value 0 if key not existing
                        // and finally increase the value by 1
                        highFrequencyActivityTypes.put(activity.getActivityType(),
                                highFrequencyActivityTypes.getOrDefault(activity.getActivityType(), 0) + 1);

                    }
                }

                // GET /activity/doit is DO activity operation
                if (null != log.getClientRequestInfo() && log.getClientRequestInfo().equals("GET /activity/doit")) {
                    if (null != activity) {
                        // look up the hash map to find if the key is already existing
                        // give the key a default value 0 if key not existing
                        // and finally increase the value by 1
                        doItActivities.put(activity.getActivityName(),
                                doItActivities.getOrDefault(activity.getActivityName(), 0) + 1);
                    }
                }

                // GET /activity/dislike is dislike activity operation
                if (null != log.getClientRequestInfo() && log.getClientRequestInfo().equals("GET /activity/dislike")) {
                    if (null != activity) {
                        // look up the hash map to find if the key is already existing
                        // give the key a default value 0 if key not existing
                        // and finally increase the value by 1
                        dislikeActivities.put(activity.getActivityName(),
                                dislikeActivities.getOrDefault(activity.getActivityName(), 0) + 1);
                    }
                }
            }
        }

        // put map entries to TreeMap object
        // that will trigger sorting of all entries
        sortedHighFrequencyActivityTypes.putAll(highFrequencyActivityTypes);
        sortedDoItActivities.putAll(doItActivities);
        sortedDislikeActivities.putAll(dislikeActivities);
    }

    // 3 public methods to get 3 analysing records
    public TreeMap<String, Integer> getSortedHighFrequencyActivityTypes() { return this.sortedHighFrequencyActivityTypes; }
    public TreeMap<String, Integer> getSortedDoItActivities() {
        return this.sortedDoItActivities;
    }
    public TreeMap<String, Integer> getSortedDislikeActivities() {
        return this.sortedDislikeActivities;
    }
}
