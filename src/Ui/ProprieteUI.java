/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Ui;

import Jeu.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author devaucod
 */


public class ProprieteUI {
    private Monopoly monop;

    
    public ProprieteUI(Monopoly monop){
        this.monop=monop;
    }
    public  void construction() {
        System.out.println("Construction :");
    }
    
    public  void aucunGroupeComplet() {
        System.out.println("Vous ne possédez aucun groupe de proprietes complet...");
    }
    
    public  CouleurPropriete chooseGroupe(HashMap<CouleurPropriete,ArrayList<ProprieteAConstruire>> list) {
        System.out.println("Vous possédez les groupes modifiables suivants :");
        for (CouleurPropriete c : list.keySet()) {
            System.out.println(c.toString());
        }
        boolean ok = false; //Boolean anti-faute de frappe
        CouleurPropriete coul = CouleurPropriete.bleuCiel; //Initialisation de variable
        do {
            try {
                coul = CouleurPropriete.valueOf(TexteUI.question("Sur quel groupe voulez-vous agir ?"));
                if (list.containsKey(coul)) {
                    ok = true;
                } else {
                    System.out.println("Cette couleur ne fait pas partie de la liste");
                }
            } catch(java.lang.IllegalArgumentException e) {
                System.out.println("Erreur, recommencez. (Sensible à la casse)");
            }
        } while (!ok);
        return coul;
    }
    
    public  void erreurHypo() {
        System.out.println("Au moins une des propriétés de ce groupe est hypothéquée, impossible de construire");
    }
    
    public  void erreurHotel() {
        System.out.println("Tous ces terrains on déjà des hotels, impossible de construire dessus");
    }
    
    public  void construireSur() {
        System.out.println("Vous pouvez construire sur :");
    }
    
    public  void printPropriete(ProprieteAConstruire p) {
        System.out.println(p.getNom() + " ; Construction existante : "+ getImmobilier(p) + " ; n°" + p.getNum());
    }
    
    public void printProprieteProprietaire(ProprieteAConstruire p) {
        System.out.println(p.getNom() + " ; Groupe : " + p.getCouleur().toString() + "(" + p.getProprietaire().getProprietesAConstruire(p.getCouleur()).size() + "/" + p.getNbPropriete() + ") ; Construction existante : "+ getImmobilier(p) + " ; n°" + p.getNum());
    }

    
    
    public  String getImmobilier(ProprieteAConstruire p) {
		String rep;
        switch (p.getImmobilier()) {
                    case 0:
                    {
                        rep = " vide";
                        break;
                    }
                    case 1:
                    {
                        rep = " une maison";
                        break;
                    }
                    case 2:
                    {
                        rep = " deux maisons";
                        break;
                    }
                    case 3:
                    {
                        rep = " trois maisons";
                        break;
                    }
                    case 4:
                    {
                        rep = " quatre maisons";
                        break;
                    }
                    case 5:
                    {
                        rep = " un hôtel";
                        break;
                    }
                    default:
                    {
                        rep = "error";
                        break;
                    }
                }
                return rep;
	}
    
        public  boolean errorBanque(boolean c) {
            String rep;
            if (c) {
                rep = " d'hotel ";
            } else {
                rep = " de maison ";
            }
            System.out.println("La banque n'a malheuresement plus" + rep + "en stock, vous ne pouvez pas construire sur ces terrains...");
            return true;
        }
        
        public int chooseNum() {
            return monop.gettUi().inte("Sur quel numéro voulez construire ? (numéro)");
        }
        
        public  void errorConstruction(){
            System.out.println("Ce terrain n'est pas constructible pour le moment");
        }
        
        public  void errorDestruction() {
            System.out.println("Ce terrain n'est pas destructible pour le moment");
        }
        
        public  void errorHypoNonProposee(){
            System.out.println("Erreur : Ce terrain n'est pas disponible à l'hypothèque, recommencez :");
        }
        
        public  void errorNonHypo(){
            System.out.println("Erreur : Ce terrain n'est pas hypothéqué, recommencez :");
        }
        
        public int chooseHypo() {
            return monop.gettUi().inte("Sur quelle propriété voulez-vous lever a ? (numéro)");
        }
        
        public  boolean encoreConstruire() {
            return BoiteDialogUI.afficherBool(monop.getPlateau(),"Voulez-vous construire une autre maison/hotel ? (oui/non)");
        }
        
        public  boolean encoreDetruire() {
            return BoiteDialogUI.afficherBool(monop.getPlateau(),"Voulez-vous detruire une autre maison/hotel ? (oui/non)");
        }
        
        public  void printDetruire() {
            System.out.println("Vous pouvez détruire sur les proprietes suivantes : (attention les hotels ne rendront pas des maisons)");
        }
        
        public  boolean errorVide() {
            System.out.println("Les terrains de ce groupe sont tous vides, vous ne pouvez plus rien détruire");
            return true;
        }
        
