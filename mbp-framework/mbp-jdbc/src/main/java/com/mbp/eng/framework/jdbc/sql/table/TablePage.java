package com.mbp.eng.framework.jdbc.sql.table;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Vector;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class TablePage implements Serializable {
    protected int currentPage = 0;

    private int linesPerPage = 0;

    private int totalLines = 0;

    private int selectedRowNo = -1;

    private Vector func = new Vector();

    private boolean scrollButtons = false;

    private boolean setLayoutButton = false;

    private boolean addButton = false;

    private boolean addButtonIsEnabled = false;

    public TablePage() {
    }

    public TablePage(Table table, int linesPerPage) {
        this.totalLines = table.getRowCount();
        this.linesPerPage = linesPerPage;
    }

    public boolean addButtonIsEnabled() {
        return this.addButtonIsEnabled;
    }

    public boolean addButtonIsVisible() {
        return this.addButton;
    }

    public void addFunc(String funcLabel, String funcAction) {
        String[] funcAct = {funcLabel, funcAction};
        this.func.addElement(funcAct);
    }

    public void addNewRow() {
        this.totalLines += 1;

        this.currentPage = (getTotalPages() - 1);

        this.selectedRowNo = (this.totalLines - 1);
    }

    public void cancelAdd() {
        this.totalLines -= 1;
        this.selectedRowNo = -1;
    }

    public void cancelEdit() {
        this.selectedRowNo = -1;
    }

    public void clearAllFunc() {
        this.addButton = false;
        this.addButtonIsEnabled = false;
        this.scrollButtons = false;
        this.setLayoutButton = false;
        this.func = new Vector();
    }

    public void deleteRowInCurrentPage() {
        int linesInPage = getLinesInCurrentPage();

        this.totalLines -= 1;
        if ((linesInPage == 1) && (this.currentPage > 0))
            this.currentPage -= 1;
    }

    public void editRowAt(int rowNo) {
        gotoSelectedPage(rowNo);
        this.selectedRowNo = rowNo;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public int getEditedRowNo() {
        return this.selectedRowNo;
    }

    public String getFuncActionAt(int i) {
        String[] funcAct = (String[]) this.func.elementAt(i);
        return funcAct[1];
    }

    public int getFuncCount() {
        return this.func.size();
    }

    public String getFuncLabelAt(int i) {
        String[] funcAct = (String[]) this.func.elementAt(i);
        return funcAct[0];
    }

    public int getLinesInCurrentPage() {
        if (this.totalLines == 0) {
            return 0;
        }

        if (this.currentPage == getTotalPages() - 1) {
            BigInteger m = BigInteger.valueOf(this.totalLines);

            BigInteger e = BigInteger.valueOf(this.linesPerPage);

            BigInteger[] x = m.divideAndRemainder(e);

            if (x[1].intValue() == 0) {
                return this.linesPerPage;
            }
            return x[1].intValue();
        }

        return this.linesPerPage;
    }

    public int getLinesInLastPage() {
        if (this.totalLines == 0) {
            return 0;
        }

        BigInteger m = BigInteger.valueOf(this.totalLines);

        BigInteger e = BigInteger.valueOf(this.linesPerPage);

        BigInteger[] x = m.divideAndRemainder(e);

        if (x[1].intValue() == 0) {
            return this.linesPerPage;
        }
        return x[1].intValue();
    }

    public int getLinesPerPage() {
        return this.linesPerPage;
    }

    public int getSelectedRowNo() {
        return this.selectedRowNo;
    }

    public int getStartIndex() {
        return (this.currentPage * this.linesPerPage);
    }

    public int getTotalLines() {
        return this.totalLines;
    }

    public int getTotalPages() {
        if (this.totalLines == 0) {
            return 0;
        }
        BigInteger m = BigInteger.valueOf(this.totalLines);

        BigInteger e = BigInteger.valueOf(this.linesPerPage);

        BigInteger[] x = m.divideAndRemainder(e);

        if (x[1].intValue() == 0) {
            return x[0].intValue();
        }
        return (x[0].intValue() + 1);
    }

    public void gotoBeginPage() {
        this.currentPage = 0;
    }

    public void gotoEndPage() {
        this.currentPage = (getTotalPages() - 1);
    }

    public void gotoNextPage() {
        if (this.currentPage < getTotalPages() - 1)
            this.currentPage += 1;
    }

    public void gotoPageAt(int newCurrentPage) {
        this.currentPage = newCurrentPage;
    }

    public void gotoPrevPage() {
        if (this.currentPage > 0)
            this.currentPage -= 1;
    }

    public void gotoSelectedPage(int newSelectedRowNo) {
        if (this.totalLines == 0) {
            return;
        }
        if (newSelectedRowNo > this.totalLines - 1) {
            this.currentPage = (getTotalPages() - 1);
            return;
        }

        BigInteger m = BigInteger.valueOf(newSelectedRowNo);

        BigInteger e = BigInteger.valueOf(this.linesPerPage);

        BigInteger[] x = m.divideAndRemainder(e);

        this.currentPage = x[0].intValue();
    }

    public boolean nextPageIsEnabled() {
        return ((getTotalPages() > 1) && (this.currentPage != getTotalPages() - 1));
    }

    public boolean prevPageIsEnabled() {
        return ((getTotalPages() > 1) && (this.currentPage != 0));
    }

    public void reset(Table table) {
        this.totalLines = table.getRowCount();
        this.currentPage = 0;

        this.selectedRowNo = -1;
    }

    public void saveAdd() {
        this.selectedRowNo = -1;
    }

    public void saveEdit() {
        this.selectedRowNo = -1;
    }

    public boolean scrollButtons() {
        return this.scrollButtons;
    }

    public void setAddButton(boolean show) {
        this.addButton = show;
        this.addButtonIsEnabled = true;
    }

    public void setAddButton(boolean show, boolean addButtonIsEnabled) {
        this.addButton = show;
        this.addButtonIsEnabled = addButtonIsEnabled;
    }

    public void setCurrentPage(int newCurrentPage) {
        int totalPage = getTotalPages();
        if ((newCurrentPage > 0) && (newCurrentPage > totalPage - 1))
            this.currentPage = (totalPage - 1);
        else
            this.currentPage = newCurrentPage;
    }

    public boolean setLayout() {
        return this.setLayoutButton;
    }

    public void setLayoutButton(boolean show) {
        this.setLayoutButton = show;
    }

    public void setLinesPerPage(int newLinesPerPage) {
        this.linesPerPage = newLinesPerPage;
    }

    public void setScrollButtons(boolean show) {
        this.scrollButtons = show;
    }

    public void setSelectedRowNo(int newSelectedRowNo) {
        this.selectedRowNo = newSelectedRowNo;
    }

    public void setTotalLines(int newTotalLines) {
        this.totalLines = newTotalLines;
    }

    public int getEndIndex() {
        if (getCurrentPage() >= getTotalPages()) {
            return getTotalLines();
        }
        return (getStartIndex() + getLinesPerPage());
    }
}
