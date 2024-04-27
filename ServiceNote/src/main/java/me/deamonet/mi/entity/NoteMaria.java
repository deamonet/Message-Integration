package me.deamonet.mi.entity;

import lombok.Data;

import javax.persistence.Table;
import java.util.Date;


@Data
public class NoteMaria extends Note{
    private Integer id;
    private String content;
    private String cover;
    private Date firstEdit;
    private Date lastEdit;
    private String category;
}
