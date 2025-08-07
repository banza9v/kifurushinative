package ridc.sduma.kifurushi.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import ridc.sduma.kifurushi.models.History;

@Database(entities = {History.class}, version = 1)
public abstract class KifurushiDB extends RoomDatabase {
    private static  KifurushiDB instance;
    public abstract HistoryDao historyDao();

    public static KifurushiDB getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, KifurushiDB.class, "kifurushi_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }


}
