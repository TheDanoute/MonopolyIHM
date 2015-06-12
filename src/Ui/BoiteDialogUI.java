/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Ui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Jeu.*;
import java.util.ArrayList;

/**
 *
 * @author laurillau
 */
public class BoiteDialogUI {
    /**
     * Boîte de dialogue pour confirmer la suppression d'un contact
     * @param c un contact
     * @return vrai si confirmé
     */
    public static boolean afficherBool(JFrame fenetre, String s) {
        boolean res = false;

        if (s != null) {
            String [] choix = new String[] { "Oui", "Non" }; 
            
            Object selectedValue = JOptionPane.showOptionDialog(fenetre,
                  s, 
                  "Confirmation",
                  JOptionPane.DEFAULT_OPTION,
                  JOptionPane.QUESTION_MESSAGE, 
                  null,
                  choix,
                  choix[1]);
            res = (((Integer) selectedValue) == 0);
        }
        
        return res;
    }
    
    public static String afficherBoolS(JFrame fenetre, String s) {

        boolean res = false;
        if (s != null) {
            String [] choix = new String[] { "Oui", "Non" }; 
            Object selectedValue = JOptionPane.showOptionDialog(fenetre,
                  s, 
                  "Confirmation",
                  JOptionPane.DEFAULT_OPTION,
                  JOptionPane.QUESTION_MESSAGE, 
                  null,
                  choix,
                  choix[1]);
            res = (((Integer) selectedValue) == 0);
        }
        if (res){
            return "oui";
        }else{
           return "non"; 
        }    
    }
    
    public static String afficherProCarte(JFrame fenetre, String s) {

        boolean res = false;
        if (s != null) {
            String [] choix = new String[] { "Propriete", "Carte" }; 
            Object selectedValue = JOptionPane.showOptionDialog(fenetre,
                  s, 
                  "Confirmation",
                  JOptionPane.DEFAULT_OPTION,
                  JOptionPane.QUESTION_MESSAGE, 
                  null,
                  choix,
                  choix[1]);
            res = (((Integer) selectedValue) == 0);
        }
        if (res){
            return "Propriete";
        }else{
           return "Carte"; 
        }    
    }
    
    public static Joueur afficherChoixNumero(JFrame fenetre, String titre, Joueur [] listJ) {
        Joueur res = null;
        if (titre == null) { titre = "titre"; }
        
        Object selectedValue = JOptionPane.showInputDialog(fenetre, res, titre, 1, null, listJ, listJ[0]);
        res = ((Joueur) selectedValue);
        
        return res;
    }
    
    public static String afficherPayerCarte(JFrame fenetre, String s) {

        boolean res = false;
        if (s != null) {
            String [] choix = new String[] { "Payer", "Carte" }; 
            Object selectedValue = JOptionPane.showOptionDialog(fenetre,
                  s, 
                  "Confirmation",
                  JOptionPane.DEFAULT_OPTION,
                  JOptionPane.QUESTION_MESSAGE, 
                  null,
                  choix,
                  choix[1]);
            res = (((Integer) selectedValue) == 0);
        }
        if (res){
            return "Payer";
        }else{
           return "Carte"; 
        } 
    }
    
    public static String afficherPayerCarteDes(JFrame fenetre, String s) {
        String res = "";

        if (s != null) {
            String [] choix = new String[] { "Payer", "Carte", "Des" }; 
            
            int rang = JOptionPane.showOptionDialog(fenetre,
                  s, 
                  "Confirmation",
                  JOptionPane.DEFAULT_OPTION,
                  JOptionPane.QUESTION_MESSAGE, 
                  null,
                  choix,
                  choix[2]);
            res = choix[rang];
        }
        return res;
    }
    
    public static CarreauPropriete ajouterPropriete (JFrame fenetre, String titre, ArrayList<CarreauPropriete> listCp) {
        String res = null;
        String [] tabCp = new String [listCp.size()];
        int i =0;
        for (CarreauPropriete cp : listCp){
            tabCp[i]=cp.getNom();
            i++;
        }
        if (titre == null) { titre = "Ajouter une propriété"; }
        
        Object selectedValue = JOptionPane.showInputDialog(fenetre, res, titre, 1, null, tabCp, tabCp[0]);
        res = ((String) selectedValue);
        for (CarreauPropriete cp2 : listCp){
            if (res.equals(cp2.getNom())){
                return cp2;        
            }
        }
        return null;
    }
    
    public static Joueur choixJoueur (JFrame fenetre, String titre, Monopoly monop) {
        Joueur j;
        String res = null;
        String [] tabJ = new String [monop.getJoueurs().size()];
        int i =0;
        for (Joueur j2 : monop.getJoueurs()){
            tabJ[i]=j2.getNomJoueur();
            i++;
        }
        if (titre == null) { titre = "Choisir un joueur"; }
        
        Object selectedValue = JOptionPane.showInputDialog(fenetre, res, titre, 1, null, tabJ, tabJ[0]);
        res = ((String) selectedValue);
        for (Joueur j3 : monop.getJoueurs()){
            if (res==j3.getNomJoueur()){
                return j = j3;
            }
        }
        return null;
    }
    
    public static int choixSomme(JFrame fenetre, String titre) {
        int res =0;
        Integer [] listS = new Integer [5];
        listS[0]=50;
        listS[1]=100;
        listS[2]=250;
        listS[3]=500;
        listS[4]=1000;
        if (titre == null) { titre = "titre"; }
        
        Object selectedValue = JOptionPane.showInputDialog(fenetre, res, titre, 1, null, listS, listS[0]);
        res = ((Integer) selectedValue);
        
        return res;
    }
    
    
}
