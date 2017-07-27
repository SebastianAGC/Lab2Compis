
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sebas
 */
public class MainLab2{

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        
        RegExConverter sC = new RegExConverter();
        // TODO code application logic here
        String regexp;
        String regexpPF;
        
        regexp = JOptionPane.showInputDialog("Ingrese la expresión regular que desee: ");
        regexpPF = sC.infixToPostfix(regexp);
        JOptionPane.showMessageDialog(null,"Esta es la expresion regular que ingreso: " + regexpPF);
        
    }

    
}
