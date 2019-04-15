package com.jgp.ljoa.bean;

/**
 * 项目   wfgl
 * 作者   ZWL
 * 时间   ${date}
 */
public class ExcelCellBean {


    /** The cell value. */
    private String cellValue;

    /** The column id. */
    private int columnId;

    /** The row id. */
    private int rowId;

    /** The align. */
    private String align; // 左右位置

    /** The valign. */
    private String valign; // 上下位置

    private int rightMerged;

    private int downMerged;

    /** The width. */
    private int width;

    /** The height. */
    private int height;

    /** The top position. */
    private int topPosition;

    /** The left position. */
    private int leftPosition;

    public ExcelCellBean(int rowId, int columnId) {
        this.rowId = rowId;
        this.columnId = columnId;
    }

    public String getAlign() {
        return align;
    }

    public String getCellValue() {
        return cellValue;
    }

    public int getColumnId() {
        return columnId;
    }

    public int getDownMerged() {
        return downMerged;
    }

    public int getHeight() {
        return height;
    }

    public int getLeftPosition() {
        return leftPosition;
    }

    public int getRightMerged() {
        return rightMerged;
    }

    public int getRowId() {
        return rowId;
    }

    public int getTopPosition() {
        return topPosition;
    }

    public String getValign() {
        return valign;
    }

    public int getWidth() {
        return width;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public void setCellValue(String cellValue) {
        this.cellValue = cellValue;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public void setDownMerged(int downMerged) {
        this.downMerged = downMerged;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLeftPosition(int leftPosition) {
        this.leftPosition = leftPosition;
    }

    public void setRightMerged(int rightMerged) {
        this.rightMerged = rightMerged;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public void setTopPosition(int topPosition) {
        this.topPosition = topPosition;
    }

    public void setValign(String valign) {
        this.valign = valign;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}