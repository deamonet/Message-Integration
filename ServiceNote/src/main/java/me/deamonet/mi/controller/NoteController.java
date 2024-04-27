package me.deamonet.mi.controller;

import me.deamonet.mi.entity.Note;
import me.deamonet.mi.entity.NoteMaria;
import me.deamonet.mi.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class NoteController {

    @Autowired
    NoteService noteService;

    @PostMapping("/note")
    public Integer addNote(NoteMaria note){
        return noteService.addNote(note);
    }

    @GetMapping("/note/{nid}")
    public NoteMaria getNote(@PathVariable("nid") int id){
        return noteService.getNoteById(id);
    }

    @GetMapping("/user/{uid}/notes")
    public List<NoteMaria> getAllNoteFromUser(@PathVariable("uid") int uid){
        return noteService.getLinkedNote(uid);
    }


}
