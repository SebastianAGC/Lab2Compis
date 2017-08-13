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
    private ArrayList<DtranHoja> transicionesAFDHoja = new ArrayList<>();
    private ArrayList<EstadoAFDHoja> Dstates = new ArrayList<>();
    private EstadoAFDHoja S;
    private EstadoAFD estadoInicial;
    private EstadoAFD estadoFinal;
    private EstadoAFDHoja estadoInicialHoja;
    private EstadoAFDHoja estadoFinalHoja;

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

    public ArrayList<EstadoAFDHoja> getDstates() {
        return Dstates;
    }

    public void setDstates(ArrayList<EstadoAFDHoja> dstates) {
        Dstates = dstates;
    }

    public EstadoAFDHoja getS() {
        return S;
    }

    public void setS(EstadoAFDHoja s) {
        S = s;
    }

    public ArrayList<DtranHoja> getTransicionesAFDHoja() {
        return transicionesAFDHoja;
    }

    public void setTransicionesAFDHoja(ArrayList<DtranHoja> transicionesAFDHoja) {
        this.transicionesAFDHoja = transicionesAFDHoja;
    }

    public EstadoAFDHoja getEstadoInicialHoja() {
        return estadoInicialHoja;
    }

    public void setEstadoInicialHoja(EstadoAFDHoja estadoInicialHoja) {
        this.estadoInicialHoja = estadoInicialHoja;
    }

    public EstadoAFDHoja getEstadoFinalHoja() {
        return estadoFinalHoja;
    }

    public void setEstadoFinalHoja(EstadoAFDHoja estadoFinalHoja) {
        this.estadoFinalHoja = estadoFinalHoja;
    }
}
