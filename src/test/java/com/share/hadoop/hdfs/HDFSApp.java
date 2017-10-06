package com.share.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;

/**
 * Hadoop HDFS Java API 操作
 */
public class HDFSApp {

    public static final String HDFS_PATH = "hdfs://192.168.10.20:8020";//设置hdfs路径，域名解析不了，用ip替代

    FileSystem fileSystem=null;
    Configuration configuration=null;

    /**
     * 创建hdfs文件目录
     */
    @Test//单元测试
    public void mkdir() throws Exception{
        fileSystem.mkdirs(new Path("/hdfsapi/test"));
    }

    /**
     *创建文件
     */
    @Test
    public void create() throws Exception{
        FSDataOutputStream output = fileSystem.create(new Path("/hdfsapi/test/a.txt"));
        output.write("hello hadoop".getBytes());
        output.flush();
        output.close();
    }

    /**
     *查看HDFS文件的内容
     */
    @Test
    public void cat() throws Exception{
        FSDataInputStream in = fileSystem.open(new Path("/hdfsapi/test/b.txt"));
        IOUtils.copyBytes(in, System.out, 1024);
        in.close();
    }

    /**
     *重命名
     */
    @Test
    public void rename() throws Exception{
        Path oldPath = new Path("/hdfsapi/test/a.txt");
        Path newPath = new Path("/hdfsapi/test/b.txt");
        fileSystem.rename(oldPath,newPath);
    }

    /**
     *上传文件到HDFS
     */
    @Test
    public void copyFromLocalFile() throws Exception{
        Path localPath = new Path("F:\\a\\share.txt");
        Path hdfsPath = new Path("/hdfsapi/test");
        fileSystem.copyFromLocalFile(localPath,hdfsPath);
    }

    /**
     *上传文件到HDFS(带进度条)
     */
    @Test
    public void copyFromLocalFileWithProgress() throws Exception{
        InputStream in = new BufferedInputStream(
                new FileInputStream(
                        new File("F:\\a\\movie.mp4")));
        FSDataOutputStream output = fileSystem.create(new Path("/hdfsapi/test/movie.mp4"),
                new Progressable() {
                    public void progress() {
                        System.out.print(".");//带进度提醒信息
                    }
                });
        IOUtils.copyBytes(in,output,1024);
    }

    /**
     *下载HDFS文件
     */
    @Test
    public void copyToLocalFile() throws Exception{
        Path localPath = new Path("F:\\a\\aa.txt");
        Path hdfsPath = new Path("/aaa.txt");
        fileSystem.copyToLocalFile(false,hdfsPath,localPath,true);
    }

    /**
     *查看某个目录下的所有文件
     */
    @Test
    public void listFiles() throws Exception{
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/hdfsapi/test"));
        for(FileStatus fileStatus : fileStatuses){
            String isDir = fileStatus.isDirectory() ? "文件夹":"文件";
            short replication = fileStatus.getReplication();
            long len = fileStatus.getLen();
            String path = fileStatus.getPath().toString();
            System.out.println(isDir+"\t"+replication+"\t"+len+"\t"+path);
        }
    }

    /**
     *删除
     */
    @Test
    public void delete() throws Exception{
        fileSystem.delete(new Path("/hdfsapi/test"), true);
    }

    @Before//准备环境
    public void setUp() throws Exception{
        System.out.println("HDFSApp.setUp");
        configuration=new Configuration();
        fileSystem=FileSystem.get(new URI(HDFS_PATH),configuration, "root");
    }
    @After//释放资源
    public void tearDown() throws Exception{
        configuration = null;
        fileSystem = null;
        System.out.println("HDFSApp.tearDown");

    }
}
