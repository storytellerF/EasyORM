package com.storyteller.gui.main;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassLoaderManager {
    private static ClassLoaderManager classLoaderManager;
    private String lastClassPath;

    private ClassLoaderManager() {

    }

    public static ClassLoaderManager getInstance() {
        if (classLoaderManager == null) {
            classLoaderManager = new ClassLoaderManager();
        }
        return classLoaderManager;
    }

    public void init() {
        lastClassPath = null;
    }

    public List<String> allFile(String path,String subPath) {
        System.out.println("path:" + path);
        File file = new File(path);
        File[] files = file.listFiles();
        List<String> list = new ArrayList<>();
        if (files == null) {
            return list;
        }
        for (File file1 : files) {
            if (file1.isFile()) {
                list.add(subPath+"\\"+file1.getName());
            } else {
                list.addAll(allFile(file1.getAbsolutePath(),"\\"+file1.getName()));
            }
        }
        return list;
    }

    public List<String> createCommand(String... strings) {
        List<String> command = new ArrayList<>();
        Collections.addAll(command, strings);
        return command;
    }

    public boolean produceJar(String path, String modelName) {
        try {
            File file = new File("output");
            System.out.println("输出目录：" + file.getAbsolutePath());
            if (!file.exists()) {
                System.out.println("文件夹不存在");
                boolean mk_return = file.mkdir();
                if (!mk_return) {
                    System.out.println("创建文件夹失败");
                    return false;
                }
            }

            JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(getClass().getResourceAsStream("/config.json")));
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String jarName = asJsonObject.get("jarName").getAsString();

            String classPath = getClass().getResource("/" + jarName).getFile().substring(1);
            System.out.println("com.storyteller_f.sql_query.query 库位置：" + classPath);
            List<String> files = allFile(path,"");
            List<String> absFiles = new ArrayList<>();
            for (String s : files) {
                absFiles.add(new File(path, s).getAbsolutePath());
            }

            List<String> compileCommand = createCommand("javac", "-classpath", classPath, "-d", "output", "-encoding", "utf-8");
            compileCommand.addAll(absFiles);
            ProcessBuilder javac = new ProcessBuilder().command(compileCommand);
            List<String> command = javac.command();
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : command) {
                stringBuilder.append(s).append(" ");
            }
            System.out.println(stringBuilder);
            Process process = javac.start();
            String encoding = "gbk";
            Thread compileNormal = new Thread(new TerminalReader(process, new InputStreamReader(process.getInputStream(), encoding), System.out, "编译input", 10));
            compileNormal.start();
            Thread compileError = new Thread(new TerminalReader(process, new InputStreamReader(process.getErrorStream(), encoding), System.err, "编译error", 10));
            compileError.start();
            process.waitFor();
            List<String> pack = createCommand("jar", "-cvf", "output.jar",modelName.replace(".","\\")+"\\**\\*.class");
            for (int i = 0; i < files.size(); i++) {
                files.set(i,modelName.replace(".","\\")+ files.get(i).replace(".java", ".class"));
            }
            pack.addAll(files);
            Process addLoaderProcess = new ProcessBuilder()
                    .command(pack)
                    .directory(new File("output")).start();
            Thread packNormal = new Thread(new TerminalReader(addLoaderProcess, new InputStreamReader(process.getInputStream(), encoding), System.out, "导出jar input", 10));
            packNormal.start();
            Thread packError = new Thread(new TerminalReader(addLoaderProcess, new InputStreamReader(process.getErrorStream(), encoding), System.err, "导出jar error", 10));
            packError.start();
            int i = addLoaderProcess.waitFor();
            System.err.println(i);
            if (i != 1) return false;
            boolean loader_result = loadJar("output\\output.jar");
            if (!loader_result) {
                return false;
            }
            lastClassPath = path;
            return true;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isNewClassPath(String classPath) {
        return !classPath.equals(lastClassPath);
    }

    public boolean loadJar(String jarPath) {
        File jarFile = new File(jarPath);
        try {
            URL url = jarFile.toURI().toURL();
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            if (classLoader instanceof URLClassLoader) {
                System.out.println("DEB: classLoader instanceof URLClassLoader");
                URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
                Class<?> urlClassLoaderClass = URLClassLoader.class;
                Method method = urlClassLoaderClass.getDeclaredMethod("addURL", URL.class);
                boolean a = method.isAccessible();
                method.setAccessible(true);
                method.invoke(systemClassLoader, url);
                method.setAccessible(a);
            } else {
                Field field = classLoader.getClass().getDeclaredField("ucp");
                field.setAccessible(true);
                Object ucp = field.get(classLoader);
                System.out.println("DEB: invoke method!");
                Method method = ucp.getClass().getDeclaredMethod("addURL", URL.class);
                boolean a = method.isAccessible();
                method.setAccessible(true);
                method.invoke(ucp, url);
                method.setAccessible(a);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public boolean oneStep(String classPath, String packageName) {
        if (isNewClassPath(classPath)) {
            return produceJar(classPath, packageName);
        }
        return true;
    }
}
