package consoles;

import java.sql.*;

public class Main {
    public void getOrders() throws SQLException {
        DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
        try (Connection connection = DriverManager.getConnection("jdbc:derby://127.0.0.1:1527/db0")) {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "select NAME, COLOR " +
                            "from PRODUCTS pr " +
                            "join POSITIONS pp on pr.ARTICLE = pr.ARTICLE " +
                            "where pp.ORDER_ID = ?"
            );

            preparedStatement.setInt(1, 1);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("Содержимое заказа:\n");
                while (resultSet.next()) {
                    System.out.println("NAME - " + resultSet.getString("NAME"));
                    System.out.println("COLOR - " + resultSet.getString("COLOR"));
                    System.out.println();
                }
                System.out.println("---");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void registerOrder(String fio, String phone, String email, String deliveryAddr,
                              String[] articles, int[] amount) throws SQLException {
        if (articles == null) {
            throw new IllegalArgumentException("Articles is null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount is null");
        }
        if (articles.length != amount.length) {
            throw new IllegalArgumentException("Articles and amount must have the same size");
        }

        try (Connection connection = DriverManager.getConnection("jdbc:derby://127.0.0.1:1527/db0")) {
            int maxId;
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                statement.execute("select max(ID) from ORDERS");
                ResultSet resultSet = statement.getResultSet();
                resultSet.next();
                maxId = resultSet.getInt(1);
            }

            try {
                try (PreparedStatement pstmt = connection.prepareStatement(
                        "insert into orders (ID, REG_DATE, CUSTOMER_FIO, PHONE, EMAIL, DELIVERY_ADDRESS, STATUS) " +
                                "values (?, CURRENT_DATE, ?, ?, ?, ?, 'P')")) {
                    pstmt.setInt(1, maxId + 1);
                    pstmt.setString(2, fio);
                    pstmt.setString(3, phone);
                    pstmt.setString(4, email);
                    pstmt.setString(5, deliveryAddr);
                    pstmt.executeUpdate();
                }

                try (PreparedStatement pstmt = connection.prepareStatement(
                        "insert into POSITIONS (ORDER_ID, ARTICLE, PRICE, QUANTITY)\n" +
                                "    select ?, ARTICLE, PRICE, ?\n" +
                                "    from PRODUCTS\n" +
                                "    where ARTICLE = ?")) {
                    pstmt.setInt(1, maxId + 1);
                    for (int i = 0; i < articles.length; i++) {
                        pstmt.setInt(2, amount[i]);
                        pstmt.setString(3, articles[i]);
                        pstmt.addBatch();
                    }
                    pstmt.executeBatch();
                    connection.commit();
                }
            }catch (SQLException e){
                connection.rollback();
                throw e;
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        registerOrder("FIO","+7 962 981-00-13", "asvikulin@mail.ru","Address",
                new String[]{"3251615","3251616"},new int[]{3,-1});
    }
}
