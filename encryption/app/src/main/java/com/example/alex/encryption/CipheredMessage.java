package com.example.alex.encryption;

import android.util.Base64;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Created by sglitowitz on 3/12/2015.
 */
public class CipheredMessage {
    private static String[] _BASE64CHARSET = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v",
            "w", "x", "y", "z", "+", "/", "=", "0", "1", "2", "3", "4", "5", "6", "7", "8",
            "9", ""
    };

    private static HashSet<String> _BASE64HASHSET = new HashSet<String>(Arrays.asList(CipheredMessage._BASE64CHARSET));

    private final byte[] _CIPHERTEXT;
    private final byte[] _IV;
    private final byte[] _SALT;
    private final String _DELIM;

    /**
     * @brief CipheredMessage Constructor
     *
     * @param p_ciphertext  Ciphertext as byte array
     * @param p_iv          Initializing vector as byte array
     * @param p_salt        Salt as byte array
     * @param p_delim       Deliminator as a string, should be 1 character
     */
    public CipheredMessage(byte[] p_ciphertext, byte[] p_iv, byte[] p_salt, String p_delim) {
        this._CIPHERTEXT = p_ciphertext;
        this._IV = p_iv;
        this._SALT = p_salt;
        this._DELIM = p_delim;
    }

    /**
     * @brief CipheredMessage Contstructor.
     *          This constructor attempts to determine the deliminator.
     *
     * @param p_cipheredmessage     Ciphertext as string
     * @throws Exception
     */
    public CipheredMessage(String p_cipheredmessage) throws Exception {
        this(p_cipheredmessage, CipheredMessage.get_delim(p_cipheredmessage));
    }

    /**
     * @brief CipheredMessage Constructor
     *
     * @param p_cipheredmessage     Ciphertext as string
     * @param p_delim               Deliminator as string, shoudl be 1 character
     */
    public CipheredMessage(String p_cipheredmessage, String p_delim) {
        this._DELIM = p_delim;
        Log.d("RVS", "Delim: "+p_delim);
        String[] temp = p_cipheredmessage.split(this._DELIM);
        Log.d("RVS", "ciphertext: "+temp[0]);
        this._CIPHERTEXT = Base64.decode(temp[0], Base64.NO_WRAP);
        Log.d("RVS", "iv: "+ temp[1]);
        this._IV = Base64.decode(temp[1], Base64.NO_WRAP);
        Log.d("RVS", "salt: "+temp[2]);
        this._SALT = Base64.decode(temp[2], Base64.NO_WRAP);
    }

    /**
     * @brief Return a string representing the CipheredMessage ready to be transmitted
     * @return
     */
    public String toTransmitionString() {
        return (new String(Base64.encodeToString(this._CIPHERTEXT, Base64.NO_WRAP)
                + this._DELIM
                + Base64.encodeToString(this._IV, Base64.NO_WRAP)
                + this._DELIM
                + Base64.encodeToString(this._SALT, Base64.NO_WRAP)));
    }

    public byte[] get_ciphertext() {
        return (this._CIPHERTEXT);
    }

    public byte[] get_iv() {
        return (this._IV);
    }

    public byte[] get_salt() {
        return (this._SALT);
    }

    public String get_delim() {
        return (this._DELIM);
    }

    /**
     * @brief Attempts to determine the deliminator without being told
     *
     * @param p_cipheredmessage     Ciphertext as string
     * @return
     * @throws Exception
     */
    private static String get_delim(String p_cipheredmessage) throws Exception {
        char c;
        Log.d("RVS", "Getting Deliminator");
        HashSet<String> cmSet = new HashSet<String>(Arrays.asList(p_cipheredmessage.split("")));
        cmSet.removeAll(CipheredMessage._BASE64HASHSET);

        Log.d("RVS", "Made HashSet");
        if (cmSet.size() > 0) {
            Iterator<String> iter = cmSet.iterator();
            Log.d("RVS", cmSet.toString());
            for(String s : cmSet) {
                Log.d("RVS", s);
            }
            Log.d("RVS", "delim found");

            return (iter.next());
        }
        Log.d("RVS", "No delim found");
        throw new Exception("No deliminator found.");
    }
}
