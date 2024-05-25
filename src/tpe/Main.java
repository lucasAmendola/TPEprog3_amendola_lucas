package tpe;
import java.util.HashMap;
import tpe.utils.CSVReader;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        String pathTareas = "C:\\Users\\s7\\Desktop\\TPE\\programacion3\\src\\tpe\\datasets\\Tareas.csv";
        String pathProcesadores = "C:\\Users\\s7\\Desktop\\TPE\\programacion3\\src\\tpe\\datasets\\Procesadores.csv";

        CSVReader reader = new CSVReader();
        
        Servicios s1 = new Servicios(pathProcesadores, pathTareas);

        String t1 = "T1";

        Tarea tarea1 = s1.servicio1(t1);

        System.out.println(tarea1);
    }
}