/*
 * Copyright (c) 2003 Sun Microsystems, Inc. All  Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduct the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT
 * BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT
 * OF OR RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN
 * IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that Software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility.
 */

/*
 * @(#)TableSorter.java	1.12 03/01/23
 */

/**
 * A sorter for TableModels. The sorter has a model (conforming to TableModel) 
 * and itself implements TableModel. TableSorter does not store or copy 
 * the data in the TableModel, instead it maintains an array of 
 * integers which it keeps the same size as the number of rows in its 
 * model. When the model changes it notifies the sorter that something 
 * has changed eg. "rowsAdded" so that its internal array of integers 
 * can be reallocated. As requests are made of the sorter (like 
 * getValueAt(row, col) it redirects them to its model via the mapping 
 * array. That way the TableSorter appears to hold another copy of the table 
 * with the rows in a different order. The sorting algorthm used is stable 
 * which means that it does not move around rows when its comparison 
 * function returns 0 to denote that they are equivalent. 
 *
 * @version 1.12 01/23/03
 * @author Philip Milne
 */

package ceefee.gui;


import java.util.*;

import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;

// Imports for picking up mouse events from the JTable. 

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.InputEvent;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.ImageIcon;
import java.io.File;

import javax.swing.JLabel;

import javax.swing.ImageIcon;



public class TableSorter extends TableMap
{
    private int             indexes[];
    private Vector          sortingColumns = new Vector();
    private boolean         ascending = true;
    private int compares;
    private int previousColumn;
 
    private ceefee.display.general displayClassInstance;
    

    

   
    public TableSorter( final TableModel model, final ceefee.display.general _displayClassInstance )
    {
        displayClassInstance=_displayClassInstance;
        setModel(model);
    }
        
    public void setModel(final TableModel model) {
        super.setModel(model); 
        reallocateIndexes(); 
    }

    boolean isFileSize( Object object ) {
        final String whimsicalString=object.toString();
        if ( whimsicalString.length()>3 && whimsicalString.substring(whimsicalString.length()-3).equals(" KB") ) {
            try {
                Double.parseDouble(whimsicalString.substring(0,whimsicalString.indexOf(" KB")));
            } catch ( java.lang.NumberFormatException nfe ) {
                return false;
            }

            return true;
        } else {

            return false;
        }

    }
    int compareFileSize( final Object fileSize1, final Object fileSize2 ) {
        String fss1=fileSize1.toString();
        String fss2=fileSize2.toString();
        try {
            double fsi1=Double.parseDouble(fss1.substring(0,fss1.indexOf(" KB")));
            double fsi2=Double.parseDouble(fss2.substring(0,fss2.indexOf(" KB")));

            if ( fsi1<fsi2 )
                return -1;
            else if ( fsi1>fsi2 )
                return 1;
            else
                return 0;
            
        } catch ( java.lang.NumberFormatException nfe ) {
            
            return fss1.compareTo(fss2);
        }
        
    }
    
    private int compareRowsByColumn(final int row1, final int row2, final int column)
    {
        TableModel data = model;

        // Check for nulls
        Object o1 = data.getValueAt(row1, column);
        Object o2 = data.getValueAt(row2, column); 

        // If both values are null return 0
        if (o1 == null && o2 == null) {
            return 0; 
        }
        else if (o1 == null) { // Define null less than everything. 
            return -1; 
        } 
        else if (o2 == null) { 
            return 1; 
        }

/* We copy all returned values from the getValue call in case
an optimised model is reusing one object to return many values.
The Number subclasses in the JDK are immutable and so will not be used in 
this way but other subclasses of Number might want to do this to save 
space and avoid unnecessary heap allocation. 
*/

        if ( o1.getClass().equals(java.lang.Number.class)==true && o2.getClass().equals(java.lang.Number.class)==true )
            {
                Number n1 = (Number)data.getValueAt(row1, column);
                double d1 = n1.doubleValue();
                Number n2 = (Number)data.getValueAt(row2, column);
                double d2 = n2.doubleValue();

                if (d1 < d2)
                    return -1;
                else if (d1 > d2)
                    return 1;
                else
                    return 0;
            }
        else if ( o1.getClass().equals(java.util.Date.class)==true && o2.getClass().equals(java.util.Date.class)==true )
            {
                Date d1 = (Date)data.getValueAt(row1, column);
                long n1 = d1.getTime();
                Date d2 = (Date)data.getValueAt(row2, column);
                long n2 = d2.getTime();

                if (n1 < n2)
                    return -1;
                else if (n1 > n2)
                    return 1;
                else return 0;
            }
        else if ( isFileSize(o1) && isFileSize(o2) )
            {
                return compareFileSize(o1,o2);
            }
        else if ( o1.getClass().equals(java.lang.String.class)==true && o2.getClass().equals(java.lang.String.class)==true )
            {
                String s1 = (String)data.getValueAt(row1, column);
                String s2    = (String)data.getValueAt(row2, column);
                int result = s1.compareTo(s2);

                if (result < 0)
                    return -1;
                else if (result > 0)
                    return 1;
                else return 0;
            }
        else if ( o1.getClass().equals(Boolean.class)==true && o2.getClass().equals(Boolean.class)==true )
            {
                Boolean bool1 = (Boolean)data.getValueAt(row1, column);
                boolean b1 = bool1.booleanValue();
                Boolean bool2 = (Boolean)data.getValueAt(row2, column);
                boolean b2 = bool2.booleanValue();

                if (b1 == b2)
                    return 0;
                else if (b1) // Define false < true
                    return 1;
                else
                    return -1;
            }
        else
            {
                Object v1 = data.getValueAt(row1, column);
                String s1 = v1.toString();
                Object v2 = data.getValueAt(row2, column);
                String s2 = v2.toString();
                int result = s1.compareTo(s2);

                if (result < 0)
                    return -1;
                else if (result > 0)
                    return 1;
                else return 0;
            }
    }



