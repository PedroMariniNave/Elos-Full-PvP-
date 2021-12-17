package com.zpedroo.voltzelos.mysql;

import com.zpedroo.voltzelos.managers.EloManager;
import com.zpedroo.voltzelos.objects.PlayerData;
import com.zpedroo.voltzelos.objects.Elo;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBManager {

    public void saveData(PlayerData data) {
        if (contains(data.getUUID().toString(), "uuid")) {
            String query = "UPDATE `" + DBConnection.TABLE + "` SET" +
                    "`uuid`='" + data.getUUID().toString() + "', " +
                    "`elo`='" + data.getElo().getId() + "', " +
                    "`kills`='" + data.getKills() + "', " +
                    "`deaths`='" + data.getDeaths() + "', " +
                    "`points`='" + data.getPoints() + "' " +
                    "WHERE `uuid`='" + data.getUUID().toString() + "';";
            executeUpdate(query);
            return;
        }

        String query = "INSERT INTO `" + DBConnection.TABLE + "` (`uuid`, `elo`, `kills`, `deaths`, `points`) VALUES " +
                "('" + data.getUUID().toString() + "', " +
                "'" + data.getElo().getId() + "', " +
                "'" + data.getKills() + "', " +
                "'" + data.getDeaths() + "', " +
                "'" + data.getPoints() + "');";
        executeUpdate(query);
    }

    public PlayerData loadData(Player player) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT * FROM `" + DBConnection.TABLE + "` WHERE `uuid`='" + player.getUniqueId().toString() + "';";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();

            if (result.next()) {
                UUID uuid = UUID.fromString(result.getString(1));
                Elo elo = EloManager.getInstance().getElo(result.getInt(2));
                int kills = result.getInt(3);
                int deaths = result.getInt(4);
                long points = result.getLong(5);

                return new PlayerData(uuid, elo == null ? EloManager.getInstance().getDefaultElo() : elo, kills, deaths, points);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, result, preparedStatement, null);
        }

        return new PlayerData(player.getUniqueId(), EloManager.getInstance().getDefaultElo(), 0, 0, 0);
    }

    public List<PlayerData> getTopPoints() {
        List<PlayerData> top = new ArrayList<>(10);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT * FROM `" + DBConnection.TABLE + "` ORDER BY `points` DESC LIMIT 10;";

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();

            while (result.next()) {
                UUID uuid = UUID.fromString(result.getString(1));
                Elo elo = EloManager.getInstance().getElo(result.getInt(2));
                int kills = result.getInt(3);
                int deaths = result.getInt(4);
                long points = result.getLong(5);

                top.add(new PlayerData(uuid, elo == null ? EloManager.getInstance().getDefaultElo() : elo, kills, deaths, points));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, result, preparedStatement, null);
        }

        return top;
    }

    private Boolean contains(String value, String column) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet result = null;
        String query = "SELECT `" + column + "` FROM `" + DBConnection.TABLE + "` WHERE `" + column + "`='" + value + "';";
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            result = preparedStatement.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, result, preparedStatement, null);
        }

        return false;
    }

    private void executeUpdate(String query) {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(connection, null, null, statement);
        }
    }

    private void closeConnection(Connection connection, ResultSet resultSet, PreparedStatement preparedStatement, Statement statement) {
        try {
            if (connection != null) connection.close();
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
            if (statement != null) statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    protected void createTable() {
        String query = "CREATE TABLE IF NOT EXISTS `" + DBConnection.TABLE + "` (`uuid` VARCHAR(255), `elo` INTEGER, `kills` INTEGER, `deaths` INTEGER, `points` LONG, PRIMARY KEY(`uuid`));";
        executeUpdate(query);
    }

    private Connection getConnection() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }
}