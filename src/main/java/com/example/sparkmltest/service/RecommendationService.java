package com.example.sparkmltest.service;


import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

@Service
public class RecommendationService implements InitializingBean {

    @Autowired
    private SparkSession sparkSession;
    private ALSModel model;

    //@PostConstruct
    public void trainModel(){
        Dataset<Row> ratings = loadRatingsData();
        model = trainALSModel(ratings);
        System.out.println("Training the model completed..");
    }

    private Dataset<Row> loadRatingsData(){
        String path = "movie_ratings_sample.csv";
        return sparkSession.read().format("csv")
                .option("header","true")
                .option("inferSchema","true")
                .load(path);
    }

    private ALSModel trainALSModel(Dataset<Row> ratings){
        ALS als = new ALS()
                .setMaxIter(10)
                .setRegParam(0.1)
                .setUserCol("user_id")
                .setItemCol("movie_id")
                .setRatingCol("rating");

        ALSModel model = als.fit(ratings);
        model.setColdStartStrategy("drop");

        return model;

    }

    public void printRows (){

        //sparkSession.sparkContext().setLogLevel("ERROR");

        Dataset<Row> rows = loadRatingsData();

        System.out.println("Dataframe:");
        rows.show();
        System.out.println("Scheme:");
        rows.printSchema();

    }

    public List<String> getRecommendations(int user_id){

        printRows();

        //Dataset<Row> users = sparkSession.createDataFrame(Collections.singletonList(Integer.valueOf(user_id)),Integer.class)
        //        .toDF("user_id");

        String filter = "user_id="+user_id;

        Dataset<Row> users = loadRatingsData().filter(filter);
        users.printSchema();
        users.show();
        //Dataset<Row> users = sparkSession.createDataset(users.filter(), Integer.class);

        //Dataset<Row> users = sparkSession.createDataset(Collections.singletonList(user_id),Encoders.STRING());


        Dataset<Row> recommendations = model.recommendForUserSubset(users,5);
        List<Row> rows = recommendations.collectAsList();

        if (rows.isEmpty()){
            return Collections.emptyList();
        }

        Row row=rows.get(0);
        List<Row> records = row.getList(1);
        List<String> movieIds = new ArrayList<>();

        for(Row rec :records){
            movieIds.add(rec.getAs(0).toString());
        }

        return movieIds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        trainModel();
    }
}
