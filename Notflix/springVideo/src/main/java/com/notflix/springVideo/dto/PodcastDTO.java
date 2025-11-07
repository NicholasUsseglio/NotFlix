package com.notflix.springVideo.dto;


import lombok.Data;

@Data
public class PodcastDTO implements GenericDTO {

    private Long id;
    private String presentatore;
    private String programmazioni;
    private int numero_episodi;
}