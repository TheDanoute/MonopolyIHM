/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Jeu;

/**
 *
 * @author devaucod
 */
public class CarreauPrison extends Carreau {
    
    public CarreauPrison(int n,String nom,Monopoly m) {
        super(n,nom,m);
    }
    
    @Override
    public void action(Joueur j){
        if (j.getPrison()) {
            int nbCarte = j.getNBCartePrison();
            super.getMonopoly().getcUi().accueilPrison(nbCarte);
            boolean fini = false;
            while (!fini) {
            String rep = super.getMonopoly().getcUi().choixActionPrison();
            if (j.getNBTourPrison()>3 && rep.equals("Des")) {
                rep = super.getMonopoly().getcUi().toMuchWastedTime();
            }
            if (rep.equals("Payer")) {
                j.removeCash(50);
                j.sortPrison();
                fini = true;
                super.getMonopoly().getcUi().prisonPayer(j);
                super.getMonopoly().lancerDes(0);
            } else if (rep.equals("Carte")) {
                if (nbCarte==0) {
                    super.getMonopoly().getcUi().errorCartePrison();
                } else {
                    j.utilCartePrison();
                    fini = true;
                }
            } else {
                int d1,d2;
                d1 = this.getMonopoly().lancerDe();
                d2 = this.getMonopoly().lancerDe();
                super.getMonopoly().getcUi().desPrison(d1,d2);
                if (d1==d2) {
                    j.sortPrison();
                    this.getMonopoly().lancerDes(d1+d2);
                    fini = true ;
                } else {
                    j.ajouterTourPrison();
                    fini = true ;
                }
            }
            }
        } else {
            super.getMonopoly().getcUi().visitePrison();
        }
            
    }
}
