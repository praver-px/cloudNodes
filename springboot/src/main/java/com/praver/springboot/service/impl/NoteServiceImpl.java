package com.praver.springboot.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.praver.springboot.entity.Note;
import com.praver.springboot.entity.NoteThingLog;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.exception.ServiceRollbackException;
import com.praver.springboot.mapper.INoteMapper;
import com.praver.springboot.service.INoteService;
import com.praver.springboot.service.INoteThingLogService;
import com.praver.springboot.util.code.EventCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.praver.springboot.entity.table.Tables.NOTE;

@Service
public class NoteServiceImpl implements INoteService {

    @Resource
    private INoteMapper noteMapper;

    @Resource
    private INoteThingLogService noteThingLogService;

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

    @Override
    public void topNote(boolean isTop, int noteId, int userId) throws ServiceException {
        int beforeTop = 0, afterTop = 1;
        if (!isTop) {
            beforeTop = 1;
            afterTop = 0;
        }

        //封装修改条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(NOTE.ID.eq(noteId))
                .and(NOTE.USER_ID.eq(userId))
                .and(NOTE.STATUS.eq(1))
                .and(NOTE.TOP.eq(beforeTop));
        //封装修改字段
        Note note = Note.builder()
                .top(afterTop)
                .build();
        int count = 0;
        try {
            count = noteMapper.updateByQuery(note, queryWrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (count != 1)
            throw new ServiceRollbackException("笔记置顶状态修改失败", EventCode.NOTE_TOP_FAILED);

        //置顶操作存入小记日子记录中（感觉没必要）
        NoteThingLog noteThingLog = NoteThingLog.builder()
                .time(new Date())
                .event(EventCode.NOTE_TOP_SUCCESS)
                .desc("修改了笔记的置顶状态")
                .noteId(noteId)
                .userId(userId)
                .build();

        noteThingLogService.addLog(noteThingLog, true);
    }

    @Override
    public void deleteNoteById(boolean complete, int noteId, int userId, boolean isRecycleBin) throws ServiceException {
        int beforeStatus = 1, afterStatus = 0;
        String desc = "删除笔记", event = EventCode.NOTE_DELETE_SUCCESS;
        if (complete) {
            afterStatus = -1;
            desc = "彻底删除笔记";
            event = EventCode.NOTE_COMPLETE_DELETE_SUCCESS;
            if (isRecycleBin) beforeStatus = 0;

        }

        //对删除小记进行操作
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(NOTE.ID.eq(noteId))
                .and(NOTE.USER_ID.eq(userId))
                .and(NOTE.STATUS.eq(beforeStatus));

        Note note = Note.builder().status(afterStatus).updateTime(new Date()).build();

        int count = 0;
        try {
            count = noteMapper.updateByQuery(note, queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(desc + "失败！", EventCode.UPDATE_EXCEPTION);
        }

        if (count != 1)
            throw new ServiceRollbackException(desc + "失败！", EventCode.UPDATE_ERROR);


        //记录删除小记的日志事件
        NoteThingLog noteThingLog = NoteThingLog.builder()
                .time(new Date())
                .event(event)
                .desc(desc)
                .noteId(noteId)
                .userId(userId)
                .build();

        noteThingLogService.addLog(noteThingLog, true);
    }


}
