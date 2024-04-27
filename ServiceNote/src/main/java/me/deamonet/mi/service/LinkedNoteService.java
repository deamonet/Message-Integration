package me.deamonet.mi.service;

import me.deamonet.mi.entity.LinkedNote;
import me.deamonet.mi.entity.NoteMaria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LinkedNoteService {
    public List<LinkedNote> getLinkedNoteByUser(Integer uid);
    public List<NoteMaria> getLinkedNoteByNote(String nid);
}