        public  String menuHypo(boolean display) {
            String rep;
            if (display) {
                rep = TexteUI.question("Voulez-vous lever une hypotheque ou hypothequer ? (lever/hypotheque)");
                while (rep.equals("lever") && rep.equals("hypotheque")) {
                    System.out.println("Erreur : vous devez repondre par lever ou hypotheque ! Recommencez :");
                    rep = TexteUI.question("Voulez-vous lever une hypotheque ou hypothequer ? (lever/hypotheque)");
                }
            } else {
                rep = "hypotheque";
            }
            return rep;    
        }
        
        public  void printListHypo(){
            System.out.println("Liste des proprietes hypothéquées :");
        }
        
        public  void printGare(Gare g) {
            System.out.println("Gare : " + g.getNom() + " ; n°" + g.getNum());
        }
        
        public  void printCompagnie(Compagnie c) {
            System.out.println("Compagnie : " + c.getNom() + " ; n°" + c.getNum());
        }
        
        public  void errorHypo() {
            System.out.println("Vous n'avez aucune propriété hypothequée");
        }
         
        public  void leverHypo(CarreauPropriete c) {
            System.out.println("Lever l'hypothèque de cette propriété coute : " + c.getPrixHypotheque() + "€");
        }
        
        public  boolean continuerHypo() {
            return BoiteDialogUI.afficherBool(monop.getPlateau(),"Voulez-vous continuer ? (oui/non)");
        }
        
        public  void hypoDispo() {
            System.out.println("Liste des proprietes disponible à l'hypotheque :");
        }
        
        public  void printHypo(CarreauPropriete c) {
            System.out.println("L'hypotheque vous rapporte" + c.getPrix()/2 + "€");
        }
        
        public boolean printAchat(CarreauPropriete c) {
            monop.getPlateau().messageLog("Cette " + c.getClass().getSimpleName() + " : " + c.getNom() + " est disponible à l'achat au prix de " + c.getPrix() +"€");
            return BoiteDialogUI.afficherBool(monop.getPlateau(),"Voulez-vous acheter cette " + c.getClass().getSimpleName() + " ?");
        }
        
        public  void printProprePAC(CarreauPropriete c) {
            System.out.println("Vous êtes sur votre : " + c.getNom());
        }
        
        public  boolean toucherLoyer(CarreauPropriete c) {
             System.out.println("Vous êtes tomber sur une " + c.getClass().getSimpleName() + " qui à déjà un propriétaire.");
             if (c.isHypotheque()) {
                 System.out.println("Cette " + c.getClass().getSimpleName() + " est hypothéquée, vous ne payez rien");
                 return false;
             } else {
                System.out.println("Vous devez payer " + c.getLoyer() + "€");
                return true;
             }
        }
        
        public  void nouvelleConstruction(ProprieteAConstruire p) {
            System.out.println("Ce terrain dispose maintenant de : " + getImmobilier(p));
            System.out.println("Les joueurs qui passeront sur ce terrain payeront : " + p.getLoyer() + "€");
            monop.getjUi().printCashVous(p.getProprietaire());
        }
        
        public  void destruction(ProprieteAConstruire p) {
            System.out.println("la vente de cette maison vous rapporte " + p.getProprietaire().getCash() + "€");
            System.out.println("Il reste sur ce terrain : " + getImmobilier(p));
        }
        
        public void payerCompagnie(int value) {
            System.out.println("Le proprietaire possédant une seule compagnie, vous allez payer 4 fois la somme de votre lancer de dés");
            System.out.println("Vous avez abtenu " + value/10 + " aux dés, donc vous payer : " + value +"€");
        }
        
        public void payerCompagnies(int value) {
            System.out.println("Le proprietaire possédant les deux compagnies, vous allez payer dix fois la somme de votre lancer de dés");
            System.out.println("Vous avez abtenu " + value/10 + " aux dés, donc vous payer : " + value +"€");
        }
        
         public boolean jEssaye() {
            return BoiteDialogUI.afficherBool(monop.getPlateau(),"Voulez-vous essayé de vendre ou d'hypothequer pour avoir assez d'argent ?");
        }
         
        
        public void destructionHotel(ProprieteAConstruire p,int argent) {
            System.out.println("La vente de cet hotel vous rapporte " + argent + "€");
            System.out.println("Ce terrain est maintenant vide de construction");
            monop.getjUi().printCashVous(p.getProprietaire());
        }
         
        public void possession(Joueur j) {
            System.out.println(j.getNomJoueur() + " possède maintenant tout les propriétés du groupe, il peut désormais construire dessus");
        }
        
        public void destructionMaison(ProprieteAConstruire p,int argent) {
            System.out.println("La vente de cette maison vous rapporte " + argent + "€");
            if(p.getImmobilier()>0) {
                System.out.println("Il reste sur ce terrain : " + getImmobilier(p));
            } else {
                System.out.println("Ce terrain est maintenant vide de construction");
            }
            monop.getjUi().printCashVous(p.getProprietaire());
        }

}