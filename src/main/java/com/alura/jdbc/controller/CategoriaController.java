package com.alura.jdbc.controller;

import java.sql.SQLException;
import java.util.List;

import com.alura.jdbc.dao.CategoriaDAO;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;

public class CategoriaController {

    private CategoriaDAO categoriaDAO;

    public CategoriaController() throws SQLException {
        var factory = new ConnectionFactory();
        this.categoriaDAO = new CategoriaDAO(factory.recuperarConexion());
    }

    public List<Categoria> listar() {
        return categoriaDAO.listar();
    }

    public List<Categoria> cargaReporte() {
        return this.categoriaDAO.listarConProductos();
    }

}