package whisper.mongo.sink

import com.mongodb.MongoClient
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import whisper.mongo.Test
import whisper.mongo.util.MongoUtil
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.UpdateOptions
import org.bson.Document


class MongoSink extends RichSinkFunction[Test] {

    var mongoClient: MongoClient = _
    var collection: MongoCollection[Document] = _

    override def invoke(value: Test, context: SinkFunction.Context): Unit = {
        val query = new BasicDBObject("date", value.date).
            append("app", value.app).
            append("scene", value.scene).
            append("result", value.result).
            append("type", value.checkType)
        val update = new BasicDBObject("$set", new BasicDBObject("num", value.num))
        val options = new UpdateOptions().upsert(true)

        collection.updateOne(query, update, options)
    }

    override def open(parameters: Configuration): Unit = {
        mongoClient = MongoUtil.getConnect()
        collection = mongoClient.getDatabase("test").getCollection("test")
    }

    override def close(): Unit = {
        if (mongoClient != null) {
            mongoClient.close()
        }
    }
}