    void  reallocateIndexes()
    {
        int rowCount = model.getRowCount();

        // Set up a new array of indexes with the right number of elements
        // for the new data model.
        indexes = new int[rowCount];

        // Initialise with the identity mapping.
        for(int row = 0; row < rowCount; row++)
            indexes[row] = row;
    }

    public void tableChanged(TableModelEvent e)
    {
        reallocateIndexes();
        super.tableChanged(e);
    }

    public void  sort(Object sender)
    {
        compares = 0;
        // n2sort();
        // qsort(0, indexes.length-1);
        shuttlesort((int[])indexes.clone(), indexes, 0, indexes.length);
    }

    public void n2sort() {
        for(int i = 0; i < getRowCount(); i++) {
            for(int j = i+1; j < getRowCount(); j++) {
                if (compare(indexes[i], indexes[j]) == -1) {
                    swap(i, j);
                }
            }
        }
    }
    int compare(int row1, int row2)
    {
        compares++;
        for(int level = 0; level < sortingColumns.size(); level++)
            {
                Integer column = (Integer)sortingColumns.elementAt(level);
                int result = compareRowsByColumn(row1, row2, column.intValue());
                
                if (result != 0){
                    return ascending?result:-result;
                }
                else {
                    return 1;
                }
                                    
            }
        return 0;
    }
    // This is a home-grown implementation which we have not had time
    // to research - it may perform poorly in some circumstances. It
    // requires twice the space of an in-place algorithm and makes
    // NlogN assigments shuttling the values between the two
    // arrays. The number of compares appears to vary between N-1 and
    // NlogN depending on the initial order but the main reason for
    // using it here is that, unlike qsort, it is stable.
    public void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high)/2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);

        int p = low;
        int q = middle;

        /* This is an optional short-cut; at each recursive call,
        check to see if the elements in this subset are already
        ordered.  If so, no further comparisons are needed; the
        sub-array can just be copied.  The array must be copied rather
        than assigned otherwise sister calls in the recursion might
        get out of sinc.  When the number of elements is three they
        are partitioned so that the first set, [low, mid), has one
        element and and the second, [mid, high), has two. We skip the
        optimisation when the number of elements is three or less as
        the first compare in the normal merge will produce the same
        sequence of steps. This optimisation seems to be worthwhile
        for partially ordered lists but some analysis is needed to
        find out how the performance drops to Nlog(N) as the initial
        order diminishes - it may drop very quickly.  */

        if (high - low >= 4 && compare(from[middle-1], from[middle]) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }

        // A normal merge. 

        for(int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            }
            else {
                to[i] = from[q++];
            }
        }
    }

    public void swap(int i, int j) {
        int tmp = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = tmp;
    }

    // The mapping only affects the contents of the data rows.
    // Pass all requests to these rows through the mapping array: "indexes".

    public Object getValueAt(int aRow, int aColumn)
    {
        if ( model.getRowCount()>aRow )
            return model.getValueAt(indexes[aRow], aColumn);
        return null;
    }

    public void setValueAt(Object aValue, int aRow, int aColumn)
    {
        model.setValueAt(aValue, indexes[aRow], aColumn);
    }

    public void sortByColumn(int column) {
        sortByColumn(column, true);
    }

    public void sortByColumn(int column, boolean ascending) {
        this.ascending = ascending;
        sortingColumns.removeAllElements();
        sortingColumns.addElement(new Integer(column));
        sort(this);

        super.tableChanged(new TableModelEvent(this)); 
    }

    // There is no-where else to put this. 
    // Add a mouse listener to the Table to trigger a table sort 
    // when a column heading is clicked in the JTable. 
    public void addMouseListenerToHeaderInTable(JTable table) { 
        final TableSorter sorter = this; 
        final JTable tableView = table; 
        tableView.setColumnSelectionAllowed(false); 
    
        
        MouseAdapter listMouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX()); 
                int column = tableView.convertColumnIndexToModel(viewColumn); 
                if(e.getClickCount() == 1 && column != -1 && tableView.isEnabled()==true ) {
                    //System.out.println("Sorting ..."); 
                    int shiftPressed = e.getModifiers()&InputEvent.SHIFT_MASK; 

                    if ( previousColumn==column ) ascending=!ascending; else ascending=true;
                    previousColumn=column;
                    sorter.sortByColumn(column, ascending); 
//                    for ( int columnHeaderLooper=-1; ++columnHeaderLooper<columnModel.getColumnCount() ; ) {
//                        columnModel.getColumn(columnHeaderLooper).setHeaderRenderer(displayClassInstance.new customBlankTableColumnHeaderCellRenderer());
//                    }
//                    columnModel.getColumn(column).setHeaderRenderer(displayClassInstance.new customTableColumnHeaderCellRenderer());
                }
             }
         };
        JTableHeader th = tableView.getTableHeader(); 
        th.addMouseListener(listMouseListener); 
    }
}
