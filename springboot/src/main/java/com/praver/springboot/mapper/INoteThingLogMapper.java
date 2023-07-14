package com.praver.springboot.mapper;

import com.mybatisflex.core.BaseMapper;
import com.praver.springboot.entity.NoteThingLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface INoteThingLogMapper extends BaseMapper<NoteThingLog> {
}
