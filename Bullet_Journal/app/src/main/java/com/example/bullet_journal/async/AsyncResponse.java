package com.example.bullet_journal.async;

public interface AsyncResponse<T> {

    void taskFinished(T retVal);
}
