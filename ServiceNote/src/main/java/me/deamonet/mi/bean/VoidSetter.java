package me.deamonet.mi.bean;

import org.nebula.contrib.ngbatis.binding.Setter;
import org.springframework.stereotype.Component;

@Component
public class VoidSetter implements Setter<Void> {

    @Override
    public Object set(Void param) {
        return null;
    }

}