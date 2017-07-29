
import javax.swing.JOptionPane;
import java.util.Stack;

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
        String regexp;
        String regexpPF;
        Stack<Automata> miStack = new Stack<Automata>();

        
        regexp = JOptionPane.showInputDialog("Ingrese la expresión regular que desee: ");
        regexpPF = sC.infixToPostfix(regexp);

        for (int x=0;x<regexpPF.length();x++){
            //System.out.println("Caracter " + x + ": " + regexpPF.charAt(x));
            String caracter = String.valueOf(regexpPF.charAt(x));
            if(caracter.equals("*") || caracter.equals("|") || caracter.equals("?") || caracter.equals(".") || caracter.equals("+")){
                if(caracter.equals(".")){
                    //Hacer aqui la concatenacion
                }
                if(caracter.equals("|")){
                    //Hacer aqui el OR
                }
                if(caracter.equals("*")){
                    //Hacer aqui Kleene
                }
                if(caracter.equals("?")){
                    //Hacer aqui (x|e)*
                }
                if(caracter.equals("+")){
                    //Hacer aqui cerradura positiva
                }
            }else{
                Automata elAutomata = new Automata(caracter);
                miStack.push(elAutomata);
            }
        }




        JOptionPane.showMessageDialog(null,"Esta es la expresion regular que ingreso: " + regexpPF);
        
    }

    
}
