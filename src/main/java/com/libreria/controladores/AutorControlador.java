package com.libreria.controladores;

import com.libreria.entidades.Autor;
import com.libreria.errores.ErrorServicio;
import com.libreria.servicios.AutorServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/autor")
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/form")
    public String autorForm() {
        return "autor-form.html";
    }

    @PostMapping("/form")
    public String autorForm(ModelMap modelo, @RequestParam String nombre) {

        try {
            autorServicio.guardarAutor(nombre);
            modelo.put("cargado", "El Autor '" + nombre + " ' se cargo correctamente!!");
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }
        return "autor-form.html";
    }

    @GetMapping("/list")
    public String listarAutores(ModelMap modelo) {

        List<Autor> autores = autorServicio.listarAutores();
        modelo.put("autores", autores);
        return "autor-lista.html";
    }

    @GetMapping("/baja/{id}")
    public String baja(RedirectAttributes attr, @PathVariable String id) {
        try {
            autorServicio.darBaja(id);
            attr.addFlashAttribute("exito", "El Autor se dio de baja exitosamente.");
        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            attr.addFlashAttribute("error", "El Autor no se pudo dar de baja .");
        }

        return "redirect:/autor/list";
    }
    
     @GetMapping("/eliminar/{id}")
    public String eliminar(RedirectAttributes attr, @PathVariable String id) {
        
            autorServicio.eliminar(id);
            attr.addFlashAttribute("exito", "El Autor se dio de baja exitosamente.");
            return "redirect:/autor/list";
    }
    @GetMapping("/estado/{id}")
    public String cambiarEstado(RedirectAttributes attr, @PathVariable String id){
        
        try{
            autorServicio.cambiarEstado(id);
            attr.addFlashAttribute("exito", "Se ha cambiado el estado del Autor");
        }catch(ErrorServicio ex){
             attr.addFlashAttribute("error", "No se ha cambiado el estado del Autor .");
        }
        return "redirect:/autor/list";
    }
    
    @GetMapping("/editar/{idAutor}")
    public String editar(RedirectAttributes attr, @RequestParam String nombre, @PathVariable String idAutor) {

        try {
            autorServicio.editar(idAutor, nombre);
            attr.addFlashAttribute("exito", "Se ha cambiado el nombre del Autor exitosamnete!!");
        } catch (Exception e) {
             attr.addFlashAttribute("error", "No se ha podido cambiar nombre del Autor");
             
        }

        return "redirect:/autor/list";
    }
}
