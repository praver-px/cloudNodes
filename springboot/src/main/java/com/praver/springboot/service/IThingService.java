package com.praver.springboot.service;

import com.praver.springboot.entity.Thing;
import com.praver.springboot.exception.ServiceException;

import java.util.List;

public interface IThingService {

    List<Thing> getUserNormalTing(int userId) throws ServiceException;

    void topThing(boolean isTop, int thingId, int userId) throws ServiceException;
}
