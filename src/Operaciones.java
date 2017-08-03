
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
    
    public static ArrayList<Nodo> miArrayNodos = new ArrayList<>();
    ArrayList<Nodo> nodosSR = new ArrayList<>(); //ArrayList de Nodos sin repetir
    
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
    
    public void quitarNodosRepetidos(){
        for(int x=0;x<miArrayNodos.size();x++){
            if(nodosSR.contains(miArrayNodos.get(x))==false){
                nodosSR.add(miArrayNodos.get(x));
            }else{
                System.out.print("Ese nodo ya existe.");
            }
        }
        System.out.println("El tamaño de este array es: " + nodosSR.size());
    }
    public String listadoNodos(){
        String listadoDeNodos="{";
        System.out.println("El tamaño del ArrayList de Nodos es de: "+ nodosSR.size());
        for(int i=0;i<nodosSR.size();i++){
            listadoDeNodos+=""+ nodosSR.get(i).getNumeroEstado() + ", ";
        }
        listadoDeNodos+="}";
        return listadoDeNodos;
       
    }
    
    public void nombrandoNodos(){
        for(int i=0; i<nodosSR.size();i++){
            nodosSR.get(i).setNumeroEstado(i);
        }
    }
    public String transiciones(){
        String transiciones="";
        ArrayList nodosFinales;
        ArrayList transicionesN;
        Nodo nodo;
        for(int i=0; i<nodosSR.size();i++){
            //Obteniendo el nodo-i.
            nodo = nodosSR.get(i);
            //Obtienendo los nodos a los que esta conectado el nodo-i.
            nodosFinales = nodo.getElNodo();
            //Obteniendo las transisiones desde el nodo-i a los nodos conectados.
            transicionesN = nodo.getTransiciones();
            
            for(int j=0; j<nodo.getElNodo().size();j++){
                transiciones+="(" + nodo.getNumeroEstado() + ", " + transicionesN.get(j) + ", " + nodo.getElNodo().get(j).getNumeroEstado() +"), ";
            }
        }
        return transiciones;
    }

    
}
