package com.musabeli.frontrecetas.controller;

import com.musabeli.frontrecetas.dto.Receta;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class RecetaController {

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        model.addAttribute("recientes", getRecetasRecientes());
        model.addAttribute("populares", getRecetasPopulares());

        String username = (String) session.getAttribute("username");
        model.addAttribute("currentUser", username);

        return "home";
    }

    @GetMapping("/buscar")
    public String buscar(Model model, HttpSession session) {
        model.addAttribute("resultados", getResultadosBusqueda());
        model.addAttribute("currentUser", (String) session.getAttribute("username"));
        return "buscar";
    }

    @GetMapping("/receta/{id}")
    public String detalle(@PathVariable Long id, Model model, HttpSession session) {
        Receta receta = getRecetaPorId(id);
        if (receta == null) return "redirect:/";
        model.addAttribute("receta", receta);
        model.addAttribute("currentUser", (String) session.getAttribute("username"));
        return "detalle-receta";
    }


    private List<Receta> getRecetasRecientes() {
        return List.of(
            new Receta(1L, "Tacos al Pastor", "Mexicana", "Carne de cerdo, piña, cebolla", "México", "Media", 45, "/img/tacos-al-pastor.jpg"),
            new Receta(2L, "Paella Valenciana", "Española", "Arroz, conejo, garbanzos", "España", "Alta", 90, "/img/paella-valenciana.jpg"),
            new Receta(3L, "Sushi California", "Japonesa", "Arroz, cangrejo, palta", "EE.UU.", "Media", 40, "/img/sushi-california.jpg")
        );
    }

    private List<Receta> getRecetasPopulares() {
        return List.of(
            new Receta(4L, "Pizza Margherita", "Italiana", "Tomate, mozzarella, albahaca", "Italia", "Baja", 30, "/img/pizza-margarita.jpg"),
            new Receta(5L, "Hamburguesa Clásica", "Americana", "Carne, queso, lechuga", "EE.UU.", "Baja", 20, "/img/hamburguesa-clasica.jpg"),
            new Receta(6L, "Ceviche Mixto", "Peruana", "Pescado, camarón, limón", "Perú", "Baja", 15, "/img/ceviche-mixto.jpg")
        );
    }

    private List<Receta> getResultadosBusqueda() {
        return List.of(
            new Receta(1L, "Tacos al Pastor", "Mexicana", "Carne marinada con salsa", "México", "Media", 45, "/img/tacos-al-pastor.jpg"),
            new Receta(7L, "Ramen Tonkotsu", "Japonesa", "Caldo de cerdo, fideos", "Japón", "Media", 120, "/img/ramen-tonkotsu.jpg"),
            new Receta(4L, "Pizza Margherita", "Italiana", "Masa fina, tomate natural", "Italia", "Baja", 30, "/img/pizza-margarita.jpg")
        );
    }

    private Receta getRecetaPorId(Long id) {
        return switch (id.intValue()) {
            case 1 -> new Receta(
            1L,
            "Tacos al Pastor",
            "Mexicana",
            """
            • 500 g. carne de cerdo
            • 1 piña
            • 12 tortillas de maíz
            • 1 cebolla blanca
            • 1 manojo de cilantro
            • Salsa verde
            """,
            "México",
            "Media",
            5,
            """
            1. Marinar la carne con achiote, ajo, vinagre y especias por 2 horas.
            2. Asar en sartén hasta dorar bien.
            3. Cortar en tiras finas.
            4. Calentar tortillas.
            5. Servir con piña asada, cebolla picada, cilantro y mucha salsa.
            """,
            "/img/tacos-al-pastor.jpg"
        );

        case 2 -> new Receta(
            2L,
            "Paella Valenciana",
            "Española",
            """
            • 400 g. arroz valenciano
            • 1 conejo
            • 1 pollo
            • 200 g. judías verdes
            • 100 g. garrofó
            • Azafrán, pimentón, romero
            • Caldo de pollo
            """,
            "España",
            "Alta",
            90,
            """
            1. Dorar conejo y pollo en paellera con aceite.
            2. Añadir judías y garrofó, sofreír.
            3. Incorporar tomate rallado y pimentón.
            4. Agregar arroz, tostar 2 min.
            5. Verter caldo caliente con azafrán.
            6. Cocinar 18 min a fuego medio-bajo.
            7. Dejar reposar 5 min antes de servir.
            """,
            "/img/paella-valenciana.jpg"
        );

        case 3 -> new Receta(
            3L,
            "Sushi California",
            "Japonesa",
            """
            • 200g arroz sushi
            • 100g cangrejo surimi
            • 1 aguacate
            • 1 pepino
            • Alga nori
            • Semillas de sésamo
            • Vinagre de arroz
            """,
            "EE.UU.",
            "Media",
            40,
            """
            1. Cocer arroz y aliñar con vinagre.
            2. Colocar alga nori en esterilla.
            3. Extender arroz dejando 2cm libres.
            4. Añadir tiras de cangrejo, aguacate y pepino.
            5. Enrollar con presión.
            6. Cortar en 8 piezas.
            7. Espolvorear sésamo.
            """,
            "/img/sushi-california.jpg"
        );

        case 4 -> new Receta(
            4L,
            "Pizza Margherita",
            "Italiana",
            """
            • 300g harina 00
            • 200ml agua tibia
            • 7g levadura seca
            • 1 lata tomate triturado
            • 150g mozzarella fresca
            • Albahaca fresca
            • Aceite de oliva
            """,
            "Italia",
            "Baja",
            30,
            """
            1. Disolver levadura en agua, mezclar con harina y sal.
            2. Amasar 10 min, dejar leudar 1 hora.
            3. Extender masa fina.
            4. Cubrir con tomate, mozzarella y aceite.
            5. Hornear 12 min a 220°C.
            6. Añadir albahaca fresca al salir.
            """,
            "/img/pizza-margarita.jpg"
        );

        case 5 -> new Receta(
            5L,
            "Hamburguesa Clásica",
            "Americana",
            """
            • 400g carne molida (80/20)
            • 4 panes de hamburguesa
            • 4 lonchas queso cheddar
            • Lechuga, tomate, cebolla
            • Pepinillos
            • Kétchup, mostaza, mayonesa
            """,
            "EE.UU.",
            "Baja",
            20,
            """
            1. Formar 4 hamburguesas de 100g.
            2. Salpimentar y cocinar 4 min por lado.
            3. Tostar panes.
            4. Derretir queso sobre la carne.
            5. Armar: pan, lechuga, hamburguesa, tomate, cebolla, pepinillos.
            6. Añadir salsas al gusto.
            """,
            "/img/hamburguesa-clasica.jpg"
        );

        case 6 -> new Receta(
            6L,
            "Ceviche Mixto",
            "Peruana",
            """
            • 300g pescado blanco
            • 100g camarones
            • Jugo de 6 limas
            • 1 ají limo
            • 1 cebolla morada
            • Cilantro
            • Cancha, camote
            """,
            "Perú",
            "Baja",
            15,
            """
            1. Cortar pescado en cubos de 1cm.
            2. Cocer camarones 1 min, enfriar.
            3. Mezclar con jugo de lima y sal.
            4. Añadir cebolla en juliana y ají picado.
            5. Dejar marinar 10 min.
            6. Servir con cancha y camote.
            """,
            "/img/ceviche-mixto.jpg"
        );

        case 7 -> new Receta(
            7L,
            "Ramen Tonkotsu",
            "Japonesa",
            """
            • 1kg huesos de cerdo
            • 200g panceta chashu
            • 4 huevos marinado
            • Fideos ramen
            • Cebollín, alga nori
            • Ajo, jengibre
            """,
            "Japón",
            "Media",
            120,
            """
            1. Hervir huesos 12 horas para caldo blanco.
            2. Marinar panceta en soja, sake y mirin.
            3. Cocer huevos 6 min, marinar 4 horas.
            4. Hervir fideos 2 min.
            5. Servir: caldo, fideos, chashu, huevo, cebollín, nori.
            """,
            "/img/ramen-tonkotsu.jpg"
        );

            default -> null;
        };
    }
}