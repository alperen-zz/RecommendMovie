package com.example.sparkmltest.service;


import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.sql.Dataset;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;

import java.util.List;

@Service
public class RecommendationService implements InitializingBean {

    @Autowired
    private SparkSession sparkSession;
    private ALSModel model;

    private static Dataset<Row> rowDataset;

    public void trainModel(){
        Dataset<Row> ratings = loadRatingsData();
        model = trainALSModel(ratings);
        System.out.println("Training the model completed..");
    }

    private Dataset<Row> loadRatingsData(){
        String path = "movie_ratings_sample.csv";
        this.rowDataset = sparkSession.read().format("csv")
                .option("header","true")
                .option("inferSchema","true")
                .load(path);

        return rowDataset;
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


        String filter = "user_id="+user_id;

        Dataset<Row> users = this.rowDataset.filter(filter);
        users.printSchema();
        users.show();


        Dataset<Row> recommendations = model.recommendForUserSubset(users,5);
        List<Row> rows = recommendations.collectAsList();

        if (rows.isEmpty()){
            return Collections.emptyList();
        }
        System.out.println("Size of the recommendation list:"+ rows.size());
        Row row=rows.get(0);
        List<Row> records = row.getList(1);
        List<String> movieIds = new ArrayList<>();

        String movieIdFilter = "movie_id=";
        List<Row> movieNames;
        String movieName;

        for(Row rec :records){

            movieNames = this.rowDataset.filter(movieIdFilter+rec.getAs(0).toString()).collectAsList();
            Row tempRow = movieNames.get(0);
            movieName = tempRow.getAs("movie_name");
            movieIds.add(movieName);

            //movieIds.add(rec.getAs(0).toString());

        }
        //movieIds contains movie names :)
        return movieIds;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        trainModel();
    }
}
