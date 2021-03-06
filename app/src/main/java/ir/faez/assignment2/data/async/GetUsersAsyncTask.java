package ir.faez.assignment2.data.async;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.db.DAO.UserDao;
import ir.faez.assignment2.data.db.DbManager;
import ir.faez.assignment2.data.model.User;

public class GetUsersAsyncTask extends AsyncTask<Void, Void, List<User>> {
    private Context context;
    private UserDao userDao;
    private DbResponse<List<User>> dbResponse;

    public GetUsersAsyncTask(Context context, DbResponse<List<User>> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance((context));
        userDao = dbManager.userDao();

    }

    @Override
    protected List<User> doInBackground(Void... voids) {
        return userDao.getAllUsers();
    }

    @Override
    protected void onPostExecute(List<User> users) {
        super.onPostExecute(users);
        if (users != null) {
            dbResponse.onSuccess(users);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrong));
            dbResponse.onError(error);
        }
    }
}
