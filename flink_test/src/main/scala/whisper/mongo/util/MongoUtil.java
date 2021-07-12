package whisper.mongo.util;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoUtil   {

    public static MongoClient getConnect(){
        String mongoURI = "mongodb://root:RzdHMDDrt3WGPunCtCKd@dds-uf675e57473a95041.mongodb.rds.aliyuncs.com:3717,dds-uf675e57473a95042.mongodb.rds.aliyuncs.com:3717/admin?replicaSet=mgset-45211812";
        MongoClientURI uri = new MongoClientURI(mongoURI);
        return new MongoClient(uri);
    }

    public static void main(String[] args) {
        MongoClient connect = getConnect();
        MongoCollection<Document> col = connect.getDatabase("test").getCollection("test");

        long num = col.countDocuments();
        System.out.println(num);
    }
}