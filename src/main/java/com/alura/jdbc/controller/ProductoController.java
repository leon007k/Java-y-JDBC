package com.alura.jdbc.controller;

import com.alura.jdbc.CreaConexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public void modificar(String nombre, String descripcion, Integer id) {
		// TODO
	}

	public void eliminar(Integer id) {
		// TODO
	}

	public List<Map<String, String>> listar() throws SQLException {
		// * Abrimos conexion
		Connection con = new CreaConexion().recuperarConexion();

		// * con este statement, podremos crear nuestro query
		Statement statement = con.createStatement();
		statement.execute("SELECT ID,NOMBRE,DESCRIPCION,CANTIDAD FROM PRODUCTOS");

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

    public void guardar(Object producto) {
		// TODO
	}

}
