package com.musabeli.frontrecetas.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RecetaTest {

    private static final Long ID = 1L;
    private static final String NOMBRE = "Arroz con pollo";
    private static final String TIPO_COCINA = "Peruana";
    private static final String INGREDIENTES = "arroz, pollo, ajo...";
    private static final String PAIS = "Perú";
    private static final String DIFICULTAD = "Media";
    private static final Integer TIEMPO = 45;
    private static final String INSTRUCCIONES = "Cocinar el pollo...";
    private static final String FOTO_URL = "https://example.com/arroz.jpg";

    @Nested
    @DisplayName("Constructor canónico (todos los campos)")
    class ConstructorCompleto {

        @Test
        void creaRecetaConTodosLosCamposCorrectamente() {
            Receta receta = new Receta(
                    ID, NOMBRE, TIPO_COCINA, INGREDIENTES,
                    PAIS, DIFICULTAD, TIEMPO, INSTRUCCIONES, FOTO_URL);

            assertThat(receta.id()).isEqualTo(ID);
            assertThat(receta.nombre()).isEqualTo(NOMBRE);
            assertThat(receta.tipoCocina()).isEqualTo(TIPO_COCINA);
            assertThat(receta.ingredientes()).isEqualTo(INGREDIENTES);
            assertThat(receta.pais()).isEqualTo(PAIS);
            assertThat(receta.dificultad()).isEqualTo(DIFICULTAD);
            assertThat(receta.tiempo()).isEqualTo(TIEMPO);
            assertThat(receta.instrucciones()).isEqualTo(INSTRUCCIONES);
            assertThat(receta.fotoUrl()).isEqualTo(FOTO_URL);
        }
    }

    @Nested
    @DisplayName("Constructor sin instrucciones")
    class ConstructorSinInstrucciones {

        @Test
        void usaConstructorSinInstruccionesYDejaCampoVacio() {
            Receta receta = new Receta(
                    ID, NOMBRE, TIPO_COCINA, INGREDIENTES,
                    PAIS, DIFICULTAD, TIEMPO, FOTO_URL);

            assertThat(receta.instrucciones()).isEmpty();
            assertThat(receta.id()).isEqualTo(ID);
            assertThat(receta.nombre()).isEqualTo(NOMBRE);
            assertThat(receta.fotoUrl()).isEqualTo(FOTO_URL);
        }
    }

    @Nested
    @DisplayName("Constructor mínimo (solo id, nombre y tipoCocina)")
    class ConstructorMinimo {

        @Test
        void usaConstructorParaMenusYCompletaCamposConValoresPorDefecto() {
            Receta receta = new Receta(ID, NOMBRE, TIPO_COCINA);

            assertThat(receta.id()).isEqualTo(ID);
            assertThat(receta.nombre()).isEqualTo(NOMBRE);
            assertThat(receta.tipoCocina()).isEqualTo(TIPO_COCINA);
            assertThat(receta.ingredientes()).isEmpty();
            assertThat(receta.pais()).isEmpty();
            assertThat(receta.dificultad()).isEmpty();
            assertThat(receta.tiempo()).isZero();
            assertThat(receta.instrucciones()).isEmpty();
            assertThat(receta.fotoUrl()).isEmpty();
        }
    }

    @Test
    void equalsYHashCodeFuncionanCorrectamenteConMismosValores() {
        Receta receta1 = new Receta(ID, NOMBRE, TIPO_COCINA, INGREDIENTES,
                PAIS, DIFICULTAD, TIEMPO, INSTRUCCIONES, FOTO_URL);

        Receta receta2 = new Receta(ID, NOMBRE, TIPO_COCINA, INGREDIENTES,
                PAIS, DIFICULTAD, TIEMPO, INSTRUCCIONES, FOTO_URL);

        assertThat(receta1)
                .isEqualTo(receta2)
                .hasSameHashCodeAs(receta2);
    }

    @Test
    void toStringContieneTodosLosCampos() {
        Receta receta = new Receta(ID, NOMBRE, TIPO_COCINA, INGREDIENTES,
                PAIS, DIFICULTAD, TIEMPO, INSTRUCCIONES, FOTO_URL);

        assertThat(receta.toString())
                .contains("id=1")
                .contains("nombre=Arroz con pollo")
                .contains("tipoCocina=Peruana");
    }
}