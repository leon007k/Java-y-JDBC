package com.alura.jdbc.controller;


import com.alura.jdbc.dao.ProductoDAO;
import com.alura.jdbc.factory.ConnectionFactory;
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

    public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
        final Connection con = factory.recuperarConexion();
		/*Statement statement = con.createStatement();
		statement.execute("UPDATE PRODUCTOS SET NOMBRE = '" + nombre + "'," +
				"DESCRIPCION = '" + descripcion + "', CANTIDAD = " + cantidad +
				"WHERE ID = " + id);*/
		// * Se hace uso de try - with - resources version java 9, para evitar olvidar cerrar conexiones
		try(con) {
			final PreparedStatement statement = con.prepareStatement("UPDATE PRODUCTOS SET NOMBRE = ?" +
					"DESCRIPCION = ?, CANTIDAD =  ? " +
					"WHERE ID = ?");
			try(statement) {
				statement.setString(1, nombre);
				statement.setString(2, descripcion);
				statement.setInt(3, cantidad);
				statement.setInt(4, id);

				statement.execute();

				int cantidadActualizados = statement.getUpdateCount();
				return cantidadActualizados;
			}
		}
    }

    public int eliminar(Integer id) throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
        final Connection con = factory.recuperarConexion();
		/*Statement statement = con.createStatement();
		statement.execute("DELETE FROM PRODUCTOS WHERE ID = " + id);*/

		try(con) {
			final PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTOS WHERE ID = ?");
			try(statement){
				statement.setInt(1, id);
				statement.execute();
				int cantidadEliminados = statement.getUpdateCount();
				// * Devuelve cuantas filas fueron modificadas
				return cantidadEliminados;
			}
		}
    }

    public List<Producto> listar() {
		return productoDAO.listar();
    }

    public void guardar(Producto producto) {
		productoDAO.guardar(producto);
    }
}
