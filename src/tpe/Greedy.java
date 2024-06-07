package tpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import tpe.utils.CSVReader;

public class Greedy {

    private ArrayList<Tarea> tareas;
    private ArrayList<Procesador> procesadores;
    private HashMap<String, ArrayList<Tarea>> solucionFinal;
    private HashMap<String, Integer> hashTiempo;
    private HashMap<String, ArrayList<Tarea>> hashCriticas;
    private HashMap<Tarea, Boolean> hashTareasAsignadas;
    private int metrica;

    public Greedy(String pathTareas, String pathProcesadores) {

        //data procesadores y tareas
        this.procesadores = new ArrayList<>();
        this.tareas = new ArrayList<>();

        //hashmaps auxiliares y metrica de solucion
        this.metrica = 0;
        this.hashTiempo = new HashMap<String, Integer>();
        this.hashCriticas = new HashMap<String, ArrayList<Tarea>>();
        this.hashTareasAsignadas = new HashMap<Tarea, Boolean>();

        //estructura de solucion final
        this.solucionFinal = new HashMap<String, ArrayList<Tarea>>();

        CSVReader reader = new CSVReader();
		reader.readProcessors(pathProcesadores, procesadores);
        reader.readTasks(pathTareas, tareas, new ArrayList<>(), new ArrayList<>(), new HashMap<>());

        for (Procesador procesador : procesadores) {
            hashTiempo.put(procesador.getId(), 0);
            hashCriticas.put(procesador.getId(), new ArrayList<>());
        }
    }

    /*
    * <<La estrategia Greedy utilizada en este enfoque se basa en que para cada tarea que 
        querramos asignar, se buscara hayar el MEJOR procesador en donde esta pueda ser ubicada en el momento, 
        esto se logra mediante el metodo "getMejorProcesadorPosible", el cual devolvera aquel procesador
        que tenga el menor tiempo de ejecucion cargado hasta el momento o que todavia no posea tareas asignadas,
        con el objetivo de ir colocando las tareas sin sobrecargar procesadores con muchas tareas y asi lograr
        una distribucion uniforme de las mismas.
        A su vez son aplicadas 2 restricciones, la primera de ellas se basa en que el procesador
        al que le estamos asignando tareas no puede sobrepasar el limite de 2 tareas criticas asignadas,
        esto se comprueba a través del HashMap auxiliar "hashCriticas", en donde se van almacenando
        las tareas criticas que se le van asignando a cada procesador. Una vez superada esta restriccion, se
        encuentra la segunda, que consta de comprobar que aquellos procesadores que no posean refiregeración
        no sobrepasen un limite determinado de tiempo de ejecución, el cual es asignado por el usuario
        y que tambien se comprueba a través de la utilización de un HashMap auxiliar, en donde se va
        alamecenando el tiempo de ejecucion de cada procesador a medida de que se le asignan tareas. Una
        vez superadas las 2 podas, se llega a la instancia de asignacion final de la tarea al procesador 
        actual y se pasa a la siguiente tarea. Una vez se hayan asignado todas las tareas, obtendremos
        nuestra solucion final.>>
    */

    //Metodo publico que llamra al metodo encargado de la asignacion
    public void buscarSolucion(int tiempoMaximo){
        //si el metodo "encontrarSolucion devuelve true sigfica que hay solucion posible"
        if (encontrarSolucion(tiempoMaximo)) {
            //imprime la solucion obetenida
            this.imprimirSolucion(metrica);
        }
        else{
            //en caso de que no haya podido asignar todas las tareas, informa cuales fueron estas
            this.imprimirSolucion(tiempoMaximo);
            System.out.println("\n");
            System.out.println("Tareas que no pudieron asignarse");
            for (Map.Entry<Tarea, Boolean> entry : hashTareasAsignadas.entrySet()) {
                if (!this.hashTareasAsignadas.get(entry.getKey())) {
                    System.out.println(entry);
                }
            }
        }
    }

