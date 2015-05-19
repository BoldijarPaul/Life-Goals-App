package com.lifegoals.app.helper;

import android.os.AsyncTask;

public class AsyncTaskHelper {
	public static void create(final AsyncMethods methods) {
		new AsyncTask<Void, Void, Object>() {
			private long time;

			@Override
			protected Object doInBackground(Void... params) {
				time = System.currentTimeMillis();
				try {
					return methods.doInBackground();
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}

			@Override
			protected void onPostExecute(Object o) {
				super.onPostExecute(o);
				methods.onDone(o, System.currentTimeMillis() - time);
			}
		}.execute();
	}

	public static interface AsyncMethods<T> {
		public T doInBackground();

		public void onDone(T value, long ms);
	}
}
