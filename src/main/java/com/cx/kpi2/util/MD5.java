package com.cx.kpi2.util;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
/**
 * MD5���ܹ�����
 * @author luckp
 *
 */

public class MD5 {

// public static void main(String[] args) {
//  Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
//  String v = "V1";
//  String one = v.substring(0, 1);
//  String two = v.substring(1);
//  if(!"V".equals(one) || !pattern.matcher(two).matches() ) {
//   System.out.println("false");
//  }else {
//   System.out.println("true");
//  }
// }

    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //��ӡ���ɵ�MD5������ϢժҪ
            //System.out.println(buf.toString().substring(8, 24));
            //32λ����
            return buf.toString().toUpperCase();
            // 16λ�ļ���
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}

