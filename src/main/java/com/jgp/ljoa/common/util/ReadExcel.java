package com.jgp.ljoa.common.util;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class ReadExcel {
    public static void main(String[] args) {
        ReadExcel obj = new ReadExcel();
        // 此处为我创建Excel路径：E:/zhanhj/studysrc/jxl下
        File file = new File("D:/readExcel.xls");
        List excelList = obj.readExcel(file);
        System.out.println("list中的数据打印出来");
        for (int i = 0; i < excelList.size(); i++) {
            List list = (List) excelList.get(i);
            for (int j = 0; j < list.size(); j++) {
                System.out.print(list.get(j));
            }
            System.out.println();
        }

    }
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public static List readExcel(File file) {
        try {
            Workbook wb = null;
            if(file==null){
                return null;
            }
            String extString = file.getPath().substring(file.getPath().lastIndexOf("."));
            InputStream is = null;
            try {
                is = new FileInputStream(file);
                if(".xls".equals(extString)){
                    wb = new HSSFWorkbook(is);
                }else if(".xlsx".equals(extString)){
                    wb = new XSSFWorkbook(is);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                List<List> outerList=new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheetAt(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i <=sheet.getLastRowNum(); i++) {
                    List innerList=new ArrayList();
                    Row row = sheet.getRow(i);
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                       row.getCell(j).setCellType(CellType.STRING);
                        String cellinfo = row.getCell(j).getStringCellValue();
                        if(cellinfo.isEmpty()){
                            continue;
                        }
                        innerList.add(cellinfo);
                        System.out.print(cellinfo);
                    }
                    outerList.add(i, innerList);
                    System.out.println();
                }
                return outerList;
            }
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }
}