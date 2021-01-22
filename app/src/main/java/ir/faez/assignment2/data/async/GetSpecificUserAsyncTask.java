package ir.faez.assignment2.data.async;

import android.content.Context;
import android.os.AsyncTask;

import ir.faez.assignment2.R;
import ir.faez.assignment2.data.db.DAO.DbResponse;
import ir.faez.assignment2.data.db.DAO.UserDao;
import ir.faez.assignment2.data.db.DbManager;
import ir.faez.assignment2.data.model.User;

public class GetSpecificUserAsyncTask extends AsyncTask<String, Void, User> {
    private Context context;
    private UserDao userDao;
    private DbResponse<User> dbResponse;

    public GetSpecificUserAsyncTask(Context context, DbResponse<User> dbResponse) {
        this.context = context;
        this.dbResponse = dbResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DbManager dbManager = DbManager.getInstance(context);
        userDao = dbManager.userDao();
    }

    @Override
    protected User doInBackground(String... username) {
        return userDao.getUserByUsername(username[0]);
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        if (user != null) {
            dbResponse.onSuccess(user);
        } else {
            Error error = new Error(String.valueOf(R.string.somethingWentWrong));
            dbResponse.onError(error);
        }
    }
}
