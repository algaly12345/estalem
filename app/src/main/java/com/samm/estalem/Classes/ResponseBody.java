package com.samm.estalem.Classes;

public class ResponseBody {
    private Object message;
    private Object error;

    public void setMessage(Object message) {
        this.message = message;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public Object getError() {
        return error;
    }
}
