package ir.faez.assignment2.data.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.db.DAO.ExpenseDao;
import ir.faez.assignment2.data.db.DbManager;
import ir.faez.assignment2.data.model.Expense;

public class GetExpensesAsyncTask extends AsyncTask<Long, Void, List<Expense>> {
    private Context context;
    private ExpenseDao expenseDao;
    private DbResponse<List<Expense>> dbResponse;

    public GetExpensesAsyncTask(Context context, DbResponse<List<Expense>> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
    }

    @Override
    protected List<Expense> doInBackground(Long... id) {
        return expenseDao.getAllExpenses(id[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance((context));
        expenseDao = dbManager.expenseDao();

    }


    @Override
    protected void onPostExecute(List<Expense> expenses) {
        super.onPostExecute(expenses);
        if (expenses != null) {
            dbResponse.onSuccess(expenses);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrong));
            dbResponse.onError(error);
        }
    }
}
