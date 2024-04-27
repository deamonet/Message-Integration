package me.deamonet.mi.service;

import me.deamonet.mi.entity.NoteMaria;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface NoteService {
    NoteMaria getNoteById(Integer nid);
    Integer addNote(NoteMaria note);
    Integer deleteNote(Integer nid);
    Integer editNote(NoteMaria note);
    List<NoteMaria> getLinkedNote(Integer uid);
}
