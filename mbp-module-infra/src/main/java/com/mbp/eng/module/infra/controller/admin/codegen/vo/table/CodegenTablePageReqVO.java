package com.mbp.eng.module.infra.controller.admin.codegen.vo.table;

import com.mbp.eng.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.mbp.eng.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 表定义分页 Request VO")
@Data
public class CodegenTablePageReqVO extends PageParam {

    @Schema(description = "表名称,模糊匹配", example = "mbp")
    private String tableName;

    @Schema(description = "表描述,模糊匹配", example = "MBP")
    private String tableComment;

    @Schema(description = "实体,模糊匹配", example = "Mbp")
    private String className;

    @Schema(description = "创建时间", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
