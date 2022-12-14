package com.softaai.mpga.sms;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.softaai.mpga.common.BackgroundThreadPoster;
import com.softaai.mpga.common.BaseObservableManager;
import com.softaai.mpga.common.MainThreadPoster;

import java.util.ArrayList;
import java.util.List;

/**
 * This class encapsulates the logic related to SMS messages, including interactions with MVC model.
 */
public class SmsMessagesManager extends BaseObservableManager<SmsMessagesManager.SmsMessagesManagerListener> {

    /*
     * MVC model of the app is a database of SMS messages stored on the device. The model is accessed
     * via a non-standard ContentProvider having CONTENT_URI "content://sms/inbox".
     */
    private static final String CONTENT_URI = "content://sms/inbox";

    // The "data columns" used in our app
    private static final String[] COLUMNS_OF_INTEREST = new String[] {
            "_id",
            "address",
            "date",
            "body",
            "read"
    };

    // Default sort order by descending date
    private static final String DEFAULT_SORT_ORDER = "date DESC";


    /**
     * Classes implementing this interface can be registered as callbacks with
     * {@link SmsMessagesManager}
     */
    public interface SmsMessagesManagerListener {
        /**
         * This method will be called on UI thread when fetching of requested SMS messages
         * completes.
         * @param smsMessages a list of fetched SMS messages; will never be null
         */
        void onSmsMessagesFetched(List<SmsMessage> smsMessages);
    }


    private ContentResolver mContentResolver;
    private final MainThreadPoster mMainThreadPoster;
    private final BackgroundThreadPoster mBackgroundThreadPoster;

    public SmsMessagesManager(ContentResolver contentResolver,
                              MainThreadPoster mainThreadPoster,
                              BackgroundThreadPoster backgroundThreadPoster) {
        mContentResolver = contentResolver;
        mMainThreadPoster = mainThreadPoster;
        mBackgroundThreadPoster = backgroundThreadPoster;
    }

    /**
     * Fetch an SMS message by its ID. Fetch will be done on background thread and registered
     * listeners will be notified of result on UI thread.
     * @param id ID of message to fetch
     */
    public void fetchSmsMessageById(final long id) {
        mBackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = mContentResolver.query(
                        ContentUris.withAppendedId(Uri.parse(CONTENT_URI), id),
                        COLUMNS_OF_INTEREST,
                        null,
                        null,
                        DEFAULT_SORT_ORDER
                );

                List<SmsMessage> result = extractSmsMessagesFromCursor(cursor);

                if (cursor != null) cursor.close();

                notifySmsMessagesFetched(result);
            }
        });
    }


    /**
     * Fetch all SMS messages. Fetch will be done on background thread and registered
     * listeners will be notified of result on UI thread.
     */
    public void fetchAllSmsMessages() {
        mBackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {

                Cursor cursor = mContentResolver.query(
                        Uri.parse(CONTENT_URI),
                        COLUMNS_OF_INTEREST,
                        null,
                        null,
                        DEFAULT_SORT_ORDER
                );

                List<SmsMessage> result = extractSmsMessagesFromCursor(cursor);

                if (cursor != null) cursor.close();

                notifySmsMessagesFetched(result);
            }
        });
    }

    private List<SmsMessage> extractSmsMessagesFromCursor(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            List<SmsMessage> result = new ArrayList<>(cursor.getCount());
            do {
                result.add(new SmsMessage(
                        cursor.getLong(cursor.getColumnIndexOrThrow("_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("address")),
                        cursor.getString(cursor.getColumnIndexOrThrow("body")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("read")) == 0));
            } while (cursor.moveToNext());
            return result;
        } else {
            return new ArrayList<>(0);
        }
    }

    /**
     * Mark specific SMS message as read.
     * @param id ID of message to be marked as read.
     */
    public void markMessageAsRead(final long id) {
        mBackgroundThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                // Designating the fields that should be updated
                ContentValues values = new ContentValues();
                values.put("read", true);

                mContentResolver.update(
                        ContentUris.withAppendedId(Uri.parse(CONTENT_URI), id),
                        values,
                        null,
                        null);
            }
        });

    }

    private void notifySmsMessagesFetched(final List<SmsMessage> smsMessages) {
        mMainThreadPoster.post(new Runnable() {
            @Override
            public void run() {
                for (SmsMessagesManagerListener listener : getListeners()) {
                    listener.onSmsMessagesFetched(smsMessages);
                }
            }
        });
    }

}
