import org.apache.flink.streaming.api.scala._
import org.apache.flink.table.api.EnvironmentSettings
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.types.Row



object FlinkTest {
    def main(args: Array[String]): Unit = {
        val bsEnv = StreamExecutionEnvironment.getExecutionEnvironment
        val bsSettings = EnvironmentSettings
            .newInstance()
            .useBlinkPlanner()
            .inStreamingMode()
            .build()
        val bsTableEnv = StreamTableEnvironment.create(bsEnv, bsSettings)
        bsTableEnv.getConfig.getConfiguration.setString("parallelism.default", "1")

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
              |    -- ts AS TO_TIMESTAMP(CONCAT(SUBSTR(create_time, 0, 10), ' ', SUBSTR(create_time, 12, 8))),
              |    -- WATERMARK FOR ts AS ts - INTERVAL '1' SECOND
              |    ts as PROCTIME()
              |) WITH (
              |    'connector' = 'kafka',
              |    'topic' = 'dev_xd_service_whisper_check_log',
              |    'properties.bootstrap.servers' = '172.20.12.68:9092,172.20.12.69:9092,172.20.12.70:9092',
              |    'properties.group.id' = 'test',
              |    'format' = 'json',
              |    'scan.startup.mode' = 'latest-offset'
              |)
              |""".stripMargin)


        bsTableEnv.executeSql(
            """
              |CREATE TABLE mysql_sink (
              |    `start` timestamp,
              |    `end` timestamp,
              |    app varchar(20),
              |    num BIGINT,
              |    PRIMARY KEY (`start`, `end`, app) NOT ENFORCED
              |) WITH (
              |     'connector' = 'jdbc',
              |     'url' = 'jdbc:mysql://rm-2ze15k16a959rv0t6ao.mysql.rds.aliyuncs.com:3306/whisper_test',
              |     'username' = 'ailab',
              |     'password' = 'Ailab123',
              |     'table-name' = 'test'
              |)
              |""".stripMargin)


        val mysqlSink = bsTableEnv.executeSql(
            """
              |INSERT INTO mysql_sink
              |SELECT
              |     TUMBLE_START(ts, INTERVAL '1' MINUTE),
              |     TUMBLE_END(ts, INTERVAL '1' MINUTE),
              |     app,
              |     COUNT(*)
              |FROM kafka_source
              |GROUP BY
              |     TUMBLE(ts, INTERVAL '1' MINUTE),
              |     app
              |""".stripMargin)


        val res = bsTableEnv.sqlQuery(
            """
              |select * from mysql_sink
              |""".stripMargin)
        bsTableEnv.toRetractStream[Row](res).print().setParallelism(1)
        bsEnv.execute("test")

    }
}
