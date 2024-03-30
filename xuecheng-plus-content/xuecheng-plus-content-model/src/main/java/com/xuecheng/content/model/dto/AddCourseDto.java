package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0
 * @description 添加课程dto
 */
@Data
@ApiModel(value = "AddCourseDto", description = "新增课程基本信息")
public class AddCourseDto {

    @ApiModelProperty(value = "课程名称", required = true)
    private String name;

    @ApiModelProperty(value = "适用人群", required = true)
    private String users;

    @ApiModelProperty(value = "课程标签")
    private String tags;

    @ApiModelProperty(value = "大分类", required = true)
    private String mt;

    @ApiModelProperty(value = "小分类", required = true)
    private String st;

    @ApiModelProperty(value = "课程等级", required = true)
    private String grade;

    @ApiModelProperty(value = "教学模式（普通，录播，直播等）", required = true)
    private String teachmode;

    @ApiModelProperty(value = "课程介绍")
    private String description;

    @ApiModelProperty(value = "课程图片", required = true)
    private String pic;

    @ApiModelProperty(value = "收费规则，对应数据字典", required = true)
    private String charge;

    @ApiModelProperty(value = "价格")
    private Float price;

    @ApiModelProperty(value = "原价")
    private Float originalPrice;

    @ApiModelProperty(value = "qq")
    private String qq;

    @ApiModelProperty(value = "微信")
    private String wechat;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "有效期")
    private Integer validDays;
}
