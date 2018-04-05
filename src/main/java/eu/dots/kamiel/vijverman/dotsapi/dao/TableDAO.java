/*
 */
package eu.dots.kamiel.vijverman.dotsapi.dao;


import com.mongodb.client.MongoCollection;

import org.bson.Document;


import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;

import com.mongodb.client.result.UpdateResult;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author kamie_itw2x3t
 */
public class TableDAO {

    public static String getAllFromCollection(String col) {
        MongoCollection<Document> collection = Database.getCollection(col);
        MongoCursor<Document> cursor = collection.find().iterator();
        String json = "[";
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                if (doc.containsKey("_acl")) {
                    doc.remove("_acl");
                }
                json = json + doc.toJson() + ",";
            }
        } finally {
            cursor.close();
        }
        return formatJson(col, json);
    }

    public static String getAllFromCollectionAndFilter(String col, String field, String filter) {
        MongoCollection<Document> collection = Database.getCollection(col);
        MongoCursor<Document> cursor = collection.find(eq(field, filter)).iterator();
        String json = "[";
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                if (doc.containsKey("_acl")) {
                    doc.remove("_acl");
                }
                json = json + doc.toJson() + ",";
            }
        } finally {
            cursor.close();
        }
        return formatJson(col, json);
    }

    public static String getAttendeesFromSession(String col, String field, String filter) {
        MongoCollection<Document> collection = Database.getCollection(col);
        MongoCursor<Document> cursor = collection.find(eq(field, filter)).iterator();
        String json = "[";
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                if (doc.containsKey("_acl")) {
                    doc.remove("_acl");
                }
                String attendeeID = doc.getString("_p_attendee");
                attendeeID = attendeeID.substring(9, attendeeID.length());
                MongoCollection<Document> coll = Database.getCollection("Attendee");
                Document document = coll.find(eq("_id", attendeeID)).first();
                json = json + document.toJson() + ",";
            }
        } finally {
            cursor.close();
        }
        return formatJson(col, json);
    }

    public static String getByName(String col, String field, String regex) {
        MongoCollection<Document> collection = Database.getCollection(col);
        MongoCursor<Document> cursor = collection.find(regex(field, regex)).iterator();
        String json = "[";
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                if (doc.containsKey("_acl")) {
                    doc.remove("_acl");
                }
                json = json + doc.toJson() + ",";
            }
        } finally {
            cursor.close();
        }
        return formatJson(col, json);
    }

    public static String getSomeFromCol(String col, Integer page, Integer size) {
        MongoCollection<Document> collection = Database.getCollection(col);
        MongoCursor<Document> cursor = collection.find().skip(size * (page - 1)).limit(size).iterator();
        String json = "[";
        try {
            while (cursor.hasNext()) {
                //cursor.next().remove("_acl");
                Document doc = cursor.next();
                if (doc.containsKey("_acl")) {
                    doc.remove("_acl");
                }
                json = json + doc.toJson() + ",";
                //json = json + cursor.next().toJson() + ",";
            }
        } finally {
            cursor.close();
        }
        return formatJson(col, json);
    }

    public static String insertInCol(String col, String body) {
        MongoCollection<Document> collection = Database.getCollection(col);
        Document doc = Document.parse(body);
        collection.insertOne(doc);
        return "ok";
    }

    public static String insertManyInCol(String col, String body) {
        MongoCollection<Document> collection = Database.getCollection(col);
        List<Document> listToInsert = toListOfDocuments(body);
        listToInsert.stream().forEach((doc) -> {
            if (!doc.containsKey("_id")) {
                String generatedString = RandomStringUtils.randomAlphanumeric(10);
                doc.put("_id", generatedString);
            }
            String id = doc.getString("_id");
            if(col.equals("Event")) {
                String generatedString = RandomStringUtils.randomAlphanumeric(10);
                Document userEventDoc = new Document("_id", generatedString)
                                        .append("_p_user", "_User$"+"rX6BEFe0v3")
                                        .append("_p_event", "Event$"+id)
                                        .append("isEventAdmin", "true");
                MongoCollection<Document> userEventCol = Database.getCollection("UserEvent");
                userEventCol.insertOne(userEventDoc);
            }
            collection.insertOne(formatDoc(doc, col));
        });
        return "finshed with " + listToInsert;
    }

    public static Boolean updateOneInCol(String col, String body) {
        MongoCollection<Document> collection = Database.getCollection(col);
        Document doc = Document.parse(body);
        UpdateResult res = collection.updateOne(eq("_id", doc.getString("_id")), new Document("$set", doc));
        return res.wasAcknowledged();
    }

    public static Boolean updateManyInCol(String col, String body) {
        MongoCollection<Document> collection = Database.getCollection(col);
        List<Document> listToUpdate = toListOfDocuments(body);
        Boolean result = false;
        for (Document doc : listToUpdate) {
            UpdateResult res = collection.updateOne(eq("_id", doc.getString("_id")), new Document("$set", formatDoc(doc, col)));
            result = res.wasAcknowledged();
        }
        return result;
    }

    public static Boolean deleteDocFromCol(String col, String id) {
        MongoCollection<Document> collection = Database.getCollection(col);
        DeleteResult res = collection.deleteOne(eq("_id", id));
        return res.wasAcknowledged();
    }

    public static Boolean deleteManyInCol(String col, String body) {
        MongoCollection<Document> collection = Database.getCollection(col);
        List<Document> listToDelete = toListOfDocuments(body);
        Boolean result = false;
        for (Document doc : listToDelete) {
            doc = formatDoc(doc,col);
            DeleteResult res = collection.deleteOne(eq("_id", doc.get("_id")));
            result = res.wasAcknowledged();
        }
        return result;
    }

    public static String getStringValueFromDoc(String col, String id, String field) {
        MongoCollection<Document> collection = Database.getCollection(col);
        MongoCursor<Document> cursor = collection.find(eq("_id", id)).iterator();
        String val = "";
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                if (doc.containsKey("_acl")) {
                    doc.remove("_acl");
                }
                val = doc.getString(field);
            }
        } finally {
            cursor.close();
        }
        return val;
    }

    public static long count(String col) {
        MongoCollection<Document> collection = Database.getCollection(col);
        return collection.count();
    }

    private static String formatJson(String col, String json) {
        json = json.substring(0, json.length() - 1) + "]";
        json = json.replace("_", "");
        json = json.replace("$date", "xdate");
        json = json.replace("role:", "role");
        if (col.equals("Session") || col.equals("Attendee")) {
            String regex = ".{5}?\\$";
            json = json.replaceAll(regex, "");
        }
        if (col.equals("SessionAttendee")) {
            json = json.replace("Session$", "");
            json = json.replace("Attendee$", "");
        }
        return json;
    }

    private static Document formatDoc(Document doc, String col) {
        if (doc.containsKey("CreatedById")) {
            doc.remove("CreatedById");
        }
        if (doc.containsKey("LastModifiedById")) {
            doc.remove("LastModifiedById");
        }
        if (doc.containsKey("OwnerId")) {
            doc.remove("OwnerId");
        }
        if (doc.containsKey("LastModifiedDate")) {
            doc.remove("LastModifiedDate");
        }
        if (doc.containsKey("IsDeleted")) {
            doc.remove("IsDeleted");
        }
        if (doc.containsKey("SystemModstamp")) {
            doc.remove("SystemModstamp");
        }
        if (doc.containsKey("CreatedDate")) {
            doc.remove("CreatedDate");
        }
        if (doc.containsKey("Id")) {
            doc.remove("Id");
        }
        if (doc.containsKey("attributes")) {
            doc.remove("attributes");
        }
        if (doc.containsKey("ScanEvent__c")) {
            doc.remove("ScanEvent__c");
        }
        if (doc.containsKey("ScanAttendee__c")) {
            doc.remove("ScanAttendee__c");
        }
        if (doc.containsKey("ScanSession__c")) {
            doc.remove("ScanSession__c");
        }
        if (doc.containsKey("Contact__c")) {
            doc.remove("Contact__c");
        }
        if (doc.containsKey("eventDate")) {
            String stringDate = doc.getString("eventDate");
            stringDate = stringDate.replaceAll("\\+.*", "");
            LocalDateTime newDateTime = LocalDateTime.parse(stringDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            Date newDate = Date.from(newDateTime.toInstant(ZoneOffset.UTC));
            doc.remove("eventDate");
            doc.put("eventDate", newDate);

        }
        if (doc.containsKey("sessionDate")) {
            String stringDate = doc.getString("sessionDate");
            stringDate = stringDate.replaceAll("\\+.*", "");
            LocalDateTime newDateTime = LocalDateTime.parse(stringDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            Date newDate = Date.from(newDateTime.toInstant(ZoneOffset.UTC));
            doc.remove("sessionDate");
            doc.put("sessionDate", newDate);
        }
        if (doc.containsKey("inTime")) {
            String stringDate = doc.getString("inTime");
            stringDate = stringDate.replaceAll("\\+.*", "");
            LocalDateTime newDateTime = LocalDateTime.parse(stringDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            Date newDate = Date.from(newDateTime.toInstant(ZoneOffset.UTC));
            doc.remove("inTime");
            doc.put("inTime", newDate);
        }
        if (doc.containsKey("outTime")) {
            String stringDate = doc.getString("outTime");
            stringDate = stringDate.replaceAll("\\+.*", "");
            LocalDateTime newDateTime = LocalDateTime.parse(stringDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            Date newDate = Date.from(newDateTime.toInstant(ZoneOffset.UTC));
            doc.remove("outTime");
            doc.put("outTime", newDate);
        }
        if (doc.containsKey("_id") && col.equals("SessionAttendee")) {
            String id = doc.getString("_id");
            id = id.replaceFirst(".* ", "");
            doc.remove("_id");
            doc.put("_id", id);
        }
        if (col.equals("Session")) {
            String val = doc.getString("sessionName");
            val = val.replaceFirst(".*: ", "");
            doc.remove("sessionName");
            doc.put("sessionName", val);
        }
        if (doc.containsKey("_p_attendee")) {
            String newVal = "Attendee$" + doc.getString("_p_attendee");
            doc.remove("_p_attendee");
            doc.put("_p_attendee", newVal);
        }
        if (doc.containsKey("_p_session")) {
            String newVal = "Session$" + doc.getString("_p_session");
            doc.remove("_p_session");
            doc.put("_p_session", newVal);
        }
        if (doc.containsKey("_p_attendeeEvent")) {
            String newVal = "Event$" + doc.getString("_p_attendeeEvent");
            doc.remove("_p_attendeeEvent");
            doc.put("_p_attendeeEvent", newVal);
        }
        if (doc.containsKey("_p_sessionEvent")) {
            String newVal = "Event$" + doc.getString("_p_sessionEvent");
            doc.remove("_p_sessionEvent");
            doc.put("_p_sessionEvent", newVal);
        }
        return doc;
    }

    private static List<Document> toListOfDocuments(String json) {
        Document doc = Document.parse("{ \"list\":" + json + "}");
        Object list = doc.get("list");
        if (list instanceof List<?>) {
            return (List<Document>) doc.get("list");
        }
        return null;
    }
}
