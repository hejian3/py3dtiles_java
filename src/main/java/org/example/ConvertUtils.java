package org.example;

import com.sun.jna.Platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.jar.JarFile;


public class ConvertUtils {

    private static final String JAVA_IO_TMPDIR = "java.io.tmpdir";
    private static final String TEMP_DIR = System.getProperty(JAVA_IO_TMPDIR);
    private static final String JNA_LIBRARY_PATH = "jna.library.path";

    private static final String SPRING_JAR = "BOOT-INF/classes";

    private static final String JNA_LIB_PATH = getJnaLibraryPath();

    public static void main(String[] args) throws URISyntaxException, IOException {
        System.setProperty("jna.encoding", "UTF-8");
        System.out.println("jnaLibraryPath = " + JNA_LIB_PATH);
        ConvertUtils.exec("C:\\Users\\hejian\\Desktop\\着色后的\\202305161756050332_202305161756110548.las", "d:\\las");
    }

    private static String getJnaLibraryPath() {
        try {
            URL current_jar_dir = ConvertUtils.class.getProtectionDomain().getCodeSource().getLocation();
            System.out.println("getJnaLibraryPath.current_jar_dir = " + current_jar_dir);
            Path jar_path;
            String path = Platform.RESOURCE_PREFIX;
            System.out.println("getJnaLibraryPath.Platform.RESOURCE_PREFIX = " + Platform.RESOURCE_PREFIX);
            if (current_jar_dir.getPath().contains(SPRING_JAR)) {
                jar_path = Paths.get(current_jar_dir.toString().substring(10, current_jar_dir.toString().indexOf(SPRING_JAR) - 2));
                path = SPRING_JAR + "/" + Platform.RESOURCE_PREFIX;
            } else {
                jar_path = Paths.get(current_jar_dir.toURI());
            }
            String folderContainingJar = jar_path.getParent().toString();
            ResourceCopy r = new ResourceCopy();
            Optional<JarFile> jar = r.jar(ConvertUtils.class);
            if (jar.isPresent()) {
                try {
                    System.out.println("JAR detected");
                    File target_dir = new File(folderContainingJar);
                    System.out.println("target_dir =" + target_dir.getPath());
                    System.out.println(String.format("Trying copy from %s %s to %s", jar.get().getName(), path, target_dir));
                    // perform dir copy
                    r.copyResourceDirectory(jar.get(), path, target_dir);
                    // add created folders to JNA lib loading path
                    return target_dir.getCanonicalPath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Thread.currentThread().getContextClassLoader().getResource("").getPath().substring(1) + Platform.RESOURCE_PREFIX;
    }

    public static void exec(String input, String output) {
        System.out.println("jnaLibraryPath = " + JNA_LIB_PATH);
        String cmdStr = "%s convert --srs_in 2364 --srs_out 4978 --color_scale 256 %s --out  %s --jobs 2 --overwrite ";
        if (Platform.RESOURCE_PREFIX.contains("win")) {
            openExe(String.format(cmdStr, JNA_LIB_PATH + "/command_line.exe", input, output));
        } else if (Platform.RESOURCE_PREFIX.contains("linux")) {
            String shell = JNA_LIB_PATH + "/command_line";
            openExe("chmod u+x " + shell);
            openExe(String.format(cmdStr, JNA_LIB_PATH + "/command_line", input, output));
        }
    }

    private static void openExe(String cmd) {
        System.out.println("openExe.cmd = " + cmd);
        BufferedReader br = null;
        BufferedReader brError = null;

        try {
            //执行exe  cmd可以为字符串(exe存放路径)也可为数组，调用exe时需要传入参数时，可以传数组调用(参数有顺序要求)
            Process p = Runtime.getRuntime().exec(cmd);
            String line = null;
            //获得子进程的输入流。
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //获得子进程的错误流。
            brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = br.readLine()) != null || (line = brError.readLine()) != null) {
                //输出exe输出的信息以及错误信息
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
