package com.share.hadoop.spring;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sun.management.FileSystem;

/**
 * 使用Spring Hadoop来访问HDFS文件系统
 */
public class SpringHadoopHDFSApp {
    private org.springframework.context.ApplicationContext ctx;
    private org.apache.hadoop.fs.FileSystem fileSystem;

    /**
     * 创建HDFS文件夹
     */
    @Test
    public void testMkdir() throws Exception{
        fileSystem.mkdirs(new Path("/springhdfs"));
    }

    /**
     * 读取HDFS文件内容
     */
    @Test
    public void testLog() throws Exception{
        FSDataInputStream in = fileSystem.open(new Path("/springhdfs/access.log"));
        IOUtils.copyBytes(in, System.out, 1024);
        in.close();
    }
    @Before
    public void setUp(){
        ctx = new ClassPathXmlApplicationContext("beans.xml");
        fileSystem = (org.apache.hadoop.fs.FileSystem)ctx.getBean("fileSystem");
    }
    @After
    public void tearDown() throws Exception{
        ctx = null;
        fileSystem.close();
    }
}
