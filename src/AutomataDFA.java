/*
 * Universdidad del Valle de Guatemala
 * Diseño de lenguajes de programacion
 * Compiladores
 *
 * @author Sebastian Galindo, Carnet: 15452
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AutomataDFA {
    private ArrayList<Dtran> transicionesAFD = new ArrayList<>();
    private EstadoAFD estadoInicial;
    private EstadoAFD estadoFinal;
    private Set<EstadoAFD> Dstates = new HashSet<>();

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


    public Set<EstadoAFD> getDstates() {
        return Dstates;
    }

    public void setDstates(Set<EstadoAFD> dstates) {
        Dstates = dstates;
    }
}
