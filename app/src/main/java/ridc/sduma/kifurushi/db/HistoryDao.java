package ridc.sduma.kifurushi.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import ridc.sduma.kifurushi.models.History;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insertHistory(History history);

    @Query("SELECT * FROM history ORDER BY complaintDate DESC")
    LiveData<List<History>> getAllHistories();
}
