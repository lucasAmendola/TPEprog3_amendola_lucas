package tpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Greedy {

    private List<Tarea> tareas;
    private List<Procesador> procesadores;
    private HashMap<String, ArrayList<Tarea>> solucionFinal;
    private HashMap<String, Integer> hashTiempo;
    private HashMap<String, ArrayList<Tarea>> hashCriticas;
    private HashMap<Tarea, Boolean> hashTareasAsignadas;

    public Greedy(ArrayList<Tarea> tareas, ArrayList<Procesador> procesadores) {
        this.tareas = tareas;
        this.procesadores = procesadores;
        this.solucionFinal = new HashMap<String, ArrayList<Tarea>>();
        this.hashTiempo = new HashMap<String, Integer>();
        this.hashCriticas = new HashMap<String, ArrayList<Tarea>>();
        this.hashTareasAsignadas = new HashMap<Tarea, Boolean>();

        for (Procesador procesador : procesadores) {
            hashTiempo.put(procesador.getId(), 0);
            hashCriticas.put(procesador.getId(), new ArrayList<>());
        
            ArrayList<Tarea> arrayListTareas = new ArrayList<>();
            solucionFinal.put(procesador.getId(), arrayListTareas);
        }
    }

    public Boolean encontrarSolucion(int tiempoMaximo){

        for (Tarea tarea : tareas) {
            hashTareasAsignadas.put(tarea, false);
        }

        for (Tarea tarea : tareas) {
                String idMejorProcesadorPosible = getMejorProcesadoPosible(tarea, tiempoMaximo);
                this.solucionFinal.get(idMejorProcesadorPosible).add(tarea);
        }

        for (Tarea tarea : hashTareasAsignadas.keySet()) {
                if (!hashTareasAsignadas.get(tarea)) {
                    return false;
                }
        }
        return true;
    }

    private String getMejorProcesadoPosible(Tarea tarea, int tiempoMaximo) {
        String idMejorProcesador = "";

        for (Procesador procesador : procesadores) {

            if ((tarea.getEsCritica() && this.hashCriticas.get(procesador.getId()).size() < 2) || !tarea.getEsCritica()) {
               
                int sumaParcial = this.hashTiempo.get(procesador.getId());
                int sumaNueva = sumaParcial + tarea.getTiempoEjecucion();

                // Poda para procesadores no refrigerados
                if ((!procesador.getEstaRefrigerado() && sumaNueva < tiempoMaximo) || procesador.getEstaRefrigerado()) {
                        this.hashTiempo.put(procesador.getId(), sumaNueva);

                        // Si es crítica la agrego al hash auxiliar de tareas críticas    
                        if (tarea.getEsCritica()) {
                            this.hashCriticas.get(procesador.getId()).add(tarea);
                        }

                        if(idMejorProcesador.isEmpty()){
                            idMejorProcesador = procesador.getId();
                        }
                        else if((this.hashTiempo.get(procesador.getId()) < this.hashTiempo.get(idMejorProcesador))){
                            idMejorProcesador = procesador.getId();
                        }
                } 
            }
        }
        return idMejorProcesador;
    }
}
