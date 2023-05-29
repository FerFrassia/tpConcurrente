import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.util.JSONPObject;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Experimenter {
    boolean print_tiempo_tests = true;
    int cantidad_elementos = 10000;
    int cantOperacionesTotales = 10000;

    int variacionMaximaDeHilos = 10;

    int iteracionesParaPaliarAspectosNoControlables = 10;

    public void correrExperimentos() {
        variarCantidadDeHilosTotalesManteniendoOperacionesTotales();
    }

    public void variarCantidadDeHilosTotalesManteniendoOperacionesTotales() {
        try {
            JSONObject json = new JSONObject();
            variarCantidadDeHilosTotalesManteniendoOperacionesTotales2(new ConjuntoConLocksFinos<Integer>(), json, "locks finos");
            variarCantidadDeHilosTotalesManteniendoOperacionesTotales2(new ConjuntoOptimista<Integer>(), json, "optimista");
            variarCantidadDeHilosTotalesManteniendoOperacionesTotales2(new ConjuntoSinLocks<Integer>(), json, "sin locks");

            File file = new File("variandoCantidadDeHilosTotales.json");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json.toString());
            bw.close();
        } catch (IOException e) {
            System.out.println("variando cantidad de hilos totales: NOT OK");
        }
    }

    public void variarCantidadDeHilosTotalesManteniendoOperacionesTotales2(Conjunto<Integer> c, JSONObject json, String mensaje) {
        for (int cantHilos = 1; cantHilos <= variacionMaximaDeHilos; ++cantHilos) {
            String cantHilosString = String.valueOf(cantHilos);
            if (!json.has(cantHilosString)) {
                json.put(cantHilosString, new JSONArray());
            }

            System.out.println(mensaje + ", cant hilos: " + String.valueOf(cantHilos));

            int addedResults = 0;
            for (int i = 0; i < iteracionesParaPaliarAspectosNoControlables; ++i) {
                CountDownLatch latch = new CountDownLatch(cantHilos);
                int corte = cantOperacionesTotales / cantHilos;
                long startTime = System.currentTimeMillis();
                for (int hilo = 1; hilo <= cantHilos; ++hilo) {
                    int desde = 1 + (hilo-1) * corte;
                    int hasta = hilo * corte;
                    Thread t = new Thread(new HiloAgregadorDeConsecutivos(c, desde, hasta, latch), "T" + String.valueOf(hilo));
                    t.start();
                }

                try {
                    latch.await();
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    addedResults += elapsedTime;
                } catch (InterruptedException e) {
                    System.out.println("EXCEPTION:  " + e.toString());
                }
            }

            float averageResult = addedResults / iteracionesParaPaliarAspectosNoControlables;
            JSONArray arr = json.getJSONArray(cantHilosString);
            arr.put(mensaje + ": " + String.valueOf(averageResult));
            json.put(cantHilosString, arr);
        }
    }

}
