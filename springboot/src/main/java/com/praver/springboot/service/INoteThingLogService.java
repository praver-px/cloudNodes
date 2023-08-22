package com.praver.springboot.service;

import com.praver.springboot.entity.NoteThingLog;
import com.praver.springboot.exception.ServiceException;

//日志业务
public interface INoteThingLogService {

    void addLog(NoteThingLog log, boolean isRollback) throws ServiceException;

}
