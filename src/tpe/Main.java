package tpe;
import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        String pathTareas = "C:\\Users\\s7\\Desktop\\TPE\\programacion3\\src\\tpe\\datasets\\Tareas.csv";
        String pathProcesadores = "C:\\Users\\s7\\Desktop\\TPE\\programacion3\\src\\tpe\\datasets\\Procesadores.csv";

        Servicios s1 = new Servicios(pathProcesadores, pathTareas);

        String t1 = "T1";
        String t2 = "T2";
        String t3 = "T3";
        String t4 = "T4";

        System.out.println(s1.servicio1(t1));
        System.out.println(s1.servicio1(t2));
        System.out.println(s1.servicio1(t3));
        System.out.println(s1.servicio1(t4));

        List<Tarea> tareasServcio2 = s1.servicio2(false);
        List<Tarea> tareasServcio3 = s1.servicio3(30, 59);

        s1.procesarDatosServicio(tareasServcio2);
        s1.procesarDatosServicio(tareasServcio3);
    }
}