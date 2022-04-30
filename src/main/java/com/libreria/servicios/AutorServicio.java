package com.libreria.servicios;

import com.libreria.entidades.Autor;
import com.libreria.entidades.Libro;
import com.libreria.errores.ErrorServicio;
import com.libreria.repositorios.AutorRepositorio;
import com.libreria.repositorios.LibroRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Autowired
    private LibroRepositorio libroRepositorio;

    @Transactional(rollbackFor = {Exception.class})
    public Autor guardarAutor(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Debe ingresar un Autor!!");
        }

        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setActivo(true);
        autor.setAlta(new Date());

        return autorRepositorio.save(autor);
    }

    @Transactional(rollbackFor = {Exception.class})
    public void editar(String id, String nombre) throws ErrorServicio {
       
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("Debe ingresar nombre");
        }
        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
            autor.setActivo(true);
            autor.setAlta(new Date());

            autorRepositorio.save(autor);
        } else {
            throw new ErrorServicio("No se encontro Autor solicitado");
        }
    }

    
    @Transactional(rollbackFor = {Exception.class})
    public void darBaja(String id) throws ErrorServicio {
    
        consultarPorId(id);
        autorRepositorio.deleteById(id);
    }
    
    @Transactional(rollbackFor = {Exception.class})
    public void eliminar(String id){
     //Traer lista de libros asociados a los autores   
     List<Libro> libros = libroRepositorio.buscarLibroPorIdAutor(id);
     //Por cada libro setear el autor a null
     libros.forEach(libro -> libro.setAutor(null));
     //Actualizra los libros
     libroRepositorio.saveAllAndFlush(libros);
     //Borrar el Autor
     autorRepositorio.deleteById(id);
    }
    
    @Transactional(rollbackFor = {Exception.class})
    public Autor cambiarEstado(String id) throws ErrorServicio{
        
       Autor autor =  consultarPorId(id);
        if (autor.isActivo()) {
            autor.setActivo(false);
            return autor;
        }else{
            autor.setActivo(true);
            return autor;
        }
    }

    @Transactional(readOnly = true)
    public Autor consultarPorId(String id) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
              return autor;
        } else {
            throw new ErrorServicio("No se encontro el Autor");
        }
    }

    @Transactional(readOnly = true)
    public Autor consultarPorNombre(String nombre) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepositorio.findByNombre(nombre);
        if (respuesta.isPresent()) {
            return respuesta.get();
        } else {
            throw new ErrorServicio("No se encontro Autor");
        }
    }

    @Transactional(readOnly = true)
    public List<Autor> listarAutores() {
        return autorRepositorio.findAll();
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
