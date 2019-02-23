package edu.buffalo.cse.cse486586.groupmessenger1;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * GroupMessengerActivity is the main Activity for the assignment.
 * 
 * @author stevko
 *
 */
public class GroupMessengerActivity extends Activity {

    private static final String TAG = GroupMessengerActivity.class.getName();
    static final int SERVER_PORT = 10000;
    private static AtomicInteger seqId = new AtomicInteger(0);
    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";
    private static final String REMOTE_PORT [] = { "11108",  "11112", "11116", "11120", "11124"};

    private Uri mUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);


        final TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());



        final EditText editText = (EditText) findViewById(R.id.editText1);

        mUri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger1.provider");


        TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr) * 2));

        try {

            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);

        } catch (IOException e) {

            Log.e(TAG, "Can't create a ServerSocket");
            return;
        }


        findViewById(R.id.button1).setOnClickListener(
                new OnPTestClickListener(tv, getContentResolver()));

        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String msg = editText.getText().toString() + "\n";
                editText.setText(""); // This is one way to reset the input box.

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                ContentValues mContentValues = new ContentValues();

                mContentValues.put(KEY_FIELD, ts);
                mContentValues.put(VALUE_FIELD, msg);


                getContentResolver().insert(mUri, mContentValues);

                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, msg, myPort);

                Log.e(TAG,msg);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }


    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];

            /* infinite while loop to accept multiple messeges */

            while (true) {

                try {


                    Socket clientSocket = serverSocket.accept();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


                    String incomingMessege = bufferedReader.readLine();

                    if (incomingMessege != null) {

                        publishProgress(incomingMessege);
                    }

                    clientSocket.close();


                } catch (IOException e) {
                    Log.e(TAG, "Client Connection failed");
                }
            }


        }

        protected void onProgressUpdate(String...strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
            String strReceived = strings[0].trim();
            final TextView tv = (TextView) findViewById(R.id.textView1);


            String key = Integer.toString(seqId.getAndIncrement());



            ContentValues mContentValues = new ContentValues();

            mContentValues.put(KEY_FIELD, key);
            mContentValues.put(VALUE_FIELD, strReceived);

            getContentResolver().insert(mUri, mContentValues);


            tv.append(strReceived+ "\n");

//
//            Cursor resultCursor = getContentResolver().query(mUri, null, key, null, null);
//
//            if (resultCursor == null) {
//                Log.e(TAG, "Result null");
//            }
//
//            int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
//            int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
//            if (keyIndex == -1 || valueIndex == -1) {
//                Log.e(TAG, "Wrong columns");
//                resultCursor.close();
//            }
//
//            resultCursor.moveToFirst();
//
//            if (!(resultCursor.isFirst() && resultCursor.isLast())) {
//                Log.e(TAG, "Wrong number of rows");
//                resultCursor.close();
//            }
//
//
//            String returnValue = resultCursor.getString(valueIndex);
//
//
//            tv.append(returnValue+ "\n");
//
//
//            resultCursor.close();


            return;
        }
    }


    private class ClientTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... msgs) {
            try {


                for (int z=0; z<5; z++) {

                    Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                            Integer.parseInt(REMOTE_PORT[z]));


                    String msgToSend = msgs[0];

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    dataOutputStream.writeBytes(msgToSend);
                    dataOutputStream.flush();

                    socket.close();
                    dataOutputStream.close();
                }


            } catch (UnknownHostException e) {
                Log.e(TAG, "ClientTask UnknownHostException");
            } catch (IOException e) {
                Log.e(TAG, "ClientTask socket IOException");
            }

            return null;
        }
    }

    private static Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }


}
