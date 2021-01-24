package ir.faez.assignment2.data.async;

import android.content.Context;
import android.os.AsyncTask;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.db.DAO.ExpenseDao;
import ir.faez.assignment2.data.db.DbManager;
import ir.faez.assignment2.data.model.Expense;
import ir.faez.assignment2.utils.Action;

public class ExpenseCudAsyncTask extends AsyncTask<Expense, Void, Long> {
    private Context context;
    private ExpenseDao expenseDao;
    private Expense expense;
    private DbResponse<Expense> dbResponse;
    private String action;

    public ExpenseCudAsyncTask(Context context, String action, DbResponse<Expense> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
        this.action = action;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance((context));
        expenseDao = dbManager.expenseDao();

    }

    @Override
    protected Long doInBackground(Expense... expenses) {
        expense = expenses[0];

        switch (action) {
            case Action.INSERT_ACTION:

                return insertDoInBackground(expenses);

            case Action.UPDATE_ACTION:

                return updateDoInBackground(expenses);

            case Action.DELETE_ACTION:

                return deleteDoInBackground(expense);
        }
        return null;
    }

    private Long deleteDoInBackground(Expense expenses) {
        return (long) expenseDao.delete(expense.getId());
    }

    private Long updateDoInBackground(Expense[] expenses) {
        return (long) expenseDao.update(expense);
    }

    private Long insertDoInBackground(Expense[] expenses) {
        return expenseDao.insert(expense);
    }

    @Override
    protected void onPostExecute(Long response) {

        switch (action) {
            case Action.INSERT_ACTION:
                insertPostExecute(response);
                break;
            case Action.UPDATE_ACTION:
                updatePostExecute(response);
                break;
            case Action.DELETE_ACTION:
                deletePostExecute(response);
                break;
        }
    }

    private void deletePostExecute(Long response) {
        if (response > 0) {
            dbResponse.onSuccess(expense);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnDelete));
            dbResponse.onError(error);
        }

    }

    private void updatePostExecute(Long affectedRows) {
        if (affectedRows > 0) {
            dbResponse.onSuccess(expense);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnUpdate));
            dbResponse.onError(error);
        }
    }

    private void insertPostExecute(Long expenseId) {

        if (expenseId > 0) {
            expense.setId(expenseId.toString());
            dbResponse.onSuccess(expense);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrongOnInsert));
            dbResponse.onError(error);
        }
    }
}
