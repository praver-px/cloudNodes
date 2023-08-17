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
@Table("z_note")
public class Note {
    @Id(keyType = KeyType.Auto)
    private Integer id;
    private String title;
    private String body;
    private String content;
    @Column("u_id")
    private Integer userId;
    private Date time;
    private Date updateTime;
    private Integer top;
    @Column(onInsertValue = "1")
    private Integer status;
    @Column(onInsertValue = "1")
    private Integer type;
}


