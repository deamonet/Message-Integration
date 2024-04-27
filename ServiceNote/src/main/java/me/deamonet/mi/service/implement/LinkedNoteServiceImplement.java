package me.deamonet.mi.service.implement;

import me.deamonet.mi.entity.LinkedNote;
import me.deamonet.mi.entity.Note;
import me.deamonet.mi.entity.NoteMaria;
import me.deamonet.mi.mariamapper.MariaNoteMapper;
import me.deamonet.mi.nebulamapper.NebulaNoteMapper;
import me.deamonet.mi.service.LinkedNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LinkedNoteServiceImplement implements LinkedNoteService {
    @Autowired
    MariaNoteMapper mariaNoteMapper;
    @Autowired
    NebulaNoteMapper nebulaNoteMapper;

    @Override
    public List<LinkedNote> getLinkedNoteByUser(Integer uid) {
        List<LinkedNote> linkedNoteList = nebulaNoteMapper.selectLinkedNoteByUser(uid);
        return Objects.isNull(linkedNoteList) ? Collections.emptyList() : linkedNoteList;
    }

    @Override
    public List<NoteMaria> getLinkedNoteByNote(String nid){
        List<Note> noteList = nebulaNoteMapper.selectNoteByNote(nid);
        List<NoteMaria> mariaNoteList = mariaNoteMapper.selectNoteByIds(
                noteList.stream()
                        .map(Note -> Integer.parseInt(Note.getNid().substring(1)))
                        .collect(Collectors.toList()));
        return Objects.isNull(mariaNoteList) ? Collections.emptyList() : mariaNoteList;
    }
}
