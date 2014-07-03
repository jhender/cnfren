package com.jhdev.coinfriends;

import android.app.backup.BackupAgentHelper;
import android.app.backup.FileBackupHelper;

public class TheBackupAgent extends BackupAgentHelper {
    // The name of the SharedPreferences file
    static final String DATA_FILENAME = "itemsqlitedb";

    // A key to uniquely identify the set of backup data
    static final String FILES_BACKUP_KEY = "datafiles";

    // Allocate a helper and add it to the backup agent
    @Override
    public void onCreate() {
        FileBackupHelper helper = new FileBackupHelper(this, DATA_FILENAME);
        addHelper(FILES_BACKUP_KEY, helper);
    }
}