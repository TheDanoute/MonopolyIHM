package Jeu;

import java.util.ArrayList;
import java.util.Scanner;

public class ProprieteAConstruire extends CarreauPropriete {
	private int immobilier;
	private ArrayList<Integer> loyers;
	private Groupe groupe;
        private int prixMaison;

	public ProprieteAConstruire(int num,String nom,Monopoly m,int p,ArrayList<Integer> l,Groupe g,int pM){
            super(num,nom,m,p);
            this.setImmobilier(0);
            this.setLoyers(l);
            this.setGroupe(g);
            this.setPrixMaison(pM);
        }
        
        @Override
        public int getLoyer(){
            if (immobilier==0 && super.getProprietaire().getProprietesAConstruire(this.getCouleur()).size()==this.getNbPropriete() && this.noHypo()) {
                return loyers.get(immobilier)*2;
            } else {
                return loyers.get(immobilier);
            }
        }
        
        public ArrayList<Integer> getListLoyers() {
            return loyers;
        }

        private void setLoyers(ArrayList<Integer> l){
            loyers=l;
        }
        
        public int getImmobilier() {
		return immobilier;
	}

	public void setImmobilier(int i) {
		immobilier=i;
	}
        
        private void addImmobilier() {
		immobilier++;
	}
        
        private void removeImmobilier() {
		immobilier--;
	}
        
        public Groupe getGroupe(){
            return groupe;
        }
        
        private void setGroupe(Groupe g){
            groupe=g;
            groupe.ajouterPropriete(this);
        }
        
        @Override
	public int getNbPropriete() {
            return groupe.getNbPropriete();
	}
        
        public CouleurPropriete getCouleur(){
            return this.groupe.getCouleur();
        }

        public int getPrixMaison() {
            return prixMaison;
        }

        public void setPrixMaison(int prixMaison) {
            this.prixMaison = prixMaison;
        }
        
        public void construire() {
            if (this.getProprietaire().getCash()<this.getPrixMaison()) {
                super.getMonopoly().getjUi().errorArgent(super.getProprietaire());
            } else {
                this.getProprietaire().removeCash(this.getPrixMaison());
                this.getProprietaire().getMonopoly().getPlateau().setValues(this.getProprietaire());
                this.addImmobilier();
                if (this.getImmobilier()<5) {
                    super.getMonopoly().removeMaison();
                } else {
                    super.getMonopoly().addMaison(4);
                    super.getMonopoly().removeHotel();
                }
                super.getMonopoly().getPlateau().update();
                super.getMonopoly().getpUi().nouvelleConstruction(this);
            }
        }
        
        
        public void detruire() {
            int argent;
            if(this.getImmobilier()>4) {
                this.setImmobilier(0);
                super.getMonopoly().addHotel();
                argent = (int)(prixMaison*2.5);
                super.getProprietaire().addCash(argent);
                this.getProprietaire().getMonopoly().getPlateau().setValues(this.getProprietaire());
                super.getMonopoly().getpUi().destructionHotel(this,argent);
            } else {
                this.removeImmobilier();
                super.getMonopoly().addMaison();
                argent = prixMaison/2;
                super.getProprietaire().addCash(argent);
                super.getMonopoly().getpUi().destructionMaison(this,argent);
            }
            super.getMonopoly().getPlateau().update();
        }
        
        @Override
        public void setProprietaire(Joueur j) {
            if (this.getProprietaire()!=null) {
                this.getProprietaire().removePropriete(this);
            }
            super.setProprietaire(j);
            j.addPropriete(this);
            if (j.getProprietesAConstruire(this.getCouleur()).size()==this.getNbPropriete()) {
                super.getMonopoly().getpUi().possession(super.getProprietaire());
            }
        }
        @Override
        public void action(Joueur j){
            if (this.getProprietaire()==null) {
                if (super.getMonopoly().getpUi().printAchat(this)) {
                    if (j.getCash()>=this.getPrix()) {
                        j.removeCash(this.getPrix());
                        j.getMonopoly().getPlateau().setValues(j);
                        super.getMonopoly().getjUi().printCashVous(j);
                        this.setProprietaire(j);
                    } else {
                        super.getMonopoly().getjUi().errorArgent(j);
                        if (super.getMonopoly().getpUi().jEssaye()) {
                            if (j.jEssaye(super.getPrix())) {
                                j.removeCash(this.getPrix());
                                j.getMonopoly().getPlateau().setValues(j);
                                super.getMonopoly().getjUi().printCashVous(j);
                                this.setProprietaire(j);
                            }
                        }
                    }
                }
            } else if (this.getProprietaire()==j) {
                super.getMonopoly().getpUi().printProprePAC(this);
            } else {
                if (super.getMonopoly().getpUi().toucherLoyer(this)) {
                    int l = this.getLoyer();
                    j.removeCash(l);
                    j.getMonopoly().getPlateau().setValues(j);
                    this.getProprietaire().addCash(l);
                    this.getProprietaire().getMonopoly().getPlateau().setValues(this.getProprietaire());
                    super.getMonopoly().getjUi().printCashLe(super.getProprietaire());
                    super.getMonopoly().getjUi().printCashLe(j);
                 }
            }
            super.getMonopoly().getPlateau().update();
        }
        
        
        public boolean noHypo() {
            boolean retour = true;
            ArrayList<ProprieteAConstruire> listP = groupe.getProprietes();
            int i = 0;
            while (retour && i<listP.size()) {
                retour = !listP.get(i).isHypotheque();
                i++;
            }
            return retour;
        }
        
        @Override
        public void retourBanque() {
            super.retourBanque();
            if (this.getImmobilier()>4) {
                this.getMonopoly().addHotel();
            } else {
                this.getMonopoly().addMaison(immobilier);
            }
            this.setImmobilier(0);
        }
}