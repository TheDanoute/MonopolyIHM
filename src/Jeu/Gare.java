package Jeu;


public class Gare extends CarreauPropriete {
        
        private int loyer;
        
        public Gare (int num,String nom,Monopoly m,int p) {
            super(num,nom,m,p);
            this.setLoyer(25);
        }

    
        
        @Override
	public int getLoyer() {
		return loyer*2^(this.getNbPropriete()-1);//Echelle : 25-50-100-200€
	}
        private void setLoyer(int l){
            loyer=l;//Toujours égal à 25€
        }

        @Override
	public int getNbPropriete() {
		return super.getProprietaire().getNombreGare();//Revoies le nombre de gare du propriétaire
	}

        @Override
        public void action(Joueur j){
            if (this.getProprietaire()==null) {//Achat possible
                if (super.getMonopoly().getpUi().printAchat(this)) {//Si le joueur a assez d'argent
                    if (j.getCash()>=this.getPrix()) {
                        j.removeCash(this.getPrix());
                        j.getMonopoly().getPlateau().setValues(j);
                        super.getMonopoly().getjUi().printCashVous(j);
                        this.setProprietaire(j);
                    } else {//Si le joueur n'a pas assez d'argent
                        super.getMonopoly().getjUi().errorArgent(j);
                        if (super.getMonopoly().getpUi().jEssaye()) {//Proposition de vente ou hypotheque pour gagner de l'argent
                            if (j.jEssaye(super.getPrix())) {
                                j.removeCash(this.getPrix());
                                j.getMonopoly().getPlateau().setValues(j);
                                super.getMonopoly().getjUi().printCashVous(j);
                                this.setProprietaire(j);
                            }
                        }
                    }
                }
            } else if (this.getProprietaire()==j) {//Propre gare
                super.getMonopoly().getpUi().printProprePAC(this);
            } else {//Déjà un propriétaire
                if (super.getMonopoly().getpUi().toucherLoyer(this)) {//Vérification si la propriété n'est pas hypothéquée
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
            if (this.getProprietaire()!=null) {//Si la gare a déjà un propriétaire
                this.getProprietaire().removeGare(this);
            }
            super.setProprietaire(j);
            if (j!=null){//Si ce n'est pas le cas d'un retour de la propriété a la banque
                j.addGare(this);
            }
        }
        
        @Override
        public void retourBanque(){
            super.retourBanque();//Leve l'hypotheque
            this.setProprietaire(null);
        }
}