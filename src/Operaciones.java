
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringStack;

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
                if(!yaexisteDFA(U)){
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


    public boolean yaexisteDFA(EstadoAFD U){
        boolean yaesta = false;
        for (EstadoAFD e: Dstates) {
            if(e.getConjuntoEstados().equals(U.getConjuntoEstados())){
                yaesta=true;
            }
        }

        return yaesta;
    }

    /* *********************************GENERACION DE AFD DIRECTA********************************************************/

    public Hoja generarArbolSintactico(String regexExtendidaPF){
        int cont=1;
        Stack<Hoja> arbolSintactico = new Stack<>();
        for(int x = 0;x<regexExtendidaPF.length(); x++){
            String caracter = String.valueOf(regexExtendidaPF.charAt(x));
            if(caracter.equals("|")){
                Hoja hojaDerecha = arbolSintactico.pop();
                Hoja hojaIzquierda = arbolSintactico.pop();
                Hoja hojaOr = new Hoja(hojaIzquierda, hojaDerecha, caracter);

                //Calculando las operaciones nullable, fistpos, lastpos y followpos de la hoja creada
                hojaOr.setNullable(nullableComplejo(hojaOr));
                hojaOr.setFirstpos(firstposComplejo(hojaOr));
                hojaOr.setLastpos(lastposComplejo(hojaOr));
                followpos(hojaOr);
                positionsCompuesto(hojaOr);

                arbolSintactico.push(hojaOr);
            }else if(caracter.equals(".")){
                Hoja hojaDerecha = arbolSintactico.pop();
                Hoja hojaIzquierda = arbolSintactico.pop();
                Hoja hojaCon = new Hoja(hojaIzquierda, hojaDerecha, caracter);

                //Calculando las operaciones nullable, fistpos, lastpos y followpos de la hoja creada
                hojaCon.setNullable(nullableComplejo(hojaCon));
                hojaCon.setFirstpos(firstposComplejo(hojaCon));
                hojaCon.setLastpos(lastposComplejo(hojaCon));
                followpos(hojaCon);
                positionsCompuesto(hojaCon);

                arbolSintactico.push(hojaCon);
            }else if(caracter.equals("*")){
                Hoja hojaUnica = arbolSintactico.pop();
                Hoja hojaKleene = new Hoja(hojaUnica, caracter);

                //Calculando las operaciones nullable, fistpos, lastpos y followpos de la hoja creada
                hojaKleene.setNullable(nullableComplejo(hojaKleene));
                hojaKleene.setFirstpos(firstposComplejo(hojaKleene));
                hojaKleene.setLastpos(lastposComplejo(hojaKleene));
                followpos(hojaKleene);
                positionsCompuesto(hojaKleene);

                arbolSintactico.push(hojaKleene);
            }else{
                Hoja hojaBasica = new Hoja(caracter, cont);
                //Seteando el valor del nullable de la hoja básica
                hojaBasica.setNullable(nullableBasico(hojaBasica));

                //Obteniendo el first pos de la hoja básica
                hojaBasica.setFirstpos(firstposBasico(hojaBasica));

                //Obteniendo el last pos de la hoja basica
                hojaBasica.setLastpos(lastposBasico(hojaBasica));

                positionBasico(hojaBasica);
                //Pusheando la hoja basica al stack del arbol
                arbolSintactico.push(hojaBasica);
                cont++;
            }
        }
        return arbolSintactico.pop();
    }

    public boolean nullableBasico(Hoja hojaBasica){
        boolean nullable;
        if(hojaBasica.getCaracter().equals("$")){
            nullable = true;
        }else{
            nullable = false;
        }
        return nullable;
    }

    public boolean nullableComplejo(Hoja hojaCompuesta){
        boolean nullable=false;
        String simbolo = hojaCompuesta.getSimbolo();
        Hoja c1 = hojaCompuesta.getHijoIzquierdo();
        Hoja c2 = hojaCompuesta.getHijoDerecho();
        if(simbolo.equals("|")){
            nullable = c1.isNullable() | c2.isNullable();
        }else if(simbolo.equals(".")){
            nullable = c1.isNullable() & c2.isNullable();
        }else if(simbolo.equals("*")){
            nullable = true;
        }
        return nullable;
    }

    public Set<Hoja> firstposBasico(Hoja hojaBasica){
        Set<Hoja> firstpos = new HashSet<>();
        if(hojaBasica.getCaracter().equals("$")){
            firstpos = firstpos;
        }else{
            firstpos.add(hojaBasica);
        }
        return firstpos;
    }

    public Set<Hoja> firstposComplejo(Hoja hojaCompuesta){
        Set<Hoja> firstpos = new HashSet<>();

        String simbolo = hojaCompuesta.getSimbolo();
        Hoja c1 = hojaCompuesta.getHijoIzquierdo();
        Hoja c2 = hojaCompuesta.getHijoDerecho();
        Hoja c = hojaCompuesta.getHojaUnica();

        if(simbolo.equals("|")){
            firstpos.addAll(c1.getFirstpos());
            firstpos.addAll(c2.getFirstpos());
        }else if(simbolo.equals(".")){
            if(c1.isNullable()){
                firstpos.addAll(c1.getFirstpos());
                firstpos.addAll(c2.getFirstpos());
            }else{
                firstpos.addAll(c1.getFirstpos());
            }
        }else if(simbolo.equals("*")){
            firstpos.addAll(c.getFirstpos());
        }
        return firstpos;
    }

    public Set<Hoja> lastposBasico(Hoja hojaBasica){
        Set<Hoja>lastpos = new HashSet<>();
        if(hojaBasica.getCaracter().equals("$")){
            lastpos = lastpos;
        }else{
            lastpos.add(hojaBasica);
        }
        return lastpos;
    }

    public Set<Hoja> lastposComplejo(Hoja hojaCompuesta){
        Set<Hoja> lastpos = new HashSet<>();

        String simbolo = hojaCompuesta.getSimbolo();
        Hoja c1 = hojaCompuesta.getHijoIzquierdo();
        Hoja c2 = hojaCompuesta.getHijoDerecho();
        Hoja c = hojaCompuesta.getHojaUnica();

        if (simbolo.equals("|")) {
            for (Hoja h : c1.getLastpos()) {
                lastpos.add(h);
            }
            for (Hoja h : c2.getLastpos()) {
                lastpos.add(h);
            }

        } else if (simbolo.equals(".")) {
            if (c2.isNullable()) {
                for (Hoja h : c1.getLastpos()) {
                    lastpos.add(h);
                }
                for (Hoja h : c2.getLastpos()) {
                    lastpos.add(h);
                }
            } else {
                lastpos = c2.getLastpos();
            }

        } else if (simbolo.equals("*")) {
            lastpos = c.getLastpos();

        }
        return lastpos;
    }

    public void followpos(Hoja hojaCompuesta){
        String simbolo = hojaCompuesta.getSimbolo();
        Hoja c1 = hojaCompuesta.getHijoIzquierdo();
        Hoja c2 = hojaCompuesta.getHijoDerecho();

        if(simbolo.equals(".")){
            for (Hoja i: c1.getLastpos()) {
                i.setFollowpos(c2.getFirstpos());
            }
        }else if(simbolo.equals("*")){
            for (Hoja i: hojaCompuesta.getLastpos()) {
                i.setFollowpos(hojaCompuesta.getFirstpos());
            }
        }

    }
    public void estadoFinal(AutomataDFA a){
        for (EstadoAFDHoja e: a.getDstates()) {
            for (Hoja h: e.getElSet()) {
                if(h.getCaracter().equals("#")){
                    a.setEstadoFinalHoja(e);
                }
            }
        }
    }

    public void positionBasico(Hoja hojaBasica){
        Set<Hoja> a = new HashSet<>();
        a.add(hojaBasica);
        hojaBasica.setPositions(a);
    }
    public void positionsCompuesto(Hoja hojaCompuesta){
        String s = hojaCompuesta.getSimbolo();
        Hoja c1 = hojaCompuesta.getHijoIzquierdo();
        Hoja c2 = hojaCompuesta.getHijoDerecho();
        Hoja c = hojaCompuesta.getHojaUnica();

        if(s.equals(".") || s.equals("|")){
            hojaCompuesta.getPositions().addAll(c1.getPositions());
            hojaCompuesta.getPositions().addAll(c2.getPositions());
        }else if(s.equals("*")){
            hojaCompuesta.getPositions().addAll(c.getPositions());
        }
    }


    public void construccionDirecta(AutomataDFA automata,Hoja n, ArrayList<String> alfabeto){

        //Creando una nueva instancia del objeto EstadoAFDHoja  que contiene como atributo el firstpos de la hoja n.
        EstadoAFDHoja elEstado = new EstadoAFDHoja(n.getFirstpos(), false);

        //Inicializando Dstates con el EstadoAFDHOja sin marcar
        automata.getDstates().add(elEstado);
        automata.setEstadoInicialHoja(elEstado);

        while(hayDesmarcado(automata)){
            //Marcando el estado S como verdadero
            automata.getS().setMarcado(true);
            //Obteniendo el conjunto firstpos de S
            EstadoAFDHoja S = automata.getS();

            //Empezando a recorrer el alfabeto
            for(String a : alfabeto){
                Set<Hoja> setDeU = new HashSet<>();
                for (Hoja h: S.getElSet()) {
                    if(h.getCaracter().equals(a)){
                        setDeU.addAll(h.getFollowpos());
                    }
                }
                EstadoAFDHoja U = new EstadoAFDHoja(setDeU, false);
                if(!yaexiste(automata,U)){
                    automata.getDstates().add(U);
                }
                DtranHoja d = new DtranHoja(S, a, U);
                automata.getTransicionesAFDHoja().add(d);
            }
        }
    }

    public boolean yaexiste(AutomataDFA automata, EstadoAFDHoja U){
        boolean yaesta = false;
        for (EstadoAFDHoja e: automata.getDstates()) {
            if(e.getElSet().equals(U.getElSet())){
                yaesta=true;
            }
        }

        return yaesta;
    }

    public void nombrarNodos(AutomataDFA automata){
        for(int x=0; x<automata.getDstates().size(); x++){
            automata.getDstates().get(x).setNumeroEstadoDFA(x);
        }
    }


    public boolean hayDesmarcado(AutomataDFA automata){
        boolean hayDesmarcado=false;
        for (EstadoAFDHoja e:automata.getDstates()) {
            if(!e.isMarcado()){
                hayDesmarcado = true;
                automata.setS(e);
            }
        }
        return hayDesmarcado;
    }


    //Metodo que genera la descripcion del automata AFD generado directamente
    public String descripcionAFDdirecto(AutomataDFA automata, ArrayList<String> alfabeto){
        String descripcion="\n\nAFD Construccion Directa: \n\nEstados: {";
        for (EstadoAFDHoja e:automata.getDstates()) {
            descripcion += e.getNumeroEstadoDFA() +", ";
        }
        descripcion += "}\nAlfabeto: {";
        for (String a:alfabeto) {
            descripcion += a +", ";
        }
        estadoFinal(automata);
        descripcion+="}\nEstado inicial: " + automata.getEstadoInicialHoja().getNumeroEstadoDFA()+"\nEstado Final: "+ automata.getEstadoFinalHoja().getNumeroEstadoDFA()+"\nTransiciones: {";
        for (DtranHoja d:automata.getTransicionesAFDHoja()) {
            descripcion+=d.getOrigen().getNumeroEstadoDFA()+"-"+d.getTransicion()+"-"+d.getDestino().getNumeroEstadoDFA() +", ";
        }
        descripcion+="}";
        return descripcion;
    }
}
