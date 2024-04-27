package me.deamonet.mi.mariamapper;

import me.deamonet.mi.entity.NoteMaria;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface MariaNoteMapper {
    NoteMaria selectNoteById(Integer id);
    List<NoteMaria> selectNoteByIds(List<Integer> nids);
    Integer insertNote(NoteMaria note);
    Integer updateNote(NoteMaria note);
    Integer deleteNoteById(Integer id);
}
