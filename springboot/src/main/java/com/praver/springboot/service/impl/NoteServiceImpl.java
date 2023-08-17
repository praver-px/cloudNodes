package com.praver.springboot.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.praver.springboot.entity.Note;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.mapper.INoteMapper;
import com.praver.springboot.service.INoteService;
import com.praver.springboot.util.code.EventCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.praver.springboot.entity.table.Tables.NOTE;

@Service
public class NoteServiceImpl implements INoteService {

    @Resource
    private INoteMapper noteMapper;

    @Override
    public List<Note> getUserNormalNotes(int userId) throws ServiceException {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(NOTE.ID, NOTE.TITLE, NOTE.BODY, NOTE.UPDATE_TIME, NOTE.TOP)
                .where(NOTE.STATUS.eq(1))
                .and(NOTE.USER_ID.eq(userId))
                .orderBy(NOTE.TOP.desc(), NOTE.UPDATE_TIME.desc());
        List<Note> notes;
        try {
            notes = noteMapper.selectListByQuery(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("获取笔记失败", EventCode.SELECT_EXCEPTION);
        }
        return notes;
    }
}
