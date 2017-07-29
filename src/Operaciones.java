
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sebas
 */
public class Operaciones {
    
    
    public Automata concatenacion(Automata automataA, Automata automataB){
        automataA.setNodoFinal(automataB.getNodoInicial());
        Automata automataAB = new Automata(automataA.getNodoInicial(), automataB.getNodoFinal());
        return automataAB;
    }
    
    public Automata or(Automata a, Automata b){
         
        //Creando un nodo inicial que sera en comun entre los dos automatas.
        Nodo nuevoNodoInicial = new Nodo();
        
        /*Agregando a nuevoNodoInicial una nueva transicion, por medio de 
        epsilon "$", hacie el nodo inicial del automata a. */
        nuevoNodoInicial.agregar("$", a.getNodoInicial());
        
        /*Agregando a nuevoNodoInicial una nueva transicion, por medio de 
        epsilon "$", hacie el nodo inicial del automata b. */
        nuevoNodoInicial.agregar("$", b.getNodoInicial());
        
        //Creando un nodo final
        Nodo nuevoNodoFinal = new Nodo();
        
        /*Asignando la transicion desde los nodos finales de a,b 
        al nuevoNodoFinal*/
        a.getNodoFinal().agregar("$", nuevoNodoFinal);
        b.getNodoFinal().agregar("$", nuevoNodoFinal);
        
        //Creando el nuevo automataAorB
        Automata automataAorB = new Automata(nuevoNodoInicial, nuevoNodoFinal);
        
        return automataAorB;
    }
    
    public Automata kleene(Automata a){
        
        //Creando un nuevo nodo inicial
        Nodo nuevoNodoInicial = new Nodo();
        
        //Creando un nuevo nodo final
        Nodo nuevoNodoFinal = new Nodo();
        
        //Relacionando el nuevoNodoInicial con el automata a
        nuevoNodoInicial.agregar("$", a.getNodoInicial());
        
        //Relacionando el nuevoNodoFinal con el nodo final de a
        a.getNodoFinal().agregar("$", nuevoNodoFinal);
        
        //Relacionando el nodo final de a con el nodo incial de a.
        a.getNodoFinal().agregar("$", a.getNodoInicial());
        
        //Relacionando el nuevoNodoFinal con el nuevoNodoInicial
        nuevoNodoFinal.agregar("$", nuevoNodoInicial);
        
        Automata automataK = new Automata(nuevoNodoInicial, nuevoNodoFinal);
        return automataK;
    }
    
    public String alfabeto(ArrayList<String> Alf){
        String alfabeto="{";
        for(int i=0; i<Alf.size();i++){
            alfabeto+=Alf.get(i) + ", ";
        }
        alfabeto+="}";
        return alfabeto;
    }
    
    /*
    *Formula creada para saber cuandos nodos deben de haber
    */
    public int TamañoRegex(int ch,int c,int k,int o){
        int tamaño = (ch*2)+(c*-1)+(o*2)+(k*2);
        return tamaño;
    }

    
}
