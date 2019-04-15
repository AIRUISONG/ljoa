package com.jgp.ljoa.common.util;


import com.jgp.ljoa.bean.ExcelCellBean;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目   wfgl
 * 作者   ZWL
 * 时间   ${date}
 */
public class ReadExcelUtil {


    private Sheet sheet;
    private Workbook workbook;
    private CreationHelper createHelper;
    private int nowrow = 0;// 当前行
    private int nowcolumn = 0;
    private Row row;
    private List<String> head;
    private CellStyle style = null;// 主要类型,边框

    /**
     * 设置宽度,1个汉字的宽度
     * @param array
     */
    public void setWidth(int[] array) {
        for (int i = 0; i < array.length; i++) {
            sheet.setColumnWidth(i, array[i] * 512);
        }
    }

    /**
     * 导入excel用,返回行数
     * @param file
     * @
     */
    public int importExcel(File file) {
        workbook = this.openFile(file);
        sheet = workbook.getSheetAt(0);
        createHelper = workbook.getCreationHelper();
        row = sheet.getRow(0);
        head = this.getRowHead();
        return sheet.getLastRowNum();
    }

    /**
     * sheetName是sheet,显示名
     * @param file
     * @param sheetName
     * @return
     * @
     */
    public int importExcel(File file, String sheetName) {
        workbook = this.openFile(file);
        sheet = workbook.getSheet(sheetName);
        createHelper = workbook.getCreationHelper();
        row = sheet.getRow(0);
        head = this.getRowHead();
        return sheet.getLastRowNum();
    }

    /**
     * 导出excel
     * @
     */
    public Row exportExcel(String sheetName) {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
        createHelper = workbook.getCreationHelper();
        row = sheet.createRow(0);
        style = workbook.createCellStyle();
        initBorder(style);
        return row;
    }

    /**
     * 导出excel
     * @
     */
    public Row exportExcel(List<String> sheetNames, Map<String, List<List<String>>> data) {
        workbook = new HSSFWorkbook();
        for (String sheetName : sheetNames) {
            Sheet sheet = workbook.createSheet(sheetName);
            List<List<String>> sheetData = data.get(sheetName);
            int rowNum = 0;
            if (sheetData == null)
                continue;
            for (List<String> rowData : sheetData) {
                Row row = sheet.createRow(rowNum);
                int cellNum = 0;
                for (String cellData : rowData) {
                    Cell cell = row.createCell(cellNum);
                    cell.setCellValue(cellData);
                    cellNum++;
                }
                rowNum++;
            }
        }
        return row;
    }

    /**
     * 获得excel数据
     * 传入文件路径及要读取的行数
     * 1、判断文件格式是否正确
     * 2、将数据拼成List<List<String>>类型，并返回
     * @return
     * @
     */
    public List<List<String>> getExcelData(File file, int rowNum) {
        int totalNum = this.importExcel(file) + 1;// 返回的是最后一行的序列（从0开始），要加1
        if (rowNum == 0 || totalNum < rowNum) {
            rowNum = totalNum;
        }
        List<List<String>> resultList = new ArrayList<List<String>>();
        for (int i = 0; i < rowNum; i++) {
            List<String> list = new ArrayList<String>();
            row = sheet.getRow(i);
            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                    // 日期格式需要单独处理，否则读出来的可能为yyyy年mm月dd日或其他格式
                    // format为yyyy-MM-dd格式
                    Date date = cell.getDateCellValue();
                    SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd");
                    list.add(from.format(date));
                } else if (cell.getCellType() == CellType.STRING) {
                    list.add(cell.getStringCellValue());
                } else if (cell.getCellType() == CellType.BLANK) {
                    list.add(cell.getStringCellValue());
                } else {
                    try {
                        if (cell.toString() != null) {
                            String temp = cell.toString();
                            if (Integer.valueOf(temp.substring(temp.indexOf(".") + 1)) == 0) {
                                temp = temp.substring(0, temp.indexOf("."));
                            }
                            list.add(temp);
                        } else {
                            list.add(cell.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        list.add(cell.toString());
                    }
                }
            }
            resultList.add(list);
        }
        return resultList;
    }

