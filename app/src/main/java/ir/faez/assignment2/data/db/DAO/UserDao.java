package ir.faez.assignment2.data.db.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.faez.assignment2.data.model.User;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Update
    int update(User user);

    @Query("DELETE FROM user WHERE id= :id")
    int delete(long id);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE userName= :username")
    User getUserByUsername(String username);
}
