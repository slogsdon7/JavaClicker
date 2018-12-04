package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    static String db = "jdbc:sqlite:db.sqlite";
    static Connection conn;

    public static void init() {
        try {
            conn = DriverManager.getConnection(db);
            Statement s = conn.createStatement();
            s.executeUpdate("PRAGMA foreign_keys = ON; ");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static int handleStatement(String SQL) {
        try {
            System.out.println(SQL);
            PreparedStatement stmt = conn.prepareStatement(SQL);
            stmt.execute();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next())
                return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return 0;
    }

    private static ResultSet handleSelect(String SQL) {
        try {
            System.out.println(SQL);
            PreparedStatement stmt = conn.prepareStatement(SQL);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    static List<Game> getSaves() {
        ResultSet rs = handleSelect("Select * from save");
        List<Game> games = new ArrayList<>();
        try {
            while (rs.next()) {
                Game game = new Game();
                game.setId(rs.getInt("id"));
                game.getCurrency().addAmount(rs.getDouble("currency_amount"));
                game.setName(rs.getString("name"));
                load(game);
                games.add(game);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return games;
    }

    static void load(Game game) {
        String SQL = "SELECT * from save " +
                "LEFT JOIN save_has_producer " +
                "ON save_id = id " +
                "LEFT JOIN producer " +
                "ON producer.name = producer_name " +
                "WHERE save.id = " + game.getId();
        ResultSet rs = handleSelect(SQL);

        try {
            while (rs.next()) {
                Producer producer = new Producer(
                        rs.getDouble("base_production"),
                        rs.getDouble("base_cost"),
                        1.15,
                        1.1,
                        rs.getInt("level"),
                        rs.getBoolean("is_auto"),
                        rs.getString("name"),
                        rs.getInt("interval"));
                if (producer.isAutomatic())
                    game.getAutoProducers().add(producer);
                else
                    game.setManualProducer(producer);

            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            System.err.println(e.getSQLState());
        }
    }

    static void insert(Producer producer) {
        String SQL = String.format(
                "INSERT OR REPLACE INTO producer (base_cost, base_production, name, is_auto, interval)" +
                        "VALUES (%f, %f, \"%s\", %b, %d)",
                producer.getCostBase(),
                producer.getBaseProduction(),
                producer.getName(),
                producer.isAutomatic(),
                producer.getInterval()
        );
        handleStatement(SQL);
    }

    static void insert(Game game) {
        String SQL = String.format("INSERT INTO save (name, currency_amount, last_saved) values (\"%s\",%f,%d)",
                game.getName(),
                game.getCurrency().getAmount(),
                System.currentTimeMillis());
        int id = handleStatement(SQL);
        game.setId(id);
        game.getAutoProducers().forEach((producer) -> {
                    handleStatement(String.format("INSERT INTO save_has_producer (save_id, level, producer_name) values (%d,%d,\"%s\")",
                            id,
                            producer.getLevel(),
                            producer.getName()
                    ));
                }
        );


    }


    static void update(Game game) {
        String SQL = String.format(
                "UPDATE save " +
                        "SET currency_amount = %f, last_saved= %d" +
                        "WHERE id = %d",
                game.getCurrency().getAmount(),
                System.currentTimeMillis(),
                game.getId()
        );

        game.getAutoProducers().forEach((producer) -> {
                    handleStatement(String.format("UPDATE save_has_producer " +
                                    "SET level = %d " +
                                    "WHERE save_id = %d AND producer_name = %s",
                            producer.getLevel(),
                            game.getId(),
                            producer.getName()
                    ));
                }
        );

    }

}

