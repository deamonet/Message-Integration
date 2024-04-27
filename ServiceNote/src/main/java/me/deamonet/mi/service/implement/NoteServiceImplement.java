package me.deamonet.mi.service.implement;

import me.deamonet.mi.entity.Note;
import me.deamonet.mi.entity.NoteMaria;
import me.deamonet.mi.mariamapper.MariaNoteMapper;
import me.deamonet.mi.nebulamapper.NebulaNoteMapper;
import me.deamonet.mi.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class NoteServiceImplement implements NoteService {
    @Autowired
    MariaNoteMapper mariaNoteMapper;
    @Autowired
    NebulaNoteMapper nebulaNoteMapper;

    @Override
    @Transactional
    public NoteMaria getNoteById(Integer nid) {
        return mariaNoteMapper.selectNoteById(nid);
    }

    @Override
    @Transactional
    public Integer addNote(NoteMaria note) {
        return mariaNoteMapper.insertNote(note) + nebulaNoteMapper.insert(note);
    }

    @Override
    @Transactional
    public Integer deleteNote(Integer nid) {
        return mariaNoteMapper.deleteNoteById(nid) + nebulaNoteMapper.deleteById("u" + nid);
    }

    @Override
    @Transactional
    public Integer editNote(NoteMaria note) {
        nebulaNoteMapper.updateById(note);
        return mariaNoteMapper.updateNote(note);
    }

    @Override
    public List<NoteMaria> getLinkedNote(Integer uid) {
        List<Note> noteList = nebulaNoteMapper.selectNoteByUser(uid);
        List<NoteMaria> mariaNoteList = mariaNoteMapper.selectNoteByIds(
                noteList.stream()
                        .map(Note -> Integer.parseInt(Note.getNid().substring(1)))
                        .collect(Collectors.toList()));
        return Objects.isNull(mariaNoteList) ? Collections.emptyList() : mariaNoteList;
    }
}
