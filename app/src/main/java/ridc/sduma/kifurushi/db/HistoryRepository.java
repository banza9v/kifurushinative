package ridc.sduma.kifurushi.db;

import android.app.Application;

import androidx.lifecycle.LiveData;

import ridc.sduma.kifurushi.models.History;

import java.util.List;

public class HistoryRepository {
    private final HistoryDao dao;

    public HistoryRepository(Application application){
        KifurushiDB db = KifurushiDB.getInstance(application);
        dao = db.historyDao();
    }

    public void insertHistory(History history){
        dao.insertHistory(history);
    }

    public LiveData<List<History>> getAllHistory(){
        return dao.getAllHistories();
    }

}
