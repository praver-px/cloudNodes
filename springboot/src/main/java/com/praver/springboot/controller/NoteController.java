package com.praver.springboot.controller;

import cn.hutool.core.lang.Validator;
import com.praver.springboot.entity.Note;
import com.praver.springboot.entity.User;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.service.INoteService;
import com.praver.springboot.util.code.EventCode;
import com.praver.springboot.util.response.ResponseData;
import com.praver.springboot.util.validate.TokenValidateUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/note")
public class NoteController {
    @Resource
    private INoteService noteService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/list")
    public ResponseData getUserNoteList(@RequestHeader String userToken) {
        try {
            User user = TokenValidateUtil.validateUserToken(userToken, stringRedisTemplate);
            List<Note> notes = noteService.getUserNormalNotes(user.getId());
            return new ResponseData(true, "获取成功！", EventCode.SELECT_SUCCESS, notes);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }

    @GetMapping("/top")
    public ResponseData topNote(boolean isTop, int noteId, @RequestHeader String userToken) {
        try {
            User user = TokenValidateUtil.validateUserToken(userToken, stringRedisTemplate);
            if (Validator.isEmpty(isTop))
                return new ResponseData(false, "置顶状态参数为空！", EventCode.PARAM_TOP_WRONG);
            if (Validator.isEmpty(noteId))
                return new ResponseData(false, "笔记编号参数为空！", EventCode.PARAM_ID_WRONG);
            noteService.topNote(isTop, noteId, user.getId());
            return new ResponseData(true, isTop ? "置顶成功！" : "取消置顶成功！", EventCode.UPDATE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }

    @GetMapping("/delete")
    public ResponseData deleteNote(boolean complete, int noteId, boolean isRecycleBin, @RequestHeader String userToken) {
        try {
            User user = TokenValidateUtil.validateUserToken(userToken, stringRedisTemplate);
            if (Validator.isEmpty(complete))
                return new ResponseData(false, "置顶状态参数为空！", EventCode.PARAM_DELETE_COMPLETE_WRONG);
            if (Validator.isEmpty(isRecycleBin))
                return new ResponseData(false, "置顶状态参数为空！", EventCode.PARAM_DELETE_ReCYCLE_WRONG);
            if (Validator.isEmpty(noteId))
                return new ResponseData(false, "笔记编号参数为空！", EventCode.PARAM_THING_ID_WRONG);
            noteService.deleteNoteById(complete, noteId, user.getId(), isRecycleBin);
            return new ResponseData(true, complete ? "彻底删除成功！" : "删除成功！", EventCode.UPDATE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }
}