    public List<List<String>> listAll() {
        List<List<String>> resultList = new ArrayList<List<String>>();
        for (Row myrow : sheet) {
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < myrow.getLastCellNum() + 1; i++) {

                Cell mycell = myrow.getCell(i);
                if (mycell == null) {
                    list.add("");
                } else if (mycell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(mycell)) {
                    // 日期格式需要单独处理，否则读出来的可能为yyyy年mm月dd日或其他格式
                    // format为yyyy-MM-dd格式
                    Date date = mycell.getDateCellValue();
                    SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd");
                    list.add(from.format(date));
                } else {
                    list.add(mycell.toString());
                }

            }
            resultList.add(list);
        }
        return resultList;
    }

    public List<List<ExcelCellBean>> listCellBean() {
        List<List<ExcelCellBean>> resultList = new ArrayList<List<ExcelCellBean>>();
        int rowNum = sheet.getLastRowNum();
        for (int rowId = 0; rowId <= rowNum; rowId++) {
            Row myrow = sheet.getRow(rowId);
            List<ExcelCellBean> list = new ArrayList<ExcelCellBean>();
            for (int columnId = 0; columnId < myrow.getLastCellNum() + 1; columnId++) {
                ExcelCellBean ecb = new ExcelCellBean(rowId, columnId);
                Cell mycell = myrow.getCell(columnId);
                if (mycell == null) {
                    ecb.setCellValue("");
                    continue;
                }

                ecb.setWidth(sheet.getColumnWidth(columnId));
                ecb.setHeight(myrow.getHeight());
                ecb.setAlign(convertAlignToHtml(mycell.getCellStyle().getAlignment()));
                ecb.setValign(convertValignToHtml(mycell.getCellStyle().getVerticalAlignment()));

                if (mycell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(mycell)) {
                    // 日期格式需要单独处理，否则读出来的可能为yyyy年mm月dd日或其他格式
                    // format为yyyy-MM-dd格式
                    Date date = mycell.getDateCellValue();
                    SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd");
                    ecb.setCellValue(from.format(date));
                } else {
                    ecb.setCellValue(mycell.toString());
                }
                list.add(ecb);
            }
            resultList.add(list);
        }
        String delList = ";";
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            try {
                CellRangeAddress region = sheet.getMergedRegion(i);
                int firstRow = region.getFirstRow();
                int firstColumn = region.getFirstColumn();
                int lastRow = region.getLastRow();
                int lastColumn = region.getLastColumn();
                resultList.get(firstRow).get(firstColumn).setRightMerged(lastColumn - firstColumn + 1);
                resultList.get(firstRow).get(firstColumn).setDownMerged(lastRow - firstRow + 1);
                for (int delRow = lastRow; delRow >= firstRow; delRow--) {
                    for (int delColumn = lastColumn; delColumn >= firstColumn; delColumn--) {
                        if (delList.indexOf(delRow + "," + delColumn + ";") < 0
                                && ((delRow != firstRow || delColumn != firstColumn))) {
                            delList += delRow + "," + delColumn + ";";
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        for (int delRow = resultList.size() - 1; delRow >= 0; delRow--) {
            for (int delColumn = resultList.get(delRow).size() - 1; delColumn >= 0; delColumn--) {
                if (delList.indexOf(";" + delRow + "," + delColumn + ";") >= 0)
                    resultList.get(delRow).remove(delColumn);
            }
        }
        return resultList;
    }

    /**
     * 写输出,最后用
     * @return
     * @
     */
    public InputStream write() {
        ByteArrayOutputStream byteStream = null;
        InputStream result = null;
        try {
            byteStream = new ByteArrayOutputStream();
            try {
                workbook.write(byteStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = new ByteArrayInputStream(byteStream.toByteArray());
            return result;
        } finally {

        }
    }

    /**
     * 重新单元格指定位置
     * @param nowrow
     * @param nowcolumn
     */
    public void setPosition(int nowrow, int nowcolumn) {
        this.nowcolumn = nowcolumn;
        this.nowrow = nowrow;
    }

    /**
     * 移到下一行,列开头(读取)
     */
    public Row nextRow() {
        row = sheet.getRow(++nowrow);
        this.nowcolumn = 0;
        return row;
    }

    /**
     * 移到下i行,列开头(读取)
     */
    public Row nextRow(int i) {
        nowrow = nowrow + i;
        row = sheet.getRow(nowrow);
        this.nowcolumn = 0;
        return row;
    }

    /**
     *
     * 创建一新行(写入用)
     * @return
     */
    public Row createRow() {
        row = sheet.createRow(++nowrow);
        this.nowcolumn = 0;
        return row;
    }

    /**
     * 跳一个单元格
     */
    public void skipCell() {
        this.nowcolumn = nowcolumn + 1;
    }

    /**
     * 时间类型与文本不能同时存在
     * @return 时间类型的style(yyyy-MM-dd)
     * @
     */
    public short getDateStyle() {
        return createHelper.createDataFormat().getFormat("yyyy-MM-dd");

    }

    /**
     * 设置输出文本格式,与日期不能同时存在
     * @return
     * @
     */
    public short getTextStyle() {
        return createHelper.createDataFormat().getFormat("@");
    }

    /**
     * 灰色背景
     * @return
     * @
     */
    public void setGrayGround() {
        short groundcolor = HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex();
        style.setFillForegroundColor(groundcolor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    public void removeGrayGround() {
        short groundcolor = HSSFColor.HSSFColorPredefined.WHITE.getIndex();
        style.setFillForegroundColor(groundcolor);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 添加数字的字符文本
     * @param value
     * @
     */
    public Cell addValue(String value) {
        CellStyle mystyle = workbook.createCellStyle();
        mystyle.cloneStyleFrom(style);// 主要类型
        mystyle.setDataFormat(createHelper.createDataFormat().getFormat("@"));
        Cell cell = this.addValue(value, mystyle);
        return cell;
    }

    /**
     * 添加指定类型的,非日期
     * @param value
     * @
     */
    public Cell addValue(Object value, CellStyle style) {

        Cell cell = row.createCell(nowcolumn++);
        cell.setCellStyle(style);
        if (value instanceof String) {
            cell.setCellValue((String) value);
        }
        if (value instanceof Double) {
            cell.setCellValue((Double) value);
        }
        if (value instanceof Date) {
            if (value != null)
                cell.setCellValue((Date) value);
        }
        return cell;
    }

    /**
     * Workbook读取excel工厂,可以判断扩展名选择2007或者2003的excelapi
     *
     * @return Sheet
     * @
     */
    public Workbook openFile(File file){
        InputStream inp = null;
        try {
            if (!file.exists() || !file.isFile()) {
                return null;
            }
            // 判断扩展名是否正确 -- xlsx或者xls的
            if (!"xls".equalsIgnoreCase(getExtName(file.getName()))
                    && !"xlsx".equalsIgnoreCase(getExtName(file.getName()))) {
                return null;
            }
            try {
                inp = new FileInputStream(file);
                return WorkbookFactory.create(inp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            if (inp != null) {
                try {
                    inp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取第一行所有字符串集合.表头验证
     * @return
     * @
     */
    public List<String> getRowHead() {
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
     * 当前行列的位置
     * 读取的单元格本身必须是文本类型的数字或者是字符
     * @return
     * @
     */
    public Long getLong(String strindex) {
        int numIndex = head.indexOf(strindex);
        String strLong = row.getCell(numIndex).toString();
        if (strLong == null || "".equals(strLong.trim())) {
            return null;
        }
        return Long.valueOf(strLong);
    }

    public Timestamp getDate(String strindex) {
        int numIndex = head.indexOf(strindex);
        Cell mycell = row.getCell(numIndex);
        Date utilDate = null;
        if (mycell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(mycell)) {
            // 日期格式需要单独处理，否则读不出来yyyy年mm月dd日或其他格式
            // format为yyyy-MM-dd格式
            utilDate = row.getCell(numIndex).getDateCellValue();
            if (utilDate == null) {
                return null;
            }
        } else {
            try {
                String datestr = row.getCell(numIndex).getStringCellValue();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                utilDate = dateFormat.parse(datestr);
            } catch (Exception e) {
                return null;
            }
        }
        return new java.sql.Timestamp(utilDate.getTime());
    }

    public String getStr(String strindex) {
        int numIndex = head.indexOf(strindex);
        return getStrByNumIndex(numIndex);

    }

    public String getStrByNumIndex(int numIndex) {
        String result = null;
        if (row != null) {
            Cell cell = row.getCell(numIndex,Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell == null) {
                return "";
            }
            // 如果是公式，那么需要处理，公式结算结果必须为数字，否则抛出异常
            if (cell.getCellType() == CellType.FORMULA) {
                result = cell.getNumericCellValue() + "";
            } else if (cell.getCellType() == CellType.STRING) {// 单元格是字符
                result = row.getCell(numIndex) == null ? null : row.getCell(numIndex).getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC) {// 单元格是数字
                cell.setCellType(CellType.STRING);
                result = row.getCell(numIndex) == null ? null : row.getCell(numIndex).getStringCellValue() + "";
//                if (result != null && result.indexOf(".0") == result.length() - 2) {
//                    result = result.substring(0, result.length() - 2);
//                }
            } else {
                result = row.getCell(numIndex) == null ? null : row.getCell(numIndex).toString();
            }
        }
        return result;
    }

    /**
     * 获取当前行指定列日期值
     * @return
     * @throws Exception
     */
    public Timestamp getDate(int numIndex) throws Exception {
        Cell mycell = row.getCell(numIndex);
        Date utilDate = null;
        if (mycell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(mycell)) {
            // 日期格式需要单独处理，否则读不出来yyyy年mm月dd日或其他格式
            // format为yyyy-MM-dd格式
            utilDate = row.getCell(numIndex).getDateCellValue();
            if (utilDate == null) {
                return null;
            }
        } else {
            try {
                String datestr = row.getCell(numIndex).getStringCellValue();
                if (datestr.length() == 7 && datestr.indexOf("-") > 0)
                    datestr = datestr + "-01";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                utilDate = dateFormat.parse(datestr);
            } catch (Exception e) {
                return null;
            }
        }
        return new java.sql.Timestamp(utilDate.getTime());
    }

    /**
     * 读取指定单元格数据
     * @param curRow 当前行对象
     * @param dataRow  需要读取的行对象
     * @param numIndex 需要读取的列
     * @return
     */
    public String getStrByRow(int curRow, int dataRow, int numIndex) {
        row = sheet.getRow(dataRow);
        String result = this.getStrByNumIndex(numIndex);
        row = sheet.getRow(curRow);// 重新指定当前行为读取行
        return result;
    }

    // 获取标题头中的一个名字下对应多个列
    public List<String> getStrLs(String strindex) {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < head.size(); i++) {
            if (strindex.equals(head.get(i))) {
                result.add(getStrByNumIndex(i));
            }
        }
        return result;
    }

    /**
     * 自动调整显示宽度
     * @
     */
    public void autoSize() {
        if (sheet == null || sheet.getRow(0) == null || sheet.getRow(0).getLastCellNum() == 0) {
            return;
        }
        for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    public void setFrozen(int rowNum) {
        sheet.createFreezePane(0, rowNum, 0, rowNum);
    }

    // 单元格黑框
    private void initBorder(CellStyle strStyle) {
        style.setBorderBottom(strStyle.getBorderBottom());
        style.setBorderLeft(strStyle.getBorderLeft());
        style.setBorderRight(strStyle.getBorderRight());
        style.setBorderTop(strStyle.getBorderTop());
    }

    /**
     * @return the sheet
     */
    public Sheet getSheet() {
        return sheet;
    }

    /**
     * @param sheet the sheet to set
     */
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    /**
     * 设置head--虽然可以在加载excel的时候获取到head，但是有的没有head设置，要单独设置head
     */
    public void setHead(List<String> head) {
        this.head = head;
    }

    private String convertAlignToHtml(HorizontalAlignment alignment) {
        String align = "left";
        switch (alignment) {
            case LEFT:
                align = "left";
                break;
            case CENTER:
                align = "center";
                break;
            case RIGHT:
                align = "right";
                break;
            default:
                break;
        }
        return align;
    }

    private String convertValignToHtml(VerticalAlignment valignment) {
        String align = "left";
        switch (valignment) {
            case TOP:
                align = "top";
                break;
            case CENTER:
                align = "middle";
                break;
            case BOTTOM:
                align = "bottom";
                break;
            default:
                break;
        }
        return align;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public static String getExtName(String fileName) {
        if ((null != fileName) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length() - 1))) {
                return fileName.substring(dot + 1);
            }
        }
        return fileName;
    }
}