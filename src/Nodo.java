import java.util.ArrayList;

public class Nodo {
    private int numeroEstado;
    private boolean esInicial;
    private boolean esFinal;
    private ArrayList<String> transiciones = new ArrayList<String>();
    private ArrayList<Nodo> elNodo = new ArrayList<Nodo>();

    public void agregar(String transicion, Nodo nodo){
        transiciones.add(transicion);
        elNodo.add(nodo);
    }


}
