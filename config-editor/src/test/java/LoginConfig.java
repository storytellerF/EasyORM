import com.config_editor.model.Config;

public class LoginConfig extends Config {
    private String username;
    private String password;

    public LoginConfig(int id, String name, String username, String password) {
        super(id, name);
        this.username = username;
        this.password = password;
    }

    public LoginConfig(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LoginConfig(int id, String name) {
        super(id, name);
    }

    public LoginConfig() {
    }
}
