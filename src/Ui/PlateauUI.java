package Ui;

import Jeu.*;
import java.awt.*;

public class PlateauUI extends Canvas{

    private Monopoly monopoly;
    
    private Image background;
    
    public PlateauUI(Monopoly m){
        this.setMonopoly(m);
        setSize(750, 746);
        setBackground(Color.white);
        background = Toolkit.getDefaultToolkit().getImage("src/Data/PlateauMonopoly.png"); 
    }

    public Monopoly getMonopoly() {
        return monopoly;
    }

    private void setMonopoly(Monopoly monopoly) {
        this.monopoly = monopoly;
    }
    
    

    @Override
    public void paint(Graphics g){
        g.drawImage(background, 0, 0, this);
        this.paintJoueurs(g);
    }
    
    public void paintJoueurs(Graphics g) {
        for (Joueur j : this.getMonopoly().getJoueurs()){
            g.setColor(j.getColor());
            Dimension dJ = this.getCoordonnéeJoueur(j);
            g.fillOval(dJ.width, dJ.height, 25, 25);
        }
    }
    
    public void update(){
        update(this.getGraphics());
    }
    
    public Dimension getCoordonnéeJoueur(Joueur j) {
        int tampon = 750/26;
        if (j.getPositionCourante().getNum()<11) {
            return new Dimension(tampon*4 + (750 * (10-j.getPositionCourante().getNum()))/12,this.getSize().height-tampon-tampon);
        } else if (j.getPositionCourante().getNum()>10 && j.getPositionCourante().getNum()<21) {
            return new Dimension(tampon+tampon,tampon*4 + (750 * (20-j.getPositionCourante().getNum()))/12);
        } else if (j.getPositionCourante().getNum()>20 && j.getPositionCourante().getNum()<31) {
            return new Dimension(tampon*2 + (750 * (j.getPositionCourante().getNum()-21))/12,tampon+tampon);
        } else {
            return new Dimension(this.getSize().width-tampon-tampon,tampon*2 + (750 * (j.getPositionCourante().getNum()-31)));
        }
    }
}