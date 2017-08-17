
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
/*
 * Universdidad del Valle de Guatemala
 * Diseño de lenguajes de programacion
 * Compiladores
 *
 * @author Sebastian Galindo, Carnet: 15452
 */

public class Operaciones {
    
    public static ArrayList<Nodo> miArrayNodos = new ArrayList<>();
    
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
        ArrayList transicionesN;
        Nodo nodo;
        for(int i=0; i<miArrayNodos.size();i++){
            //Obteniendo el nodo-i.
            nodo = miArrayNodos.get(i);
            //Obteniendo las transisiones desde el nodo-i a los nodos conectados.
            transicionesN = nodo.getTransiciones();
            for(int j=0; j<nodo.getElNodo().size();j++){
                transiciones+="(" + nodo.getNumeroEstado() + "-" + transicionesN.get(j) + "-" + nodo.getElNodo().get(j).getNumeroEstado() +"), ";
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

    public void simulacionAFN(Automata automata, String cadena){
        Set<Nodo> setInicial = new HashSet<>();
        setInicial.add(automata.getNodoInicial());
        Set<Nodo> S = eClosure(setInicial);
        String c;
        int x = 0, y = 0;

        while(x < cadena.length()){
            c=String.valueOf(cadena.charAt(x));
            S=eClosure(move(S, c));
            x++;
        }
        for (Nodo n: S) {
            if(n.isEsFinal()){
                y=1;
            }
        }
        if(y==1){
            System.out.println("La cadena SI es aceptada.");
        }else{
            System.out.println("La cadena NO es aceptada.");
        }

    }


    /* *************************************OPERACIONES PARA AFD******************************************/

    public Set<Nodo> eClosure(Set<Nodo> T){
        Set<Nodo> eclosureT = new HashSet<>();
        Stack<Nodo> Stack = new Stack<>();


        Stack.addAll(T);
        eclosureT.addAll(T);
        while(!Stack.isEmpty()){
            Nodo t = Stack.pop();
            ArrayList<String> transicionesDet = t.getTransiciones();
            ArrayList<Nodo> nodosDet = t.getElNodo();
            for (int i=0;i<transicionesDet.size();i++) {
                String a = transicionesDet.get(i);
                if(a.equals("$")){
                    Nodo u = nodosDet.get(i);
                    if(!eclosureT.contains(u)){
                        eclosureT.add(u);
                        Stack.add(u);
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

    public void subsetConstruction(Nodo nodoInicial, ArrayList<String> alfabeto, AutomataDFA afd){
        //Creando el conjunto inicial de Dstates
        Set<Nodo>  conjuntoS0 = new HashSet<>();
        conjuntoS0.add(nodoInicial);

        EstadoAFD miEstadoAFD = new EstadoAFD(eClosure(conjuntoS0));
        miEstadoAFD.setInicial(true);

        //añadiendo el conjunto inicial con el que empezará el conjunto de conjuntos Dstates
        afd.getDstatesAFD().add(miEstadoAFD);
        afd.setEstadoInicial(miEstadoAFD);

        //empezando el ciclo que verifica los conjuntos marcados en Dstates;
        while(isUnmarkedState(afd)){
            afd.getT().setMarcado(true);//Marcando el estado T como marcado
            EstadoAFD T = afd.getT();//Obteniendo el estado T
            for (String a: alfabeto) {   //Empezando a recorrer el alfabeto
                EstadoAFD U = new EstadoAFD(eClosure(move(T.getConjuntoEstados(), a)));
                if(!yaexisteDFA(afd, U)){
                    U.setMarcado(false);
                    afd.getDstatesAFD().add(U);
                    afd.setU(U);
                }
                //Creando la nueva transicion Dtran que contiene los datos de la transicion en el DFA
                Dtran nuevaTransicion = new Dtran(T, a, afd.getU());
                afd.getTransicionesAFD().add(nuevaTransicion);
            }
        }
    }

    public boolean isUnmarkedState(AutomataDFA automata){
        boolean hayDesmarcadoAun=false;
        for (EstadoAFD conjunto: automata.getDstatesAFD()){
            if (!conjunto.isMarcado()) {
                hayDesmarcadoAun = true;
                automata.setT(conjunto);
            }
        }
        return hayDesmarcadoAun;
    }

    /*
    * Método para nombrar todos los estados del arrayList de estados del AFD
     */
    public void nombrarNodosDFA(AutomataDFA automata){
        for(int i=0; i<automata.getDstatesAFD().size();i++){
            automata.getDstatesAFD().get(i).setNumeroEstadoDFA(i);
        }
        for (EstadoAFD e: automata.getDstatesAFD()) {
            for (Nodo n: e.getConjuntoEstados()) {
                if(n.isEsFinal()){
                    e.setFinal(true);
                }
            }
        }
    }

    /*
     * Metodo para obtener la descripcion del AFD convertido
     */
    public String descripcionAFD(AutomataDFA afd, ArrayList<String> alfabeto){
        String descripcion="";
        descripcion+="Lista de nodos: {";
        for(EstadoAFD c: afd.getDstatesAFD()){
            descripcion+=c.getNumeroEstadoDFA()+", ";
        }
        descripcion+="}\nAlfabeto: {";
        for (String a: alfabeto) {
            descripcion+=a+", ";
        }
        descripcion+="}\nEstado incial: " + afd.getEstadoInicial().getNumeroEstadoDFA() + "\nEstado final: ";
        for (EstadoAFD e: afd.getDstatesAFD()) {
            if(e.isFinal()){
                descripcion+=e.getNumeroEstadoDFA() +", ";
            }
        }

        descripcion+="\nTransiciones: ";
        for (Dtran d: afd.getTransicionesAFD()) {
            descripcion+="("+ d.getOrigen().getNumeroEstadoDFA()+"-"+d.getTransicion()+"-"+d.getDestino().getNumeroEstadoDFA()+"), ";
        }
        return descripcion;
    }


    public boolean yaexisteDFA(AutomataDFA automata, EstadoAFD U){
        boolean yaesta = false;
        for (EstadoAFD e: automata.getDstatesAFD()) {
            if(e.getConjuntoEstados().equals(U.getConjuntoEstados())){
                automata.setU(e);
                yaesta=true;
            }
        }

        return yaesta;
    }

    public EstadoAFD moveSimulacion(EstadoAFD estado, String a, AutomataDFA automata){
        EstadoAFD moveTA = null;
        ArrayList<Dtran> transiciones = automata.getTransicionesAFD();
        for (Dtran d: transiciones) {
            if(d.getOrigen().equals(estado)){
                if(d.getTransicion().equals(a)){
                    moveTA=d.getDestino();
                }
            }
        }
        return moveTA;
    }

    public void simulacionAFD(AutomataDFA automata, String cadena){
        EstadoAFD e = automata.getEstadoInicial();
        String c;
        int x = 0, y=0;

        while(x<cadena.length()){
            c=String.valueOf(cadena.charAt(x));
            e = moveSimulacion(e, c, automata);
            x++;
            if(e==null){
                y=0;
                break;
            }else{
                if(e.isFinal()){
                    y=1;
                }else{
                    y=0;
                }
            }

        }

        if(y==1){
            System.out.println("La cadena SI es aceptada.");
        }else{
            System.out.println("La cadena NO es aceptada.");
        }

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
                i.getFollowpos().addAll(c2.getFirstpos());
                //i.setFollowpos(c2.getFirstpos());
            }
        }else if(simbolo.equals("*")){
            for (Hoja i: hojaCompuesta.getLastpos()) {
                i.getFollowpos().addAll(hojaCompuesta.getFirstpos());
                //i.setFollowpos(hojaCompuesta.getFirstpos());
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
        EstadoAFDHoja elEstado = new EstadoAFDHoja(n.getFirstpos());

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
                Set<Hoja> setDeU2 = new HashSet<>();
                for (Hoja h: S.getElSet()) {
                    if(h.getCaracter().equals(a)){
                        setDeU2.addAll(h.getFollowpos());
                    }
                }
                EstadoAFDHoja U2 = new EstadoAFDHoja(setDeU2);
                if(!yaexiste(automata,U2)){
                    U2.setMarcado(false);
                    automata.getDstates().add(U2);
                    automata.setU2(U2);
                }
                DtranHoja nuevaTransicion = new DtranHoja(S, a, automata.getU2());
                automata.getTransicionesAFDHoja().add(nuevaTransicion);
            }
        }
    }

    public boolean yaexiste(AutomataDFA automata, EstadoAFDHoja U){
        boolean yaesta = false;
        for (EstadoAFDHoja e: automata.getDstates()) {
            if(e.getElSet().equals(U.getElSet())){
                automata.setU2(e);
                yaesta=true;
            }
        }

        return yaesta;
    }

    public void nombrarNodos(AutomataDFA automata){
        for(int x=0; x<automata.getDstates().size(); x++){
            automata.getDstates().get(x).setNumeroEstadoDFA(x);
        }
        for (EstadoAFDHoja e: automata.getDstates()) {
            if(e.equals(automata.getEstadoFinalHoja())){
                e.setFinal(true);
            }
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

        String descripcion="AFD Construccion Directa: \n\nEstados: {";
        for (EstadoAFDHoja e:automata.getDstates()) {
            descripcion += e.getNumeroEstadoDFA() +", ";
        }
        descripcion += "}\nAlfabeto: {";
        for (String a:alfabeto) {
            descripcion += a +", ";
        }
        estadoFinal(automata);
        descripcion+="}\nEstado inicial: " + automata.getEstadoInicialHoja().getNumeroEstadoDFA()+"\nEstado Final: "+ automata.getEstadoFinalHoja().getNumeroEstadoDFA()+"\nTransiciones: ";
        for (DtranHoja d:automata.getTransicionesAFDHoja()) {
            descripcion+="("+d.getOrigen().getNumeroEstadoDFA()+"-"+d.getTransicion()+"-"+d.getDestino().getNumeroEstadoDFA() +"), ";
        }

        descripcion+="}";
        for (EstadoAFDHoja e: automata.getDstates()) {
            if(e.equals(automata.getEstadoFinalHoja())){
                e.setFinal(true);
            }
        }
        return descripcion;
    }

    public EstadoAFDHoja moveSimulacionDirecto(EstadoAFDHoja estado, String a, AutomataDFA automata){
        EstadoAFDHoja moveTA = null;
        ArrayList<DtranHoja> transiciones = automata.getTransicionesAFDHoja();
        for (DtranHoja d: transiciones) {
            if(d.getOrigen().equals(estado)){
                if(d.getTransicion().equals(a)){
                    moveTA=d.getDestino();
                }
            }
        }
        return moveTA;
    }

    public void simulacionAFDdirecto(AutomataDFA automata, String cadena){
        EstadoAFDHoja e = automata.getEstadoInicialHoja();
        String c;
        int x = 0, y=0;

        while(x<cadena.length()){
            c=String.valueOf(cadena.charAt(x));
            e = moveSimulacionDirecto(e, c, automata);
            x++;
            if(e==null){
                y=0;
                break;
            }else{
                if(e.isFinal()){
                    y=1;
                }else{
                    y=0;
                }
            }

        }
        if(y==1){
            System.out.println("La cadena SI es aceptada.");
        }else{
            System.out.println("La cadena NO es aceptada.");
        }

    }

/* ***************************************MINIMIZACION DE AFD*************************************************************/

    public void minimizar(AutomataDFA automata, ArrayList<String> alfabeto) {
        ArrayList<EstadoAFD> estados = automata.getDstatesAFD();
        ArrayList<AFDminimizado> R = new ArrayList<>();

        //Generando la tabla de parejas de estados
        for (int i = 0; i < estados.size(); i++) {
            for (int j = 0; j < estados.size(); j++) {
                AFDminimizado am = new AFDminimizado(estados.get(i), estados.get(j), false);
                R.add(am);

            }
        }

        //Marcando las parejas de estados si alguno de los dos es estado final
        for (AFDminimizado a : R) {
            if (a.getP().isFinal() && a.getQ().isFinal()) {

            } else if (a.getP().isFinal() || a.getQ().isFinal()) {
                a.setMarcado(true);
            }

        }

        //loop(R, alfabeto, automata);
        int cont = 0;
        while (cont < 100000) {
            for (AFDminimizado a : R) {
                if (!a.isMarcado()) {
                    for (String c : alfabeto) {
                        EstadoAFD moveP = moveSimulacion(a.getP(), c, automata);
                        EstadoAFD moveQ = moveSimulacion(a.getQ(), c, automata);
                        for (AFDminimizado afdm : R) {
                            if (afdm.getP().equals(moveP) && afdm.getQ().equals(moveQ)) {
                                if (afdm.isMarcado()) {
                                    a.setMarcado(true);
                                }
                            }
                        }
                    }
                }
            }
            cont++;
        }

        Set<AFDminimizado> F = new HashSet<>();
        for (AFDminimizado a : R) {
            if (!a.isMarcado()) {
                F.add(a);
            }
        }

        for (AFDminimizado a: F) {
            a.getSet().add(a.getP());
            a.getSet().add(a.getQ());

        }

        Set<AFDminimizado> estadosRepetidos = new HashSet<>();
        Set<AFDminimizado> repetido =  new HashSet<>();
        repetido.addAll(F);


        for (AFDminimizado a: F) {
            repetido.removeAll(estadosRepetidos);
            for (AFDminimizado b: repetido) {
                if(b.equals(a)){
                    //hacernada
                }else{
                    for (EstadoAFD s: b.getSet()) {
                        if(a.getSet().contains(s)){
                            a.getSet().addAll(b.getSet());
                            estadosRepetidos.add(b);
                        }
                    }
                }
            }
            repetido.remove(a);
        }

        F.removeAll(estadosRepetidos);
        ArrayList<AFDminimizado> setFinal = new ArrayList<>();
        setFinal.addAll(F);


        //Nombrando cada uno de los estados
        for(int i=0;i<setFinal.size();i++){
            setFinal.get(i).setNumeroEstadoMin(i);
        }

        for (AFDminimizado afd: setFinal) {
            for (String a:alfabeto) {
                moveMin(afd,a,automata);
                for (AFDminimizado destino:setFinal) {
                    if(destino.getSet().equals(moveMin(afd,a,automata))){
                        DtranAFDMin d = new DtranAFDMin(afd,a, destino);
                        afd.getTransiciones().add(d);
                    }
                }
            }
        }


        String descripcionMinimizada = "Estados: ";
        for (AFDminimizado a: setFinal) {
            descripcionMinimizada+=a.getNumeroEstadoMin()+", ";
        }
        descripcionMinimizada+="\nTransiciones: ";
        for (AFDminimizado a: setFinal) {
            ArrayList<DtranAFDMin> transicionesDelEstadoMin = a.getTransiciones();
            for (DtranAFDMin d: transicionesDelEstadoMin) {
                descripcionMinimizada+="("+d.getOrigen().getNumeroEstadoMin()+"-"+d.getTransicion()+"-"+d.getDestino().getNumeroEstadoMin()+"), ";
            }

        }

        BufferedWriter bw2 = null;
        FileWriter fw2 = null;


        try {

            PrintWriter writer = new PrintWriter("Descripcion AFD Minimizado.txt");
            writer.println("\nAFN Minimizado: \n"+descripcionMinimizada);
            writer.close();

        } catch (Exception e) {

            e.printStackTrace();

        }




    }

    public void loop(ArrayList<AFDminimizado> R, ArrayList<String> alfabeto, AutomataDFA automata){
        for (AFDminimizado a: R) {
            if(!a.isMarcado()){
                for (String c: alfabeto) {
                    EstadoAFD moveP = moveSimulacion(a.getP(), c, automata);
                    EstadoAFD moveQ = moveSimulacion(a.getQ(), c, automata);
                    for (AFDminimizado afdm:R) {
                        if(afdm.getP().equals(moveP) && afdm.getQ().equals(moveQ)){
                            if(afdm.isMarcado()){
                                a.setMarcado(true);
                                loop(R,alfabeto,automata);
                            }
                        }
                    }
                }
            }
        }
    }

    public Set<EstadoAFD> moveMin(AFDminimizado estado, String a, AutomataDFA automata){
        Set<EstadoAFD> nodosDestino = new HashSet<>();
        for (EstadoAFD e: estado.getSet()) {
            EstadoAFD destino = moveSimulacion(e, a, automata);
            nodosDestino.add(destino);
        }
        return nodosDestino;
    }


}
