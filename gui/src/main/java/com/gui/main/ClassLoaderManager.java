package com.gui.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoaderManager {
	private static ClassLoaderManager classLoaderManager;
	private String lastClassPath;

	private ClassLoaderManager() {

	}

	public void init() {
		lastClassPath=null;
	}
	public static ClassLoaderManager getInstance() {
		if (classLoaderManager == null) {
			classLoaderManager = new ClassLoaderManager();
		}
		return classLoaderManager;
	}

	public boolean produceJar(String classPath, String modelName) {
		try {
			File file = new File("output");
			System.out.println("输出目录："+file.getAbsolutePath());
			if (!file.exists()) {
				System.out.println("文件夹不存在");
				boolean mk_return = file.mkdir();
				if (!mk_return) {
					System.out.println("创建文件夹失败");
					return false;
				}
			}
			String absolutePath = getClass().getResource("../../../sql-com.storyteller_f.sql_query.query-2.0.jar").getPath();
			System.out.println("sql com.storyteller_f.sql_query.query 库位置："+absolutePath);
			ProcessBuilder javac = new ProcessBuilder().command("javac", "-classpath",
					absolutePath, classPath + "\\*.java", "-d",
					"output", "-encoding", "utf-8");
			Process process = javac.start();
//			Charset utf8 = StandardCharsets.UTF_8;
			String utf8="gbk";
			Thread thread1 = new Thread(new TerminalReader(process, new InputStreamReader(process.getInputStream(),utf8),System.out,"编译input",10));
			thread1.start();
			Thread thread2 = new Thread(new TerminalReader(process, new InputStreamReader(process.getErrorStream(),utf8),System.err,"编译error",10));
			thread2.start();
			process.waitFor();
			Process process1 = new ProcessBuilder()
					.command("jar", "-cvf", "output.jar", modelName.replace(".", "\\") + "\\*.class")
					.directory(new File("output")).start();
			Thread thread = new Thread(new TerminalReader(process1, new InputStreamReader(process.getInputStream(),utf8),System.out,"导出jar input",10));
			thread.start();
			process1.waitFor();
			boolean return_result = loadJar("output\\output.jar");
			if (!return_result) {
				return false;
			}
			lastClassPath = classPath;
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
