package Jeu;

import Ui.CarreauUI;

public class CarreauMouvement extends CarreauAction {
    
    public CarreauMouvement(int n,String nom,Monopoly m){
        super(n,nom,m);
    }
    
    @Override
    public void action(Joueur j){
        super.getMonopoly().getcUi().allerEnPrison();
        j.enPrison();
    }
}