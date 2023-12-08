package com.samm.estalem.Classes.Model;

public class Proposals {
    public int id ;
    public String  proposalerName ;
    public String proposalerPhone;
    public String proposalerTitle ;
    public String proposalerContain ;

    public Proposals(String proposalerName, String proposalerPhone, String proposalerTitle, String proposalerContain) {
        this.proposalerName = proposalerName;
        this.proposalerPhone = proposalerPhone;
        this.proposalerTitle = proposalerTitle;
        this.proposalerContain = proposalerContain;
    }
}
