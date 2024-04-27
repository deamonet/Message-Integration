package me.deamonet.mi.service;

import me.deamonet.mi.entity.Note;
import me.deamonet.mi.entity.NoteLink;
import org.springframework.stereotype.Service;

@Service
public interface NoteLinkService {
    public Integer insertLink(Note note1, Note note2, NoteLink link);
    public Integer updateLink(Note note1, Note note2, NoteLink link);
    public Boolean existsLink(NoteLink link);
    public Boolean existsLink(String startId, String endId, NoteLink link);
}
