
import java.util.ArrayList;
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
        Operaciones operacion = new Operaciones();
        RegExConverter sC = new RegExConverter();
        String regexp;
        String regexpPF;
        Stack<Automata> miStack = new Stack<>();
        ArrayList<String> alfabeto = new ArrayList<>();
        int c = 0,o=0,k=0,ch=0;
             

        //Solicitando al usuario que ingrese la expresion regular
        regexp = JOptionPane.showInputDialog("Ingrese la expresión regular que desee: ");
        //Conversion de la cadena a notacion Postfix 
        regexpPF = sC.infixToPostfix(regexp);
        JOptionPane.showMessageDialog(null,"Esta es la expresion regular que ingreso en formato POSTFIX: " + regexpPF);
        
        for (int x=0;x<regexpPF.length();x++){
            String caracter = String.valueOf(regexpPF.charAt(x));
            if(caracter.equals("*") || caracter.equals("|") || caracter.equals("?") || caracter.equals(".") || caracter.equals("+")){
                if(caracter.equals(".")){
                    //Hacer aqui la concatenacion
                    c++;
                    if(miStack.size()>=2){
                        //System.out.println("Entre a la concatenacion.");
                        Automata automataB=miStack.pop();
                        Automata automataA=miStack.pop();
                        Automata automataAB = operacion.concatenacion(automataA, automataB);
                        miStack.push(automataAB);
                    }else{
                        JOptionPane.showMessageDialog(null,"La cadena no es una regex.");  
                    }
                }
                if(caracter.equals("|")){
                    //Hacer aqui el OR
                    o++;
                    if(miStack.size()>=2){
                        //System.out.println("Entre al OR.");
                        Automata automataB=miStack.pop();
                        Automata automataA=miStack.pop();
                        Automata automataAorB=operacion.or(automataA, automataB);
                        miStack.push(automataAorB);
                    }else{
                        JOptionPane.showMessageDialog(null,"La cadena no es una regex.");  
                    }       
                }
                if(caracter.equals("*")){
                    //Hacer aqui Kleene
                    k++;
                    //System.out.println("Entre al kleene.");
                    Automata automataA=miStack.pop();
                    Automata automataK=operacion.kleene(automataA); 
                    miStack.push(automataK);
                }
                if(caracter.equals("?")){
                    //Hacer aqui (x|e)*
                    //System.out.println("Entre a la abreviatura.");
                    Automata X = miStack.pop();
                    Automata e = new Automata("$");
                    Automata automataOrEpsilon = operacion.or(X, e);
                    miStack.push(automataOrEpsilon);
                    //System.out.println("Automata Creado." + miStack.size());
                }
                if(caracter.equals("+")){
                    //Hacer aqui cerradura positiva
                    //System.out.println("Entre a la cerradura positiva.");
                    Automata a = miStack.pop();
                    Automata a1 = operacion.kleene(a);
                    Automata automataCerradura = operacion.concatenacion(a, a1);
                    miStack.push(automataCerradura);
                }
            }else{
                ch++;
                //Ciclo if que verifica si el ArrayList del alfabeto ya contiene el caractér
                if(alfabeto.contains(caracter)==false){
                    alfabeto.add(caracter);
                }
                //Creando el automata básico
                Automata elAutomata = new Automata(caracter);
                miStack.push(elAutomata);
                System.out.println("Tamaño del stack: " + miStack.size());
            }
        }
        System.out.println("Tamaño del stack: " + miStack.size());
        Automata elAutomatota = miStack.pop();
        int tamaño = operacion.TamañoRegex(ch, c, k, o);
        //Quitando los nodos repetidos
        operacion.quitarNodosRepetidos();
        //Nombrando a los nodos
        operacion.nombrandoNodos();
       
        //System.out.println("El automata debe tener "+tamaño + " nodos.");
        JOptionPane.showMessageDialog(null,"Lista de nodos = "+operacion.listadoNodos() 
            + "\nAlfabeto = "+ operacion.alfabeto(alfabeto)+"\nInicio = "
            + elAutomatota.getNodoInicial().getNumeroEstado() + "\nAceptacion = " + elAutomatota.getNodoFinal().getNumeroEstado()
            + "\nTransiciones: " + operacion.transiciones());  
    }
}
