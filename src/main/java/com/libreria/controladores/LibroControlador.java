
package com.libreria.controladores;


import com.libreria.entidades.Autor;
import com.libreria.entidades.Editorial;
import com.libreria.entidades.Libro;
import com.libreria.errores.ErrorServicio;
import com.libreria.servicios.AutorServicio;
import com.libreria.servicios.EditorialServicio;
import com.libreria.servicios.LibroServicio;
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
@RequestMapping("/libro")
public class LibroControlador {
  
  @Autowired
  private LibroServicio libroServicio;  
  @Autowired  
  private AutorServicio autorServicio;
  @Autowired
  private EditorialServicio editorialServicio;
    
    @GetMapping("/form")
    public String libroForm(ModelMap modelo){
        List<Autor> autores = autorServicio.listarAutores();
        modelo.put("autores", autores);
        List<Editorial> editoriales = editorialServicio.listar();
        modelo.put("editoriales", editoriales);        
        return "libro-form.html";
    }
    
   @PostMapping("/form")
    public String libroForm(RedirectAttributes attr, @RequestParam long isbn, @RequestParam String titulo, 
    @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, 
    @RequestParam Integer ejemplaresRestantes, @RequestParam String idAutor, @RequestParam String idEditorial){
        
        try{
         libroServicio.guardarLibro(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, idAutor, idEditorial);
         attr.addFlashAttribute("exito", "El Libro '" + titulo + "' se cargo correctamente!!");  
        }catch (ErrorServicio ex){
         attr.addFlashAttribute("error", ex.getMessage());    
        }

        return "redirect:/libro/form";
    }
    
    @GetMapping("/list")
    public String listar(ModelMap modelo){
        
        List<Libro> libros = libroServicio.listar();
        modelo.put("libros", libros);
        return "libro-lista.html";
    } 
    
    @GetMapping("/index/{id}")
    public String buscarPorTitulo(RedirectAttributes attr, @RequestParam String titulo, @PathVariable String id){
    
        try{
            libroServicio.buscarPorTitulo(id, titulo);
            
        }catch(ErrorServicio e){
            attr.addFlashAttribute("error", "El Libro " + "no se ha encontrado");
        }
        return "";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminar(RedirectAttributes attr, @PathVariable String id){
        
        try{
            libroServicio.eliminar(id);
            attr.addFlashAttribute("exito", "El Libro se ha eliminado exitosamente.");
        }catch(ErrorServicio ex){
             Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            attr.addFlashAttribute("error", "El Libro no se ha eliminado.");
        }
        return"redirect:/libro/list";
    }
    
    @GetMapping("/editar/{id}")
    public String editarTitulo(RedirectAttributes attr, @RequestParam String titulo, @PathVariable String id){
        
        try{
            libroServicio.editarTitulo(id, titulo);
            attr.addFlashAttribute("exito", "Se cambió exitosamente título de Libro.");
        }catch(ErrorServicio e){
           attr.addFlashAttribute("error", "No se ha cambiado título de Libro."); 
        }
        return"redirect:/libro/list";
    }
     
      @GetMapping("/editarejemplares/{id}")
    public String editarEjemplares(RedirectAttributes attr, @RequestParam Integer ejemplares, 
            @RequestParam Integer ejemplaresPrestados, @RequestParam Integer ejemplaresRestantes, @PathVariable String id){
        
        try{
            libroServicio.editarEjemplares(id, ejemplares, ejemplaresPrestados, ejemplaresRestantes);
            attr.addFlashAttribute("exito", "Se cambió exitosamente cantidad de Libros.");
        }catch(ErrorServicio ex){
          attr.addFlashAttribute("error", ex.getMessage()); 
        }
        return"redirect:/libro/list";
    }
}
