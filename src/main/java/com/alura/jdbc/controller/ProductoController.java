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

    public List<Map<String, String>> listar() throws SQLException {
        // * Abrimos conexion
		ConnectionFactory factory = new ConnectionFactory();
        final Connection con = factory.recuperarConexion();

        // * con este statement, podremos crear nuestro query
		/*Statement statement = con.createStatement();
		statement.execute("SELECT ID,NOMBRE,DESCRIPCION,CANTIDAD FROM PRODUCTOS");*/
		try(con) {
			// * Trabajar con PreparedStatement
			final PreparedStatement statement = con.prepareStatement("SELECT ID,NOMBRE,DESCRIPCION,CANTIDAD FROM PRODUCTOS");
			statement.execute();

			try (statement) {
				// * Listado de resultados
				ResultSet resultSet = statement.getResultSet();
				List<Map<String, String>> resultado = new ArrayList<>();
				while (resultSet.next()) {
					Map<String, String> fila = new HashMap<>();
					fila.put("ID", String.valueOf(resultSet.getInt("ID")));
					fila.put("NOMBRE", String.valueOf(resultSet.getString("NOMBRE")));
					fila.put("DESCRIPCION", String.valueOf(resultSet.getString("DESCRIPCION")));
					fila.put("CANTIDAD", String.valueOf(resultSet.getInt("CANTIDAD")));
					resultado.add(fila);
				}
				return resultado;
			}
		}
    }

    public void guardar(Map<String, String> producto) throws SQLException {
        String nombre = producto.get("NOMBRE");
        String descripcion = producto.get("DESCRIPCION");
        Integer cantidad = Integer.parseInt(producto.get("CANTIDAD"));
        Integer maxCantidad = 50;


        ConnectionFactory factory = new ConnectionFactory();
        final Connection con = factory.recuperarConexion();
        // * desactivamos control de transaccion
        try (con) {
            con.setAutoCommit(false);
		/*Statement statement = con.createStatement();

		// * Query que hara el insert y nos devolvera el ID generado
		statement.execute("INSERT INTO PRODUCTO(nombre,descripcion,cantidad)" +
				"VALUES('"+ producto.get("NOMBRE") + "','" + producto.get("DESCRIPCION") + "'," +
				producto.get("CANTIDAD") + ")", Statement.RETURN_GENERATED_KEYS);*/

            // * Trabajamos con PreparedStatement
            final PreparedStatement pStatement = con.prepareStatement("INSERT INTO PRODUCTO(nombre,descripcion,cantidad) VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            try (pStatement) {
                do {
                    // * Se limita la cantidad a guardar en 50 por articulo
                    int cantidadParaGuardar = Math.min(cantidad, maxCantidad);
                    ejecutaReistro(pStatement, nombre, descripcion, cantidadParaGuardar);
                    // * Si teniamos 100, se guardan primero 50, despues se vuelve a ejecutar y se guarda el restante
                    cantidad -= maxCantidad;
                } while (cantidad > 0);

                // * mandamos a ejecutar nuestros querys
                con.commit();
            }catch (Exception e){
				con.rollback();
			}
        }
    }

    private static void ejecutaReistro(PreparedStatement pStatement, String nombre, String descripcion, Integer cantidad) throws SQLException {
        pStatement.setString(1, nombre);
        pStatement.setString(2, descripcion);
        pStatement.setInt(3, cantidad);

        pStatement.execute();

        // * Listado de IDs generados
        final ResultSet resultSet = pStatement.getGeneratedKeys();

        try (resultSet) {
            while (resultSet.next()) {
                System.out.println(String.format("Fue insertado el producto con ID %d", resultSet.getInt(1)));
            }
        }
    }
}
