/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Ui;

import Jeu.*;
import java.util.HashMap;

/**
 *
 * @author gueganb
 */
public class JoueurUI {
    private Monopoly monop;
    
    public  void afficheProprietes (Joueur j, int i){
        monop.getPlateau().messageLog("*** Joueur n°"+i+" : " + j.getNomJoueur()+" ***");
        monop.getPlateau().messageLog("Ses propriété(s) :");
        for (Compagnie c : j.getCompagnies()){
            monop.getPlateau().messageLog(c.getNom());
        }
        for (Gare g : j.getGares()){
            monop.getPlateau().messageLog(g.getNom());
        }
        for (ProprieteAConstruire p : j.getProprietesAConstruire()){
            monop.getPlateau().messageLog(p.getNom());
            monop.getPlateau().messageLog("Groupe : "+p.getGroupe());
            monop.getPlateau().messageLog("Avec "+p.getImmobilier()+" construction(s)");
        }
        monop.getPlateau().messageLog("Nombre de carte(s) sortie de prison : "+j.getNBCartePrison());
    }
    
    public  void afficheProprietes (Joueur j){
        monop.getPlateau().messageLog("*** Joueur : " + j.getNomJoueur()+" ***");
        monop.getPlateau().messageLog("Ses propriété(s) :");
        for (Compagnie c : j.getCompagnies()){
            monop.getPlateau().messageLog(c.getNom());
        }
        for (Gare g : j.getGares()){
            monop.getPlateau().messageLog(g.getNom());
        }
        for (ProprieteAConstruire p : j.getProprietesAConstruire()){
            monop.getPlateau().messageLog(p.getNom());
            monop.getPlateau().messageLog("Groupe : "+p.getGroupe());
            monop.getPlateau().messageLog("Avec "+p.getImmobilier()+" construction(s)");
        }
    }
    
    public  HashMap<Integer, CarreauPropriete> afficheProprietesEchangeables (Joueur j, Echange e,  HashMap<Integer,CarreauPropriete> listP){
        int i = 1;
                // Affichages des propriétés + intégration dans listP2 avec un indice
        for (Compagnie c : j.getCompagnies()){
            if (!e.getListP().contains(c)){
                monop.getPlateau().messageLog("Propriété n°"+i+" : "+c.getNom());
                listP.put(i, c);
                i++;
            }
        }
        for (Gare g : j.getGares()){
            if (!e.getListP().contains(g)){
                monop.getPlateau().messageLog("Propriété n°"+i+" : "+g.getNom());
                listP.put(i, g);
                i++;
            }
        } 
        for (CouleurPropriete c : CouleurPropriete.values()) {
            if (!j.getProprietesAConstruire(c).isEmpty()) {
                int immo = 0;
                for (ProprieteAConstruire p : j.getProprietesAConstruire(c)) {
                    immo+=p.getImmobilier();
                }
                if (immo==0) {
                    for (ProprieteAConstruire p : j.getProprietesAConstruire(c)) {
                        if (!e.getListP().contains(p)){
                            monop.getPlateau().messageLog("Propriété n°"+i+" : "+p.getNom());
                            monop.getPlateau().messageLog("Groupe : "+p.getGroupe());
                            listP.put(i,p);
                            i++; 
                        } 
                    }
                }
            }
        }
        return listP;
    }
    
    public  void printCashVous(Joueur j) {
        monop.getPlateau().messageLog("Vous avez maintenant " + j.getCash() + "€");
    }
     public  void printCashLe(Joueur j) {
        monop.getPlateau().messageLog("Le joueur " + j.getNomJoueur() + " a maintenant "+ j.getCash() + "€");
    }
    
     public  void errorArgent(Joueur j){
         monop.getPlateau().messageLog("Vous n'avez pas assez d'argent : vous avez " + j.getCash() + "€");
     }
     
     public  void printVosProprietes(Joueur j){
         monop.getPlateau().messageLog("Vos proriété(s) :");
         if (j.getProprietesAConstruire().isEmpty() && j.getGares().isEmpty() && j.getCompagnies().isEmpty()) {
             monop.getPlateau().messageLog("Vous n'avez aucune propriété...");
         } else {
             if (!j.getProprietesAConstruire().isEmpty()) {
                  monop.getPlateau().messageLog("Vos proriété(s) à construire :");
                  for (ProprieteAConstruire p : j.getProprietesAConstruire()){
                    monop.getpUi().printProprieteProprietaire(p);
                    }
             }
            if (!j.getGares().isEmpty()) {
                  monop.getPlateau().messageLog("Vos gare(s) :");
                  for (Gare g : j.getGares()){
                    monop.getpUi().printGare(g);
                    }
             }
            if (!j.getCompagnies().isEmpty()) {
                  monop.getPlateau().messageLog("Vos compagnies :");
                  for (Compagnie c : j.getCompagnies()){
                    monop.getpUi().printCompagnie(c);
                    }
             }
         }
     }
     
     public  void passageDepart(Joueur j) {
         monop.getPlateau().messageLog("Vous venez de passer par la case départ, vous recevez 200€");
         monop.getjUi().printCashVous(j);
     }
     
     public  void cLaFin(Joueur j) {
         monop.getPlateau().messageLog("Le joueur " + j.getNomJoueur() + " n'a pas de quoi payer, il fait donc faillite et sort du jeu...");
     }
     
     public  void okJeGere() {
         monop.getPlateau().messageLog("La faillite à été épargnée pour le moment");
     }
     
     public  void cBientotLaFin(Joueur j) {
        monop.getPlateau().messageLog("Le joueur "+j.getNomJoueur()+" n'a pas assez d'argent pour payer. Il doit detruire et/ou hypothequer"); 
     }
     public  void cLaFinDetruire() {
            monop.getPlateau().messageLog("Vous n'avez plus d'immobilier à détruire");
        }
        
        public  void cLaFinHypo() {
            monop.getPlateau().messageLog("Vous n'avez plus de propriete à hypothequer");
        }
        
    public String chooseLaFin() {
        return BoiteDialogUI.afficherVendreHypotheque(monop.getPlateau(),"Que voulez-vous faire ?");
    }    
    
    public  boolean sortDuJeu(Joueur j) {
        if (BoiteDialogUI.afficherBool(monop.getPlateau(),"Etes-vous sûr de vouloir arreter ? (oui/non)")) {
            monop.getPlateau().messageLog("Le joueur " + j.getNomJoueur() + " sort du jeu... Ses possessions retournent à la banque");
            return true;
        } else {
            return false;
        }
    }

    public JoueurUI(Monopoly aThis) {
        this.monop=aThis;
    }
    
   
}
