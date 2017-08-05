
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.util.Stack;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.*;
/*
 * Universdidad del Valle de Guatemala
 * Diseño de lenguajes de programacion
 * Compiladores
 *
 * @author Sebastian Galindo, Carnet: 15452
 */
public class MainLab2{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Operaciones operacion = new Operaciones();
        RegExConverter sC = new RegExConverter();
        String regexp;
        String regexpPF;
        Stack<Automata> miStack = new Stack<>();
        ArrayList<String> alfabeto = new ArrayList<>();

        //Solicitando al usuario que ingrese la expresion regular
        System.out.println("Ingrese la expresion regular que desee: ");
        regexp=scanner.nextLine();
        //Conversion de la cadena a notacion Postfix 
        regexpPF = sC.infixToPostfix(regexp);

        long time_start, time_end;
        time_start = System.currentTimeMillis();

        for (int x=0;x<regexpPF.length();x++){
            String caracter = String.valueOf(regexpPF.charAt(x));
            if(caracter.equals("*") || caracter.equals("|") || caracter.equals("?") || caracter.equals(".") || caracter.equals("+")){
                if(caracter.equals(".")){
                    //Hacer aqui la concatenacion
                    if(miStack.size()>=2){
                        Automata automataB=miStack.pop();
                        Automata automataA=miStack.pop();
                        Automata automataAB = operacion.concatenacion(automataA, automataB);
                        miStack.push(automataAB);
                    }else{
                        JOptionPane.showMessageDialog(null,"La cadena ingresada no es una regex.");
                        System.exit(0);
                    }
                }
                if(caracter.equals("|")){
                    //OR
                    if(miStack.size()>=2){
                        Automata automataB=miStack.pop();
                        Automata automataA=miStack.pop();
                        Automata automataAorB=operacion.or(automataA, automataB);
                        miStack.push(automataAorB);
                    }else{
                        JOptionPane.showMessageDialog(null,"La cadena ingresada no es una regex.");
                        System.exit(0);
                    }       
                }
                if(caracter.equals("*")){
                    //Kleene
                    if(miStack.size()>=1) {
                        Automata automataA = miStack.pop();
                        Automata automataK = operacion.kleene(automataA);
                        miStack.push(automataK);
                    }else{
                        JOptionPane.showMessageDialog( null, "La cadena ingresada no es una regex.");
                        System.exit(0);
                    }
                }
                if(caracter.equals("?")){
                    //Abreviatura ?
                    if(miStack.size()>=1) {
                        Automata X = miStack.pop();
                        Automata e = new Automata("$");
                        Automata automataOrEpsilon = operacion.or(X, e);
                        miStack.push(automataOrEpsilon);
                    }else{
                        JOptionPane.showMessageDialog( null, "La cadena ingresada no es una regex.");
                        System.exit(0);
                    }

                }
                if(caracter.equals("+")){
                    //Cerradura Positiva
                    if(miStack.size()>=1) {
                        Automata a = miStack.pop();
                        Automata automataCerradura = operacion.kleenemas(a);
                        miStack.push(automataCerradura);
                    }else{
                        JOptionPane.showMessageDialog( null, "La cadena ingresada no es una regex.");
                        System.exit(0);
                    }
                }
            }else{
                //Ciclo if que verifica si el ArrayList del alfabeto ya contiene el caractér
                if(alfabeto.contains(caracter)==false && !caracter.equals("$") && !caracter.equals("")){
                    alfabeto.add(caracter);
                }
                //Creando el automata básico
                Automata elAutomata = new Automata(caracter);
                miStack.push(elAutomata);
            }
        }

        time_end = System.currentTimeMillis();
        System.out.println("El automata fue creado en: "+ ( time_end - time_start ) +" millisegundos'");

        Automata elAutomatota = miStack.pop();

        //Creando un ArrayList que contiene todos
        operacion.getArrayNodos(elAutomatota.getNodoInicial());

        //Nombrando a los nodos
        operacion.nombrarNodos();

        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            PrintWriter writer = new PrintWriter("archivo.txt");
            writer.println("Lista de nodos = "+operacion.listadoDeNodos()
                    + "\nAlfabeto = "+ operacion.alfabeto(alfabeto)+"\nInicio = "
                    + elAutomatota.getNodoInicial().getNumeroEstado() + "\nAceptacion = " + elAutomatota.getNodoFinal().getNumeroEstado()
                    + "\nTransiciones: " + operacion.transiciones());
            writer.close();

        } catch (Exception e) {

            e.printStackTrace();

        }
    }
}
