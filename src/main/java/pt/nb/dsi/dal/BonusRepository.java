package pt.nb.dsi.dal;

// A  Repository implemented in plain JDBC  , no ORM

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import io.quarkus.logging.Log;

// use annnotation like @ApplicationScoped, @RequestScoped, or @Singleton.

@ApplicationScoped // Important: Make it a CDI bean
public class BonusRepository implements IBonusRepository {

    @Inject
    DataSource dataSource;

    @Override
    public int count() {
        String query = "SELECT COUNT(*) FROM Bonus";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Bonus> findAll() {
        List<Bonus> bonuses = new ArrayList<>();
        String query = "SELECT * FROM Bonus";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                bonuses.add(mapRowToBonus(resultSet));
            }
        } catch (SQLException e) {
            Log.error(e);
        }


        Log.infof("Number of bonuses retrieved: %d", bonuses.size());
        return bonuses;
    }

    @Override
    public Bonus findOne(String ename) {
        String query = "SELECT * FROM Bonus WHERE ename = ?";
        Log.warnf("Get bonus for %s",ename);
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ename);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToBonus(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Bonus save(Bonus bn) {
        String query = "INSERT INTO Bonus (ename,job,sal,comm) VALUES (?,?,?,?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, bn.getEname());
            statement.setString(2, bn.getJob());
            statement.setInt(3, bn.getSal());
            statement.setInt(4, bn.getComm());
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return bn;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Bonus b) {
        String query = "UPDATE Bonus SET job = ?, sal = ? , comm = ?  WHERE ename = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(4, b.getEname());
                statement.setString(1, b.getJob());
                statement.setInt(2, b.getSal());
                statement.setInt(3, b.getComm());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String ename) {
        String query = "DELETE FROM Bonus WHERE ename = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ename);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Bonus> findRange(int from, int to) {
        List<Bonus> bonuses = new ArrayList<>();
        String query = "SELECT * FROM Bonus LIMIT ? OFFSET ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, to - from + 1);
            statement.setInt(2, from);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    bonuses.add(mapRowToBonus(resultSet));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bonuses;
    }

    private Bonus mapRowToBonus(ResultSet resultSet) throws SQLException {
        Bonus bonus = new Bonus();
        bonus.setEname(resultSet.getString("ename"));
        bonus.setJob(resultSet.getString("job"));
        bonus.setSal(resultSet.getInt("sal"));
        bonus.setComm(resultSet.getInt("comm"));
        return bonus;
    }
}
