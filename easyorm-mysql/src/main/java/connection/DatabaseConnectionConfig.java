package connection;

import lombok.Data;

@Data
public class DatabaseConnectionConfig {
    private String url;
    private String user;
    private String password;
}
