package com.praver.springboot.service.impl;

import com.praver.springboot.entity.NoteThingLog;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.exception.ServiceRollbackException;
import com.praver.springboot.mapper.INoteThingLogMapper;
import com.praver.springboot.service.INoteThingLogService;
import com.praver.springboot.util.code.EventCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

//日志业务
@Service
public class INoteThingLogServiceImpl implements INoteThingLogService {

    @Resource
    private INoteThingLogMapper noteThingLogMapper;

    @Override
    public void addLog(NoteThingLog log, boolean isRollback) throws ServiceException {
        int count;
        String message = log.getDesc() + "失败！";
        try {
            count = noteThingLogMapper.insert(log);
        } catch (Exception e) {
            e.printStackTrace();
            if (isRollback) {
                throw new ServiceRollbackException(message, EventCode.LOG_CREATE_EXCEPTION);
            } else {
                throw new ServiceException(message, EventCode.LOG_CREATE_EXCEPTION);
            }

        }

        if (count != 1) {
            if (isRollback) {
                throw new ServiceRollbackException(message, EventCode.LOG_CREATE_ERROR);
            } else {
                throw new ServiceException(message, EventCode.LOG_CREATE_ERROR);
            }
        }
    }

}
