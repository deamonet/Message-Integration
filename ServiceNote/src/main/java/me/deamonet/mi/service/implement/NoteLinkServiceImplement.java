package me.deamonet.mi.service.implement;

import me.deamonet.mi.entity.Note;
import me.deamonet.mi.entity.NoteLink;
import me.deamonet.mi.mariamapper.MariaNoteMapper;
import me.deamonet.mi.nebulamapper.NebulaNoteMapper;
import me.deamonet.mi.service.NoteLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NoteLinkServiceImplement implements NoteLinkService {
    @Autowired
    MariaNoteMapper mariaNoteMapper;
    @Autowired
    NebulaNoteMapper nebulaNoteMapper;

    @Override
    public Integer insertLink(Note note1, Note note2, NoteLink link) {

        return null;
    }

    @Override
    public Integer updateLink(Note note1, Note note2, NoteLink link) {
        return null;
    }

    @Override
    public Boolean existsLink(NoteLink link) {
        return null;
    }

    @Override
    public Boolean existsLink(String startId, String endId, NoteLink link) {
        return null;
    }
}
