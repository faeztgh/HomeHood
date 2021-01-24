package ir.faez.assignment2.data.db.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ir.faez.assignment2.data.model.Expense;

@Dao
public interface ExpenseDao {
    @Insert
    long insert(Expense expense);

    @Update
    int update(Expense expense);

    @Query("DELETE FROM expense where id= :id")
    int delete(String id);

    @Query("SELECT * FROM expense WHERE ownerId= :id")
    List<Expense> getAllExpenses(String id);
}
