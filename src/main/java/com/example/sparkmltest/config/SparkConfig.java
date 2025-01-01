package com.example.sparkmltest.config;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.codehaus.janino.Java;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Value("${spark.app.name}")
    private String appName;
    @Value("${spark.master}")
    private String masterUri;
    @Value("${spark.home}")
    private String home;




    @Bean
    public SparkSession sparkSession(){
        return SparkSession.builder().sparkContext(mySparkConfig().sc())
                .getOrCreate();
    }



    @Bean
    public SparkConf conf(){

        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName(appName);
        sparkConf.setMaster(masterUri);
        sparkConf.setSparkHome(home);

        //sparkConf.set("spark.driver.host","192.168.0.210");
        //sparkConf.set("spark.driver.port","63934");

        return sparkConf;
    }

    @Bean
    public JavaSparkContext mySparkConfig(){

        SparkConf sc = conf();
        JavaSparkContext javaSparkContext= new JavaSparkContext(sc);

        return javaSparkContext;
    }

}
