package ru.practicum.stats.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViewStatsDto {

    private String app;
    private String uri;
    private Long hits;
}