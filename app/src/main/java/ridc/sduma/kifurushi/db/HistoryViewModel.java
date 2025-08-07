package ridc.sduma.kifurushi.db;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import ridc.sduma.kifurushi.models.History;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    HistoryRepository repository;
    public HistoryViewModel(@NonNull Application application) {
        super(application);
        repository = new HistoryRepository(application);
    }

    public void insertHistory(History history){
        repository.insertHistory(history);
    }

    public LiveData<List<History>> getAllHistory(){
        return repository.getAllHistory();
    }

}
