/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Ui;
import Jeu.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author devaucod
 */
public class TexteUI {
    private Monopoly monop;
    
    public TexteUI (Monopoly monop){
        this.monop=monop;
    }
    
    public  void message (String s){
        monop.getPlateau().messageLog(s);
    }
    
    public static String question(String question){
        System.out.println(question);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }
    
    public  void echange(){
        monop.getPlateau().messageLog("********** Echange **********");
    }
    
    public Joueur joueurChoisisEchange(ArrayList<Joueur> listJ){
        Joueur [] joueurs = new Joueur[listJ.size()];
        int i=0;
        for(Joueur j : listJ){
            joueurs[0] = j;
            i++;
        }
        return BoiteDialogUI.afficherChoixNumero(monop.getPlateau(), "Avec quel joueur voulez-vous échanger ?", joueurs);   
    }
    
    public  String typeEchange (){
        return BoiteDialogUI.afficherProCarte(monop.getPlateau(),"Que voulez-vous de ce joueur ? (propriete / carte)");
    }
    
    public  int choixPropEch (){
        return Integer.valueOf(TexteUI.question("Quelle propriete voulez-vous ? (nombre)"));
    }
    
    public  String autrePropEch (){
        return BoiteDialogUI.afficherBoolS(monop.getPlateau(),"Voulez-vous une autre propriété de ce joueur ? (oui/non)");
    }
    
    public  String rajouteArgent(){
        return BoiteDialogUI.afficherBoolS(monop.getPlateau(),"Voulez-vous rajouter de l'argent ? (oui/non)");
    }
    
    
    
    public  void pasPropEch (Joueur j){
        monop.getPlateau().messageLog(j.getNomJoueur()+" n'a pas de propriété échangeable");
    }
    
    public  void vosProps(){
        monop.getPlateau().messageLog("*** Vos propriétés ***");
    }
    
    public  String autrePropEch2 (){
        return BoiteDialogUI.afficherBoolS(monop.getPlateau(),"Voulez-vous proposer une autre propriété ? (oui/non)");
    }
    
    public  void pasEchRienProp(){
        monop.getPlateau().messageLog("Pas d'échange possible, vous n'avez rien proposé.");
    }
    
    public  String propositionEchange(Echange echangeJ1, Echange echangeJ2, Joueur j1, Joueur j2){      
        monop.getPlateau().messageLog(j2.getNomJoueur()+", "+j1.getNomJoueur()+" vous a proposé un échange.\nIl vous demande :");
        for (CarreauPropriete cp : echangeJ2.getListP()){
            monop.getPlateau().messageLog(cp.getNom());
        }
        monop.getPlateau().messageLog(String.valueOf(echangeJ2.getSomme())+"€");
        monop.getPlateau().messageLog("Et il vous propose :");
        for (CarreauPropriete cp : echangeJ1.getListP()){
            monop.getPlateau().messageLog(cp.getNom());
        }
        monop.getPlateau().messageLog(String.valueOf(echangeJ1.getSomme())+"€");
        return BoiteDialogUI.afficherBoolS(monop.getPlateau(),"Acceptez-vous l'échange ? (oui/non)");
    }
    
    public  void pasCartePrison (Joueur j){
        monop.getPlateau().messageLog(j.getNomJoueur()+" n'a pas de carte sortie de prison.");
    }
    
    public  String propositionEchange (Joueur j2, Joueur j1, int somme){
        return BoiteDialogUI.afficherBoolS(monop.getPlateau(),j2.getNomJoueur()+", "+j1.getNomJoueur()+" vous propose "+somme+"€ contre une carte sortie de prison, êtes-vous d'accord? (oui/non)");
    }
    
    public  void echAnnul(Joueur j){
        monop.getPlateau().messageLog("Echange annulé, "+j.getNomJoueur()+" l'a refusé.");
    }
    
    public  void sesProps(){
        monop.getPlateau().messageLog("*** Ses propriétés ***");
    }
    
    public  void pasEncoreEch(){
        monop.getPlateau().messageLog("Vous ne pouvez pas faire d'échange, toutes les propriétés n'ont pas encore été vendues");
    }
    
    public  int inte(String g) {
        do {
            try { 
                return Integer.valueOf(question(g));
            } catch(java.lang.NumberFormatException e) {
                System.out.println("Vous devez rentrer un numéro, recommencez :");
            }
        } while(true);
    }
    
    public  void dés(int d1,int d2) {
       int sD = d1+d2;
       monop.getPlateau().messageLog("D1 = " + d1 + " ; D2 = " + d2 + " ; Somme des dés = " + sD); 
    }
    
    public  void printAncienCarreau(Carreau c) {
        monop.getPlateau().messageLog("Ancien Carreau : " + c.getNom());
    }
    
    public  void printNouveauCarreau(Carreau c) {
        monop.getPlateau().messageLog("Nouveau Carreau : " + c.getNom());
    }
    
    public void errorFile(int error) {
       switch (error) {
           case 0:
           {
                System.err.println("[buildGamePleateau()] : Invalid Data type");
                break;
           }
           case 1:
           {
                System.err.println("[buildGamePlateau()] : File is not found!");
                break;
           }
           case 2:
           {
                System.err.println("[buildGamePlateau()] : Error while reading file!");
                break;
           }
        }
    }
    
    public static void printInfo(Carreau c) {
        String info = "";
        try {
            ProprieteAConstruire pAC = (ProprieteAConstruire)c;
            info = "Propriété : " + c.getNom() + " ; groupe : " + pAC.getCouleur().toString() +" ; case : " + c.getNum();
        } catch(java.lang.ClassCastException e) {    
            info = c.getClass().getSimpleName() + " : " + c.getNom() + " ; case : " + c.getNum();
        } finally {
            System.out.println(info);
        }
    }
    
    public void trieJoueur(Joueur j,int value) {
        System.out.println("Etant arriver ex aequo, " + j.getNomJoueur() + " relance le dé et obtient : " + value);
    }
    
}
