package br.pucpr.appdev.listacomprasapp.Model;

public class APIResponse {

    private int code;
    private String msg;
    private boolean valid;

    public APIResponse() {
    }

    public APIResponse(int code, String msg, boolean valid) {
        this.code = code;
        this.msg = msg;
        this.valid = valid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

}
