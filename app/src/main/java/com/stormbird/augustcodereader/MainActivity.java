package com.stormbird.augustcodereader;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;

public class MainActivity extends AppCompatActivity {

    private CopyTextView addrText;
    private CopyTextView handshakeText;
    private CopyTextView handshakeIndexText;
    private CopyTextView augustCode;

    private final String BlueToothAddress = "bluetoothAddress";
    private final String HandShakeKey = "handshakeKey";
    private final String HandshakeKeyIndex = "handshakeKeyIndex";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addrText = findViewById(R.id.bluetooth_addr);
        handshakeText = findViewById(R.id.bluetooth_key);
        handshakeIndexText = findViewById(R.id.key_index);
        augustCode = findViewById(R.id.key_code);

        //check if this is rooted
        if (!RootUtil.isDeviceRooted())
        {
            addrText.setText("Rooted (Jailbroken) phone required");
            showNotRooted();
        }
        else
        {
            getCodes();
        }
    }

    private void getCodesDemo()
    {
        addrText.setText("01:02:03:04:05:06");
        handshakeText.setText("1234567890ABCDEF1234567890ABCDEF");
        handshakeIndexText.setText("2");
    }

    private void showNotRooted()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog d = builder.setTitle("Phone is NOT Rooted")
                .setMessage("For this application to work, to fetch the August lock bluetooth handshake credentials a rooted Android phone is required. You can buy old rooted Android phones on eBay, Samsung S4 era or later is fine.")
                .setPositiveButton("Ok", (c, w) -> finish())
                .setCancelable(false)
                .create();
        d.show();
    }

    private void getCodes()
    {
        //read file structure
        String fileContent = getText("/data/data/com.august.luna/shared_prefs/PeripheralInfoCache.xml");
        if (fileContent.length() == 0)
        {
            //try Yale
            fileContent = getText("/data/data/com.august.bennu/shared_prefs/PeripheralInfoCache.xml");
        }

        String decodedText = fileContent;
        try {
            decodedText = URLDecoder.decode(fileContent, "UTF-8");
            decodedText = decodedText.replaceAll("&quot;", "\"");
        }
        catch (Exception e)
        {
            //
        }

        addrText.setText(getElement(BlueToothAddress, decodedText));
        handshakeText.setText(getElement(HandShakeKey, decodedText));
        handshakeIndexText.setText(getNumericElement(HandshakeKeyIndex, decodedText));

        String constructorText = "AugustLock augustLock(\"[BLUETOOTH_ADDR]\", \"[HANDSHAKE_KEY]\", [HANDSHAKE_INDEX]);";

        constructorText = constructorText.replace("[BLUETOOTH_ADDR]", getElement(BlueToothAddress, decodedText));
        constructorText = constructorText.replace("[HANDSHAKE_KEY]", getElement(HandShakeKey, decodedText));
        constructorText = constructorText.replace("[HANDSHAKE_INDEX]", getNumericElement(HandshakeKeyIndex, decodedText));

        augustCode.setText(constructorText);
    }

    private String getElement(String elementCode, String data)
    {
        int index = data.indexOf(elementCode);
        index += elementCode.length() + 3;
        int finIndex = data.indexOf("\"", index+1);
        return data.substring(index, finIndex);
    }

    private String getNumericElement(String elementCode, String data)
    {
        int index = data.indexOf(elementCode);
        index = data.indexOf(":", index + elementCode.length() + 1) + 1;
        int finIndex = data.indexOf(",", index);
        return data.substring(index, finIndex);
    }

    private String getText(String fileName)
    {
        String content = "";
        try {
            Process process = Runtime.getRuntime().exec("su");
            InputStream in = process.getInputStream();
            OutputStream out = process.getOutputStream();
            String cmd = "cat " + fileName;
            out.write(cmd.getBytes());
            out.flush();
            out.close();
            byte[] buffer = new byte[1024 * 12]; //Able to read up to 12 KB (12288 bytes)
            int length = in.read(buffer);
            content = new String(buffer, 0, length);
            //Wait until reading finishes
            process.waitFor();
        } catch (IOException | InterruptedException e) {
             e.getMessage();
        }

        return content;
    }
}