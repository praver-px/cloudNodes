package com.praver.springboot.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author praver
 * @since 2023-06-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("z_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("编号")
    @Id(keyType = KeyType.Auto)
    private Integer id;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    @Column("head_pic")
    private String headPic;

    @ApiModelProperty("用户等级【0：普通用户，1：Vip用户】")
    @Column(onInsertValue = "0")
    private Integer level;

    @ApiModelProperty("注册时间")
    private Date time;

    @ApiModelProperty("状态【0：锁定，1：正常】")
    @Column(onInsertValue = "1")
    private Integer status;


}
