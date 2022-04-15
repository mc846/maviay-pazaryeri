package com.maviay.pazaryeri.Models.Request;

public class LoginRequest {

    String pass;
    String empcode;

    public LoginRequest(String pass, String empcode){
        this.pass = pass;
        this.empcode = empcode;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }
}
