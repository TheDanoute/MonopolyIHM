/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jeu;


/**
 *
 * @author DanJeux
 */
public class CarteArgent extends Carte{
    
    private int montant;
    
    public CarteArgent(String t,String d,int m) {
        super(t,d);
        this.setMontant(m);
    }

    public int getMontant() {
        return montant;
    }

    private void setMontant(int montant) {
        this.montant = montant;
    }
    
    @Override
    public void action(Joueur j){
        j.getMonopoly().getPlateau().messageLog(this.getDescription());
        j.addCash(montant);
        j.getMonopoly().getjUi().printCashVous(j);
    }
    
}
