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
public class Scorer {
    private double dangerosity;
    private double aggressivity;
    private double visibilty;
    private  UserFeatures uf;
    public Scorer( UserFeatures uf) {
        this.uf = uf;
        this.aggressivity=(uf.getNbtweets()+uf.getNbfriends())/350;
        this.visibilty=(uf.getNbmentionEntities()*11.4+uf.getNbhashtags()*11.6)/180;
    }

    public double getAggressivity() {
        return aggressivity;
    }

    public void setAggressivity(double aggressivity) {
        this.aggressivity = aggressivity;
    }

    public double getVisibilty() {
        return visibilty;
    }

    public void setVisibilty(double visibilty) {
        this.visibilty = visibilty;
    }

    public double getDangerosity() {
        return dangerosity;
    }

    public void setDangerosity(double totaloftweets) {
        this.dangerosity=(this.uf .getMaliciousEntity())/totaloftweets;
//        this.dangerosity=(this.uf .getMaliciousEntity()+this.uf .getNbfriends())/totaloftweets;
    }
    
    
    
    
    
}
