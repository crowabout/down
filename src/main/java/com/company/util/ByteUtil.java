package com.company.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by pc on 2017/8/10.
 */
public class ByteUtil {

    private static final int bufferSize=60000;
    private static final String CHAR_SET="UTF-8";

    public static String string(ByteBuffer buffer) {
        Charset charset = null;
        CharsetDecoder decoder = null;
        CharBuffer charBuffer = null;
        try {
            charset = Charset.forName("utf-8");
            decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return "error";
        }
    }


    /**
     * Read the input stream into a byte buffer. To deal with slow input streams, you may interrupt the thread this
     * method is executing on. The data read until being interrupted will be available.
     * @param inStream the input stream to read from
     * @param maxSize the maximum size in bytes to read from the stream. Set to 0 to be unlimited.
     * @return the filled byte buffer
     * @throws IOException if an exception occurs whilst reading from the input stream.
     */
    private static ByteBuffer readToByteBuffer(InputStream inStream, int maxSize) throws IOException {
//        Validate.isTrue(maxSize >= 0, "maxSize must be 0 (unlimited) or larger");
        final boolean capped = maxSize > 0;
        byte[] buffer = new byte[capped && maxSize < bufferSize ? maxSize : bufferSize];
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(capped ? maxSize : bufferSize);
        int read;
        int remaining = maxSize;

        while (!Thread.interrupted()) {
            read = inStream.read(buffer);
            if (read == -1) break;
            if (capped) {
                if (read > remaining) {
                    outStream.write(buffer, 0, remaining);
                    break;
                }
                remaining -= read;
            }
            outStream.write(buffer, 0, read);
        }
        return ByteBuffer.wrap(outStream.toByteArray());
    }



    public static String string(InputStream in)throws Exception{
        ByteBuffer buffer =readToByteBuffer(in,0);
        return string(buffer);
    }
}
