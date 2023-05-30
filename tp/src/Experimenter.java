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

    int variacionMaximaDeHilos = 10;

    int iteracionesParaPaliarAspectosNoControlables = 100;

    public void correrExperimentos() {
        agregarVsQuitar();
        agregarVsPertenecer();
        quitarVsPertenecer();
        variarCantidadDeHilosTotalesManteniendoOperacionesTotales();
        variarCantidadDeHilosTotalesManteniendoOperacionesPorHilo();
    }

    private void variarCantidadDeHilosTotalesManteniendoOperacionesTotales() {
        System.out.println("Corriendo variarCantidadDeHilosTotalesManteniendoOperacionesTotales: ");
        try {
            JSONObject json = new JSONObject();
            variarCantidadDeHilosTotalesManteniendoOperacionesTotalesAux(json, "locks finos");
            variarCantidadDeHilosTotalesManteniendoOperacionesTotalesAux(json, "optimista");
            variarCantidadDeHilosTotalesManteniendoOperacionesTotalesAux(json, "sin locks");

            File file = new File("variandoHilosTotalesManteniendoOperacionesTotales.json");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json.toString());
            bw.close();
        } catch (IOException e) {
            System.out.println("variando cantidad de hilos, manteniendo operaciones totales: NOT OK");
        }
    }

    private void variarCantidadDeHilosTotalesManteniendoOperacionesTotalesAux(JSONObject json, String mensaje) {
        int cantOperacionesTotales = 10000;
        for (int cantHilos = 1; cantHilos <= variacionMaximaDeHilos; ++cantHilos) {
            String cantHilosString = String.valueOf(cantHilos);
            if (!json.has(cantHilosString)) {
                json.put(cantHilosString, new JSONArray());
            }

            System.out.println(mensaje + ", cant hilos: " + String.valueOf(cantHilos));

            int addedResults = 0;
            for (int i = 0; i < iteracionesParaPaliarAspectosNoControlables; ++i) {
                Conjunto<Integer> c;
                if (mensaje.equals("locks finos")) {
                    c = new ConjuntoConLocksFinos<Integer>();
                } else if (mensaje.equals("optimista")) {
                    c = new ConjuntoOptimista<Integer>();
                } else {
                    c = new ConjuntoSinLocks<Integer>();
                }
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

    private void variarCantidadDeHilosTotalesManteniendoOperacionesPorHilo() {
        System.out.println("corriendo variarCantidadDeHilosTotalesManteniendoOperacionesPorHilo:");
        try {
            JSONObject json = new JSONObject();
            variarCantidadDeHilosTotalesManteniendoOperacionesPorHiloAux(json, "locks finos");
            variarCantidadDeHilosTotalesManteniendoOperacionesPorHiloAux(json, "optimista");
            variarCantidadDeHilosTotalesManteniendoOperacionesPorHiloAux(json, "sin locks");

            File file = new File("variandoHilosTotalesManteniendoOperacionesPorHilo.json");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json.toString());
            bw.close();
        } catch (IOException e) {
            System.out.println("variando cantidad de hilos, manteniendo operaciones por hilo: NOT OK");
        }
    }

    private void variarCantidadDeHilosTotalesManteniendoOperacionesPorHiloAux(JSONObject json, String mensaje) {
        int cantOperacionesPorHilo = 1000;
        for (int cantHilos = 1; cantHilos <= variacionMaximaDeHilos; ++cantHilos) {
            String cantHilosString = String.valueOf(cantHilos);
            if (!json.has(cantHilosString)) {
                json.put(cantHilosString, new JSONArray());
            }

            System.out.println(mensaje + ", cant hilos: " + String.valueOf(cantHilos));

            int addedResults = 0;
            for (int i = 0; i < iteracionesParaPaliarAspectosNoControlables; ++i) {
                Conjunto<Integer> c;
                if (mensaje.equals("locks finos")) {
                    c = new ConjuntoConLocksFinos<Integer>();
                } else if (mensaje.equals("optimista")) {
                    c = new ConjuntoOptimista<Integer>();
                } else {
                    c = new ConjuntoSinLocks<Integer>();
                }
                CountDownLatch latch = new CountDownLatch(cantHilos);
                long startTime = System.currentTimeMillis();
                for (int hilo = 1; hilo <= cantHilos; ++hilo) {
                    int desde = 1 + (hilo-1) * cantOperacionesPorHilo;
                    int hasta = hilo * cantOperacionesPorHilo;
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

    private void agregarVsQuitar() {
        System.out.println("corriendo agregarVsQuitar:");
        try {
            JSONObject json = new JSONObject();
            agregarVsQuitarAux(json, "locks finos");
            agregarVsQuitarAux(json, "optimista");
            agregarVsQuitarAux(json, "sin locks");

            File file = new File("agregarVsQuitar.json");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json.toString());
            bw.close();
        } catch (IOException e) {
            System.out.println("agregar vs quitar: NOT OK");
        }
    }

    private void agregarVsQuitarAux(JSONObject json, String mensaje) {
        int cantOperacionesPorHilo = 10000;
        int cantFijaDeHilos = 30;
        for (int proporcionDeHilos = 0; proporcionDeHilos <= cantFijaDeHilos; proporcionDeHilos = proporcionDeHilos + cantFijaDeHilos/10) {
            String propHilosString = String.valueOf(proporcionDeHilos * 100 / cantFijaDeHilos) + "%";
            if (!json.has(propHilosString)) {
                json.put(propHilosString, new JSONArray());
            }

            System.out.println(mensaje + ", proporción agregar: " + propHilosString);

            int addedResults = 0;
            for (int i = 0; i < iteracionesParaPaliarAspectosNoControlables; ++i) {
                Conjunto<Integer> c;
                if (mensaje.equals("locks finos")) {
                    c = new ConjuntoConLocksFinos<Integer>();
                } else if (mensaje.equals("optimista")) {
                    c = new ConjuntoOptimista<Integer>();
                } else {
                    c = new ConjuntoSinLocks<Integer>();
                }
                CountDownLatch latch = new CountDownLatch(cantFijaDeHilos);
                long startTime = System.currentTimeMillis();
                for (int hilo = 1; hilo <= proporcionDeHilos; ++hilo) {
                    Thread t = new Thread(new HiloAgregadorDeRandoms(c, cantOperacionesPorHilo, 1, cantOperacionesPorHilo, latch), "T" + String.valueOf(hilo));
                    t.start();
                }

                for (int hilo = proporcionDeHilos+1; hilo <= cantFijaDeHilos; ++hilo) {
                    Thread t = new Thread(new HiloQuitadorDeRandoms(c, cantOperacionesPorHilo, 1, cantOperacionesPorHilo, latch), "T" + String.valueOf(hilo));
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
            JSONArray arr = json.getJSONArray(propHilosString);
            arr.put(mensaje + ": " + String.valueOf(averageResult));
            json.put(propHilosString, arr);
        }
    }

    private void agregarVsPertenecer() {
        System.out.println("corriendo agregarVsPertenecer:");
        try {
            JSONObject json = new JSONObject();
            agregarVsPertenecerAux(json, "locks finos");
            agregarVsPertenecerAux(json, "optimista");
            agregarVsPertenecerAux(json, "sin locks");

            File file = new File("agregarVsPertenecer.json");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json.toString());
            bw.close();
        } catch (IOException e) {
            System.out.println("agregar vs pertenecer: NOT OK");
        }
    }

    private void agregarVsPertenecerAux(JSONObject json, String mensaje) {
        int cantOperacionesPorHilo = 1000;
        int cantFijaDeHilos = 30;
        for (int proporcionDeHilos = 0; proporcionDeHilos <= cantFijaDeHilos; proporcionDeHilos = proporcionDeHilos + cantFijaDeHilos/10) {
            String propHilosString = String.valueOf(proporcionDeHilos * 100 / cantFijaDeHilos) + "%";
            if (!json.has(propHilosString)) {
                json.put(propHilosString, new JSONArray());
            }

            System.out.println(mensaje + ", proporción agregar: " + propHilosString);

            int addedResults = 0;
            for (int i = 0; i < iteracionesParaPaliarAspectosNoControlables; ++i) {
                Conjunto<Integer> c;
                if (mensaje.equals("locks finos")) {
                    c = new ConjuntoConLocksFinos<Integer>();
                } else if (mensaje.equals("optimista")) {
                    c = new ConjuntoOptimista<Integer>();
                } else {
                    c = new ConjuntoSinLocks<Integer>();
                }
                CountDownLatch latch = new CountDownLatch(cantFijaDeHilos);
                long startTime = System.currentTimeMillis();
                for (int hilo = 1; hilo <= proporcionDeHilos; ++hilo) {
                    Thread t = new Thread(new HiloAgregadorDeRandoms(c, cantOperacionesPorHilo,1, Integer.MAX_VALUE, latch), "T" + String.valueOf(hilo));
                    t.start();
                }

                for (int hilo = proporcionDeHilos+1; hilo <= cantFijaDeHilos; ++hilo) {
                    Thread t = new Thread(new HiloPerteneceRandoms(c, cantOperacionesPorHilo, 1, Integer.MAX_VALUE ,latch), "T" + String.valueOf(hilo));
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
            JSONArray arr = json.getJSONArray(propHilosString);
            arr.put(mensaje + ": " + String.valueOf(averageResult));
            json.put(propHilosString, arr);
        }
    }

    private void quitarVsPertenecer() {
        System.out.println("corriendo quitarVsPertenecer:");
        int cantFijaDeHilos = 30;
        int cantOperacionesPorHilo = 1000;
        try {
            JSONObject json = new JSONObject();

            quitarVsPertenecerAux(json, "locks finos", cantFijaDeHilos, cantOperacionesPorHilo);
            quitarVsPertenecerAux(json, "optimista", cantFijaDeHilos, cantOperacionesPorHilo);
            quitarVsPertenecerAux(json, "sin locks", cantFijaDeHilos, cantOperacionesPorHilo);

            File file = new File("quitarVsPertenecer.json");
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json.toString());
            bw.close();
        } catch (IOException e) {
            System.out.println("quitar vs pertenecer: NOT OK");
        }
    }

    private void quitarVsPertenecerAux(JSONObject json, String mensaje, int cantFijaDeHilos, int cantOperacionesPorHilo) {
        for (int proporcionDeHilos = 0; proporcionDeHilos <= cantFijaDeHilos; proporcionDeHilos = proporcionDeHilos + cantFijaDeHilos/10) {
            String propHilosString = String.valueOf(proporcionDeHilos * 100 / cantFijaDeHilos) + "%";
            if (!json.has(propHilosString)) {
                json.put(propHilosString, new JSONArray());
            }

            System.out.println(mensaje + ", proporción agregar: " + propHilosString);

            int addedResults = 0;
            for (int i = 0; i < iteracionesParaPaliarAspectosNoControlables; ++i) {
                Conjunto<Integer> c;
                if (mensaje.equals("locks finos")) {
                    c = new ConjuntoConLocksFinos<Integer>();
                } else if (mensaje.equals("optimista")) {
                    c = new ConjuntoOptimista<Integer>();
                } else {
                    c = new ConjuntoSinLocks<Integer>();
                }
                for (Integer j = 1; j <= 10*cantOperacionesPorHilo; ++j) {
                    c.agregar(j);
                }

                CountDownLatch latch = new CountDownLatch(cantFijaDeHilos);
                long startTime = System.currentTimeMillis();
                for (int hilo = 1; hilo <= proporcionDeHilos; ++hilo) {
                    Thread t = new Thread(new HiloQuitadorDeRandoms(c, cantOperacionesPorHilo, 1, 10*cantOperacionesPorHilo, latch), "T" + String.valueOf(hilo));
                    t.start();
                }

                for (int hilo = proporcionDeHilos+1; hilo <= cantFijaDeHilos; ++hilo) {
                    Thread t = new Thread(new HiloPerteneceRandoms(c, cantOperacionesPorHilo, 1, 10*cantOperacionesPorHilo, latch), "T" + String.valueOf(hilo));
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
            JSONArray arr = json.getJSONArray(propHilosString);
            arr.put(mensaje + ": " + String.valueOf(averageResult));
            json.put(propHilosString, arr);
        }
    }

}
