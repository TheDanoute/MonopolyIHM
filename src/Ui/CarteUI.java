/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Ui;

import Jeu.*;

/**
 *
 * @author devaucod
 */
public class CarteUI {
    private Monopoly monop;
    public CarteUI (Monopoly monop){
        this.monop=monop;
    }
    public  void deplacement(Carreau c){
        monop.getPlateau().messageLog("Vous êtes donc a la case n°" + c.getNum() + ":" + c.getNom());
    }
    
    public  void payerHotelMaison(int pM, int nbM, int pH, int nbH) {
        monop.getPlateau().messageLog("Prix pour les maisons : " + pM + "*" + nbM + "=" + pM*nbM);
        monop.getPlateau().messageLog("Prix pour les hotels : " + pH + "*" + nbH + "=" + pH*nbH);
        int somme = nbM*pM+nbH*pH;
        monop.getPlateau().messageLog("Total : " + somme + "€");
    }
    
    public  String jAiDeLaChance() {
        return BoiteDialogUI.afficherChancePayer(monop.getPlateau(),"Vous choisissez ?");
    }
    
    public  void printInfo(Carte c) {
        monop.getPlateau().messageLog("Carte type : " + c.getType() + " ; " + c.getDescription());
    }

    public  void laChance() {
        monop.getPlateau().messageLog("Vous êtes passé par la case départ");
    }
}
