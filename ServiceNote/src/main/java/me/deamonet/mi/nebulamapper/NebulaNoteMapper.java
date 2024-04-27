package me.deamonet.mi.nebulamapper;

import me.deamonet.mi.entity.LinkedNote;
import me.deamonet.mi.entity.Note;
import org.nebula.contrib.ngbatis.proxy.NebulaDaoBasic;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface NebulaNoteMapper extends NebulaDaoBasic<Note, String> {
    List<Note> selectNoteByUser(@Param("uid") Integer uid);
    List<LinkedNote> selectLinkedNoteByUser(@Param("uid") Integer uid);
    List<Note> selectNoteByNote(@Param("nid") String nid);
}
