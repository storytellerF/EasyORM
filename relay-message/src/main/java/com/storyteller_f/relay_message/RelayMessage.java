package com.storyteller_f.relay_message;

public class RelayMessage {
    public boolean isSuccess;
    public String message;

    public RelayMessage(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }
}
