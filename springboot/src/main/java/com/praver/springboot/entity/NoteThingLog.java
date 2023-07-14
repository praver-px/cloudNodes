package com.praver.springboot.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("z_note_thing_log")
public class NoteThingLog {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    //时间
    private Date time;
    //事件
    private String event;
    //描述
    private String desc;
    //用户编号
    @Column("u_id")
    private Integer userId;
    //笔记编号
    @Column("n_id")
    private Integer noteId;
    @Column("t_id")
    private Integer thingId;

}

