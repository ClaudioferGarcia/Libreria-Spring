package com.libreria.servicios;

import com.libreria.entidades.Editorial;
import com.libreria.entidades.Libro;
import com.libreria.errores.ErrorServicio;
import com.libreria.repositorios.EditorialRepositorio;
import com.libreria.repositorios.LibroRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Autowired
    private LibroRepositorio libroRepositorio;

    @Transactional(rollbackFor = {Exception.class})
    public Editorial guardar(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Ingrese nombre Editorial");
        }

        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setActivo(true);
        editorial.setAlta(new Date());

         return editorialRepositorio.save(editorial);

    }

    @Transactional(rollbackFor = {Exception.class})
    public void editar(String id, String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Debe ingresar nombre de Editorial");
        }

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
            editorial.setNombre(nombre);
            editorial.setActivo(true);
            editorial.setAlta(new Date());

            editorialRepositorio.save(editorial);

        } else {
            throw new ErrorServicio("La Editorial no existe");
        }

    }

    @Transactional(rollbackFor = {Exception.class})
    public void darBaja(String id) throws ErrorServicio {

        buscarPorId(id);
        
        editorialRepositorio.deleteById(id);
    }
    
    @Transactional(rollbackFor = {Exception.class})
    public void eliminar(String id){
        
     //Traer lista de libros asociados a los autores   
     List<Libro> libros = libroRepositorio.buscarLibroPorIdEditorial(id);
     //Por cada libro setear el autor a null
     libros.forEach(libro -> libro.setEditorial(null));
     //Actualizra los libros
     libroRepositorio.saveAllAndFlush(libros);
     //Borrar el Autor
     editorialRepositorio.deleteById(id);
    }
    
    @Transactional(rollbackFor = {Exception.class})
    public Editorial cambiarEstado(String id) throws ErrorServicio{
        
        Editorial editorial = buscarPorId(id);
        if (editorial.isActivo()){
            editorial.setActivo(false);
            return editorial;
        }else{
          editorial.setActivo(true);   
        }
        
        return editorial;
    }
    
    @Transactional(readOnly = true)
    public Editorial buscarPorId(String id) throws ErrorServicio{
        
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        
        if (respuesta.isPresent()){
        Editorial editorial = respuesta.get();
          return  editorial;  
          
        }else{
            throw new ErrorServicio("No se ha encontrado Editorial");     
        }
    }
    
    @Transactional(readOnly = true)
    public Editorial buscarPorNombre(String nombre) throws ErrorServicio{
        
        Optional<Editorial> respuesta = editorialRepositorio.findByNombre(nombre);
        if (respuesta.isPresent()) {
            return respuesta.get();
        }else{
            throw new ErrorServicio("No se encontro Editorial");
        }
    }
    
    public List<Editorial> listar(){
        return editorialRepositorio.findAll();
    }

    private void validar(String nombre, boolean activo) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }

        if (activo == false) {

            throw new ErrorServicio("Debe estar dado de alta");

        }
    }
}
