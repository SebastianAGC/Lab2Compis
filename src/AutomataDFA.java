/*
 * Universdidad del Valle de Guatemala
 * Diseño de lenguajes de programacion
 * Compiladores
 *
 * @author Sebastian Galindo, Carnet: 15452
 */

import java.util.ArrayList;

public class AutomataDFA {
    private ArrayList<Dtran> transicionesAFD = new ArrayList<>();
    private EstadoAFD estadoInicial;
    private EstadoAFD estadoFinal;

    public AutomataDFA() {

    }

    public ArrayList<Dtran> getTransicionesAFD() {
        return transicionesAFD;
    }

    public void setTransicionesAFD(ArrayList<Dtran> transicionesAFD) {
        this.transicionesAFD = transicionesAFD;
    }

    public EstadoAFD getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(EstadoAFD estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public EstadoAFD getEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(EstadoAFD estadoFinal) {
        this.estadoFinal = estadoFinal;
    }
}
