package com.mbp.eng.module.system.controller.admin.dept.vo.post;

import com.mbp.eng.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 岗位分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PostPageReqVO extends PageParam {

    @Schema(description = "岗位编码,模糊匹配", example = "mbp")
    private String code;

    @Schema(description = "岗位名称,模糊匹配", example = "MBP")
    private String name;

    @Schema(description = "展示状态,参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

}
