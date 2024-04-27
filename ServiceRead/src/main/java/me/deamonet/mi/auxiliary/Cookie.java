package me.deamonet.mi.auxiliary;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class Cookie {
    private String domain;
    private Date expirationDate;
    private boolean hostOnly;
    private boolean httpOnly;
    private String name;
    private String path;
    private String sameSite;
    private boolean secure;
    private boolean session;
    private String storeId;
    private String value;
}
