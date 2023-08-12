package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.model.Hit;

@Mapper
public interface HitMapper {
    HitMapper INSTANCE = Mappers.getMapper(HitMapper.class);

    Hit toHit(HitDto hitDto);
//    public static Hit toHit(HitDto hitDto) {
//        return Hit.builder()
//                .app(hitDto.getApp())
//                .uri(hitDto.getUri())
//                .ip(hitDto.getIp())
//                .timestamp(hitDto.getTimestamp())
//                .build();
//    }
}
