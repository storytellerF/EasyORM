package com.storyteller.util;

import com.storyteller.gui.main.ParseDatabase;
import com.storyteller.gui.main.TableConfig;
import com.storyteller.gui.model.ConnectionConfig;
import com.storyteller.gui.view.DatabaseConnectionInput;
import com.storyteller_f.easyorm_jdbc.JDBCObtain;
import com.storyteller_f.relay_message.RelayMessage;
import com.storyteller_f.sql_query.annotation.NoQuery;
import com.storyteller_f.sql_query.query.Create;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.rmi.UnexpectedException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class RUtil {
    public static RelayMessage reflectToDatabase(DatabaseConnectionInput databaseConnectionInput, Connection connection){
        try {
            File file = new File(databaseConnectionInput.getModel());
            File[] files = file.listFiles();
            if (files == null) {
                return new RelayMessage(false,"获取不到文件");
            }
            for (File child : files) {
                if (child.isDirectory()) {
                    continue;
                }
                String className = child.getName().substring(0, child.getName().indexOf('.'));
                Class<?> clazz = Class.forName(databaseConnectionInput.packageName() + "." + className);
                Create create = new Create(new JDBCObtain(connection), clazz);
                String parse = create.parse(true);
                System.out.println(parse);
                connection.createStatement().execute(parse);
            }
            System.out.println("执行完毕");
            return new RelayMessage(true, "执行完成");
        } catch (ClassNotFoundException | UnexpectedException | SQLException e1) {
            e1.printStackTrace();
            return new RelayMessage(false, e1.getMessage());
        }
    }

    public static RelayMessage produceStaticFunction(DatabaseConnectionInput databaseConnectionInput) {
        File[] files = new File(databaseConnectionInput.getModel()).listFiles();
        if (files == null) {
            return new RelayMessage(false,"获取文件失败");
        }

        for (File file : files) {
            StringBuilder fileContent = new StringBuilder();
            String className = file.getName().substring(0, file.getName().indexOf("."));
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    fileContent.append(temp).append("\n");
                }
                if (fileContent.length() == 0) {
                    System.out.println("文件内容为空");
                    continue;
                }
                int i;
                for (i = fileContent.length() - 1; i >= 0; i--) {
                    char c = fileContent.charAt(i);
                    if (c == '}') {
                        //找到文件结尾了
                        break;
                    }
                }
                if (i < 0) {
                    //未找到
                    System.out.println("未找到文件尾");
                    continue;
                }

                Class<?> clazz = Class.forName(databaseConnectionInput.packageName() + "." + className);
                StringBuilder staticContent = new StringBuilder();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    if (field.isAnnotationPresent(NoQuery.class)) {
                        continue;
                    }
                    String name = field.getName();
                    staticContent.append("\tpublic static String ").append(name).append("(){\n").append("\t\treturn \"").append(name).append("\";\n\t}\n");
                }
                System.out.println(i + " length:" + fileContent.length());
                fileContent.insert(i, staticContent);
                System.out.println("新的文件内容");
                System.out.println(fileContent);
                System.out.println("文件内容结束");
                Util.writeFile(file.getAbsolutePath(), fileContent.toString());

            } catch (ClassNotFoundException | IOException exception) {
                exception.printStackTrace();
                return new RelayMessage(false,file.getName()+":"+exception.getMessage());
            }
        }
        return new RelayMessage(true, "执行完成");
    }

    public static RelayMessage reflectToCode(Connection connection, DatabaseConnectionInput databaseConnectionInput) {
        try {
            Statement statement = connection.createStatement();
            ConnectionConfig config = databaseConnectionInput.getCreateConfig();
            TableConfig tableConfig = TableConfig.build(statement, config, connection);
            ParseDatabase create = new ParseDatabase(tableConfig);
            create.parseDatabase(databaseConnectionInput.isEnableLomok());
            return new RelayMessage(true, "执行完成");
        } catch (Exception e1) {
            e1.printStackTrace();
            return new RelayMessage(false, e1.getMessage());
        }
    }
}
