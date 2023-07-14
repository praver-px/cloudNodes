package com.praver.springboot.service.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.praver.springboot.entity.Thing;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.exception.ServiceRollbackException;
import com.praver.springboot.mapper.IThingMapper;
import com.praver.springboot.service.IThingService;
import com.praver.springboot.util.code.EventCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static com.praver.springboot.entity.table.Tables.THING;

@Service
@Transactional(rollbackFor = ServiceRollbackException.class)
public class ThingServiceImpl implements IThingService {

    @Resource
    private IThingMapper thingMapper;

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
