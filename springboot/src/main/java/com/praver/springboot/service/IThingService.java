package com.praver.springboot.service;

import com.praver.springboot.entity.Thing;
import com.praver.springboot.exception.ServiceException;

import java.util.List;

public interface IThingService {

    void updateThing(Thing thing) throws ServiceException;

    Thing getThing(int thingId, int userId) throws ServiceException;

    void newCreateThing(Thing thing) throws ServiceException;

    void deleteTingById(boolean complete, int thingId, int userId, boolean isRecycleBin) throws ServiceException;

    List<Thing> getUserNormalTing(String search, Integer filter, int userId) throws ServiceException;

    void topThing(boolean isTop, int thingId, int userId) throws ServiceException;
}
