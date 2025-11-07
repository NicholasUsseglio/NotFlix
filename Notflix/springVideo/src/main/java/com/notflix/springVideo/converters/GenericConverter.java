package com.notflix.springVideo.converters;

import com.notflix.springVideo.dto.GenericDTO;
import com.notflix.springVideo.entities.GenericEntity;

public interface GenericConverter <E extends GenericEntity ,D extends GenericDTO >{
    
        public E fromDToE (D dto);
            public D fromEToD (E e);


}
