package com.jgp.ljoa.common.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 项目   wfgl
 * 作者   ZWL
 * 时间   ${date}
 */
public class ExcelUtil {

    private Workbook workBook;
    private Sheet sheet;
    private CreationHelper createHelper;
    private Row curRow;
    private int rowIndex = 0;
    private int columnIndex = 0;

    // 普通单元格样式、必填项单元格样式、普通日期样式、必填项日期样式、表头样式
    private CellStyle normalStyle;
    private CellStyle requiredStyle;
    private CellStyle normalDateStyle;
    private CellStyle requiredDateStyle;
    private CellStyle headerStyle;

    private List<String> hiddenList = new ArrayList<String>();
    // 下拉sheet的索引
    private char beginIndex = 'A';
    private int dropColumnIndex = 0;

    // 数据显示的索引开始
    private int dataIndex;

    public void addValue() {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalStyle);
        cell.setCellValue("");
    }

    public void addValue(short color) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalStyle);
        cell.getCellStyle().setFillBackgroundColor(color);
        cell.setCellValue("");
    }

    /**
     * 普通单元格添加value
     * @param value
     */
    public void addValue(String value) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalStyle);
        cell.setCellValue(value);
    }

    public void addValue(String value, short color) {
        Cell cell = curRow.createCell(columnIndex++);
        CellStyle cellStyle = workBook.createCellStyle();
        cellStyle.setFillForegroundColor(color);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(value);
    }
    public void addValue(Long value) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalStyle);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(value);
    }

    public void addValue(Long value, short color) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalStyle);
        cell.getCellStyle().setFillBackgroundColor(color);
        cell.setCellValue(value);
    }

    /**
     * 单元格添加value，并添加批注
     * @param value
     * @param postil 批注
     */
    public void addValue(String value, String postil) {
        Cell cell = curRow.createCell(columnIndex);
        cell.setCellStyle(normalStyle);
        cell.setCellValue(new HSSFRichTextString(value));
        HSSFPatriarch p = (HSSFPatriarch) sheet.createDrawingPatriarch();
        HSSFComment comment = p.createComment(new HSSFClientAnchor(columnIndex, curRow.getRowNum(), columnIndex, curRow
                .getRowNum(), (short) 3, 3, (short) 5, 6));
        comment.setString(new HSSFRichTextString(postil));
        cell.setCellComment(comment);
        columnIndex++;
    }

    /**
     * 普通单元格添加日期型value
     * @param value
     */
    public void addValue(Date value) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalDateStyle);
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    /**
     * 普通单元格添加日期型value
     * @param value
     */
    public void addValue(Date value, short color) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalDateStyle);
        cell.getCellStyle().setFillBackgroundColor(color);
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    /**
     * 必填单元格添加value
     * @param value
     */
    public void addRequiredValue(String value) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(requiredStyle);
        cell.setCellValue(value);
    }

    /**
     * 必填单元格添加日期value
     * @param value
     */
    public void addRequiredValue(Date value) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(requiredDateStyle);
        if (value != null) {
            cell.setCellValue(value);
        }
    }

    /**
     * 合并单元格
     * @param rowFrom 开始行
     * @param rowTo 结束行
     * @param colFrom 开始列
     * @param colTo 结束列
     */
    public void mergeRegion(int rowFrom, int rowTo, int colFrom, int colTo) {
        sheet.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, colFrom, colTo));
        sheet.getRow(rowFrom).getCell(colFrom).getCellStyle().setAlignment(HorizontalAlignment.CENTER);
        sheet.getRow(rowFrom).getCell(colFrom).getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
    }

    /**
     * 创建名称管理器
     * @param nameName 名称的名字
     * @param list 字符串集合
     */
    public void createName(String nameName, List<String> list) {
        Sheet nameSheet = workBook.getSheet("下拉");
        if (nameSheet == null) {
            nameSheet = workBook.createSheet("下拉");
            nameSheet.createRow(0);
        }
        int lastRowNum = nameSheet.getLastRowNum();
        for (int i = 0; i < list.size(); i++) {
            if (i <= lastRowNum) {
                nameSheet.getRow(i).createCell(dropColumnIndex).setCellValue(list.get(i));
            } else {
                nameSheet.createRow(i).createCell(dropColumnIndex).setCellValue(list.get(i));
            }
        }
        Name dutyName = workBook.createName();
        dutyName.setNameName(nameName);
        dutyName.setRefersToFormula("'下拉'!$" + beginIndex + "$1:$" + beginIndex + "$" + list.size());
        dropColumnIndex++;
        beginIndex++;
    }

    /**
     * 添加数据有效性验证
     * @param colIndex 索引号
     * @param nameName 序列名称
     */
    public void addConstraint(int colIndex, String nameName) {
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(nameName);
        CellRangeAddressList regions = new CellRangeAddressList((short) dataIndex, (short) rowIndex - 1,
                (short) colIndex, (short) colIndex);
        HSSFDataValidation nameValidation = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(nameValidation);
    }

    /**
     * 添加数据有效性验证
     * @param nameName 序列名称
     */
    public void addConstraint(String colName, String nameName) {
        int index = hiddenList.indexOf(colName);
        this.addConstraint(index, nameName);
    }

    /**
     * 添加表头
     * @param value
     */
    public void addHeaderValue(String value) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(this.headerStyle);
        cell.setCellValue(value);
    }

    /**
     * 批量添加表头
     * @param strArray
     */
    public void addHeaderValue(String[] strArray) {
        for (String value : strArray) {
            this.addHeaderValue(value);
        }
    }

    /**
     * 批量添加表头
     */
    public void addHeaderValue(List<String> strList) {
        for (String value : strList) {
            this.addHeaderValue(value);
        }
    }

    /**
     * 添加隐藏属性
     * @param strArray
     */
    public void addHiddenValue(String[] strArray) {
        if (curRow == null) {
            curRow = sheet.createRow(rowIndex++);
            columnIndex = 0;
        }
        hiddenList.clear();
        for (String value : strArray) {
            Cell cell = curRow.createCell(columnIndex++);
            cell.setCellValue(value);
            hiddenList.add(value);
        }
        curRow.setHeight((short) 0);
        sheet.createFreezePane(0, 1, 0, 1);
        dataIndex++;
    }

    /**
     * 创建标题头
     */
    public void createTitle() {
        curRow = sheet.createRow(rowIndex++);
        curRow.setHeight((short) 500);
        this.columnIndex = 0;
        dataIndex++;
    }

    /**
     * 添加标题（默认居中对其）
     * @param title
     */
    public void addTitle(String title) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellValue(title);
        CellStyle titleStyle = workBook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = workBook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 300);
        titleStyle.setFont(font);
        cell.setCellStyle(titleStyle);
    }

    public void addDescribe(String describe) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellValue(describe);
        CellStyle titleStyle = workBook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = workBook.createFont();
        font.setFontHeight((short) 200);
        titleStyle.setFont(font);
        cell.setCellStyle(titleStyle);
    }

    // 添加函数
    public void addFunction(String functionStr) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalStyle);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula(functionStr);
    }

    // 添加函数
    public void addFunction(String functionStr, short color) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalStyle);
        cell.getCellStyle().setFillBackgroundColor(color);
        cell.setCellType(CellType.FORMULA);
        cell.setCellFormula(functionStr);
    }

    public void addNumber(Integer number) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalStyle);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(number);
    }

    public void addNumber(Integer number, short color) {
        Cell cell = curRow.createCell(columnIndex++);
        cell.setCellStyle(normalStyle);
        cell.setCellType(CellType.NUMERIC);
        cell.getCellStyle().setFillBackgroundColor(color);
        cell.setCellValue(number);
    }

    /**
     * 创建表头
     */
    public void createHeaderRow() {
        curRow = sheet.createRow(rowIndex++);
        curRow.setHeight((short) 400);
        this.columnIndex = 0;
        dataIndex++;
    }

    public void setRowHeight(int height) {
        curRow.setHeight((short) height);
    }

    /**
     * 创建普通行
     */
    public void createRow() {
        curRow = sheet.createRow(rowIndex++);
        this.columnIndex = 0;
    }

    /**
     * 设置当前的sheet页
     * @param sheetName
     */
    public void setCurrentSheet(String sheetName) {
        if (workBook.getSheet(sheetName) != null) {
            // 设置sheet为当前的指定的sheet
            sheet = workBook.getSheet(sheetName);
            // 将隐藏的值清空，获取指定sheet的隐藏值
            hiddenList.clear();
            hiddenList = this.getRowHead();
            // 切换sheet页，将焦点移到最后一行，第一列
            rowIndex = sheet.getLastRowNum();
            columnIndex = 0;
        }
    }

    private List<String> getRowHead() {
        Row headRow = sheet.getRow(0);
        if (headRow == null) {
            return null;
        }
        Iterator<Cell> cellIter = headRow.cellIterator();
        List<String> hiddenhead = new ArrayList<String>();// 获取第一列的所有字符
        while (cellIter.hasNext()) {
            hiddenhead.add(cellIter.next().toString());
        }
        return hiddenhead;
    }

    /**
     * 创建sheet页
     * @param sheetName
     */
    public void createSheet(String sheetName) {
        sheet = workBook.createSheet(sheetName);
        rowIndex = 0;
        columnIndex = 0;
    }

    /**
     * 导出Excel文件流
     * @return
     * @
     */
    public InputStream write() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try {
            workBook.write(byteStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(byteStream.toByteArray());
    }

    // 设置列宽（1个汉字的宽度）
    public void setColumnWidth(int[] array) {
        for (int i = 0; i < array.length; i++) {
            sheet.setColumnWidth(i, array[i] * 512);
        }
    }

    public ExcelUtil(String sheetName) {
        workBook = new HSSFWorkbook();
        sheet = workBook.createSheet(sheetName);
        createHelper = workBook.getCreationHelper();
        initStyle();
    }

    // 初始化样式
    private void initStyle() {
        // 普通单元格样式
        normalStyle = workBook.createCellStyle();
        normalStyle.setBorderBottom(BorderStyle.THIN);
        normalStyle.setBorderLeft(BorderStyle.THIN);
        normalStyle.setBorderRight(BorderStyle.THIN);
        normalStyle.setBorderTop(BorderStyle.THIN);
        normalStyle.setWrapText(true);
        normalStyle.setDataFormat(createHelper.createDataFormat().getFormat("@"));

        // 必填项单元格的样式
        requiredStyle = workBook.createCellStyle();
        requiredStyle.cloneStyleFrom(normalStyle);
        requiredStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        requiredStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex());
        requiredStyle.setDataFormat(createHelper.createDataFormat().getFormat("@"));

        // 日期样式
        normalDateStyle = workBook.createCellStyle();
        normalDateStyle.cloneStyleFrom(normalStyle);
        normalDateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        // 必填项日期的样式
        requiredDateStyle = workBook.createCellStyle();
        requiredDateStyle.cloneStyleFrom(normalDateStyle);
        requiredDateStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        requiredDateStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex());

        // 表头样式
        headerStyle = workBook.createCellStyle();
        headerStyle.cloneStyleFrom(normalStyle);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        Font font = workBook.createFont();
        font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
        font.setBold(true);
        headerStyle.setFont(font);

    }

    /**
     * 获取自定义样式，用户自己设置规则
     * @return
     */
//    public CellStyle getCustomCellStyle() {
//        CellStyle customStyle = workBook.createCellStyle();
//        customStyle.setBorderBottom(CellStyle.BORDER_THIN);
//        customStyle.setBorderLeft(CellStyle.BORDER_THIN);
//        customStyle.setBorderRight(CellStyle.BORDER_THIN);
//        customStyle.setBorderTop(CellStyle.BORDER_THIN);
//        customStyle.setWrapText(true);
//        return customStyle;
//    }

    /**
     * 获取字体
     * @return
     */
//    public Font getCustomFont() {
//        Font font = workBook.createFont();
//        font.setColor(HSSFColor.WHITE.index);
//        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//        return font;
//    }

    /**
     * 给单元格设置样式
     * @param style
     */
    public void setCurrentCellStyle(CellStyle style) {
        curRow.getCell(columnIndex - 1).setCellStyle(style);
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex - 1;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

}