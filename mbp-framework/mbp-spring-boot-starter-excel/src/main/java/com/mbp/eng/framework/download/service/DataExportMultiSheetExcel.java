package com.mbp.eng.framework.download.service;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbp.eng.framework.download.bean.ExportView;
import com.mbp.eng.framework.download.style.CustomizeExcelExportStylerBorderImpl;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * 多sheet导出
 */
@Service
public class DataExportMultiSheetExcel {
    private static final Logger logger = LoggerFactory.getLogger(DataExportMultiSheetExcel.class);

    private static final String FILE_SUFFIX = ".xlsx";
    private ObjectMapper objectMapper = new ObjectMapper();

    public void exportExcel(List<ExportView> exportViewList,
                            HttpServletResponse response,
                            String fileName) {
        try {
            //目前默认设置为写出 xlsx 类型
            ExcelType excelType = ExcelType.XSSF;
            Workbook workbook = new XSSFWorkbook();
            ExcelExportService excelExportService = new ExcelExportService();

            for (ExportView exportView : exportViewList) {
                //设置格式
                ExportParams exportParams = exportView.getExportParams();
                exportParams.setType(excelType);
                exportParams.setStyle(CustomizeExcelExportStylerBorderImpl.class);
                logger.info(exportView.getDataList().toString());
                excelExportService.createSheet(workbook, exportParams, exportView.getCls(), exportView.getDataList());
            }
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + FILE_SUFFIX, "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            //写出
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            logger.error("export to excel error:" + e.getMessage(), e);
        }
    }
}
