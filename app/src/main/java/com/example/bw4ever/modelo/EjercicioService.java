package com.example.bw4ever.modelo;

import java.util.ArrayList;
import java.util.List;

public class EjercicioService {
    public static List<Ejercicio> ejercicioList = new ArrayList<>();

    public static void addEjercicio(Ejercicio ejercicio){
        ejercicioList.add(ejercicio);
    }

    public static void removeEjercicio(Ejercicio ejercicio){
        ejercicioList.remove(ejercicio);
    }

    public static void updateEjercicio(Ejercicio ejercicio){
        ejercicioList.set(ejercicioList.indexOf(ejercicio), ejercicio);
    }
}
