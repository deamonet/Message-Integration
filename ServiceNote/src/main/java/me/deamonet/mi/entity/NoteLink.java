package me.deamonet.mi.entity;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "note_link")
public class NoteLink {
    private Double likeness;
}
