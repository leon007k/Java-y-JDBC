package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Categoria;
import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {

    final private Connection con;

    public ProductoDAO(Connection con) {
        this.con = con;
    }

    public void guardar(Producto producto) {
        try (con) {
            // * desactivamos control de transaccion
            //con.setAutoCommit(false);
		    /*Statement statement = con.createStatement();

		    // * Query que hara el insert y nos devolvera el ID generado
		    statement.execute("INSERT INTO PRODUCTO(nombre,descripcion,cantidad)" +
				"VALUES('"+ producto.get("NOMBRE") + "','" + producto.get("DESCRIPCION") + "'," +
				producto.get("CANTIDAD") + ")", Statement.RETURN_GENERATED_KEYS);*/

            // * Trabajamos con PreparedStatement
            final PreparedStatement pStatement = con.prepareStatement("INSERT INTO PRODUCTO(nombre,descripcion,cantidad)" +
                            " VALUES(?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            try (pStatement) {
                ejecutaReistro(pStatement, producto);
                // * mandamos a ejecutar nuestros querys
                //con.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void ejecutaReistro(PreparedStatement pStatement, Producto producto) throws SQLException {
        pStatement.setString(1, producto.getNombre());
        pStatement.setString(2, producto.getDescripcion());
        pStatement.setInt(3, producto.getCantidad());

        pStatement.execute();

        // * Listado de IDs generados
        final ResultSet resultSet = pStatement.getGeneratedKeys();

        try (resultSet) {
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format("Fue insertado el producto con ID %s", producto));
            }
        }
    }


    public List<Producto> listar() {
        List<Producto> resultado = new ArrayList<>();
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
                final ResultSet resultSet = statement.getResultSet();
                try(resultSet) {
                    while (resultSet.next()) {
                        Producto fila = new Producto(resultSet.getInt("ID"),
                                resultSet.getString("NOMBRE"),
                                resultSet.getString("DESCRIPCION"),
                                resultSet.getInt("CANTIDAD"));
                        resultado.add(fila);
                    }
                }
            }
            return resultado;
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    public int eliminar(Integer id) {
        try {
            final PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");

            try (statement) {
                statement.setInt(1, id);
                statement.execute();

                int updateCount = statement.getUpdateCount();

                return updateCount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) {
        try {
            final PreparedStatement statement = con.prepareStatement(
                    "UPDATE PRODUCTO SET "
                            + " NOMBRE = ?, "
                            + " DESCRIPCION = ?,"
                            + " CANTIDAD = ?"
                            + " WHERE ID = ?");

            try (statement) {
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setInt(3, cantidad);
                statement.setInt(4, id);
                statement.execute();

                int updateCount = statement.getUpdateCount();

                return updateCount;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Producto> listar(Categoria categoria) {
        List<Producto> resultado = new ArrayList<>();

        try {
            String sql = "SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD "
                    + " FROM PRODUCTO WHERE CATEGORIA_ID = ?";
            System.out.println(sql);

            final PreparedStatement statement = con.prepareStatement(
                    sql);

            try (statement) {
                statement.setInt(1, categoria.getId());
                statement.execute();

                final ResultSet resultSet = statement.getResultSet();

                try (resultSet) {
                    while (resultSet.next()) {
                        resultado.add(new Producto(
                                resultSet.getInt("ID"),
                                resultSet.getString("NOMBRE"),
                                resultSet.getString("DESCRIPCION"),
                                resultSet.getInt("CANTIDAD")));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultado;
    }

}
