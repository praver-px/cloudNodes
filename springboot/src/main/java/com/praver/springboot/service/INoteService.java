package com.praver.springboot.service;

import com.praver.springboot.entity.Note;
import com.praver.springboot.exception.ServiceException;

import java.util.List;

public interface INoteService {
    List<Note> getUserNormalNotes(int userId) throws ServiceException;
}
