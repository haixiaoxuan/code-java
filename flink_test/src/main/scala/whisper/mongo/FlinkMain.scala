package whisper.mongo

import org.apache.flink.api.common.functions.{FilterFunction, MapFunction}
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.types.Row
import whisper.mongo.sink.MongoSink
import org.apache.flink.streaming.api.scala._


case class Test(
   date:String,
   app:String,
   scene:String,
   result:Int,
   checkType:String,
   num:Long
)


object FlinkMain {

    def main(args: Array[String]): Unit = {

        val bsEnv = StreamExecutionEnvironment.getExecutionEnvironment
        val bsSettings = EnvironmentSettings
            .newInstance()
            .useBlinkPlanner()
            .inStreamingMode()
            .build()
        val bsTableEnv = StreamTableEnvironment.create(bsEnv, bsSettings)

        bsTableEnv.getConfig.getConfiguration.setString("table.exec.emit.early-fire.enabled", "true")
        bsTableEnv.getConfig.getConfiguration.setString("table.exec.emit.early-fire.delay", "1s")

        bsTableEnv.executeSql(
            """
              |CREATE TEMPORARY TABLE kafka_source (
              |    `app` STRING,
              |    `scene` STRING,
              |    `result` INT,
              |    `type` STRING,
              |    `create_time` STRING,
              |    `dt` STRING,
              |    `ms` BIGINT,
              |    ts AS TO_TIMESTAMP(CONCAT(SUBSTR(create_time, 0, 10), ' ', SUBSTR(create_time, 12, 8))) + INTERVAL '8' HOUR,
              |    WATERMARK FOR ts AS ts - INTERVAL '10' SECOND
              |    -- ts as PROCTIME()
              |) WITH (
              |    'connector' = 'kafka',
              |    'topic' = 'dev_xd_service_whisper_check_log',
              |    'properties.bootstrap.servers' = '172.20.12.68:9092,172.20.12.69:9092,172.20.12.70:9092',
              |    'properties.group.id' = 'test',
              |    'format' = 'json',
              |    'scan.startup.mode' = 'latest-offset'
              |)
              |""".stripMargin)


        val mysqlSink = bsTableEnv.sqlQuery(
            """
              |SELECT
              |     DATE_FORMAT(TUMBLE_START(ts, INTERVAL '1' DAY), 'yyyy-MM-dd'),
              |     app,
              |     scene,
              |     `result`,
              |     `type`,
              |     COUNT(*)
              |FROM kafka_source
              |GROUP BY
              |     TUMBLE(ts, INTERVAL '1' DAY),
              |     app,
              |     scene,
              |     `result`,
              |     `type`
              |""".stripMargin)


        val resStream = bsTableEnv.toRetractStream[Row](mysqlSink)

        resStream.filter(new FilterFunction[(Boolean, Row)] {
            override def filter(t: (Boolean, Row)): Boolean = {
                t._1
            }
        }).map(new MapFunction[(Boolean, Row), Test] {
            override def map(t: (Boolean, Row)): Test = {
                Test(t._2.getField(0).asInstanceOf[String],
                    t._2.getField(1).asInstanceOf[String],
                    t._2.getField(2).asInstanceOf[String],
                    t._2.getField(3).asInstanceOf[Int],
                    t._2.getField(4).asInstanceOf[String],
                    t._2.getField(5).asInstanceOf[Long]
                )
            }
        }).addSink(new MongoSink())

        bsEnv.execute("test")
    }
}

