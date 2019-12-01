package br.leserc.sparkprocess;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.json.simple.parser.JSONParser;

import static org.apache.spark.sql.functions.*;

public class TestSparkProcessing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fileUrl1 = "/home/edermatheus/smart_city_temp/parkingStatuses01.json";
		String fileUrl2 = "/home/edermatheus/smart_city_temp/parkingStatuses02.json";
		String fileUrl3 = "/home/edermatheus/smart_city_temp/parkingStatuses03.json";
		
		String uuid1 = "0500103e-1a8a-4f82-bf43-d3880f52f64a";
		String uuid2 = "b070f2ec-1482-447b-977b-9f9bc654411e";
		String uuid3 = "23089f46-678e-4b28-8294-96af69728cd5";
		
		String capability = "parking-status";
		
		String data1 = InterSCityAPI.getDataByUUIDAndCapability(uuid1, capability);
		String data2 = InterSCityAPI.getDataByUUIDAndCapability(uuid2, capability);
		String data3 = InterSCityAPI.getDataByUUIDAndCapability(uuid3, capability);
		
		FileHandler fhandler1 = new FileHandlerImpl(fileUrl1);
		FileHandler fhandler2 = new FileHandlerImpl(fileUrl2);
		FileHandler fhandler3 = new FileHandlerImpl(fileUrl3);
		
		fhandler1.saveFile(data1.getBytes());
		fhandler2.saveFile(data2.getBytes());
		fhandler3.saveFile(data3.getBytes());
		
		//Initializing spark
		//Creating a spark session
		SparkSession spark = new SparkSession.Builder()
				.appName("Testing Spark")
				.master("local")
				.getOrCreate();
				
		//Get data from resource 1
		Dataset<Row> df1 = spark.read().format("json")
				.option("multiline", true)
				.load(fileUrl1);
		
		df1.show();
		//Get data from resource 2
		Dataset<Row> df2 = spark.read().format("json")
				.option("multiline", true)
				.load(fileUrl2);
		
		//Get data from resource 3
				Dataset<Row> df3 = spark.read().format("json")
						.option("multiline", true)
						.load(fileUrl3);
		
		// Register the DataFrame as a global temporary view
		df1.createOrReplaceTempView("parkingSensors1");
		df2.createOrReplaceTempView("parkingSensors2");
		df3.createOrReplaceTempView("parkingSensors3");
		
		// Select data from the two dataframes
		String querySelect1 = "SELECT '" + uuid1 + "' as UUID, CAST(date AS Timestamp) as Timestamp, value as isOccupied from parkingSensors1";
		String querySelect2 = "SELECT '" + uuid2 + "' as UUID, CAST(date AS Timestamp) as Timestamp, value as isOccupied from parkingSensors2";
		String querySelect3 = "SELECT '" + uuid3 + "' as UUID, CAST(date AS Timestamp) as Timestamp, value as isOccupied from parkingSensors3";
		
		Dataset<Row> sqlDf1 = spark.sql(querySelect1 + " UNION ALL " + querySelect2 + " UNION ALL " + querySelect3);
		sqlDf1.createOrReplaceTempView("allResources");
		
		String query1 = "SELECT window.start as start_time, window.end as end_time, isOccupied, count(*) as status_count FROM allResources GROUP BY window(Timestamp, \"10 minutes\"), isOccupied ORDER BY window.start";
		Dataset<Row> sqlDf2 = spark.sql(query1);
		sqlDf2.createOrReplaceTempView("resourcesByTime");
		
		//Query to filter the rows with true
		//String query2 = "SELECT A.start_time, A.end_time, A.status_count as N_occ, B.status_count as N_free "
		//		+ "FROM (SELECT * FROM resourcesByTime WHERE isOccupied=true) as A "
		//		+ "FULL OUTER JOIN (SELECT * FROM resourcesByTime WHERE isOccupied=false) as B "
		//		+ "ON A.start_time = B.start_time";
		//Dataset<Row> sqlDf3 = spark.sql(query2);
		//Dataset<Row> sqlDf2 = spark.sql("SELECT '" + uuid2 + "' as UUID, CAST(date AS Timestamp) as Timestamp, value as isOccupied from parkingSensors2");
		//sqlDf1.show();
		//System.out.println("SIZE: " + sqlDf3.count());
		sqlDf2.show(100);
		//sqlDf3.show();
		//sqlDf1.printSchema();
		//System.out.println(dataFromInterSCity);
		
	}

}
