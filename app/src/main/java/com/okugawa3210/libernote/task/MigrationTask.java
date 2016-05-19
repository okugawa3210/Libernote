package com.okugawa3210.libernote.task;

import com.okugawa3210.libernote.model.OrmaDatabase;
import com.okugawa3210.libernote.task.base.BaseTask;

public class MigrationTask extends BaseTask {

    final private OrmaDatabase orma;

    public MigrationTask(OrmaDatabase orma) {
        this.orma = orma;
    }

    @Override
    protected void core() throws Exception {
        orma.migrate();
    }
}
