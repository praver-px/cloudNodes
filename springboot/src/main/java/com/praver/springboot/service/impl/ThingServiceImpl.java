package com.praver.springboot.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.praver.springboot.entity.NoteThingLog;
import com.praver.springboot.entity.Thing;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.exception.ServiceRollbackException;
import com.praver.springboot.mapper.INoteThingLogMapper;
import com.praver.springboot.mapper.IThingMapper;
import com.praver.springboot.service.IThingService;
import com.praver.springboot.util.code.EventCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static com.praver.springboot.entity.table.Tables.THING;

@Service
@Transactional(rollbackFor = ServiceRollbackException.class)
public class ThingServiceImpl implements IThingService {

    @Resource
    private IThingMapper thingMapper;

    @Resource
    private INoteThingLogMapper noteThingLogMapper;

    /**
     * 修改小记的置顶状态
     *
     * @param isTop   是否置顶
     * @param thingId 小记id
     * @param userId  用户id
     * @throws ServiceException
     */
    @Override
    public void topThing(boolean isTop, int thingId, int userId) throws ServiceException {
        int beforeTop = 0, afterTop = 1;
        if (!isTop) {
            beforeTop = 1;
            afterTop = 0;
        }

        //封装修改条件
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(THING.ID.eq(thingId))
                .and(THING.USER_ID.eq(userId))
                .and(THING.STATUS.eq(1))
                .and(THING.TOP.eq(beforeTop));
        //封装修改字段
        Thing thing = Thing.builder()
                .top(afterTop)
                .build();
        int count = 0;
        try {
            count = thingMapper.updateByQuery(thing, queryWrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (count != 1)
            throw new ServiceRollbackException("小记置顶状态修改失败", EventCode.THING_TOP_FAILED);

        //置顶操作存入小记日子记录中（感觉没必要）
        NoteThingLog noteThingLog = NoteThingLog.builder()
                .time(new Date())
                .event(EventCode.THING_TOP_SUCCESS)
                .desc("修改了小记的置顶状态")
                .thingId(thingId)
                .userId(userId)
                .build();
        try {
            count = noteThingLogMapper.insert(noteThingLog);
        } catch (Exception e) {
            throw new ServiceRollbackException("小记置顶状态修改失败", EventCode.INSERT_EXCEPTION);
        }
        if (count != 1)
            throw new ServiceRollbackException("小记置顶状态修改失败", EventCode.INSERT_ERROR);


    }

    @Override
    public Thing getThing(int thingId, int userId) throws ServiceException {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(THING.TITLE, THING.TOP, THING.TAGS, THING.CONTENT)
                .where(THING.ID.eq(thingId))
                .and(THING.USER_ID.eq(userId))
                .and(THING.STATUS.eq(1));
        Thing thing = null;
        try {
            thing = thingMapper.selectOneByQuery(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException("查询小记异常", EventCode.SELECT_EXCEPTION);
        }
        if (thing == null) throw new ServiceException("小记不存在", EventCode.SELECT_NONE);

        return thing;
    }

    @Override
    public void newCreateThing(Thing thing) throws ServiceException {
        int count = 0;
        try {
            count = thingMapper.insert(thing);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("新增小记异常", EventCode.THING_CREATE_EXCEPTION);
        }
        if (count != 1) throw new ServiceRollbackException("新增小记失败", EventCode.THING_CREATE_FAILED);

        NoteThingLog noteThingLog = NoteThingLog.builder()
                .time(thing.getUpdateTime())
                .event(EventCode.THING_CREATE_SUCCESS)
                .desc("新增小记")
                .thingId(thing.getId())
                .userId(thing.getUserId())
                .build();

        try {
            count = noteThingLogMapper.insert(noteThingLog);
        } catch (Exception e) {
            throw new ServiceRollbackException("新增小记日志失败", EventCode.INSERT_EXCEPTION);
        }
        if (count != 1) throw new ServiceRollbackException("新增小记失败", EventCode.INSERT_ERROR);
    }

    @Override
    public void deleteTingById(boolean complete, int thingId, int userId, boolean isRecycleBin) throws ServiceException {
        int beforeStatus = 1, afterStatus = 0;
        String desc = "删除小记", event = EventCode.THING_DELETE_SUCCESS;
        if (complete) {
            afterStatus = -1;
            desc = "彻底删除小记";
            event = EventCode.THING_COMPLETE_DELETE_SUCCESS;
            if (isRecycleBin) beforeStatus = 0;

        }

        //对删除小记进行操作
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(THING.ID.eq(thingId))
                .and(THING.USER_ID.eq(userId))
                .and(THING.STATUS.eq(beforeStatus));

        Thing thing = Thing.builder().status(afterStatus).updateTime(new Date()).build();

        int count = 0;
        try {
            count = thingMapper.updateByQuery(thing, queryWrapper);
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
                .thingId(thingId)
                .userId(userId)
                .build();

        try {
            count = noteThingLogMapper.insert(noteThingLog);
        } catch (Exception e) {
            throw new ServiceRollbackException(desc + "失败", EventCode.INSERT_EXCEPTION);
        }
        if (count != 1) throw new ServiceRollbackException(desc + "失败", EventCode.INSERT_ERROR);
    }

    /**
     * 获取用户正常的小记
     *
     * @param userId 用户id
     * @return 用户的List<Thing>集合
     * @throws ServiceException
     */
    @Override
    public List<Thing> getUserNormalTing(int userId) throws ServiceException {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(THING.ID, THING.TITLE, THING.TOP, THING.TAGS, THING.UPDATE_TIME, THING.FINISHED)
                .where(THING.USER_ID.eq(userId))
                .and(THING.STATUS.eq(1))
                .orderBy(THING.FINISHED.asc(), THING.TOP.desc(), THING.UPDATE_TIME.desc());
        try {
            return thingMapper.selectListByQuery(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("小记列表查询异常", EventCode.SELECT_EXCEPTION);
        }
    }
}
