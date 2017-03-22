package org.apache.nutch.protocol.httpclient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rombk on 22/03/2017.
 */
public class HttpEquiv {
    public byte[] replaceHttpEquiv(byte[] html) {
        try {
            String strHtml = new String(html);
            String strReplace = "<meta http-equiv=\"REFRESH\" content=\"(.*?)\" />";
            Pattern patternReplaceUpperCase = Pattern.compile(strReplace);
            Matcher matcherUpCase = patternReplaceUpperCase.matcher(strHtml);
            strHtml = matcherUpCase.replaceAll("");
            strReplace = strReplace.toLowerCase();
            Pattern patternReplaceLowerCase = Pattern.compile(strReplace);
            Matcher matcherLowerCase = patternReplaceLowerCase.matcher(strHtml);
            strHtml = matcherLowerCase.replaceAll("");
            Pattern patternReplaceR = Pattern.compile("<meta http-equiv=\"Refresh\" content=\"(.*?)\" />");
            Matcher matcherR = patternReplaceR.matcher(strHtml);
            strHtml = matcherR.replaceAll("");
            /*System.out.println("Strhtml : "+strHtml);*/
            html = strHtml.getBytes();
        } catch (Exception ex) {
            System.out.println("HtmlReplace.replaceHtml : " + ex.toString());
        }
        return html;
    }
}
