
import java.lang.reflect.Array;
import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;
/*
 * Universdidad del Valle de Guatemala
 * Diseño de lenguajes de programacion
 * Compiladores
 *
 * @author Sebastian Galindo, Carnet: 15452
 */

public class Operaciones {
    
    public static ArrayList<Nodo> miArrayNodos = new ArrayList<>();
    ArrayList<EstadoAFD> arrayTodosEstadosDFA = new ArrayList<>();
    Set<EstadoAFD> Dstates = new HashSet<>();
    Set<Nodo> eclosureT = new HashSet<>();
    EstadoAFD U;
    EstadoAFD conjuntoSinMarcar;
    Stack<Nodo> eClosureStack = new Stack<>();

    
    public Automata concatenacion(Automata automataA, Automata automataB){

        //asignando los nodos del inicial de b al final de a
        ArrayList<Nodo> losNodos = automataB.getNodoInicial().getElNodo();
        automataA.getNodoFinal().setElNodo(losNodos);

        //Asiganando las transiciones del inicial al final
        ArrayList<String> trans = automataB.getNodoInicial().getTransiciones();
        automataA.getNodoFinal().setTransiciones(trans);

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
        nuevoNodoInicial.agregar("$", nuevoNodoFinal);
        
        Automata automataK = new Automata(nuevoNodoInicial, nuevoNodoFinal);
        return automataK;
    }

    public Automata kleenemas(Automata a){

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

    public String transiciones(){
        String transiciones="";
        ArrayList nodosFinales;
        ArrayList transicionesN;
        Nodo nodo;
        for(int i=0; i<miArrayNodos.size();i++){
            //Obteniendo el nodo-i.
            nodo = miArrayNodos.get(i);
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

    public void getArrayNodos(Nodo nodo){
        if(!miArrayNodos.contains(nodo)){
            miArrayNodos.add(nodo);
            ArrayList<Nodo> listadoDeNodos = nodo.getElNodo();
            for (Nodo nodoR: listadoDeNodos) {
                getArrayNodos(nodoR);
            }
        }

    }

    public void nombrarNodos(){
        for (int i = 0; i<miArrayNodos.size();i++) {
            miArrayNodos.get(i).setNumeroEstado(i);
        }

    }
    public String listadoDeNodos(){
        String cadena="{";
        for (int i = 0; i<miArrayNodos.size();i++) {
            cadena+=miArrayNodos.get(i).getNumeroEstado() + ", ";
        }
        cadena+="}";
        return cadena;
    }


    /* *************************************OPERACIONES PARA AFD******************************************/

    public Set<Nodo> eClosure(Set<Nodo> T){
        eClosureStack.addAll(T);
        eclosureT.addAll(T);
        while(!eClosureStack.isEmpty()){
            Nodo t;
            t = eClosureStack.pop();
            ArrayList<String> transicionesDet = t.getTransiciones();
            ArrayList<Nodo> nodosDet = t.getElNodo();
            for (int i=0;i<transicionesDet.size();i++) {
                String a = transicionesDet.get(i);
                if(a.equals("$")){
                    Nodo u = nodosDet.get(i);
                    if(!eclosureT.contains(u)){
                        eclosureT.add(u);
                        eClosureStack.add(u);
                    }
                }
            }
        }
        return eclosureT;
    }

    public Set<Nodo> move(Set<Nodo> T, String a){
        Set<Nodo> moveTA = new HashSet<>();
        for (Nodo n: T) {
            ArrayList<Nodo> nodosDeN = n.getElNodo();
            ArrayList<String> transicionesDeN = n.getTransiciones();
            for(int i=0; i<transicionesDeN.size();i++){
                if(transicionesDeN.get(i).equals(a)){
                    moveTA.add(nodosDeN.get(i));
                }
            }
        }
        return moveTA;
    }

    public void subsetConstruction(Nodo nodoInicial, ArrayList<String> alfabeto, AutomataDFA dfa){
        //Creando el conjunto inicial de Dstates
        Set<Nodo>  conjuntoS0 = new HashSet<>();
        conjuntoS0.add(nodoInicial);

        EstadoAFD miEstadoAFD = new EstadoAFD(eClosure(conjuntoS0));
        miEstadoAFD.setInicial(true);

        //añadiendo el conjunto inicial con el que empezará el conjunto de conjuntos Dstates
        Dstates.add(miEstadoAFD); //CAMBIE AQUI


        //empezando el ciclo que verifica los conjuntos marcados en Dstates;
        while(isUnmarkedState()){
            conjuntoSinMarcar.setMarcado(true);
            for (String a: alfabeto) {
                Set<Nodo> conjuntoMove = conjuntoSinMarcar.getConjuntoEstados();
                Set<Nodo> move = move(conjuntoMove,a);
                Set<Nodo> eclosure = eClosure(move);
                U = new EstadoAFD(eclosure);
                if(Dstates.contains(U)){
                    U.setMarcado(false);
                    Dstates.add(U);
                }
                //Creando la nueva transicion Dtran que contiene los datos de la transicion en el DFA
                Dtran nuevaTransicion = new Dtran(conjuntoSinMarcar, a, U);
                dfa.getTransicionesAFD().add(nuevaTransicion);
            }
        }
    }

    public boolean isUnmarkedState(){
        boolean hayDesmarcadoAun=false;
        for (EstadoAFD conjunto: Dstates){
            if (!conjunto.isMarcado()) {
                hayDesmarcadoAun = true;
                conjuntoSinMarcar = conjunto;
                break;
            }
        }
        return hayDesmarcadoAun;
    }
    /*
    Método para obtene
     */
    public void getArrayNodosAFD(ArrayList<Dtran> dtran){
        for (Dtran d:dtran) {
            //Verificando que el conjunto de estados Origen no exista en el arrayList antes de agregarlo
            if(!arrayTodosEstadosDFA.contains(d.getOrigen())){
                arrayTodosEstadosDFA.add(d.getOrigen());
            }

            //Verificando que el conjunto de estados Destino no exista en el arrayList antes de agregarlo
            if(!arrayTodosEstadosDFA.contains(d.getDestino())){
                arrayTodosEstadosDFA.add(d.getDestino());
            }
        }
    }
    /*
    * Método para nombrar todos los estados del arrayList de estados del AFD
     */
    public void nombrarNodosDFA(){
        for(int i=0; i<arrayTodosEstadosDFA.size();i++){
            arrayTodosEstadosDFA.get(i).setNumeroEstadoDFA(i);
        }
    }

    /*
     * Metodo para obtener la descripcion del AFD
     */
    public String descripcionAFD(AutomataDFA afd){
        String descripcion="";
        ArrayList<Dtran> elArray = afd.getTransicionesAFD();
        descripcion+="Lista de nodos: {";
        for(EstadoAFD c: arrayTodosEstadosDFA){
            descripcion+=c.getNumeroEstadoDFA()+", ";
        }
        descripcion+="}\nTransiciones: ";
        for (Dtran d: afd.getTransicionesAFD()) {
            descripcion+="("+ d.getOrigen().getNumeroEstadoDFA() + ", " + d.getTransicion() + ", " + d.getDestino().getNumeroEstadoDFA() + "),  ";
        }
        return descripcion;
    }


    public boolean noExiste(Set<EstadoAFD> Dstates, EstadoAFD U){
        int contador=0;
        boolean respuesta = false;
        for (EstadoAFD c: Dstates) {
            if(c.getConjuntoEstados().equals(U.getConjuntoEstados())){
                contador=1;
            }
        }
        if(contador==1){
            respuesta = false;
        }else{
            respuesta = true;
        }
        return respuesta;
    }

}
