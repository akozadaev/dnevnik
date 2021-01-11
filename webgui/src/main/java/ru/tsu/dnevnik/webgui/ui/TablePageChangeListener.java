package ru.tsu.dnevnik.webgui.ui;

import com.jensjansson.pagedtable.PagedTable;

/**
 * Created by Alexey on 19.01.2015.
 */
class TablePageChangeListener implements PagedTable.PageChangeListener {
    @Override
    public void pageChanged(PagedTable.PagedTableChangeEvent pagedTableChangeEvent) {
        final int currentPage = pagedTableChangeEvent.getCurrentPage();
        final int pageCount = pagedTableChangeEvent.getTotalAmountOfPages();

        if (pageCount == 1) {
            int size = pagedTableChangeEvent.getTable().getContainerDataSource().size();
            pagedTableChangeEvent.getTable().setPageLength(size);
        } else if (currentPage == pageCount) {
        }
    }
}