package com.libreria.controladores;

import com.libreria.entidades.Editorial;
import com.libreria.errores.ErrorServicio;
import com.libreria.servicios.EditorialServicio;
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
@RequestMapping("/editorial")
public class EditorialControlador {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/form")
    public String editorialForm() {
        return "editorial-form.html";
    }

    @PostMapping("/form")
    public String editorialForm(ModelMap modelo, @RequestParam String nombre) {

        try {
            editorialServicio.guardar(nombre);
            modelo.put("cargado", "La Editorial ' " + nombre + "' ha sido cargada");
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
        }
        return "editorial-form.html";
    }

    @GetMapping("/list")
    public String listar(ModelMap modelo) {
        List<Editorial> editoriales = editorialServicio.listar();
        modelo.put("editoriales", editoriales);
        return "editorial-lista.html";
    }

    @GetMapping("/baja/{id}")
    public String baja(RedirectAttributes attr, @PathVariable String id) {

        try {
            editorialServicio.darBaja(id);
            attr.addFlashAttribute("exito", "La editorial se dio de baja exitosamente.");
        } catch (ErrorServicio ex) {
            Logger.getLogger(EditorialControlador.class.getName()).log(Level.SEVERE, null, ex);
            attr.addFlashAttribute("error", "La editorial no se pudo dar de baja .");
        }
        return "redirect:/editorial/list";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id) {

            editorialServicio.eliminar(id);
            return "redirect:/editorial/list";
    }
    
    @GetMapping("/estado/{id}")
    public String cambiarEstado(RedirectAttributes attr, @PathVariable String id){
        
        try{
            editorialServicio.cambiarEstado(id);
            attr.addFlashAttribute("exito", "Cambio el estado de Editorial.");
       }catch(ErrorServicio ex){
           attr.addFlashAttribute("error", "No se ha cambiado el estado de la Editorial.");
       }
        return "redirect:/editorial/list";
    }

    @GetMapping("/editar/{idEditorial}")
    public String editar(RedirectAttributes attr, @RequestParam String nombre, @PathVariable String idEditorial) {

        try {
            editorialServicio.editar(idEditorial, nombre);
            attr.addFlashAttribute("exito", "Se cambió nombre de Editorial exitosamente!!.");
        } catch (ErrorServicio e) {
            attr.addFlashAttribute("error", "Error!! no se ha podido cambiar nombre de Editorial.");
        }
        return "redirect:/editorial/list";
    }
}
