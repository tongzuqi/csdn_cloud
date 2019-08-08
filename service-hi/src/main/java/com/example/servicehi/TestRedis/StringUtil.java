package com.example.servicehi.TestRedis;


import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 根据键值对填充字符串，如("hello ${name}",{name:"xiaoming"})
     * 输出：
     * @param content
     * @param map
     * @return
     */
    public static String renderString(String content, Map<String, String> map){
        Set<Map.Entry<String, String>> sets = map.entrySet();
        for(Map.Entry<String, String> entry : sets) {
            String regex = "\\$\\{" + entry.getKey() + "\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll(entry.getValue());
        }
        return content;
    }

    /**
     *  asciicode 转中文
     * @param asciicode
     * @return utf8中文值
     */
    public static String unicodeToUtf8 ( String asciicode )
    {
        String[] asciis = asciicode.split ("\\\\u");
        String nativeValue = asciis[0];
        try
        {
            for ( int i = 1; i < asciis.length; i++ )
            {
                String code = asciis[i];
                nativeValue += (char) Integer.parseInt (code.substring (0, 4), 16);
                if (code.length () > 4)
                {
                    nativeValue += code.substring (4);
                }
            }
        }
        catch (NumberFormatException e)
        {
            return asciicode;
        }
        return nativeValue;
    }

    /**
     * 模板替换参数
     * 根据count 个数来 替换 template 的参数
     * map 的格式为 parameter+数字
     * @return
     */
    public static String templateReplaceParameter(String  template, List<String> listStr ){
        if(listStr!=null && listStr.size()>0){
            for (int  i = 0 ; i <listStr.size() ;i++ ){
                template = template .replace("{"+(i+1)+"}", listStr.get(i));
            }
        }
        return  template;
    }

    /**
     * 补齐不足长度
     * @param length 长度
     * @param number 数字
     * @return
     */
    public  static String lpad(int length, int number) {
        String f = "%0" + length + "d";
        return String.format(f, number);
    }

    private static String domainReg  = "([a-z0-9--]{1,200})\\.(ac.cn|bj.cn|sh.cn|tj.cn|cq.cn|he.cn|sn.cn|sx.cn|nm.cn|ln.cn|jl.cn|hl.cn|js.cn|zj.cn|ah.cn|fj.cn|jx.cn|sd.cn|ha.cn|hb.cn|hn.cn|gd.cn|gx.cn|hi.cn|sc.cn|gz.cn|yn.cn|gs.cn|qh.cn|nx.cn|xj.cn|tw.cn|hk.cn|mo.cn|xz.cn" +
            "|com.cn|net.cn|org.cn|gov.cn|我爱你|在线|中国|网址|网店|中文网|公司|网络|集团" +
            "|com|cn|cc|org|net|xin|xyz|vip|shop|top|club|wang|fun|info|online|tech|store|site|ltd|ink|biz|group|link|work|pro|mobi|ren|kim|name|tv|red" +
            "|cool|team|live|pub|company|zone|today|video|art|chat|gold|guru|show|life|love|email|fund|city|plus|design|social|center|world|auto)";

    public static String domain(String url){
        Pattern p = Pattern.compile(domainReg);
        Matcher m = p.matcher(url);
        List<String> strList = new ArrayList<String>();
        while(m.find()){
            strList.add(m.group());
        }
        String categoryId = strList.toString();
        return categoryId.substring(1,categoryId.length()-1);
    }
    public static boolean isNotEmpty(String src) {
        return !isEmpty(src);
    }
    public static boolean isEmpty(Object src) {
        return src == null || src.toString().trim().equals("")||src.equals("undefined");
    }
    public static boolean isNumber(String str){
        return StringUtils.isNumeric(str);
    }
    public static String right(final String str,String separator){
        if (isEmpty(str) || separator == null) {
            return "";
        }
        int pos = str.indexOf(separator);
        return str.substring(pos + separator.length());
    }

    /**
     * 判断某字符串是否为空或长度为0或由空白符构成
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 判断某字符串是否不为空且长度不为0且不由空白符(whitespace) 构成
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String nullConversionSpace(String s) {
        if (null==s){
            return "";
        }else {
            return s;
        }
    }


    


}
