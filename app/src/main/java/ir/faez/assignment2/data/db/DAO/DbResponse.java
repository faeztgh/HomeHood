package ir.faez.assignment2.data.db.DAO;

public interface DbResponse<T> {
    void onSuccess(T t);

    void onError(Error error);
}
