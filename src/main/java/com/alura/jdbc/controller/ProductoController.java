package com.alura.jdbc.controller;


import com.alura.jdbc.dao.ProductoDAO;
import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	private ProductoDAO productoDAO;
	public ProductoController(){
		this.productoDAO = new ProductoDAO(new ConnectionFactory().recuperarConexion());
	}

    public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) {
		return productoDAO.modificar(nombre, descripcion, cantidad, id);
    }

    public int eliminar(Integer id) {
		return productoDAO.eliminar(id);
    }

    public List<Producto> listar() {
		return productoDAO.listar();
    }

    public List<Producto> listarPorCategoria(Categoria categoria){
        return productoDAO.listarPorCategoria(categoria.getId());
    }

    public void guardar(Producto producto, Integer categoriaId) {
        producto.setCategoriaId(categoriaId);
		productoDAO.guardar(producto);
    }
}
