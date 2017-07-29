import java.util.ArrayList;

public class Automata {

    
    private Nodo nodoInicial = new Nodo();
    private Nodo nodoFinal = new Nodo(); 

    public Automata(String transicion){
        //agregando al nodo inicial del automata el nodo al que esta dirigido
        nodoInicial.agregar(transicion, nodoFinal);
        if(MainLab2.miArrayNodos.contains(nodoInicial) ){
            
        }else{

            MainLab2.miArrayNodos.add(nodoInicial);
            System.out.println("Añadiendo nodo #: " +MainLab2.miArrayNodos.size());
        }

        if(MainLab2.miArrayNodos.contains(nodoFinal) ){
            
        }else{
            
            MainLab2.miArrayNodos.add(nodoFinal);
            System.out.println("Añadiendo nodo #: " +MainLab2.miArrayNodos.size());
        }
    }
    
    public Automata( Nodo I, Nodo F){
        nodoInicial=I;
        nodoFinal=F;
        if(MainLab2.miArrayNodos.contains(nodoInicial) ){
            
        }else{

            MainLab2.miArrayNodos.add(nodoInicial);
            System.out.println("Añadiendo nodo #: " +MainLab2.miArrayNodos.size());
        }
/*
        if(MainLab2.miArrayNodos.contains(nodoFinal) ){
            
        }else{

            MainLab2.miArrayNodos.add(nodoFinal);
            System.out.println("Añadiendo nodo #: " +MainLab2.miArrayNodos.size());
        }
        */
    }


    public Nodo getNodoInicial() {
        return nodoInicial;
    }

    public void setNodoInicial(Nodo nodoInicial) {
        this.nodoInicial = nodoInicial;
    }

    public Nodo getNodoFinal() {
        return nodoFinal;
    }

    public void setNodoFinal(Nodo nodoFinal) {
        this.nodoFinal = nodoFinal;
    }
    
    public String getArrayNodos(int tamaño){
        String nodosAutomatas="{";
        int cont=0-1;
        ArrayList<Nodo> temp = new ArrayList<>();
        /*Ciclo while que verifica que el tamaño del array de nodos sea del mismo
        tamaño que la cantidad de nodos que deben haber*/
        while(MainLab2.miArrayNodos.size()<tamaño){
            //Asignando un ArrayList Temporal.
            temp = MainLab2.miArrayNodos;
            System.out.println(temp.size() + ", " + tamaño);
            for(int i=0;i<temp.size();i++){
                ArrayList<Nodo> ElArray = temp.get(i).getElNodo();
                for(int j=0;j<ElArray.size();j++){
                    if(temp.contains(ElArray.get(j))){
                        //ElArray.get(j);
                    }else{
                        temp.add(ElArray.get(j));

                    }
                    
                }
                
            }
            System.out.println("Actualizando automata...");
            MainLab2.miArrayNodos = temp;
        }
        for(int y=0;y<MainLab2.miArrayNodos.size();y++){
            nodosAutomatas+=String.valueOf(MainLab2.miArrayNodos.get(y))+", ";
        }
        nodosAutomatas+="}";
        return nodosAutomatas;
    }
    
    /*
    public String getArrayNodos1(){
        int cont=0;
        ArrayList<Nodo> sinRepetir = new ArrayList<>();
        while(cont<MainLab2.miArrayNodos.size()){
            for(Nodo i: MainLab2.miArrayNodos){
                if(i.equals(i))
            }
        }
        
        return ;
    }
    */
}