    private Boolean encontrarSolucion(int tiempoMaximo){

        //inicializacion de las tareas al hashmap auxiliar encargado de comprobar que se asignen las tareas
        //se le setea false para indicar q aun no ha sido asignada
        for (Tarea tarea : tareas) {
            hashTareasAsignadas.put(tarea, false);
        }

        //para cada tarea busco el procesador mas indicado
        for (Tarea tarea : tareas) {
                metrica++;
                String idMejorProcesadorPosible = getMejorProcesadorPosible(tarea, tiempoMaximo);
                //si el procesador no es null, es porque encontro el mejor candidato en ese momento
                if(idMejorProcesadorPosible != null){
                    //si el procesador todavia no se encuentra en mi solucion final, lo agrego junto con su lista de tareas
                    solucionFinal.computeIfAbsent(idMejorProcesadorPosible, k -> new ArrayList<>()).add(tarea);
                }
        }

        for (Boolean asignada : hashTareasAsignadas.values()) {
            //En caso de que alguna tarea haya terminado sin asignar devuelvo false
                if (!asignada) {
                    return false;
                }
        }
        //todas las tareas fueron asignadas, devuelvo true
        return true;
    }

    private String getMejorProcesadorPosible(Tarea tarea, int tiempoMaximo) {
        String idMejorProcesador = null;

        for (Procesador procesador : procesadores) {

            // Restriccion de limite de tareas criticas por procesador
            if ((tarea.getEsCritica() && this.hashCriticas.get(procesador.getId()).size() < 2) || !tarea.getEsCritica()) {
               
                int sumaParcial = this.hashTiempo.get(procesador.getId());
                int sumaNueva = sumaParcial + tarea.getTiempoEjecucion();

                // Restriccion para procesadores no refrigerados
                if ((!procesador.getEstaRefrigerado() && sumaNueva <= tiempoMaximo) || procesador.getEstaRefrigerado()) {
                    //si mi Idprocesador todavia no fue comparado o el tiempo del procesador actual es menor
                    //al del anteriormente considerado
                    if (idMejorProcesador == null || sumaNueva < this.hashTiempo.get(idMejorProcesador)) {
                        //mi nuevo mejor procesador
                        idMejorProcesador = procesador.getId();
                    }
                }
            }
        }
    
        if (idMejorProcesador != null) {
            //sumo al hash auxiliar del tiempo
            int sumaNueva = hashTiempo.get(idMejorProcesador) + tarea.getTiempoEjecucion();
            hashTiempo.put(idMejorProcesador, sumaNueva);
            //si la tarea es critica la sumo al hash auxiliar
            if (tarea.getEsCritica()) {
                this.hashCriticas.get(idMejorProcesador).add(tarea);
            }
            this.hashTareasAsignadas.put(tarea, true);
        }
    
        return idMejorProcesador;
}
    
    public void imprimirSolucion(int metrica) {
        
        System.out.println("Solución Final GREEDY: ");
        for (Map.Entry<String, ArrayList<Tarea>> entry : solucionFinal.entrySet()) {
            System.out.println("Procesador: " + entry.getKey());
            for (Tarea tarea : entry.getValue()) {
                System.out.println("  Tarea: " + tarea.getNombre() + " Tiempo: " + tarea.getTiempoEjecucion());
            }
        }
        System.out.println("Metrica de solucion: " + metrica);

        int tiempoMaximoEjecucion = 0;
        for (ArrayList<Tarea> tareas : solucionFinal.values()) {
            int nuevoTiempo = this.obtenerTiempoTotalDeEjecucion(tareas);
            if(tiempoMaximoEjecucion < nuevoTiempo){
                tiempoMaximoEjecucion = nuevoTiempo;
            }
        }
        System.out.println("Tiempo máximo de ejecución: " + tiempoMaximoEjecucion);
    }

  private int obtenerTiempoTotalDeEjecucion(ArrayList<Tarea> tareas){
        int tiempoEjecucion = 0;

        for (Tarea tarea : tareas) {
             tiempoEjecucion += tarea.getTiempoEjecucion();
        }
        return tiempoEjecucion;
  }
}
