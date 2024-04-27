package me.deamonet.mi.entity;

import lombok.Data;
import javax.persistence.Id;

@Data
public class Note{
    @Id
    private String nid;
    private String title;
    private Integer uid;
}
