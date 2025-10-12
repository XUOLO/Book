package com.example.BookMangement.Controller;

import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
//import com.itextpdf.kernel.pdf.PdfEncodings;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@RequestMapping("/export")
public class ExportController {





    private Cell getHeaderTextCell(String textValue) {
        return new Cell().add(new Paragraph(textValue).setFontSize(12f).setBold()).setBorder(Border.NO_BORDER);
    }

    private Cell getHeaderTextCellValue(String textValue) {
        return new Cell().add(new Paragraph(textValue).setFontSize(12f)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    private Cell getBillingandShippingCell(String textValue) {
        return new Cell().add(new Paragraph(textValue)).setBorder(Border.NO_BORDER).setBackgroundColor(ColorConstants.BLACK, 0.7f).setFontColor(ColorConstants.WHITE).setBold();
    }

    private Cell getCell10fLeft(String textValue, Boolean isBold) {
        Paragraph paragraph = new Paragraph(textValue).setFontSize(10f).setMarginBottom(2f).setMarginTop(0f);
        if (isBold) paragraph.setBold();
        return new Cell().add(paragraph).setBorder(Border.NO_BORDER);
    }
}
