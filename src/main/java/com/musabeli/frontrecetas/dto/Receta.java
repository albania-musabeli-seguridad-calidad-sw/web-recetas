package com.musabeli.frontrecetas.dto;

public record Receta(
        Long id,
        String nombre,
        String tipoCocina,
        String ingredientes,
        String pais,
        String dificultad,
        Integer tiempo,
        String instrucciones,
        String fotoUrl
) {
    // Constructor sin instrucciones
    public Receta(Long id, String nombre, String tipoCocina, String ingredientes,
                  String pais, String dificultad, Integer tiempo, String fotoUrl) {
        this(id, nombre, tipoCocina, ingredientes, pais, dificultad, tiempo, "",fotoUrl);
    }

    // Constructor solo nombre y cocina  para menús
    public Receta(Long id, String nombre, String tipoCocina) {
        this(id, nombre, tipoCocina, "", "", "", 0, "");
    }
}