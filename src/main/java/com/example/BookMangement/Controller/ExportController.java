package com.example.BookMangement.Controller;

import com.example.BookMangement.Entity.Ticket;
import com.example.BookMangement.Entity.TicketDetail;
import com.example.BookMangement.Repository.TicketDetailRepository;
import com.example.BookMangement.Repository.TicketRepository;
import com.example.BookMangement.Service.TicketService;
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
    @Autowired
    TicketDetailRepository ticketDetailRepository;
    @Autowired
    TicketService ticketService;
    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping("/generate-pdf")
    public void generatePdf(HttpServletResponse response, @RequestParam Long ticketId) throws IOException, java.io.IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ticket.pdf");

        List<TicketDetail> ticketDetails = ticketDetailRepository.findByTicketId(ticketId);
        Ticket ticket = ticketService.getTicketById(ticketId);

        PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        // Thay thế đường dẫn hình ảnh của bạn tại đây
        String imagePath = "src/main/resources/static/assets/img/bgPDF.jpg";
        ImageData imageData = ImageDataFactory.create(imagePath);
        Image image = new Image(imageData);

        float x = pdfDocument.getDefaultPageSize().getWidth() / 2;
        float y = pdfDocument.getDefaultPageSize().getHeight() / 2;
        image.setFixedPosition(x - 150, y - 170);
        image.setOpacity(0.1f);
        document.add(image);

        // Tạo nội dung PDF
        float threecol = 190f;
        float borderWidth = 2f;
        float twocol = 205f;
        float twocol150 = twocol + 100f;
        float[] twocolumnWidth = {twocol150, twocol};
        float[] fullwidth = {threecol};
        float[] threeColumnWidth = {threecol, threecol, threecol};
        Paragraph onesp = new Paragraph("\n");

        Table table = new Table(twocolumnWidth);

        // Sử dụng font tùy chỉnh và màu sắc cho dòng chữ BenLocBookManagement
        String fontPath = "static/forms/Renita-Montes.ttf";
        PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        Text text1 = new Text("BenLoc").setFont(font).setFontSize(25f).setFontColor(ColorConstants.BLUE).setBold();
        Text text2 = new Text("BookManagement").setFont(font).setFontSize(25f).setFontColor(ColorConstants.ORANGE).setBold();

        Paragraph header = new Paragraph().add(text1).add(text2);

        table.addCell(new Cell().add(header).setBorder(Border.NO_BORDER));
        Table nestedTable = new Table(new float[]{twocol / 2, twocol / 2});
        table.setMarginBottom(-20f);
        nestedTable.addCell(getHeaderTextCell("Invoice No."));
        nestedTable.addCell(getHeaderTextCellValue(String.valueOf(ticket.getCode())));
        nestedTable.addCell(getHeaderTextCell("Invoice Date"));
        nestedTable.addCell(getHeaderTextCellValue(String.valueOf(LocalDate.now())));

        table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));

        Border gb = new SolidBorder(ColorConstants.GRAY, borderWidth);
        Table divider = new Table(fullwidth);
        divider.setBorder(gb);

        document.add(divider);
        document.add(onesp);
        document.add(table);
        document.add(onesp);

        document.add(new Paragraph("\n"));
        //document.add(divider.setBorder(new SolidBorder(ColorConstants.GRAY, 1))).setBottomMargin(40f);
        Table twoColTable = new Table(twocolumnWidth);
        twoColTable.addCell(getBillingandShippingCell("Member Info").setFontSize(15f));
        document.add(twoColTable.setMarginBottom(12f));

        Table twoColTable2 = new Table(twocolumnWidth);
        twoColTable2.addCell(getCell10fLeft("Name", true));

        document.add(twoColTable2);

        Table twoColTable3 = new Table(twocolumnWidth);
        twoColTable3.addCell(getCell10fLeft(String.valueOf(ticket.getMember().getName()), false));
        document.add(twoColTable3);

        float[] oneColumnWidth = {twocol150};

        Table oneColTable1 = new Table(oneColumnWidth);
        oneColTable1.addCell(getCell10fLeft("Address", true));
        oneColTable1.addCell(getCell10fLeft(String.valueOf(ticket.getMember().getAddress()), false));
        oneColTable1.addCell(getCell10fLeft("Email", true));
        oneColTable1.addCell(getCell10fLeft(String.valueOf(ticket.getMember().getEmail()), false));
        document.add(oneColTable1.setMarginBottom(10f));

        Table tableDivider2 = new Table(fullwidth);
        Border dgb = new DashedBorder(ColorConstants.GRAY, 0.5f);
        document.add(tableDivider2.setBorder(dgb));
        Paragraph productPara = new Paragraph("Books");

        document.add(productPara.setBold());
        Table threeColTable1 = new Table(threeColumnWidth);
        threeColTable1.setBackgroundColor(ColorConstants.BLACK, 0.7f);

        threeColTable1.addCell(new Cell().add(new Paragraph("Title")).setBold().setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
        threeColTable1.addCell(new Cell().add(new Paragraph("Quantity")).setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable1.addCell(new Cell().add(new Paragraph("Price/book")).setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
        document.add(threeColTable1);

        Table threeColTable2 = new Table(threeColumnWidth);

        float totalSum = 0f;
        for (TicketDetail ticketDetail : ticketDetails) {
            double total = ticketDetail.getQuantity() * 0.5;
            totalSum += total;
            threeColTable2.addCell(new Cell().add(new Paragraph(ticketDetail.getBook().getTitle())).setBorder(Border.NO_BORDER));
            threeColTable2.addCell(new Cell().add(new Paragraph(String.valueOf(ticketDetail.getQuantity()))).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
            threeColTable2.addCell(new Cell().add(new Paragraph(String.valueOf(total))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
        }
        document.add(threeColTable2.setMarginBottom(20f));

        float[] oneTwoColumnWidth = {threecol + 125f, threecol * 2};
        Table threeColTable4 = new Table(oneTwoColumnWidth);
        threeColTable4.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        threeColTable4.addCell(new Cell().add(tableDivider2).setBorder(Border.NO_BORDER));
        document.add(threeColTable4);

        Table threeColTable3 = new Table(threeColumnWidth);
        threeColTable3.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        threeColTable3.addCell(new Cell().add(new Paragraph("Total")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable3.addCell(new Cell().add(new Paragraph(String.valueOf(totalSum))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));

        document.add(threeColTable3);
        Table tb = new Table(fullwidth);
        Cell cell = new Cell();
        cell.add(new Paragraph("-------------------------------------------------------------").setFontColor(ColorConstants.BLACK)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER);
        tb.addCell(cell);
        Cell noteCell1 = new Cell();
        noteCell1.add(new Paragraph("Note : "+ticket.getNote())).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER);
        noteCell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
        tb.addCell(noteCell1);

        document.add(tb.setMarginBottom(30f));

        document.add(divider);
        document.add(onesp);
        document.add(new Paragraph("Terms").setBold());
        Paragraph reference = new Paragraph("Keep the voucher when returning the book must be accompanied by the voucher in case of loss of the $10 coupon \n" +
                "Returning books on time when overdue will be penalized, please keep in mind the deadline for returning book \n" +
                "Damage to the book will have to pay back to the library \n"
                +"Thanks and regards!!!");
        document.add(reference);
        document.close();
    }

    @GetMapping("/penaltyTicket-pdf")
    public void exportPenaltyTicket(HttpServletResponse response, @RequestParam Long ticketId) throws IOException, java.io.IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=penalty.pdf");

        List<TicketDetail> ticketDetails = ticketDetailRepository.findByTicketId(ticketId);
        Ticket ticket = ticketService.getTicketById(ticketId);
        LocalDate today = LocalDate.now();
        LocalDate returnDate = ticket.getReturnDate();

        long daysBetween = ChronoUnit.DAYS.between(returnDate, today);
        ticket.setOverdueAmount(daysBetween*0.25*ticket.getTotal());
        ticketRepository.save(ticket);
        PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        Document document = new Document(pdfDocument);

        // Thay thế đường dẫn hình ảnh của bạn tại đây
        String imagePath = "src/main/resources/static/assets/img/bgPDF.jpg";
        ImageData imageData = ImageDataFactory.create(imagePath);
        Image image = new Image(imageData);

        float x = pdfDocument.getDefaultPageSize().getWidth() / 2;
        float y = pdfDocument.getDefaultPageSize().getHeight() / 2;
        image.setFixedPosition(x - 150, y - 170);
        image.setOpacity(0.1f);
        document.add(image);

        // Tạo nội dung PDF
        float threecol = 190f;
        float borderWidth = 2f;
        float twocol = 205f;
        float twocol150 = twocol + 100f;
        float[] twocolumnWidth = {twocol150, twocol};
        float[] fullwidth = {threecol};
        float[] threeColumnWidth = {threecol, threecol, threecol,threecol};
        Paragraph onesp = new Paragraph("\n");

        Table table = new Table(twocolumnWidth);

        // Sử dụng font tùy chỉnh và màu sắc cho dòng chữ BenLocBookManagement
        String fontPath = "static/forms/Renita-Montes.ttf";
        PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
        Text text1 = new Text("BenLoc").setFont(font).setFontSize(25f).setFontColor(ColorConstants.BLUE).setBold();
        Text text2 = new Text("BookManagement").setFont(font).setFontSize(25f).setFontColor(ColorConstants.ORANGE).setBold();

        Paragraph header = new Paragraph().add(text1).add(text2);

        table.addCell(new Cell().add(header).setBorder(Border.NO_BORDER));
        Table nestedTable = new Table(new float[]{twocol / 2, twocol / 2});
        table.setMarginBottom(-20f);
        nestedTable.addCell(getHeaderTextCell("Invoice No."));
        nestedTable.addCell(getHeaderTextCellValue(String.valueOf(ticket.getCode())));
        nestedTable.addCell(getHeaderTextCell("Invoice Date"));
        nestedTable.addCell(getHeaderTextCellValue(String.valueOf(LocalDate.now())));

        table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));

        Border gb = new SolidBorder(ColorConstants.GRAY, borderWidth);
        Table divider = new Table(fullwidth);
        divider.setBorder(gb);

        document.add(divider);
        document.add(onesp);
        document.add(table);
        document.add(onesp);

        document.add(new Paragraph("\n"));
        //document.add(divider.setBorder(new SolidBorder(ColorConstants.GRAY, 1))).setBottomMargin(40f);
        Table twoColTable = new Table(twocolumnWidth);
        twoColTable.addCell(getBillingandShippingCell("Member Info").setFontSize(15f));
        document.add(twoColTable.setMarginBottom(12f));

        Table twoColTable2 = new Table(twocolumnWidth);
        twoColTable2.addCell(getCell10fLeft("Name", true));

        document.add(twoColTable2);

        Table twoColTable3 = new Table(twocolumnWidth);
        twoColTable3.addCell(getCell10fLeft(String.valueOf(ticket.getMember().getName()), false));
        document.add(twoColTable3);

        float[] oneColumnWidth = {twocol150};

        Table oneColTable1 = new Table(oneColumnWidth);
        oneColTable1.addCell(getCell10fLeft("Address", true));
        oneColTable1.addCell(getCell10fLeft(String.valueOf(ticket.getMember().getAddress()), false));
        oneColTable1.addCell(getCell10fLeft("Email", true));
        oneColTable1.addCell(getCell10fLeft(String.valueOf(ticket.getMember().getEmail()), false));
        document.add(oneColTable1.setMarginBottom(10f));

        Table tableDivider2 = new Table(fullwidth);
        Border dgb = new DashedBorder(ColorConstants.GRAY, 0.5f);
        document.add(tableDivider2.setBorder(dgb));
        Paragraph productPara = new Paragraph("Books");

        document.add(productPara.setBold());
        Table threeColTable1 = new Table(threeColumnWidth);
        threeColTable1.setBackgroundColor(ColorConstants.BLACK, 0.7f);

        threeColTable1.addCell(new Cell().add(new Paragraph("Title")).setBold().setFontColor(ColorConstants.WHITE).setBorder(Border.NO_BORDER));
        threeColTable1.addCell(new Cell().add(new Paragraph("Quantity")).setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable1.addCell(new Cell().add(new Paragraph("Take date")).setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable1.addCell(new Cell().add(new Paragraph("Return date")).setBold().setFontColor(ColorConstants.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
        document.add(threeColTable1);

        Table threeColTable2 = new Table(threeColumnWidth);

         for (TicketDetail ticketDetail : ticketDetails) {

            threeColTable2.addCell(new Cell().add(new Paragraph(ticketDetail.getBook().getTitle())).setBorder(Border.NO_BORDER));
            threeColTable2.addCell(new Cell().add(new Paragraph(String.valueOf(ticketDetail.getQuantity()))).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
             threeColTable2.addCell(new Cell().add(new Paragraph(String.valueOf(ticket.getTakeDate()))).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
             threeColTable2.addCell(new Cell().add(new Paragraph(String.valueOf(ticket.getReturnDate()))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
        }
        document.add(threeColTable2.setMarginBottom(20f));

        float[] oneTwoColumnWidth = {threecol + 125f, threecol * 2};
        Table threeColTable4 = new Table(oneTwoColumnWidth);
        threeColTable4.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        threeColTable4.addCell(new Cell().add(tableDivider2).setBorder(Border.NO_BORDER));
        document.add(threeColTable4);

        Table threeColTable3 = new Table(threeColumnWidth);
        threeColTable3.addCell(new Cell().add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable3.addCell(new Cell().add(new Paragraph("")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable3.addCell(new Cell().add(new Paragraph("Total overdue(0.25$/day)")).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        threeColTable3.addCell(new Cell().add(new Paragraph(String.valueOf(ticket.getOverdueAmount()+"$"))).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));

        document.add(threeColTable3);
        Table tb = new Table(fullwidth);
        Cell noteCell1 = new Cell();
        noteCell1.add(new Paragraph("Note : "+ticket.getNote())).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER);
        noteCell1.setVerticalAlignment(VerticalAlignment.MIDDLE);
        tb.addCell(noteCell1);

        document.add(tb.setMarginBottom(30f));

        document.add(divider);
        document.add(onesp);
        document.add(new Paragraph("Terms").setBold());
        Paragraph reference = new Paragraph("Keep the voucher when returning the book must be accompanied by the voucher in case of loss of the $10 coupon \n" +
                "Returning books on time when overdue will be penalized, please keep in mind the deadline for returning book \n" +
                "Damage to the book will have to pay back to the library \n"
                +"Thanks and regards!!!");
        document.add(reference);
        document.close();
    }


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
