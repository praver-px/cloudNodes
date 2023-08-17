package com.praver.springboot.controller;

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
}
