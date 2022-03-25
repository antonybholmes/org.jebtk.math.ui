/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jebtk.math.ui.external.microsoft;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jebtk.core.io.FileUtils;
import org.jebtk.modern.dataview.ModernDataCellStyle;

/**
 * Provides a view onto an excel workbook.
 * 
 * @author Antony Holmes
 *
 */
public class XlsTableModel extends ExcelTableModel {

  /**
   * The member workbook.
   */
  private HSSFWorkbook mWorkbook;

  /**
   * The member palette.
   */
  private HSSFPalette mPalette;

  /**
   * Instantiates a new xls table model.
   *
   * @param file           the file
   * @param hasHeader      the has header
   * @param rowAnnotations the row annotations
   * @param supportColor   the support color
   * @throws InvalidFormatException the invalid format exception
   * @throws IOException            Signals that an I/O exception has occurred.
   */
  public XlsTableModel(Path file, boolean hasHeader, int rowAnnotations, boolean supportColor)
      throws InvalidFormatException, IOException {
    this(new HSSFWorkbook(FileUtils.newBufferedInputStream(file)), null, hasHeader, rowAnnotations, supportColor);// new
                                                                                                                  // HSSFWorkbook(file));
  }

  /**
   * Instantiates a new xls table model.
   *
   * @param file           the file
   * @param sheet          the sheet
   * @param hasHeader      the has header
   * @param rowAnnotations the row annotations
   * @param supportColor   the support color
   * @throws InvalidFormatException the invalid format exception
   * @throws IOException            Signals that an I/O exception has occurred.
   */
  public XlsTableModel(Path file, String sheet, boolean hasHeader, int rowAnnotations, boolean supportColor)
      throws InvalidFormatException, IOException {
    this(new HSSFWorkbook(FileUtils.newBufferedInputStream(file)), sheet, hasHeader, rowAnnotations, supportColor);// new
                                                                                                                   // HSSFWorkbook(file));
  }

  /**
   * Instantiates a new xls table model.
   *
   * @param workbook       the workbook
   * @param hasHeader      the has header
   * @param rowAnnotations the row annotations
   * @param supportColor   the support color
   */
  public XlsTableModel(HSSFWorkbook workbook, boolean hasHeader, int rowAnnotations, boolean supportColor) {
    this(workbook, null, hasHeader, rowAnnotations, supportColor);
  }

  /**
   * Instantiates a new xls table model.
   *
   * @param workbook       the workbook
   * @param sheet          the sheet
   * @param hasHeader      the has header
   * @param rowAnnotations the row annotations
   * @param supportColor   the support color
   */
  public XlsTableModel(HSSFWorkbook workbook, String sheet, boolean hasHeader, int rowAnnotations,
      boolean supportColor) {
    super(sheet != null ? workbook.getSheet(sheet) : workbook.getSheetAt(0), hasHeader, rowAnnotations, supportColor);

    mWorkbook = workbook;

    mPalette = workbook.getCustomPalette();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.ui.dataview.ModernDataGridModel#getCellStyle(int, int)
   */
  @Override
  public ModernDataCellStyle getCellStyle(int row, int column) {
    if (!getSupportColor()) {
      return ModernDataCellStyle.DEFAULT_STYLE;
    }

    short colorIndex = mWorkbook.getFontAt(getSheet().getRow(row).getCell(column).getCellStyle().getFontIndex())
        .getColor();

    HSSFColor color = mPalette.getColor(colorIndex);

    if (color == null) {
      return ModernDataCellStyle.DEFAULT_STYLE;
    }

    short[] triplet = color.getTriplet();

    ModernDataCellStyle style = new ModernDataCellStyle();

    style.setColor(new Color(triplet[0], triplet[1], triplet[2]));

    return style;
  }
}
