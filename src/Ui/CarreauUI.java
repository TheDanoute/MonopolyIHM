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
public class CarreauUI {
    private Monopoly monop;
    
    public CarreauUI (Monopoly m){
        this.monop=m;
    }
    public  void accueilPrison(int nb){
        monop.getPlateau().messageLog("Vous êtes en prison, nombre de carte sortie de prison disponible : " + nb);
    }
    
    public  String choixActionPrison() {
        String   rep = BoiteDialogUI.afficherPayerCarteDes(monop.getPlateau(),"Que voulez-vous faire ?");       
        return rep;
    }
    
    public  String toMuchWastedTime() {
        String rep = TexteUI.question("Vous ne pouvez pas lancer les dés quatre fois de suite, vous devez payer ou utiliser une carte. (payer/carte)");
            rep = BoiteDialogUI.afficherPayerCarte(monop.getPlateau(),"Que voulez-vous faire ? (payer/carte)");
        return rep;
    }
    
    public  void prisonPayer(Joueur j) {
        monop.getPlateau().messageLog("Vous devez payer 50€");
        monop.getjUi().printCashVous(j);
    }
    
    public  void errorCartePrison() {
        monop.getPlateau().messageLog("Vous n'avez pas de carte sortie de prison...");
    }

    public  void desPrison(int d1, int d2) {
        monop.getPlateau().messageLog("Vous avez obtenu : D1 = " + d1 + " ; D2 = " + d2);
        if (d1==d2) {
           monop.getPlateau().messageLog("Vous sortez de prison");
        } else {
           monop.getPlateau().messageLog("Vous restez en prison"); 
        }
    }
    
    public  void allerEnPrison() {
        monop.getPlateau().messageLog("Vous êtes tombé sur la case Allez en Prison ! Vous allez directement en prison sans passer par la case départ");
    }
    
    public  void visitePrison() {
        monop.getPlateau().messageLog("Vous êtes en visite à la prison.");
    }
    
    public  void removeArgent(int value) {
        monop.getPlateau().messageLog("Vous devez payer : "+value+"€");
    }
    
    public  void addArgent(int value) {
        monop.getPlateau().messageLog("Vous recevez : "+value+"€");
    }
    
    public  void piocherCarte(CarteType type) {
        monop.getPlateau().messageLog("Vous piochez une carte " + type.toString());
    }
}
