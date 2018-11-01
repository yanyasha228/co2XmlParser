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

public class XmlFtpUploader extends AsyncTask<Void, Void, Boolean> {
    private static final String server = "ftp.s27.freehost.com.ua";
    private static final String user = "********"; //username
    private static final String pass = "********";//password

    private Context context;

    private File fileToUploading;


    public XmlFtpUploader(Context context, File fileToUploading) {
        this.context = context;
        this.fileToUploading = fileToUploading;
    }

    public boolean uploadXml() {

        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server);

            ftpClient.login(user, pass);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String firstRemoteFile = "/www.co2.biz.ua/wp-content/uploads/2018/03/co2ShopPriceListForRozetkaTEST.xml";
            InputStream inputStream = new FileInputStream(fileToUploading);

            boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);

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
            Toast.makeText(this.context, "Файл успешно загружен!!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this.context, "Не удалось загрузить файл!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
