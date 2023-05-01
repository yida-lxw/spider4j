package com.yida.spider4j.crawler.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.monotonically_increasing_id;

public class DBApp {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("MySparkSQL");
        JavaSparkContext sparkContext = new JavaSparkContext(conf);
        SparkSession sparkSession = SparkSession.builder()
                .config(conf)
                .getOrCreate();

        // 读取MySQL表中的数据，创建DataFrame对象
        String url = "jdbc:mysql://localhost:3306/spider?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String user = "root";
        String password = "1111";
        String dbName = "spider";
        String tableName = "news";
        Dataset<Row> df = sparkSession.read()
                .format("jdbc")
                .option("url", url)
                .option("dbtable", dbName + "." + tableName)
                .option("user", user)
                .option("password", password)
                .option("driver", "org.gjt.mm.mysql.Driver")
                .load();

        // 注册一个名为"news"的表
        df.createOrReplaceTempView("news");

        SQLContext sqlContext = new SQLContext(sparkContext);
        // 需求1：按照media分组，每组内按commentCount降序排序，取每组的top 5
        String sql1 = "SELECT pageId, media, channel, title, commentCount FROM ("
                + "   SELECT pageId, media, channel, title, commentCount,"
                + "          ROW_NUMBER() OVER (PARTITION BY media ORDER BY commentCount DESC) AS rank"
                + "   FROM news WHERE commentCount IS NOT NULL AND media IS NOT NULL"
                + ") tmp WHERE rank <= 5";

        Dataset<Row> result1 = sparkSession.sql(sql1);
        result1.show();
        write2MySQL(result1.toJavaRDD(), "news_media_stat", sqlContext);

        // 需求2：按commentCount降序排序，取top 10
        String sql2 = "SELECT pageId, media, channel, title, commentCount FROM ("
                + "   SELECT pageId, media, channel, title, commentCount,"
                + "          ROW_NUMBER() OVER (ORDER BY commentCount DESC) AS rank"
                + "   FROM news WHERE commentCount IS NOT NULL AND media IS NOT NULL"
                + ") tmp WHERE rank <= 10";

        Dataset<Row> result2 = sparkSession.sql(sql2);
        result2.show();
        write2MySQL(result2.toJavaRDD(), "news_all_stat", sqlContext);

        // 需求3：按照channel分组，每组内按commentCount降序排序，取每组的top 5
        String sql3 = "SELECT pageId, media, channel, title, commentCount FROM ("
                + "   SELECT pageId, media, channel, title, commentCount,"
                + "          ROW_NUMBER() OVER (PARTITION BY channel ORDER BY commentCount DESC) AS rank"
                + "   FROM news WHERE commentCount IS NOT NULL AND channel IS NOT NULL"
                + ") tmp WHERE rank <= 5";

        Dataset<Row> result3 = sparkSession.sql(sql3);
        result3.show();
        write2MySQL(result3.toJavaRDD(), "news_channel_stat", sqlContext);
    }

    /**
     * 写入数据
     * @param sqlContext
     * @param tableName   写入的表名称
     */
    private static void write2MySQL(JavaRDD<Row> javaRowRDD, String tableName, SQLContext sqlContext) {
        String url = "jdbc:mysql://localhost:3306/spider?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String userName = "root";
        String password = "1111";
        String driverClass = "org.gjt.mm.mysql.Driver";
        // 数据库连接
        Properties connProperties = new Properties();
        connProperties.put("user", userName);
        connProperties.put("password", password);
        connProperties.put("driver", driverClass);

        // 动态构造DataFrame的元数据。
        List structFields = new ArrayList();
        //structFields.add(DataTypes.createStructField("id", DataTypes.LongType, false));
        structFields.add(DataTypes.createStructField("pageId", DataTypes.StringType, false));
        structFields.add(DataTypes.createStructField("media", DataTypes.StringType, false));
        structFields.add(DataTypes.createStructField("channel", DataTypes.StringType, false));
        structFields.add(DataTypes.createStructField("title", DataTypes.StringType, true));
        structFields.add(DataTypes.createStructField("commentCount", DataTypes.IntegerType, false));
        // 构建StructType，用于最后DataFrame元数据的描述。
        StructType structType = DataTypes.createStructType(structFields);

        // 基于已有的元数据以及RDD<Row>来构造DataFrame。
        Dataset dsDateset = sqlContext.createDataFrame(javaRowRDD, structType);
        dsDateset = dsDateset.toDF().select(col("*"), functions.lit(null).alias("id"));
        // 为每个行分配自增主键ID
        Column idColumn =
                functions.row_number().over(Window.orderBy(monotonically_increasing_id())).cast(DataTypes.LongType);
        dsDateset = dsDateset.withColumn("id", idColumn);
        // 将数据写入到user表中。
        dsDateset.write()
                .mode("overwrite")
                .option("batchSize", "5000")
                //设置事务隔离级别为RC,可选值有NONE、READ_COMMITTED、READ_UNCOMMITTED、REPEATABLE_READ、SERIALIZABLE,
                //默认值为READ_COMMITTED
                .option("isolationLevel", "READ_COMMITTED")
                .option("truncate", "true")
                .option("rewriteBatchedStatements", "true")
                .jdbc(url, tableName, connProperties);
    }
}
