package com.alura.jdbc.controller;


import com.alura.jdbc.factory.ConnectionFactory;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();
		/*Statement statement = con.createStatement();
		statement.execute("UPDATE PRODUCTOS SET NOMBRE = '" + nombre + "'," +
				"DESCRIPCION = '" + descripcion + "', CANTIDAD = " + cantidad +
				"WHERE ID = " + id);*/

		PreparedStatement statement = con.prepareStatement("UPDATE PRODUCTOS SET NOMBRE = ?" +
				"DESCRIPCION = ?, CANTIDAD =  ? " +
				"WHERE ID = ?");
		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);
		statement.setInt(4, id);

		statement.execute();

		int cantidadActualizados = statement.getUpdateCount();
		con.close();
		return cantidadActualizados;
	}

	public int eliminar(Integer id) throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();
		/*Statement statement = con.createStatement();
		statement.execute("DELETE FROM PRODUCTOS WHERE ID = " + id);*/

		PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTOS WHERE ID = ?");
		statement.setInt(1, id);
		statement.execute();

		int cantidadEliminados = statement.getUpdateCount();
		con.close();
		// * Devuelve cuantas filas fueron modificadas
		return cantidadEliminados;
	}

	public List<Map<String, String>> listar() throws SQLException {
		// * Abrimos conexion
		Connection con = new ConnectionFactory().recuperarConexion();

		// * con este statement, podremos crear nuestro query
		/*Statement statement = con.createStatement();
		statement.execute("SELECT ID,NOMBRE,DESCRIPCION,CANTIDAD FROM PRODUCTOS");*/

		// * Trabajar con PreparedStatement
		PreparedStatement statement = con.prepareStatement("SELECT ID,NOMBRE,DESCRIPCION,CANTIDAD FROM PRODUCTOS");
		statement.execute();

		// * Listado de resultados
		ResultSet resultSet = statement.getResultSet();
		List<Map<String, String>> resultado = new ArrayList<>();
		while(resultSet.next()){
			Map<String, String> fila = new HashMap<>();
			fila.put("ID", String.valueOf(resultSet.getInt("ID")));
			fila.put("NOMBRE", String.valueOf(resultSet.getString("NOMBRE")));
			fila.put("DESCRIPCION", String.valueOf(resultSet.getString("DESCRIPCION")));
			fila.put("CANTIDAD", String.valueOf(resultSet.getInt("CANTIDAD")));
			resultado.add(fila);
		}

		con.close();
		return resultado;
	}

    public void guardar(Map<String, String> producto) throws SQLException {
		Connection con = new ConnectionFactory().recuperarConexion();
		/*Statement statement = con.createStatement();

		// * Query que hara el insert y nos devolvera el ID generado
		statement.execute("INSERT INTO PRODUCTO(nombre,descripcion,cantidad)" +
				"VALUES('"+ producto.get("NOMBRE") + "','" + producto.get("DESCRIPCION") + "'," +
				producto.get("CANTIDAD") + ")", Statement.RETURN_GENERATED_KEYS);*/

		// * Trabajamos con PreparedStatement
		PreparedStatement pStatement = con.prepareStatement("INSERT INTO PRODUCTO(nombre,descripcion,cantidad) VALUES(?,?,?)",
				Statement.RETURN_GENERATED_KEYS);
		pStatement.setString(1,producto.get("NOMBRE"));
		pStatement.setString(2,producto.get("DESCRIPCION"));
		pStatement.setInt(3,Integer.parseInt(producto.get("CANTIDAD")));

		pStatement.execute();

		// * Listado de IDs generados
		ResultSet resultSet = pStatement.getGeneratedKeys();

		while (resultSet.next()){
			System.out.println(String.format("Fue insertado el producto con ID %d",resultSet.getInt(1)));
		}
		con.close();
	}
}
