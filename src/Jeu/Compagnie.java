package Jeu;

import java.util.Random;

public class Compagnie extends CarreauPropriete {
        
        public Compagnie(int num,String nom,Monopoly m,int p) {
            super(num,nom,m,p);
        }

        @Override
	public int getLoyer() {
            Random gene = new Random();
                if (this.getNbPropriete()>1) {
                    int i = (gene.nextInt(6)+1)*10;
                    super.getMonopoly().getpUi().payerCompagnies(i);
                    return i;
                } else {
                    int i = (gene.nextInt(6)+1)*4;
                     super.getMonopoly().getpUi().payerCompagnie(i);
                    return i;
                }
	}
           
        @Override
	public int getNbPropriete() {
		return super.getProprietaire().getNombreCompagnies();
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
                    super.getMonopoly().getjUi().printCashLe(super.getProprietaire());
                    super.getMonopoly().getjUi().printCashLe(j);
                 }
            }
            super.getMonopoly().getPlateau().update();
        }
        @Override
        public void setProprietaire(Joueur j) {
            if (this.getProprietaire()!=null) {
                this.getProprietaire().removeCompagnie(this);
            }
            super.setProprietaire(j);
            j.addCompagnie(this);
        }
        /*@Override
        public String getDescription() {
            if (super.isHypotheque()) {
                return "Compagnie HYPOTHEQUEE : " + super.getDescription();
            } else {
                return "Compagnie : " + super.getDescription();
            }
        }*/
        
        @Override
        public void retourBanque(){
            super.retourBanque();
            this.setProprietaire(null);
        }
}