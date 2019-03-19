package com.example.test.testproj.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XmlFtpBackUpUploader extends AsyncTask<Void, Void, Boolean> {

    private static final String server = "ftp.s27.freehost.com.ua";

    private static final String user = "************"; //username
    private static final String pass = "************";//password

    private Context context;

    private File fileToUploading;

    private String firstRemoteFileUrl;

    private String backUpFileUrl;


    public XmlFtpBackUpUploader(Context context, File fileToUploading, String validOffersUrl, String backUpOffersUrl) {
        this.context = context;
        this.fileToUploading = fileToUploading;
        this.firstRemoteFileUrl = validOffersUrl;
        this.backUpFileUrl = backUpOffersUrl;
    }

    private XmlFtpBackUpUploader(Context context, File fileToUploading, String validOffersUrl) {
        this.context = context;
        this.fileToUploading = fileToUploading;
        this.firstRemoteFileUrl = validOffersUrl;
    }

    private boolean uploadXml() {

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server);

            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            InputStream inputStream = new FileInputStream(fileToUploading);

            boolean done;
            if (backUpFileUrl == null) {
                done = ftpClient.storeFile(firstRemoteFileUrl, inputStream);
            } else {
                done = ftpClient.storeFile(backUpFileUrl, inputStream);
            }

            inputStream.close();
            if (done) {
                return true;
            }


        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return false;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {

        if (this.uploadXml()) {
            return true;
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            Toast.makeText(this.context, "Бэкап успешно создан!!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.context, "Не удалось создать бэкап!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
