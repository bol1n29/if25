/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.utt.entities;

/**
 *
 * @author kevin
 */
public class UserFeatures {

    private long Id;
    private String name;
    private int nbtweets=0;
    private int nbhashtags=0;
    private int nbcontributors=0;
    private int nbmediaEntities=0;
    private int nbextendedMediaEntities=0;
    private int nbsymbolEntities=0;
    private int nbMaliciousEntity=0;
    private int nbmentionEntities=0;
    private int nbfriends=0;
    private int nbCharacterPerTweets=0;
    private int amountOfTweet=0;

    public UserFeatures() {
    }



    
    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getNbhashtags() {
        return nbhashtags;
    }

    public void setNbhashtags(int nbhashtags) {
        this.nbhashtags = nbhashtags;
    }

    public int getNbcontributors() {
        return nbcontributors;
    }

    public void setNbcontributors(int nbcontributors) {
        this.nbcontributors = nbcontributors;
    }

    public int getNbmediaEntities() {
        return nbmediaEntities;
    }

    public void setNbmediaEntities(int nbmediaEntities) {
        this.nbmediaEntities = nbmediaEntities;
    }

    public int getNbextendedMediaEntities() {
        return nbextendedMediaEntities;
    }

    public void setNbextendedMediaEntities(int nbextendedMediaEntities) {
        this.nbextendedMediaEntities = nbextendedMediaEntities;
    }

    public int getNbsymbolEntities() {
        return nbsymbolEntities;
    }

    public void setNbsymbolEntities(int nbsymbolEntities) {
        this.nbsymbolEntities = nbsymbolEntities;
    }

    public int getMaliciousEntity() {
        return nbMaliciousEntity;
    }

    public void setMaliciousEntity(int nbMaliciousEntity) {
        this.nbMaliciousEntity = nbMaliciousEntity;
    }

    public int getNbmentionEntities() {
        return nbmentionEntities;
    }

    public void setNbmentionEntities(int nbmentionEntities) {
        this.nbmentionEntities = nbmentionEntities;
    }

    public int getNbCharacterPerTweets() {
        return nbCharacterPerTweets;
    }

    public void setNbCharacterPerTweets(int nbCharacterPerTweets) {
        this.nbCharacterPerTweets = nbCharacterPerTweets;
    }

    public int getNbfriends() {
        return nbfriends;
    }

    public void setNbfriends(int nbfriends) {
        this.nbfriends = nbfriends;
    }

    public int getNbMaliciousEntity() {
        return nbMaliciousEntity;
    }

    public void setNbMaliciousEntity(int nbMaliciousEntity) {
        this.nbMaliciousEntity = nbMaliciousEntity;
    }

    public int getAmountOfTweet() {
        return amountOfTweet;
    }

    public void setAmountOfTweet(int amountOfTweet) {
        this.amountOfTweet = amountOfTweet;
    }

    public int getNbtweets() {
        return nbtweets;
    }

    public void setNbtweets(int nbtweets) {
        this.nbtweets = nbtweets;
    }
    
    
    
}
