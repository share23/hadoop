package com.share.hadoop.project;

import com.kumkee.userAgent.UserAgent;
import com.kumkee.userAgent.UserAgentParser;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UserAgent测试类
 */
public class UserAgentTest {
    @Test
    public void testReadFile() throws Exception{
        //定义日志文件的路径
        String path = "C:\\Users\\share\\Desktop\\hadoop\\access.log";

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(new File(path)))
        );

        String line = "";
        int i = 0;

        Map<String,Integer> browserMap = new HashMap<String,Integer>();

        UserAgentParser userAgentParser = new UserAgentParser();
        while(line !=null){
            line = reader.readLine();//一次读入一行数据
            i++;
            if(StringUtils.isNotBlank(line)){
                String source = line.substring(getCharacterPosition(line,"\"",5)) + 1;
                UserAgent agent = userAgentParser.parse(source);

                String browser = agent.getBrowser();
                String engine = agent.getEngine();
                String engineVersion = agent.getEngineVersion();
                String os = agent.getOs();
                String platform = agent.getPlatform();
                boolean isMobile = agent.isMobile();

                Integer browserValue = browserMap.get(browser);
                if(browserValue != null){
                    browserMap.put(browser,browserValue +1);
                }else{
                    browserMap.put(browser,1);
                }


                System.out.println("浏览器：" + browser + "  搜索引擎：" + engine + "  引擎版本：" + engineVersion + "  操作系统：" + os + "  平台：" + platform + "  是否是手机：" + isMobile);
            }
        }
        System.out.println(i);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        for(Map.Entry<String,Integer> entry : browserMap.entrySet()){
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }

    /**
     * 测试自定义方法
     */
    @Test
    public void testGetCharacterPosition(){
        String value = "101.226.35.225 - - [19/Oct/2017:04:02:13 +0800] \"GET //?feed=rss2 HTTP/1.1\" 301 5 \"-\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.76 Safari/537.36";
        int index = getCharacterPosition(value,"\"",5);
        System.out.println(index);
    }
    /**
     * 获取指定字符串中指定标识的字符串出现的索引位置
     */
    private int getCharacterPosition(String value,String operator,int index){
        Matcher slashMatcher = Pattern.compile(operator).matcher(value);
        int mIdx = 0;
        while (slashMatcher.find()){
            mIdx++;
            if (mIdx == index){
                break;
            }
        }
        return slashMatcher.start();
    }
    /**
     * 单元测试: UserAgent工具类的使用
     */
    @Test
    public void testUserAgentParser() {
        //public static void main(String[] args) {
        //String source = "Mozilla/5.0 (iPhone; CPU iPhone OS 9_3_4 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Mobile/13G35 QQ/6.5.3.410 V1_IPH_SQ_6.5.3_1_APP_A Pixel/750 Core/UIWebView NetType/2G Mem/117";
        //String source = "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1)";
        String source = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.80 Safari/537.36";
        UserAgentParser userAgentParser = new UserAgentParser();
        UserAgent agent = userAgentParser.parse(source);
        String browser = agent.getBrowser();
        String engine = agent.getEngine();
        String engineVersion = agent.getEngineVersion();
        String os = agent.getOs();
        String platform = agent.getPlatform();
        boolean isMobile = agent.isMobile();
        System.out.println("浏览器：" + browser + "  搜索引擎：" + engine + "  引擎版本：" + engineVersion + "  操作系统：" + os + "  平台：" + platform + "  是否是手机：" + isMobile);
        //}
    }
}
