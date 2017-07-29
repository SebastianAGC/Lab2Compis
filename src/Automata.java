import java.util.ArrayList;

public class Automata {

    ArrayList<Nodo> miArrayNodos = new ArrayList<>();
    Nodo elNodo;

    public Automata(String transicion){

        Nodo nodoInicial = new Nodo();
        Nodo nodoFinal = new Nodo();

        //agregando al nodo inicial del automata el nodo al que esta dirigido
        nodoInicial.agregar(transicion, nodoFinal);


    }

    public String toString(){
        //System.out.println("Este es el automata creado: " );
        return "Este es el automata creado: ";
    }




}
