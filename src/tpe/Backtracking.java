package tpe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Backtracking {

    private List<Tarea> tareas;
    private List<Procesador> procesadores;
    private HashMap<String, ArrayList<Tarea>> solucionFinal;
    private int tareasAsignadas;

    public Backtracking(ArrayList<Tarea> tareas, ArrayList<Procesador> procesadores) {
        this.tareas = tareas;
        this.procesadores = procesadores;
        this.solucionFinal = new HashMap<String, ArrayList<Tarea>>();
    }

    //FUNCION PUBLICA
    public void encontrarSolucion(int tiempoMaximo) {
        int metrica = 0;
        tareasAsignadas = -1;
        HashMap<String, ArrayList<Tarea>> solucionActual = new HashMap<String, ArrayList<Tarea>>();
        //mandar a recursivo
        this.encontrarSolucion(solucionActual,metrica,tiempoMaximo,0);

        //impre solucion
        this.imprimirSolucion(metrica);
    }

    //FUNCION PRIVADA
    private void encontrarSolucion(HashMap<String, ArrayList<Tarea>> solucionActual, int metrica,int tiempoMaximo, int indiceTareas) {
        metrica += 1;
        if(this.tareas.size() == tareasAsignadas) { //asigne todas las tareas a algun procesador
            if(this.solucionFinal.isEmpty()) {
                this.solucionFinal.putAll(solucionActual);
            }
            else {
                this.quedarseConLaMejorSolucion(solucionActual);
            }
        }
        else {

            int i = indiceTareas;
            //recorrer tareas
            //i = 10
            //tareas size = 10
            while(i < this.tareas.size()) {

                Tarea tarea = this.tareas.get(i);

                int j = 0;
                //recorro procesadores
                while (j < this.procesadores.size()) {

                    Procesador procesador = this.procesadores.get(j);
                    //poda 1
                    if(tarea.getEsCritica() && noSobrepasaLimiteCriticas(solucionActual.get(procesador.getId()))) {

                        //poda 2
                        if(!procesador.getEstaRefrigerado() && 
                        (getTiempoParcial(solucionActual.get(procesador.getId())) + tarea.getTiempoEjecucion()) < tiempoMaximo) 
                        {   
                            //ASIGNO TAREA AL PROCESADOR
                            if(!solucionActual.containsKey(procesador.getId())) {
                                //si no existe el procesador en el hash
                                ArrayList<Tarea> newListTareas = new ArrayList<Tarea>();
                                solucionActual.put(procesador.getId(), newListTareas);
                                solucionActual.get(procesador.getId()).add(tarea);
                            }
                            else {
                                solucionActual.get(procesador.getId()).add(tarea);
                            }
                            //llamo a recursivad
                            int nuevoIndiceTareas = i+1;
                            tareasAsignadas++;
                            this.encontrarSolucion(solucionActual,metrica,tiempoMaximo,nuevoIndiceTareas);
                            //termino recursividad

                            //saco tarea de procesador
                            solucionActual.get(procesador.getId()).remove(tarea);
                        }
                    }
                    //aumento iterador de procesadores
                    j++;
                }
                //aumento iterador de tareas
                i++;
            }
        }
        tareasAsignadas--;
    }


    private int getTiempoParcial(ArrayList<Tarea> tareasDeProcesador) {
        int tiempoAcumulado = 0;

        for (Tarea tarea : tareasDeProcesador) {
            tiempoAcumulado += tarea.getTiempoEjecucion();
        }
        return tiempoAcumulado;
    }

    private void quedarseConLaMejorSolucion(HashMap<String, ArrayList<Tarea>> solucionActual) {

        Integer timepoMaximoDeSolucionActual = 0;

        for (ArrayList<Tarea> tareas : solucionActual.values()) {
            Integer nuevoTiempo = this.getTiempoMaximoEjecucion(tareas);

            if(timepoMaximoDeSolucionActual < nuevoTiempo) {
                timepoMaximoDeSolucionActual = nuevoTiempo;
            }
        }

        Integer timepoMaximoDeSolucionFinal = 0;

        for (ArrayList<Tarea> tareas : solucionFinal.values()) {
            Integer nuevoTiempo = this.getTiempoMaximoEjecucion(tareas);

            if(timepoMaximoDeSolucionFinal < nuevoTiempo) {
                timepoMaximoDeSolucionFinal = nuevoTiempo;
            }
        }

        if(timepoMaximoDeSolucionFinal > timepoMaximoDeSolucionActual) {
            this.solucionFinal = solucionActual;
        }

    }


    private boolean noSobrepasaLimiteCriticas(ArrayList<Tarea> tareasDeProcesador) {
        Integer cantCriticas = 0;
        for (Tarea tarea : tareasDeProcesador) {
            if(tarea.getEsCritica()) {
                cantCriticas++;
            }
        }

        return cantCriticas < 2;
    }

    public void imprimirSolucion(int metrica) {
        System.out.println("Metrica de solucion: "+metrica);

        Integer tiempoMaximoEjecucion = 0;
        for (ArrayList<Tarea> tareas : this.solucionFinal.values()) {

            Integer nuevoTiempo = this.getTiempoMaximoEjecucion(tareas);

            if(tiempoMaximoEjecucion < nuevoTiempo) {
                tiempoMaximoEjecucion = nuevoTiempo;
            }

        }
        System.out.println("tiempo máximo de ejecución: " + tiempoMaximoEjecucion);


        for (String key : this.solucionFinal.keySet()) {
            //procesador
            // System.out.println("Procesador con el codigo: " + this.procesadores.get().getCodigo());
            this.solucionFinal.get(key);
            //tareas
            for (Tarea tarea : this.solucionFinal.get(key)) {
                System.out.println("Tarea: "+tarea.getNombre());
            }
        }
    }
    //
    /*public int getTiempoMaximoEjecucion(ArrayList<Tarea> tareas) {
        return 0;
    }*/
}