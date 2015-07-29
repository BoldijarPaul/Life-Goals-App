package com.lifegoals.app.facebookfanpageapi.helper;

import android.os.AsyncTask;
import android.os.Build;

import java.util.concurrent.Executors;

/**
 * Created by Paul on 2/21/2015.
 */
public class AsyncTaskHelper {

    /* this method will create a new async task */
    public static void create(final AsyncMethods methods) {
        AsyncTask<Void, Void, Object> asyncTask = new DefaultAsyncClass(methods);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(Executors.newFixedThreadPool(4));
        else {
            asyncTask.execute();
        }
    }

    /* this class handles everything that happen async , and will return the object & the time of the request in milliseconds*/
    private static class DefaultAsyncClass extends AsyncTask<Void, Void, Object> {
        private long time;
        private AsyncMethods asyncMethods;

        private DefaultAsyncClass(AsyncMethods asyncMethods) {
            this.asyncMethods = asyncMethods;
        }

        @Override
        protected Object doInBackground(Void... params) {
            time = System.currentTimeMillis();
            try {
                return asyncMethods.doInBackground();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            asyncMethods.onDone(o, System.currentTimeMillis() - time);
        }
    }

    /* the interface that will handle the callback that happens on the UI thread */
    public static interface AsyncMethods<T> {
        public T doInBackground();

        public void onDone(T value, long ms);
    }
}
