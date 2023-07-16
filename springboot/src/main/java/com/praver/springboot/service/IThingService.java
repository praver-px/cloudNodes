package com.praver.springboot.service;

import com.praver.springboot.entity.Thing;
import com.praver.springboot.exception.ServiceException;

import java.util.List;

public interface IThingService {

    void deleteTingById(boolean complete, int thingId, int userId, boolean isRecycleBin) throws ServiceException;

    List<Thing> getUserNormalTing(int userId) throws ServiceException;

    void topThing(boolean isTop, int thingId, int userId) throws ServiceException;
}
