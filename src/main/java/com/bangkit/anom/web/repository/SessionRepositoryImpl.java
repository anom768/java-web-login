package com.bangkit.anom.web.repository;

import com.bangkit.anom.web.entity.Session;
import com.bangkit.anom.web.util.DatabaseUtil;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionRepositoryImpl implements SessionRepository {

    private HikariDataSource dataSource;

    public SessionRepositoryImpl(HikariDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Session insert(Session session) {
        String sql = "INSERT INTO session(id, id_user) VALUES(?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, session.getId());
                preparedStatement.setString(2, session.getId_user());
                preparedStatement.executeUpdate();
                return session;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public Session findById(String id) {
        String sql = "SELECT * FROM session WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Session session = new Session(
                                resultSet.getString("id"),
                                resultSet.getString("id_user")
                        );
                        return session;
                    }
                    return null;
                }
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(String id) {
        String sql = "DELETE FROM session WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, id);
                preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM session";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
