package ru.practicum.stats.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.model.Hit;

@Mapper
public interface HitMapper {
    HitMapper INSTANCE = Mappers.getMapper(HitMapper.class);

    Hit toHit(HitDto hitDto);
}
